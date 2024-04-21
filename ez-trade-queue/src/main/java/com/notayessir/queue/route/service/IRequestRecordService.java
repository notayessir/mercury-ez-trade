package com.notayessir.queue.route.service;

import com.notayessir.queue.route.entity.RequestRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IRequestRecordService extends IService<RequestRecord> {



    void createRequest(RequestRecord requestRecord);

    long countRequestRecord(Long requestId);
}
