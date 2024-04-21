package com.notayessir.user.order.task;


import com.notayessir.user.order.service.FacadeOrderService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class OrderCreateCompensateTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private FacadeOrderService facadeOrderService;


    //    @Scheduled
    public void orderCreateCompensate(){
        String lockName = "createOrderCompensate";
        RLock lock = redissonClient.getLock(lockName);
        boolean lockSuccess;
        try {
            lockSuccess = lock.tryLock(20, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("fail to get lock:{}", lockName, e);
            throw new RuntimeException(e);
        }
        if (!lockSuccess){
            logger.error("lock is occupied, yield:{}", lockName);
        }
        try {

            facadeOrderService.orderCreateCompensate();

        } catch (Exception e){
            logger.error("fail to compensate order:", e);
        }

    }






}
