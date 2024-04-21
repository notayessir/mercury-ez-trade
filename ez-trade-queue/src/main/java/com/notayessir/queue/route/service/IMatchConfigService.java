package com.notayessir.queue.route.service;

import com.notayessir.queue.route.entity.MatchConfig;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IMatchConfigService extends IService<MatchConfig> {

    MatchConfig findByName(String routeTo);
}
