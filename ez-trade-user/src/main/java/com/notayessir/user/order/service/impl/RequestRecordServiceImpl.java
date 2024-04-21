package com.notayessir.user.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notayessir.user.order.entity.RequestRecord;
import com.notayessir.user.order.mapper.RequestRecordMapper;
import com.notayessir.user.order.service.IRequestRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RequestRecordServiceImpl extends ServiceImpl<RequestRecordMapper, RequestRecord> implements IRequestRecordService {

    @Override
    public RequestRecord findByRequestId(String requestId) {
        LambdaQueryWrapper<RequestRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(RequestRecord::getRequestId, requestId);
        return getOne(qw);
    }

    @Override
    public void createRequest(RequestRecord requestRecord) {
        this.save(requestRecord);
    }


}
