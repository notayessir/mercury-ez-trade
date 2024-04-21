package com.notayessir.user.order.service;

import com.notayessir.user.order.entity.RequestRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IRequestRecordService extends IService<RequestRecord> {

    RequestRecord findByRequestId(String requestId);

    void createRequest(RequestRecord requestRecord);
}
