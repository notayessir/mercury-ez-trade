package com.notayessir.user.user.vo;

import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.web.BusinessException;
import com.notayessir.user.common.constant.EnumUserResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;


@Getter
@Setter
public class RegisterReq implements ReqCheck {

    private String username;



    @Override
    public void checkAndInit() {
        if (StringUtils.isBlank(username)){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());
        }

    }

}
