package com.notayessir.queue.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notayessir.queue.route.constant.EnumOperationStatus;
import com.notayessir.queue.route.entity.RequestRecord;
import com.notayessir.queue.route.mapper.RequestRecordMapper;
import com.notayessir.queue.route.service.IRequestRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestRecordServiceImpl extends ServiceImpl<RequestRecordMapper, RequestRecord> implements IRequestRecordService {



    @Override
    public void createRequest(RequestRecord requestRecord) {
        this.save(requestRecord);
    }


    @Override
    public long countRequestRecord(Long requestId) {
        LambdaQueryWrapper<RequestRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(RequestRecord::getRequestId, requestId);
        return this.count(qw);
    }

    private boolean updateRequestRecord(RequestRecord record) {
        int curVersion = record.getVersion();
        record.setVersion(curVersion + 1);
        LambdaQueryWrapper<RequestRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RequestRecord::getVersion, curVersion)
                .eq(RequestRecord::getId, record.getId());
        return this.update(record, queryWrapper);
    }

}
