package com.notayessir.quote.spot.mapper;

import com.notayessir.quote.spot.entity.Handicap;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface HandicapMapper extends BaseMapper<Handicap> {

    List<Long> distinctAllCoinId();

}
