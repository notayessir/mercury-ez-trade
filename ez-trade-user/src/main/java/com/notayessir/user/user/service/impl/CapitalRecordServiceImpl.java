package com.notayessir.user.user.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notayessir.common.constant.EnumFieldDeleted;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.user.order.entity.Order;
import com.notayessir.user.user.bo.FindCapitalRecordReqBO;
import com.notayessir.user.user.constant.EnumCapitalBusinessCode;
import com.notayessir.user.user.constant.EnumCapitalDirection;
import com.notayessir.user.user.entity.CapitalRecord;
import com.notayessir.user.user.mapper.CapitalRecordMapper;
import com.notayessir.user.user.service.ICapitalRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CapitalRecordServiceImpl extends ServiceImpl<CapitalRecordMapper, CapitalRecord> implements ICapitalRecordService {

    @Override
    public void createCapitalFlowIn(Long userId, String currency, String qty, EnumCapitalBusinessCode businessCode) {
        CapitalRecord capitalRecord = new CapitalRecord();
        capitalRecord.setId(IdUtil.getSnowflakeNextId());
        capitalRecord.setUserId(userId);
        capitalRecord.setCurrency(currency);
        capitalRecord.setNum(qty);
        capitalRecord.setBusinessCode(businessCode.getCode());
        capitalRecord.setCreateTime(LocalDateTime.now());
        capitalRecord.setUpdateTime(LocalDateTime.now());
        capitalRecord.setDeleted(EnumFieldDeleted.NOT_DELETED.getCode());
        capitalRecord.setVersion(EnumFieldVersion.INIT.getCode());
        capitalRecord.setDirection(EnumCapitalDirection.FLOW_IN.getCode());
        save(capitalRecord);
    }

    @Override
    public void createCapitalFlowOut(Long userId, String currency, String qty, EnumCapitalBusinessCode businessCode) {
        CapitalRecord capitalRecord = new CapitalRecord();
        capitalRecord.setId(IdUtil.getSnowflakeNextId());
        capitalRecord.setUserId(userId);
        capitalRecord.setCurrency(currency);
        capitalRecord.setNum(qty);
        capitalRecord.setBusinessCode(businessCode.getCode());
        capitalRecord.setCreateTime(LocalDateTime.now());
        capitalRecord.setUpdateTime(LocalDateTime.now());
        capitalRecord.setDeleted(EnumFieldDeleted.NOT_DELETED.getCode());
        capitalRecord.setVersion(EnumFieldVersion.INIT.getCode());
        capitalRecord.setDirection(EnumCapitalDirection.FLOW_OUT.getCode());
        save(capitalRecord);
    }

    @Override
    public void createCapitalFlowInOrOut(EnumCapitalDirection direction, Long userId, String currency, String qty, EnumCapitalBusinessCode businessCode) {
        if (direction == EnumCapitalDirection.FLOW_IN){
            createCapitalFlowIn(userId, currency, qty, businessCode);
        } else {
            createCapitalFlowOut(userId, currency, qty, businessCode);
        }
    }

    @Override
    public Page<CapitalRecord> findCapitalRecords(BasePageReq<FindCapitalRecordReqBO> req) {
        Page<CapitalRecord> iPage = Page.of(req.getPageNum(), req.getPageSize());
        FindCapitalRecordReqBO query = req.getQuery();

        LambdaQueryWrapper<CapitalRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(CapitalRecord::getUserId, req.getUserId());
        if (StringUtils.isNotBlank(query.getCurrency())){
            qw.eq(CapitalRecord::getCurrency, query.getCurrency());
        }
        qw.orderByDesc(CapitalRecord::getId);

        return this.page(iPage, qw);
    }

}
