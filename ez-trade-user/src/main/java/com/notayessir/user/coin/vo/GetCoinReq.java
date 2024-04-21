package com.notayessir.user.coin.vo;


import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.vo.req.BaseReq;
import com.notayessir.common.web.BusinessException;
import com.notayessir.user.common.vo.EnumUserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class GetCoinReq extends BaseReq implements ReqCheck {


    private Long id;


    @Override
    public void checkAndInit() {
        if (Objects.isNull(id)){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());
        }
    }

}
