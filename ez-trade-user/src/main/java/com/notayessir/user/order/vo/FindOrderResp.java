package com.notayessir.user.order.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.notayessir.user.order.entity.ClinchRecord;
import com.notayessir.user.order.entity.Order;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class FindOrderResp {


    private OrderVO order;

    private List<ClinchRecordVO> clinchRecords = new ArrayList<>(3);

    public FindOrderResp() {
    }

    public FindOrderResp(Order order, List<ClinchRecord> clinchRecords) {
        if (Objects.nonNull(order)){
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            this.order = orderVO;

            if (CollectionUtil.isNotEmpty(clinchRecords)){
                for (ClinchRecord clinchRecord : clinchRecords) {
                    ClinchRecordVO clinchRecordVO = new ClinchRecordVO();
                    BeanUtils.copyProperties(clinchRecord, clinchRecordVO);
                    this.clinchRecords.add(clinchRecordVO);
                }
            }
        }
    }
}
