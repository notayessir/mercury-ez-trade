package com.notayessir.quote.spot.service;

import com.notayessir.quote.spot.entity.TickRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ITickRecordService extends IService<TickRecord> {


    void saveTickRecord(TickRecord tickRecord);

}
