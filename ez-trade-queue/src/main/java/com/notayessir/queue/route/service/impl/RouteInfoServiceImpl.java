package com.notayessir.queue.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notayessir.queue.route.entity.RouteInfo;
import com.notayessir.queue.route.mapper.RouteInfoMapper;
import com.notayessir.queue.route.service.IRouteInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RouteInfoServiceImpl extends ServiceImpl<RouteInfoMapper, RouteInfo> implements IRouteInfoService {

    @Override
    public RouteInfo findByCoinId(Long coinId) {
        LambdaQueryWrapper<RouteInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(RouteInfo::getCoinId, coinId);
        return getOne(qw);
    }

    @Override
    public void createRouteInfo(RouteInfo routeInfo) {
        save(routeInfo);
    }
}
