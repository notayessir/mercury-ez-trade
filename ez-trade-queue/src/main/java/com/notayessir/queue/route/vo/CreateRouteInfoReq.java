package com.notayessir.queue.route.vo;


import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.vo.req.BaseReq;
import com.notayessir.common.web.BusinessException;
import com.notayessir.queue.common.constant.EnumResponseOfQueue;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
@Setter
public class CreateRouteInfoReq extends BaseReq implements ReqCheck {


    private Long coinId;

    private String routeTo;

    @Override
    public void checkAndInit() {
        if (Objects.isNull(coinId)){
            throw new BusinessException(EnumResponseOfQueue.PARAM_BLANK.getCode(), EnumResponseOfQueue.PARAM_BLANK.getMessage());

        }
        if (StringUtils.isBlank(routeTo)){
            throw new BusinessException(EnumResponseOfQueue.PARAM_BLANK.getCode(), EnumResponseOfQueue.PARAM_BLANK.getMessage());

        }
    }

}
