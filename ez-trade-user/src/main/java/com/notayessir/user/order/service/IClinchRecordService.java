package com.notayessir.user.order.service;

import com.notayessir.user.order.entity.ClinchRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IClinchRecordService extends IService<ClinchRecord> {

    List<ClinchRecord> findByOrderId(Long orderId);

    List<ClinchRecord> findByOrderIds(List<Long> orderIds);
}
