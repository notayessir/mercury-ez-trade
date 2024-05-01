package com.notayessir.user.user.service;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.notayessir.user.user.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Service
public class UserTokenService {


    private final static Integer DAY_TO_EXPIRE = 7;

    private final static String JWT_SECRET = "PERFECT_TRADE";

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IUserService iUserService;

    public String generateToken(User user) {
        Date iat = new Date();
        Date exp = DateUtils.addDays(iat, 7);
        return JWT.create()
                .setPayload("sub", user.getId())
                .setPayload("username", user.getUsername())
                .setIssuedAt(iat)
                .setExpiresAt(exp)
                .setKey(JWT_SECRET.getBytes(StandardCharsets.UTF_8))
                .sign();
    }



    public boolean verifyToken(String token){
        JWT jwt;
        try {
            jwt = JWTUtil.parseToken(token);
        }catch (Exception e){
            logger.info("invalid jwt token: {}", token);
            return false;
        }

        Object id = jwt.getPayload("sub");
        if (Objects.isNull(id) || !StringUtils.isNumeric(id.toString())){
            logger.info("sub is not expected, sub :{}", id);
            return false;
        }

        User user = iUserService.findById(Long.parseLong(id.toString()));
        boolean verified = JWTUtil.verify(token, user.getSecret().getBytes(StandardCharsets.UTF_8));
        if (!verified){
            return false;
        }

        // checkAndInit if token is expired
        long expAt = Long.parseLong(jwt.getPayload("exp").toString());
        long currentTimeMillis = System.currentTimeMillis();
        long now = currentTimeMillis / 1000;
        return expAt >= now;
    }

    public static void main(String[] args) {
        String secret = "my-secret";

        long currentTimeMillis = System.currentTimeMillis();
        long iat = currentTimeMillis / 1000;
        long exp = iat + 3600L * 24 * DAY_TO_EXPIRE;
        String token = JWT.create()
                .setPayload("sub", "6352389")
                .setPayload("email", "smart@clever.com")
                .setPayload("iat", iat)
                .setPayload("exp", exp)
                .setKey(secret.getBytes(StandardCharsets.UTF_8))
                .sign();
        System.out.println(token);

        boolean verified = JWTUtil.verify(token, secret.getBytes(StandardCharsets.UTF_8));
        System.out.println(verified);

        secret += secret;
        verified = JWTUtil.verify(token, secret.getBytes(StandardCharsets.UTF_8));
        System.out.println(verified);
    }

}
