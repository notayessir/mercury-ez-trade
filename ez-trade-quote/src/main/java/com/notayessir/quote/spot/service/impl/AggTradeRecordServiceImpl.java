package com.notayessir.quote.spot.service.impl;

import com.notayessir.quote.spot.entity.AggTradeRecord;
import com.notayessir.quote.spot.mapper.AggTradeRecordMapper;
import com.notayessir.quote.spot.service.IAggTradeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AggTradeRecordServiceImpl extends ServiceImpl<AggTradeRecordMapper, AggTradeRecord> implements IAggTradeRecordService {

    @Override
    public void saveAggTradeRecord(AggTradeRecord aggTradeRecord) {
        this.save(aggTradeRecord);
    }
}
