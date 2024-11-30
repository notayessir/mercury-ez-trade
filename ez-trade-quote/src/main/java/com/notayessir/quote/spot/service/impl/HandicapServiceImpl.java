package com.notayessir.quote.spot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notayessir.quote.spot.entity.Handicap;
import com.notayessir.quote.spot.mapper.HandicapMapper;
import com.notayessir.quote.spot.service.IHandicapService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HandicapServiceImpl extends ServiceImpl<HandicapMapper, Handicap> implements IHandicapService {



    @Override
    public boolean updateHandicap(Handicap handicap) {
        long curVersion = handicap.getVersion();
        handicap.setVersion(curVersion + 1);
        LambdaQueryWrapper<Handicap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Handicap::getVersion, curVersion)
                .eq(Handicap::getId, handicap.getId());
        return this.update(handicap, queryWrapper);
    }

//    @Override
//    public boolean updateHandicapAndSyncCache(Handicap handicap) {
//        boolean updated = updateHandicap(handicap);
//        if (updated){
//            for (EnumPricePrecision pricePrecision : EnumPricePrecision.values()) {
//                updateHandicapInCache(handicap, pricePrecision);
//            }
//            return true;
//        }
//        return false;
//    }



//    @Override
//    public boolean saveHandicapAndSyncCache(Handicap handicap) {
//        boolean updated = save(handicap);
//        if (updated){
//            for (EnumPricePrecision pricePrecision : EnumPricePrecision.values()) {
//                updateHandicapInCache(handicap, pricePrecision);
//            }
//            return true;
//        }
//        return false;
//    }

//    public Map<BigDecimal, BigDecimal> findHandicapFromCache(Long coinId, EnumEntrustSide side, EnumPricePrecision precision) {
//        String cacheKey;
//        if (side == EnumEntrustSide.BUY){
//            cacheKey = createHandicapCacheKey(BUY_HANDICAP_PREFIX , coinId , precision.name());
//        } else {
//            cacheKey = createHandicapCacheKey(SELL_HANDICAP_PREFIX , coinId , precision.name());
//        }
//        RMap<BigDecimal, BigDecimal> rMap = redissonClient.getMap(cacheKey);
//        return rMap.readAllMap();
//    }


//    private void deleteHandicapFromCache(Long coinId) {
//        EnumPricePrecision[] values = EnumPricePrecision.values();
//        for (EnumPricePrecision precision : values) {
//            String cacheKey = createHandicapCacheKey(BUY_HANDICAP_PREFIX, coinId, precision.name());
//            redissonClient.getMap(cacheKey).delete();
//            cacheKey = createHandicapCacheKey(SELL_HANDICAP_PREFIX, coinId, precision.name());
//            redissonClient.getMap(cacheKey).delete();
//        }
//    }

//    @Override
//    public OrderBookDTO findHandicapFromCache(Long coinId, EnumPricePrecision precision) {
//        Map<BigDecimal, BigDecimal> bid = findHandicapFromCache(coinId, EnumEntrustSide.BUY, precision);
//        Map<BigDecimal, BigDecimal> ask = findHandicapFromCache(coinId, EnumEntrustSide.SELL, precision);;
//
//        OrderBookDTO orderBookDTO = new OrderBookDTO();
//        orderBookDTO.setCoinId(coinId);
//        orderBookDTO.setPrecision(precision);
//        orderBookDTO.setAsk(ask);
//        orderBookDTO.setBid(bid);
//
//        return orderBookDTO;
//    }

//    @Override
//    public List<OrderBookDTO> findHandicapFromCache(Long coinId) {
//        List<OrderBookDTO> list = new ArrayList<>();
//        EnumPricePrecision[] values = EnumPricePrecision.values();
//        for (EnumPricePrecision precision : values) {
//            OrderBookDTO orderBookDTO = findHandicapFromCache(coinId, precision);
//            list.add(orderBookDTO);
//        }
//        return list;
//    }


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
