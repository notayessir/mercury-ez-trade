package com.notayessir.quote.spot.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.engine.api.bo.MatchResultBO;
import com.notayessir.engine.api.bo.OrderItemBO;
import com.notayessir.quote.spot.bo.GetKlineReqBO;
import com.notayessir.quote.spot.constant.EnumKLineInterval;
import com.notayessir.quote.spot.entity.KLine;
import com.notayessir.quote.spot.mapper.KLineMapper;
import com.notayessir.quote.spot.service.IKLineService;
import com.notayessir.quote.spot.util.KLineIntervalUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class KLineServiceImpl extends ServiceImpl<KLineMapper, KLine> implements IKLineService {


    @Override
    public List<KLine> handleKLineUpdatedEvent(MatchResultBO event) {
        EnumKLineInterval[] values = EnumKLineInterval.values();
        List<KLine> kLines = new ArrayList<>(values.length);
        for (EnumKLineInterval interval : values) {
            KLine kLine = handleKLine(event, interval);
            kLines.add(kLine);
        }
        return kLines;
    }

    @Override
    public List<KLine> findKline(GetKlineReqBO query) {
        LambdaQueryWrapper<KLine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KLine::getCoinId, query.getCoinId())
                .eq(KLine::getTimeInterval, query.getInterval())
                .orderByDesc(KLine::getId);
        return list(queryWrapper);
    }



    private KLine handleKLine(MatchResultBO event, EnumKLineInterval interval){
        long timestamp = event.getTimestamp();
        long startOfInterval = KLineIntervalUtil.calcTimestamp(timestamp, interval);
        KLine kLine = findKline(interval, startOfInterval);
        if (Objects.isNull(kLine)){
            kLine = buildNewKLine(event, interval, startOfInterval);
            save(kLine);
        } else {
            // update
            buildUpdateKLine(kLine, event);
            updateKLine(kLine);
        }
        return kLine;
    }


    private boolean updateKLine(KLine kLine) {
        int curVersion = kLine.getVersion();
        kLine.setVersion(curVersion + 1);
        LambdaQueryWrapper<KLine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KLine::getVersion, curVersion)
                .eq(KLine::getId, kLine.getId());
        return this.update(kLine, queryWrapper);
    }

    private void buildUpdateKLine(KLine kLine, MatchResultBO event) {
        OrderItemBO takerOrder = event.getTakerOrder();
        BigDecimal clinchAmount = event.gatherClinchAmount();
        BigDecimal clinchQty = event.gatherClinchQty();
        int baseScale = takerOrder.getBaseScale();
        BigDecimal price = clinchAmount.divide(clinchQty, baseScale, RoundingMode.HALF_DOWN);

        kLine.setClosePrice(price.toString());

        kLine.setHighPrice(new BigDecimal(kLine.getHighPrice()).max(price).toString());
        kLine.setLowPrice(new BigDecimal(kLine.getLowPrice()).min(price).toString());

        kLine.setClinchQty(new BigDecimal(kLine.getClinchQty()).add(clinchQty).toString());
        kLine.setClinchAmount(new BigDecimal(kLine.getClinchAmount()).add(clinchAmount).toString());

        LocalDateTime now = LocalDateTime.now();
        kLine.setUpdateTime(now);
    }


    private KLine buildNewKLine(MatchResultBO event, EnumKLineInterval timeInterval, long startOfInterval) {
        OrderItemBO takerOrder = event.getTakerOrder();
        int baseScale = takerOrder.getBaseScale();

        KLine kLine = new KLine();
        kLine.setId(IdUtil.getSnowflakeNextId());
        kLine.setCoinId(event.getCoinId());
        kLine.setTimeInterval(timeInterval.name());
        kLine.setStatisticStartTime(startOfInterval);

//        Instant instant = Instant.ofEpochSecond(startOfInterval);
//        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//        kLine.setStatisticStartTimeHint(localDateTime);

        BigDecimal clinchAmount = event.gatherClinchAmount();
        BigDecimal clinchQty = event.gatherClinchQty();

        BigDecimal price = clinchAmount.divide(clinchQty, baseScale, RoundingMode.HALF_DOWN);
        kLine.setOpenPrice(price.toString());
        kLine.setHighPrice(price.toString());
        kLine.setLowPrice(price.toString());
        kLine.setClosePrice(price.toString());

        kLine.setClinchQty(clinchQty.toString());
        kLine.setClinchAmount(clinchAmount.toString());
        LocalDateTime now = LocalDateTime.now();
        kLine.setCreateTime(now);
        kLine.setUpdateTime(now);
        kLine.setVersion(EnumFieldVersion.INIT.getCode());
        return kLine;
    }

    private KLine findKline(EnumKLineInterval timeInterval, long startOfInterval) {
        LambdaQueryWrapper<KLine> qw = new LambdaQueryWrapper<>();
        qw.eq(KLine::getStatisticStartTime, startOfInterval)
                .eq(KLine::getTimeInterval, timeInterval.name());
        return getOne(qw);
    }


}
