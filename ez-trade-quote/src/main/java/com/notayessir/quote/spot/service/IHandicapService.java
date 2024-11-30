package com.notayessir.quote.spot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.notayessir.quote.spot.entity.Handicap;

import java.math.BigDecimal;
import java.util.List;

public interface IHandicapService extends IService<Handicap> {

    Handicap findHandicap(Long coinId, BigDecimal price, Integer entrustSide);

    boolean updateHandicap(Handicap handicap);

    List<Handicap> findHandicap(Long coinId);

}
