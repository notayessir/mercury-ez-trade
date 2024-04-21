package com.notayessir.user.order.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notayessir.user.order.constant.EnumOperationStatus;
import com.notayessir.user.order.entity.MqRecord;
import com.notayessir.user.order.mapper.MqRecordMapper;
import com.notayessir.user.order.service.IMqRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class MqRecordServiceImpl extends ServiceImpl<MqRecordMapper, MqRecord> implements IMqRecordService {

    @Override
    public void createMqRecord(MqRecord mqRecord) {
        if (Objects.isNull(mqRecord.getId())){
            mqRecord.setId(IdUtil.getSnowflakeNextId());
        }
        save(mqRecord);
    }

    @Override
    public boolean finishMqRecord(MqRecord mqRecord) {
        mqRecord.setUpdateTime(LocalDateTime.now());
        mqRecord.setOpStatus(EnumOperationStatus.DONE.getCode());

        return updateMqRecord(mqRecord);
    }


    @Override
    public List<MqRecord> findToFinishRecord(Integer opType, Integer num) {
        String limit = "limit " + num;
        LambdaQueryWrapper<MqRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(MqRecord::getOpStatus, EnumOperationStatus.UNDONE.getCode())
                .eq(MqRecord::getOpType, opType)
                .orderByAsc(MqRecord::getId).last(limit);
        return this.list(qw);
    }

    private boolean updateMqRecord(MqRecord record) {
        int curVersion = record.getVersion();
        record.setVersion(curVersion + 1);
        LambdaQueryWrapper<MqRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MqRecord::getVersion, curVersion)
                .eq(MqRecord::getId, record.getId());
        return this.update(record, queryWrapper);
    }

}
