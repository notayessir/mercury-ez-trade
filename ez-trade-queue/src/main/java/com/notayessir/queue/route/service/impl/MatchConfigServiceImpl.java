package com.notayessir.queue.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notayessir.queue.route.entity.MatchConfig;
import com.notayessir.queue.route.mapper.MatchConfigMapper;
import com.notayessir.queue.route.service.IMatchConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchConfigServiceImpl extends ServiceImpl<MatchConfigMapper, MatchConfig> implements IMatchConfigService {


    @Override
    public List<MatchConfig> findAllMatchConfigs() {
        return list();
    }
}
