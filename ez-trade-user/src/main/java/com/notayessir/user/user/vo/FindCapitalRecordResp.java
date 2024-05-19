package com.notayessir.user.user.vo;

import com.notayessir.common.vo.req.BaseReq;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class FindCapitalRecordResp {


    private Long id;

    private String currency;

    private String num;

    private LocalDateTime createTime;

    private Integer businessCode;

    private String businessCodeDesc;

    private Integer direction;


}
