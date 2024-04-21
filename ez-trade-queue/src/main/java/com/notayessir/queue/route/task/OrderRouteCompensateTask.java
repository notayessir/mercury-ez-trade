//package com.notayessir.queue.route.task;
//
//
//import com.notayessir.queue.route.service.FacadeRouteService;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Slf4j
//public class OrderRouteCompensateTask {
//
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//
//
//    @Autowired
//    private FacadeRouteService facadeRouteService;
//
//
////    @Scheduled
//    public void orderRouteCompensate(){
//        String lockName = "routeOrderCompensate";
//        RLock lock = redissonClient.getLock(lockName);
//        boolean lockSuccess;
//        try {
//            lockSuccess = lock.tryLock(20, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            log.error("fail to get lock:{}", lockName, e);
//            throw new RuntimeException(e);
//        }
//        if (!lockSuccess){
//            log.error("lock is occupied, yield:{}", lockName);
//        }
//
//        try {
//
//            facadeRouteService.orderRouteCompensate();
//
//        } catch (Exception e){
//            log.error("fail to compensate order:", e);
//        } finally {
//            lock.unlock();
//        }
//    }
//
//
//
//}
