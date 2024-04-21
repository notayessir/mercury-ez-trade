package com.notayessir.quote.service.impl;


import cn.hutool.core.util.IdUtil;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.constant.EnumEntrustSide;
import com.notayessir.quote.spot.entity.Handicap;
import com.notayessir.quote.spot.service.FacadeSpotQuoteService;
import com.notayessir.quote.spot.service.IHandicapService;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Objects;

@SpringBootTest
public class HandicapServiceTests {


    @Autowired
    private IHandicapService iHandicapService;


    @Test
    void contextLoads() throws Exception {
        Assumptions.assumeTrue(Objects.nonNull(iHandicapService));
    }

    @Test
    public void saveHandicapAndSyncCacheTest(){
        Handicap handicap = new Handicap();
        handicap.setPrice("75.7116");
        handicap.setQty("50.7896");
        handicap.setId(IdUtil.getSnowflakeNextId());
        handicap.setCoinId(1L);
        handicap.setVersion(EnumFieldVersion.INIT.getCode().longValue());
        handicap.setCreateTime(LocalDateTime.now());
        handicap.setUpdateTime(LocalDateTime.now());
        handicap.setEntrustSide(EnumEntrustSide.SELL.getCode());
        iHandicapService.saveHandicapAndSyncCache(handicap);

         handicap = new Handicap();
        handicap.setPrice("695.7116");
        handicap.setQty("50.7896");
        handicap.setId(IdUtil.getSnowflakeNextId());
        handicap.setCoinId(1L);
        handicap.setVersion(EnumFieldVersion.INIT.getCode().longValue());
        handicap.setCreateTime(LocalDateTime.now());
        handicap.setUpdateTime(LocalDateTime.now());
        handicap.setEntrustSide(EnumEntrustSide.SELL.getCode());
        iHandicapService.saveHandicapAndSyncCache(handicap);
    }

    @Test
    public void classifyPriceTest(){

    }

}
