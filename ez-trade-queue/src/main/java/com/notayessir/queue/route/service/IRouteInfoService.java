package com.notayessir.queue.route.service;

import com.notayessir.queue.route.entity.RouteInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IRouteInfoService extends IService<RouteInfo> {

    RouteInfo findByCoinId(Long coinId);

    void createRouteInfo(RouteInfo routeInfo);
}
