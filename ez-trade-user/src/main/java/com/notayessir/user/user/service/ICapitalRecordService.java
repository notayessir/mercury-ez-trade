package com.notayessir.user.user.service;

import com.notayessir.user.user.constant.EnumCapitalBusinessCode;
import com.notayessir.user.user.constant.EnumCapitalDirection;
import com.notayessir.user.user.entity.CapitalRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ICapitalRecordService extends IService<CapitalRecord> {

    void createCapitalFlowIn(Long userId, String currency, String qty, EnumCapitalBusinessCode businessCode);

    void createCapitalFlowOut(Long userId, String currency, String qty, EnumCapitalBusinessCode businessCode);

    void createCapitalFlowInOrOut(EnumCapitalDirection direction, Long userId, String currency, String qty, EnumCapitalBusinessCode businessCode);

}
