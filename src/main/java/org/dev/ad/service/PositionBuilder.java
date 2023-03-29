package org.dev.ad.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.dev.ad.model.pojo.SecurityObj;
import org.dev.ad.model.pojo.TradeObj;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class PositionBuilder {

    private static PositionBuilder builder;
    private TradeCalculator calculator;
    private String ticker;
    private String name;
    private BigDecimal ltp = BigDecimal.ZERO;
    private List<TradeObj> trades;
    private static boolean isNewSecurity = false;
    private static SecurityObj securityObj;

    private PositionBuilder() {
    }

    public static PositionBuilder newBuilder() {
        builder = new PositionBuilder();
        isNewSecurity = true;
        return builder;
    }

    public static PositionBuilder toBuilder(SecurityObj security) {
        builder = new PositionBuilder();
        builder.ltp = security.getLtp();
        builder.name = security.getName();
        builder.ticker = security.getTicker();
        builder.trades = new ArrayList<>(security.getTrades());
        securityObj = security;
        return builder;
    }

    public PositionBuilder calculator(TradeCalculator tradeCalculator) {
        this.calculator = tradeCalculator;
        return builder;
    }

    public PositionBuilder ticker(String ticker) {
        this.ticker = ticker;
        return builder;
    }

    public PositionBuilder name(String name) {
        this.name = name;
        return builder;
    }

    public PositionBuilder ltp(BigDecimal ltp) {
        this.ltp = ltp;
        return builder;
    }

    public PositionBuilder trades(List<TradeObj> trades) {
        this.trades = trades;
        return builder;
    }

    public SecurityObj build() {
        if (isNewSecurity) {
            securityObj = createDefaultSecurity();
        }
        securityObj.setLtp(ltp);

        List<TradeObj> processedTrades = Lists.newArrayList();
        for (TradeObj trade : trades) {
            enrichTrade(trade, Optional.of(ltp), calculator);
            processedTrades.add(trade);
        }

        securityObj.getTrades().clear();
        securityObj.getTrades().addAll(trades);

        enrichSecurity(securityObj);
        return securityObj;
    }

    private void enrichSecurity(SecurityObj securityObj) {
        Set<TradeObj> trades = securityObj.getTrades();
        BigDecimal ltp = securityObj.getLtp();
        BigDecimal totalInvested = securityObj.getInvested();
        BigInteger totalQty = securityObj.getQty();
        for (TradeObj tradeObj : trades) {
            String type = tradeObj.getType();
            if (type.equalsIgnoreCase("BUY")) {
                totalInvested = totalInvested.add(tradeObj.getAmount());
                totalQty = totalQty.add(tradeObj.getQty());
            } else if (type.equalsIgnoreCase("SELL")) {
                totalInvested = totalInvested.subtract(tradeObj.getAmount());
                totalQty = totalQty.subtract(tradeObj.getQty());
            } else {
                log.error("Trade ignored : {}", tradeObj.toString());
            }
        }
        securityObj.setInvested(totalInvested);
        securityObj.setQty(totalQty);
        if (totalInvested.doubleValue() > 0.0 && totalQty.intValue() > 0) {
            BigDecimal avgPrice = totalInvested.divide(BigDecimal.valueOf(totalQty.doubleValue()), 6, RoundingMode.HALF_UP);
            securityObj.setAvgPrice(avgPrice);

            BigDecimal valuation = calculator.findValuation(totalQty, ltp);
            securityObj.setValuation(valuation);

            BigDecimal profit = valuation.subtract(totalInvested);
            securityObj.setProfit(profit);

            BigDecimal profitPercent = calculator.findProfitPerc(totalInvested, profit);
            securityObj.setProfitPercent(profitPercent);
        }
    }

    private SecurityObj createDefaultSecurity() {
        return SecurityObj.builder()
                .name(name)
                .ticker(ticker)
                .qty(BigInteger.ZERO)
                .avgPrice(BigDecimal.ZERO)
                .ltp(ltp)
                .trades(Sets.newHashSet())
                .invested(BigDecimal.ZERO)
                .valuation(BigDecimal.ZERO)
                .profit(BigDecimal.ZERO)
                .build();
    }

    private static void enrichTrade(TradeObj trade, Optional<BigDecimal> ltp, TradeCalculator calculator) {
        BigInteger qty = trade.getQty();
        if (qty != null && qty.intValue() > 0) {
            BigDecimal price = trade.getPrice();
            if (price != null && price.doubleValue() > 0) {
                BigDecimal investment = price.multiply(BigDecimal.valueOf(qty.doubleValue()));
                trade.setAmount(investment);

                BigDecimal stopLoss = calculator.findStopLoss(investment);
                trade.setStopLoss(stopLoss);

                BigDecimal breakEvenPercent = calculator.findBreakEven(investment);
                trade.setBreakEvenPercent(breakEvenPercent);

                BigDecimal profitPercent1 = calculator.findProfitPercent1(investment);
                trade.setProfitPercent1(profitPercent1);

                BigDecimal profitPercent2 = calculator.findProfitPercent2(investment);
                trade.setProfitPercent2(profitPercent2);

                BigDecimal profitPercent3 = calculator.findProfitPercent3(investment);
                trade.setProfitPercent3(profitPercent3);

                if (ltp.isPresent() && ltp.get().doubleValue() > 0.00) {
                    setLtpToTrade(trade, ltp.get(), calculator);
                }

            } else {
                log.error("PRICE is null or zero for {}", trade);
            }
        } else {
            log.error("QTY is null or zero for {}", trade);
        }
    }

    private static void setLtpToTrade(TradeObj tradeObj, BigDecimal ltp, TradeCalculator calculator) {
        BigDecimal investment = tradeObj.getAmount();
        BigInteger qty = tradeObj.getQty();
        tradeObj.setLtp(ltp);
        if (ltp.doubleValue() > 0.00) {
            BigDecimal valuation = calculator.findValuation(qty, ltp);
            tradeObj.setValuation(valuation);

            BigDecimal profit = valuation.subtract(investment);
            tradeObj.setProfit(profit);

            BigDecimal profitPercent = calculator.findProfitPerc(investment, profit);
            tradeObj.setProfitPercent(profitPercent);
        }
    }

}
