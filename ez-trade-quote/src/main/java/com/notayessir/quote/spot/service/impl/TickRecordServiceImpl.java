package com.notayessir.quote.spot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notayessir.quote.spot.entity.TickRecord;
import com.notayessir.quote.spot.mapper.TickRecordMapper;
import com.notayessir.quote.spot.service.ITickRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TickRecordServiceImpl extends ServiceImpl<TickRecordMapper, TickRecord> implements ITickRecordService {


    @Override
    public void createTickRecord(TickRecord tickRecord) {
        this.save(tickRecord);
    }

    @Override
    public long countTickRecord(Long globalSequence) {
        LambdaQueryWrapper<TickRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(TickRecord::getGlobalSequence, globalSequence);
        return this.count(qw);
    }
}
