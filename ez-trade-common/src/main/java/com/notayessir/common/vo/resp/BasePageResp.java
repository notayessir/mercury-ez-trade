package com.notayessir.common.vo.resp;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BasePageResp<R>  {


    protected long total = 0;

    protected long size = 0;

    protected long current = 0;

    protected List<R> records;

    public BasePageResp() {
    }

    public BasePageResp(long total, long size, long current) {
        this.total = total;
        this.size = size;
        this.current = current;
    }
}
