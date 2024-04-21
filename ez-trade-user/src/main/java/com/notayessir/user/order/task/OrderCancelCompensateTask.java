package com.notayessir.user.order.task;


import com.notayessir.user.order.service.FacadeOrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class OrderCancelCompensateTask {


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private FacadeOrderService facadeOrderService;

//    @Scheduled
    public void orderCancelCompensate(){
        String lockName = "cancelOrderCompensate";
        RLock lock = redissonClient.getLock(lockName);
        boolean lockSuccess;
        try {
            lockSuccess = lock.tryLock(20, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error("fail to get lock:{}", lockName, e);
            throw new RuntimeException(e);
        }
        if (!lockSuccess){
            log.error("lock is occupied, yield:{}", lockName);
            return;
        }

        try {

            facadeOrderService.orderCancelCompensate();

        } catch (Exception e){
            log.error("fail to compensate order:", e);
        } finally {
            lock.unlock();
        }

    }








}
