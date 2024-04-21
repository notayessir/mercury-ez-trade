package com.notayessir.user.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.notayessir.user.order.entity.ClinchRecord;
import com.notayessir.user.order.mapper.ClinchRecordMapper;
import com.notayessir.user.order.service.IClinchRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinchRecordServiceImpl extends ServiceImpl<ClinchRecordMapper, ClinchRecord> implements IClinchRecordService {

    @Override
    public List<ClinchRecord> findByOrderId(Long orderId) {
        LambdaQueryWrapper<ClinchRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(ClinchRecord::getOrderId, orderId);
        return list(qw);
    }

    @Override
    public List<ClinchRecord> findByOrderIds(List<Long> orderIds) {
        LambdaQueryWrapper<ClinchRecord> qw = new LambdaQueryWrapper<>();
        qw.in(ClinchRecord::getOrderId, orderIds);
        return list(qw);
    }
}
