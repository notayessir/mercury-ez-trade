package com.notayessir.user.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.notayessir.user.coin.bo.CheckAccountResultBO;
import com.notayessir.user.coin.entity.CoinPair;
import com.notayessir.user.order.bo.CreateOrderReqBO;
import com.notayessir.user.user.entity.Account;

import java.math.BigDecimal;

public interface IAccountService extends IService<Account> {

    Account getAccount(Long userId, String currency);

    Account createAccountIfAbsent(Long userId, String currency);

    void moveAvailableToHold(Account account, BigDecimal numOfAvailable);

    Account moveAvailableToHold(Long userId, String currency, BigDecimal numOfAvailable);

    void addAvailable(Account account, BigDecimal numOfAvailable);
    Account addAvailable(Long userId, String currency, BigDecimal numOfAvailable);

    void addHoldOrAvailable(Account account, BigDecimal numOfHold, BigDecimal numOfAvailable);

    Account addHoldOrAvailable(Long userId, String currency, BigDecimal numOfHold, BigDecimal numOfAvailable);

    void moveHoldToAvailable(Account account, BigDecimal numOfHold);
    Account moveHoldToAvailable(Long userId, String currency, BigDecimal numberOfHold);


    void deductHold(Account account,BigDecimal numOfHold);
    Account deductHold(Long userId,String currency, BigDecimal numOfHold);

    void exchange(Long userId, String toDeductCurrency, BigDecimal toDeductQty, String toAddCurrency, BigDecimal toAddAmount);

    CheckAccountResultBO checkAccount(CreateOrderReqBO req, CoinPair coinPair);
}
