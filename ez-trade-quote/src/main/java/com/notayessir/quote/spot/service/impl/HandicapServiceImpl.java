package com.notayessir.quote.spot.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notayessir.constant.EnumEntrustSide;
import com.notayessir.quote.api.spot.constant.EnumPricePrecision;
import com.notayessir.quote.api.spot.mq.QOrderBookDTO;
import com.notayessir.quote.spot.entity.Handicap;
import com.notayessir.quote.spot.mapper.HandicapMapper;
import com.notayessir.quote.spot.service.IHandicapService;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class HandicapServiceImpl extends ServiceImpl<HandicapMapper, Handicap> implements IHandicapService {

    public final String BUY_HANDICAP_PREFIX = "buy_handicap_cache_prefix:v1:";
    public final String SELL_HANDICAP_PREFIX = "sell_handicap_cache_prefix:v1:";


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private HandicapMapper handicapMapper;


    @PostConstruct
    public void init(){
        // 1. evict all handicaps cache
        RKeys keys = redissonClient.getKeys();
        keys.deleteByPattern(BUY_HANDICAP_PREFIX + "*");
        keys.deleteByPattern(SELL_HANDICAP_PREFIX + "*");

        // 2. reload handicap to cache
        List<Long> coinIds = handicapMapper.distinctAllCoinId();
        if (CollectionUtil.isEmpty(coinIds)){
            return;
        }
        for (Long coinId : coinIds) {
            reloadHandicapToCache(coinId);
        }
    }

    public String createHandicapCacheKey(String prefix, Long coinId, String suffix){
        return prefix + coinId + suffix;
    }


    public void reloadHandicapToCache(Long coinId){
        // 1. delete cache
        deleteHandicapFromCache(coinId);

        List<Handicap> handicaps = findHandicap(coinId);
        Map<Integer, List<Handicap>> groupedByEntrustSide = handicaps.stream()
                .collect(Collectors.groupingBy(Handicap::getEntrustSide));
        List<Handicap> buyHandicapList = groupedByEntrustSide.get(EnumEntrustSide.BUY.getCode());
        List<Handicap> sellHandicapList = groupedByEntrustSide.get(EnumEntrustSide.SELL.getCode());
        for (EnumPricePrecision pricePrecision : EnumPricePrecision.values()) {
            if (CollectionUtil.isNotEmpty(buyHandicapList)){
                Map<BigDecimal, BigDecimal> buyHandicaps = generateOrderBook(buyHandicapList, pricePrecision);
                String cacheKey = createHandicapCacheKey(BUY_HANDICAP_PREFIX , coinId , pricePrecision.name());
                RMap<Object, Object> map = redissonClient.getMap(cacheKey);
                map.putAll(buyHandicaps);
            }
            if (CollectionUtil.isNotEmpty(sellHandicapList)){
                Map<BigDecimal, BigDecimal> sellHandicaps = generateOrderBook(sellHandicapList, pricePrecision);
                String cacheKey = createHandicapCacheKey(SELL_HANDICAP_PREFIX , coinId , pricePrecision.name());
                RMap<Object, Object> map = redissonClient.getMap(cacheKey);
                map.putAll(sellHandicaps);
            }
        }
    }


    private BigDecimal classifyPrice(BigDecimal price, EnumPricePrecision pricePrecision){
        return price.divide(pricePrecision.getPrecision(), 0, RoundingMode.DOWN)
                .multiply(pricePrecision.getPrecision());
    }


    public void updateHandicapInCache(Handicap handicap, EnumPricePrecision pricePrecision){
        if (new BigDecimal(handicap.getQty()).compareTo(BigDecimal.ZERO) <= 0){
            return;
        }
        BigDecimal price = new BigDecimal(handicap.getPrice());
        BigDecimal roundedPrice = classifyPrice(price, pricePrecision);
        String cacheKey;
        if (handicap.getEntrustSide().equals(EnumEntrustSide.BUY.getCode())){
            cacheKey = createHandicapCacheKey(BUY_HANDICAP_PREFIX , handicap.getCoinId() , pricePrecision.name());;
        } else {
            cacheKey = createHandicapCacheKey(SELL_HANDICAP_PREFIX , handicap.getCoinId() , pricePrecision.name());
        }
        cacheKey = cacheKey + pricePrecision.name();
        RMap<Object, Object> map = redissonClient.getMap(cacheKey);
        map.put(roundedPrice, new BigDecimal(handicap.getQty()));
        System.out.println(roundedPrice);
    }




    private Map<BigDecimal, BigDecimal> generateOrderBook(List<Handicap> handicaps, EnumPricePrecision pricePrecision) {
        Map<BigDecimal, BigDecimal> priceQtyMap = new TreeMap<>();
        for (Handicap handicap : handicaps) {
            BigDecimal qty = new BigDecimal(handicap.getQty());
            if (qty.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }
            BigDecimal price = new BigDecimal(handicap.getPrice());
            BigDecimal roundedPrice = classifyPrice(price, pricePrecision);
            if (priceQtyMap.containsKey(roundedPrice)){
                qty = priceQtyMap.get(roundedPrice).add(qty);
            }
            priceQtyMap.put(roundedPrice, qty);
        }
        return priceQtyMap;
    }




    @Override
    public boolean updateHandicap(Handicap handicap) {
        long curVersion = handicap.getVersion();
        handicap.setVersion(curVersion + 1);
        LambdaQueryWrapper<Handicap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Handicap::getVersion, curVersion)
                .eq(Handicap::getId, handicap.getId());
        return this.update(handicap, queryWrapper);
    }

    @Override
    public boolean updateHandicapAndSyncCache(Handicap handicap) {
        boolean updated = updateHandicap(handicap);
        if (updated){
            for (EnumPricePrecision pricePrecision : EnumPricePrecision.values()) {
                updateHandicapInCache(handicap, pricePrecision);
            }
            return true;
        }
        return false;
    }



    @Override
    public boolean saveHandicapAndSyncCache(Handicap handicap) {
        boolean updated = save(handicap);
        if (updated){
            for (EnumPricePrecision pricePrecision : EnumPricePrecision.values()) {
                updateHandicapInCache(handicap, pricePrecision);
            }
            return true;
        }
        return false;
    }

    public Map<BigDecimal, BigDecimal> findHandicapFromCache(Long coinId, EnumEntrustSide side, EnumPricePrecision precision) {
        String cacheKey;
        if (side == EnumEntrustSide.BUY){
            cacheKey = createHandicapCacheKey(BUY_HANDICAP_PREFIX , coinId , precision.name());
        } else {
            cacheKey = createHandicapCacheKey(SELL_HANDICAP_PREFIX , coinId , precision.name());
        }
        RMap<BigDecimal, BigDecimal> rMap = redissonClient.getMap(cacheKey);
        return rMap.readAllMap();
    }


    private void deleteHandicapFromCache(Long coinId) {
        EnumPricePrecision[] values = EnumPricePrecision.values();
        for (EnumPricePrecision precision : values) {
            String cacheKey = createHandicapCacheKey(BUY_HANDICAP_PREFIX, coinId, precision.name());
            redissonClient.getMap(cacheKey).delete();
            cacheKey = createHandicapCacheKey(SELL_HANDICAP_PREFIX, coinId, precision.name());
            redissonClient.getMap(cacheKey).delete();
        }
    }

    @Override
    public QOrderBookDTO findHandicapFromCache(Long coinId, EnumPricePrecision precision) {
        Map<BigDecimal, BigDecimal> bid = findHandicapFromCache(coinId, EnumEntrustSide.BUY, precision);
        Map<BigDecimal, BigDecimal> ask = findHandicapFromCache(coinId, EnumEntrustSide.SELL, precision);;

        QOrderBookDTO orderBookDTO = new QOrderBookDTO();
        orderBookDTO.setCoinId(coinId);
        orderBookDTO.setPrecision(precision);
        orderBookDTO.setAsk(ask);
        orderBookDTO.setBid(bid);

        return orderBookDTO;
    }

    @Override
    public List<QOrderBookDTO> findHandicapFromCache(Long coinId) {
        List<QOrderBookDTO> list = new ArrayList<>();
        EnumPricePrecision[] values = EnumPricePrecision.values();
        for (EnumPricePrecision precision : values) {
            QOrderBookDTO orderBookDTO = findHandicapFromCache(coinId, precision);
            list.add(orderBookDTO);
        }
        return list;
    }


    @Override
    public List<Handicap> findHandicap(Long coinId) {
        LambdaQueryWrapper<Handicap> qw = new LambdaQueryWrapper<>();
        qw.eq(Handicap::getCoinId, coinId);
        return list(qw);
    }

    public Handicap findHandicap(Long coinId, BigDecimal price, Integer entrustSide) {
        LambdaQueryWrapper<Handicap> qw = new LambdaQueryWrapper<>();
        qw.eq(Handicap::getPrice, price.toString())
                .eq(Handicap::getCoinId, coinId)
                .eq(Handicap::getEntrustSide, entrustSide);
        return getOne(qw);
    }




}
