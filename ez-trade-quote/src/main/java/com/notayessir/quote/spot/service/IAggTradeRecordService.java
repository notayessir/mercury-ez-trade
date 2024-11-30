package com.notayessir.quote.spot.service;

import com.notayessir.quote.spot.entity.AggTradeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IAggTradeRecordService extends IService<AggTradeRecord> {

    void saveAggTradeRecord(AggTradeRecord aggTradeRecord);
}
