package org.dev.ad.repository;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.dev.ad.model.pojo.SecurityObj;
import org.dev.ad.model.pojo.TradeObj;
import org.dev.ad.service.TradeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
// TODO: Try to remove this and cleanup
public class PositionRepository {

    @Autowired
    private TradeCalculator tradeCalculator;

    private Map<String, SecurityObj> securityObjMap = Maps.newHashMap();
    private static String TRADES_FILE_PATH = "src/main/resources/trades.txt";

    public static TradeObj createTradeObjFromLine(long tradeId, String[] lineTokens) {
        int c = 0;
        String ticker = lineTokens[c++];
        String date = lineTokens[c++];
        String type = lineTokens[c++];
        String qtyStr = lineTokens[c++];
        String priceStr = lineTokens[c++];
        String demat = lineTokens[c++];
        return createTrade(Optional.of(tradeId), ticker, date, type, qtyStr, priceStr, demat);
    }

    public void loadTradeFromFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(TRADES_FILE_PATH));
        int i = 1;
        for (String line : lines) {
            String[] tokens = line.split(",");
            TradeObj tradeObj = createTradeObjFromLine(i++, tokens);
            addTrade(tradeObj);
        }
//        enrichTradeInfo();
    }

    public Set<TradeObj> findTrades(@NonNull String ticker) {
        return findSecurity(ticker).getTrades();
    }

    public void addTrade(TradeObj trade) {
        if (trade.getStopLoss() == null) {
            enrichTradeInfo(trade);
        }
        addTradeToSecurity(findSecurity(trade.getTicker()), trade);
    }

    private void enrichTradeInfo(TradeObj tradeObj) {
        BigInteger qty = tradeObj.getQty();
        BigDecimal price = tradeObj.getPrice();

        BigDecimal investment = price.multiply(BigDecimal.valueOf(qty.doubleValue()));
        tradeObj.setAmount(investment);

        BigDecimal stopLoss = tradeCalculator.findStopLoss(investment);
        tradeObj.setStopLoss(stopLoss);

        BigDecimal breakEvenPercent = tradeCalculator.findBreakEven(investment);
        tradeObj.setBreakEvenPercent(breakEvenPercent);

        BigDecimal profitPercent1 = tradeCalculator.findProfitPercent1(investment);
        tradeObj.setProfitPercent1(profitPercent1);

        BigDecimal profitPercent2 = tradeCalculator.findProfitPercent2(investment);
        tradeObj.setProfitPercent2(profitPercent2);

        BigDecimal profitPercent3 = tradeCalculator.findProfitPercent3(investment);
        tradeObj.setProfitPercent3(profitPercent3);
    }

    public SecurityObj setLtp(@NonNull String ticker, @NonNull String ltpStr) {
        BigDecimal ltp = new BigDecimal(ltpStr);
        SecurityObj security = findSecurity(ticker.toUpperCase());
        security.setLtp(ltp);
        security.getTrades().forEach(tradeObj -> setLtpToTrade(tradeObj, ltp));

        BigInteger totalQty = security.getQty();
        if (totalQty.intValue() > 0) {
            BigDecimal totalInvested = security.getInvested();

            BigDecimal valuation = tradeCalculator.findValuation(totalQty, ltp);
            security.setValuation(valuation);

            BigDecimal totalProfit = valuation.subtract(totalInvested);
            security.setProfit(totalProfit);

            BigDecimal totalProfitPercent = tradeCalculator.findProfitPerc(totalInvested, totalProfit);
            security.setProfitPercent(totalProfitPercent);
        }

        return security;
    }

    public void setLtpToTrade(TradeObj tradeObj, BigDecimal ltp) {
        BigDecimal investment = tradeObj.getAmount();
        BigInteger qty = tradeObj.getQty();

        BigDecimal valuation = tradeCalculator.findValuation(qty, ltp);
        tradeObj.setValuation(valuation);

        BigDecimal profit = valuation.subtract(investment);
        tradeObj.setProfit(profit);

        BigDecimal profitPercent = tradeCalculator.findProfitPerc(investment, profit);
        tradeObj.setProfitPercent(profitPercent);
    }

    public SecurityObj findSecurity(String ticker) {
        SecurityObj securityObj = securityObjMap.get(ticker);
        if (securityObj == null) {
            securityObj = createSecurity(ticker);
            securityObjMap.put(ticker, securityObj);
        }
        return securityObj;
    }

    private SecurityObj createSecurity(@NonNull String ticker) {
        ticker = ticker.toUpperCase();
        return SecurityObj.builder()
                .ticker(ticker)
                .qty(BigInteger.ZERO)
                .avgPrice(BigDecimal.ZERO)
                .trades(Sets.newHashSet())
                .invested(BigDecimal.ZERO)
                .valuation(BigDecimal.ZERO)
                .profit(BigDecimal.ZERO)
                .profitPercent(BigDecimal.ZERO)
                .build();
    }


    public void addTradeToSecurity(SecurityObj securityObj, TradeObj tradeObj) {
        BigDecimal totalInvested = securityObj.getInvested();
        BigInteger totalQty = securityObj.getQty();
        if (tradeObj.getType().equalsIgnoreCase("BUY")) {
            totalInvested = totalInvested.add(tradeObj.getAmount());
            totalQty = totalQty.add(tradeObj.getQty());

        } else if (tradeObj.getType().equalsIgnoreCase("SELL")) {
            totalInvested = totalInvested.subtract(tradeObj.getAmount());
            totalQty = totalQty.subtract(tradeObj.getQty());

        } else {
            log.error("Trade ignored : {}", tradeObj.toString());
        }
        securityObj.setInvested(totalInvested);
        securityObj.setQty(totalQty);
        BigDecimal avgPrice = totalInvested.divide(BigDecimal.valueOf(totalQty.doubleValue()), 6, RoundingMode.HALF_UP);
        securityObj.setAvgPrice(avgPrice);
        securityObj.getTrades().add(tradeObj);
        securityObjMap.put(tradeObj.getTicker(), securityObj);
    }


    // TODO: deleteTradeFromSecurity
    // TODO: addTradeToFile
    // TODO: deleteTradeFromFile
    // TODO: saveCurrentTradesToFile


    public static TradeObj createTrade(Optional<Long> id, String ticker, String date, String type, String qtyStr, String priceStr, String demat) {
        BigInteger qty = new BigInteger(qtyStr);
        BigDecimal price = new BigDecimal(priceStr);
        return TradeObj.builder()
                .id(id.isPresent() ? id.get() : System.currentTimeMillis())
                .stock(ticker)
                .ticker(ticker)
                .type(type)
                .demat(demat)
                .qty(qty)
                .price(price)
                .date(formatDate.apply(date))
                .build();
    }

    private static SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat targetDateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);

    private static Function<String, String> formatDate = dateStr -> {
        try {
            return targetDateFormat.format(originalDateFormat.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    };
}
