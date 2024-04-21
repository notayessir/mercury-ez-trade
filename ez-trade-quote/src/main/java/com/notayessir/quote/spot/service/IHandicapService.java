package com.notayessir.quote.spot.service;

import com.notayessir.quote.api.spot.constant.EnumPricePrecision;
import com.notayessir.quote.api.spot.mq.QOrderBookDTO;
import com.notayessir.quote.spot.entity.Handicap;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

public interface IHandicapService extends IService<Handicap> {

    Handicap findHandicap(Long coinId, BigDecimal price, Integer entrustSide);

    boolean updateHandicap(Handicap handicap);

    boolean updateHandicapAndSyncCache(Handicap handicap);

    boolean saveHandicapAndSyncCache(Handicap handicap);

    List<Handicap> findHandicap(Long coinId);

    QOrderBookDTO findHandicapFromCache(Long coinId, EnumPricePrecision precision);

    List<QOrderBookDTO> findHandicapFromCache(Long coinId) ;
}
