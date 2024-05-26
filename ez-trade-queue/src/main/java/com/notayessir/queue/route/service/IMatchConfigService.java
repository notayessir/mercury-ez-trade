package com.notayessir.queue.route.service;

import com.notayessir.queue.route.entity.MatchConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IMatchConfigService extends IService<MatchConfig> {


    List<MatchConfig> findAllMatchConfigs();
}
