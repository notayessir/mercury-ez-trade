package com.notayessir.queue.route.bo;


import cn.hutool.core.util.IdUtil;
import com.notayessir.common.constant.EnumFieldDeleted;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.common.vo.req.BaseReq;
import com.notayessir.queue.route.entity.RouteInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateRouteInfoReqBO extends BaseReq  {


    private Long coinId;

    private String routeTo;


    public RouteInfo toRouteInfo() {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setId(IdUtil.getSnowflakeNextId());
        routeInfo.setRouteTo(routeTo);
        routeInfo.setCoinId(coinId);
        LocalDateTime now = LocalDateTime.now();
        routeInfo.setCreateTime(now);
        routeInfo.setUpdateTime(now);
        routeInfo.setDeleted(EnumFieldDeleted.NOT_DELETED.getCode());
        routeInfo.setVersion(EnumFieldVersion.INIT.getCode());
        return routeInfo;

    }
}
