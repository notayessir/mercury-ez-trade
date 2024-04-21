package com.notayessir.common.vo.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePageReq<Q> extends BaseReq {


    protected Integer pageSize;

    protected Integer pageNum;

    protected Q query;

}
