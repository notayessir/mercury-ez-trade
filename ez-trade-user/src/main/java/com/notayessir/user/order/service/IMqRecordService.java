package com.notayessir.user.order.service;

import com.notayessir.user.order.entity.MqRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IMqRecordService extends IService<MqRecord> {

    void createMqRecord(MqRecord mqRecord);

    boolean finishMqRecord(MqRecord mqRecord);

    List<MqRecord> findToFinishRecord(Integer opType, Integer num);
}
