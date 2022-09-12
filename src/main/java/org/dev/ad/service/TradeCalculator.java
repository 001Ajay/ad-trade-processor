package org.dev.ad.service;

import org.dev.ad.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Service
public class TradeCalculator {

    private static BigDecimal BD_100 = BigDecimal.valueOf(100);

    @Autowired
    private ApplicationConfig applicationConfig;

    public BigDecimal findStopLoss(BigDecimal amount){
        return amount.subtract((
                amount.multiply(applicationConfig.getStopLossPercent())
                        .divide(BD_100, 2, RoundingMode.HALF_UP)));
    }

    public BigDecimal findBreakEven(BigDecimal amount){
        return addPercent(amount, applicationConfig.getBreakEvenPercent());
    }

    public BigDecimal findProfitPercent1(BigDecimal amount){
        return addPercent(amount, applicationConfig.getProfitPercent1());
    }

    public BigDecimal findProfitPercent2(BigDecimal amount){
        return addPercent(amount, applicationConfig.getProfitPercent2());
    }

    public BigDecimal findProfitPercent3(BigDecimal amount){
        return addPercent(amount, applicationConfig.getProfitPercent3());
    }

    public BigDecimal addPercent(BigDecimal amount, BigDecimal percentage){
        return amount.add((
                amount.multiply(percentage)
                        .divide(BD_100, 2, RoundingMode.HALF_UP)));
    }

    public BigDecimal findValuation(BigInteger qty, BigDecimal ltp){
        return ltp.multiply(BigDecimal.valueOf(qty.doubleValue()));
    }

    public BigDecimal calculateProfitPerc(BigDecimal invested, BigDecimal profit){
        return profit.divide(invested, RoundingMode.HALF_UP).multiply(BD_100);
    }

}
