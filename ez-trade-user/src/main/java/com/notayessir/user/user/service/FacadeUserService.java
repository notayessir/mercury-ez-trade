package com.notayessir.user.user.service;


import cn.hutool.core.util.IdUtil;
import com.notayessir.common.constant.EnumFieldDeleted;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.user.user.bo.RegisterReqBO;
import com.notayessir.user.user.bo.RegisterRespBO;
import com.notayessir.user.user.entity.User;
import com.notayessir.user.user.vo.RegisterReq;
import com.notayessir.user.user.vo.RegisterResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class FacadeUserService {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private UserTokenService userTokenService;



    public RegisterRespBO register(RegisterReqBO reqBO) {
        User user = iUserService.findByUsername(reqBO.getUsername());
        if (Objects.isNull(user)){
            user = buildUser(reqBO);
            iUserService.createUser(user);
        }
        String token = userTokenService.generateToken(user);
        RegisterRespBO respBO = new RegisterRespBO();
        respBO.setToken(token);
        return respBO;
    }

    private User buildUser(RegisterReqBO reqBO) {
        User user = new User();
        user.setUsername(reqBO.getUsername());


        long id = IdUtil.getSnowflake().nextId();
        user.setId(id);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(EnumFieldDeleted.NOT_DELETED.getCode());
        user.setVersion(EnumFieldVersion.INIT.getCode());
        return user;
    }



    public RegisterResp publicApiRegister(RegisterReq req) {
        RegisterReqBO registerReqBO = toSignUpReqBO(req);

        RegisterRespBO registerRespBO = register(registerReqBO);

        return toSignUpRespBO(registerRespBO);
    }

    private RegisterResp toSignUpRespBO(RegisterRespBO respBO) {
        RegisterResp registerResp = new RegisterResp();
        registerResp.setToken(respBO.getToken());
        return registerResp;
    }

    private RegisterReqBO toSignUpReqBO(RegisterReq req) {
        RegisterReqBO bo = new RegisterReqBO();
        bo.setUsername(req.getUsername());
        return bo;
    }
}
