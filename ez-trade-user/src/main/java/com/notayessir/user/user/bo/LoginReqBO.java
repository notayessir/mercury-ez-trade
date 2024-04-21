package com.notayessir.user.user.bo;

import com.notayessir.common.constant.EnumRequestSource;
import com.notayessir.common.vo.ReqCheck;
import com.notayessir.common.web.BusinessException;
import com.notayessir.user.common.vo.EnumUserResponse;
import com.notayessir.user.user.constant.EnumLoginVerifyType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;


@Getter
@Setter
public class LoginReqBO implements ReqCheck {


    private String email;

    private String password;

    private String code;

    private Integer loginVType;

    private EnumRequestSource requestSource;


    @Override
    public void checkAndInit() {
        if (StringUtils.isBlank(email) || !StringUtils.contains(email, "@")){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());

        }
        EnumLoginVerifyType way = EnumLoginVerifyType.getByCode(loginVType);
        if (Objects.isNull(way)){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());

        }
        if (loginVType.equals(EnumLoginVerifyType.PASSWORD.getCode()) && StringUtils.isBlank(password) ){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());

        }
        if (loginVType.equals(EnumLoginVerifyType.EMAIL_CODE.getCode()) && StringUtils.isBlank(code) ){
            throw new BusinessException(EnumUserResponse.PARAM_BLANK.getCode(), EnumUserResponse.PARAM_BLANK.getMessage());

        }
    }

}
