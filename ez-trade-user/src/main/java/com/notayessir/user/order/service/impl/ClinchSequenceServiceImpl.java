package com.notayessir.user.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notayessir.user.order.constant.EnumOperationStatus;
import com.notayessir.user.order.entity.ClinchSequence;
import com.notayessir.user.order.mapper.ClinchSequenceMapper;
import com.notayessir.user.order.service.IClinchSequenceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClinchSequenceServiceImpl extends ServiceImpl<ClinchSequenceMapper, ClinchSequence> implements IClinchSequenceService {

    @Override
    public void createClinchSequence(ClinchSequence clinchSequence) {
        this.save(clinchSequence);
    }

    @Override
    public long countClinchSequence(Long globalSequence) {
        LambdaQueryWrapper<ClinchSequence> qw = new LambdaQueryWrapper<>();
        qw.eq(ClinchSequence::getGlobalSequence, globalSequence);
        return this.count(qw);
    }


    private boolean updateClinchSequence(ClinchSequence record) {
        int curVersion = record.getVersion();
        record.setVersion(curVersion + 1);
        LambdaQueryWrapper<ClinchSequence> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClinchSequence::getVersion, curVersion)
                .eq(ClinchSequence::getId, record.getId());
        return this.update(record, queryWrapper);
    }
}
