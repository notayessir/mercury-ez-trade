package com.notayessir.user.order.bo;

import com.notayessir.user.order.entity.ClinchRecord;
import com.notayessir.user.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderBO {
    private Order order;
    private List<ClinchRecord> clinchRecords;

}
