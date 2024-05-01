package com.notayessir.user.user.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notayessir.common.constant.EnumFieldDeleted;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.common.web.BusinessException;
import com.notayessir.user.api.order.constant.EnumEntrustSide;
import com.notayessir.user.coin.bo.CheckAccountResultBO;
import com.notayessir.user.coin.entity.CoinPair;
import com.notayessir.user.order.bo.CreateOrderReqBO;
import com.notayessir.user.user.entity.Account;
import com.notayessir.user.user.mapper.AccountMapper;
import com.notayessir.user.user.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notayessir.user.common.vo.EnumUserResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {





    @Override
    public Account getAccount(Long userId, String currency) {
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUserId, userId)
                .eq(Account::getCurrency, currency);
        return getOne(queryWrapper);
    }

    @Override
    public Account createAccountIfAbsent(Long userId, String currency) {
        Account account = getAccount(userId, currency);

        if (Objects.isNull(account)){
            account = buildNewAccount(userId, currency);
            save(account);
        }

        return account;
    }

    private Account buildNewAccount(Long userId, String currency) {
        Account account = new Account();
        account.setUserId(userId);
        account.setCurrency(currency);
        account.setAvailable(BigDecimal.ZERO.toString());
        account.setHold(BigDecimal.ZERO.toString());
        account.setId(IdUtil.getSnowflakeNextId());
        account.setVersion(EnumFieldVersion.INIT.getCode());
        LocalDateTime now = LocalDateTime.now();
        account.setCreateTime(now);
        account.setUpdateTime(now);
        account.setDeleted(EnumFieldDeleted.NOT_DELETED.getCode());
        return account;
    }


    private boolean updateAccount(Account account) {
        int curVersion = account.getVersion();
        account.setVersion(curVersion + 1);
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getVersion, curVersion)
                .eq(Account::getId, account.getId());
        return update(account, queryWrapper);
    }

    @Override
    public Account moveAvailableToHold(Long userId, String currency, BigDecimal numOfAvailable) {
        Account account = getAccount(userId, currency);
        moveAvailableToHold(account, numOfAvailable);
        return account;
    }

    @Override
    public void moveAvailableToHold(Account account, BigDecimal numOfAvailable) {
        BigDecimal hold = new BigDecimal(account.getHold()).add(numOfAvailable);
        BigDecimal available = new BigDecimal(account.getAvailable()).subtract(numOfAvailable);
        account.setHold(hold.toString());
        account.setAvailable(available.toString());
        account.setUpdateTime(LocalDateTime.now());
        boolean success = updateAccount(account);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }

    }


    @Override
    public void moveHoldToAvailable(Account account, BigDecimal numOfHold) {
        BigDecimal hold = new BigDecimal(account.getHold()).subtract(numOfHold);
        BigDecimal available = new BigDecimal(account.getAvailable()).add(numOfHold);
        account.setHold(hold.toString());
        account.setAvailable(available.toString());
        account.setUpdateTime(LocalDateTime.now());
        boolean success = updateAccount(account);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }

    }

    @Override
    public void addAvailable(Account account, BigDecimal numOfAvailable) {
        account.setAvailable(new BigDecimal(account.getAvailable()).add(numOfAvailable).toString());
        account.setUpdateTime(LocalDateTime.now());
        boolean success = updateAccount(account);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }
    }

    @Override
    public Account addAvailable(Long userId, String currency, BigDecimal numOfAvailable) {
        Account account = createAccountIfAbsent(userId, currency);
        addAvailable(account, numOfAvailable);
        return account;
    }


    @Override
    public void addHoldOrAvailable(Account account, BigDecimal numOfHold, BigDecimal numOfAvailable) {
        account.setHold(new BigDecimal(account.getHold()).add(numOfHold).toString());
        account.setAvailable(new BigDecimal(account.getAvailable()).add(numOfAvailable).toString());
        account.setUpdateTime(LocalDateTime.now());
        boolean success = updateAccount(account);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }
    }

    @Override
    public Account addHoldOrAvailable(Long userId, String currency, BigDecimal numOfHold, BigDecimal numOfAvailable) {
        Account account = createAccountIfAbsent(userId, currency);
        addHoldOrAvailable(account, numOfHold, numOfAvailable);
        return account;
    }


    @Override
    public Account moveHoldToAvailable(Long userId, String currency, BigDecimal numberOfHold) {
        Account account = getAccount(userId, currency);
        if (Objects.isNull(numberOfHold) || numberOfHold.compareTo(BigDecimal.ZERO) == 0)
            return account;
        moveHoldToAvailable(account, numberOfHold);
        return account;
    }

    @Override
    public void exchange(Long userId, String toDeductCurrency, BigDecimal toDeductQty, String toAddCurrency, BigDecimal toAddQty) {
        deductHold(userId, toDeductCurrency, toDeductQty);
        addAvailable(userId, toAddCurrency, toAddQty);
    }

    @Override
    public CheckAccountResultBO checkAccount(CreateOrderReqBO req, CoinPair coinPair) {
        // default sell
        BigDecimal toDeduct = req.getEntrustQty();
        String currency = coinPair.getQuoteCurrency();
        if (StringUtils.equalsIgnoreCase(req.getSide(), EnumEntrustSide.BUY.getSide())){
            currency = coinPair.getBaseCurrency();
            toDeduct = req.getEntrustAmount();
        }


        Account account = createAccountIfAbsent(req.getUserId(), currency);
        BigDecimal available = new BigDecimal(account.getAvailable());
        if (available.compareTo(toDeduct) < 0){
            throw new BusinessException(EnumUserResponse.AVAILABLE_INSUFFICIENT.getCode(), EnumUserResponse.AVAILABLE_INSUFFICIENT.getMessage());
        }

        CheckAccountResultBO resultBO = new CheckAccountResultBO();
        resultBO.setAccount(account);
        return resultBO;
    }

    @Override
    public List<Account> getAccounts(Long userId) {
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUserId, userId);
        return list(queryWrapper);
    }

    @Override
    public void deductHold(Account account, BigDecimal numOfHold) {
        account.setHold(new BigDecimal(account.getHold()).subtract(numOfHold).toString());
        account.setUpdateTime(LocalDateTime.now());
        boolean success = updateAccount(account);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }
    }

    @Override
    public Account deductHold(Long userId, String currency, BigDecimal numOfHold) {
        Account account = getAccount(userId, currency);
        deductHold(account, numOfHold);
        return account;
    }



}
