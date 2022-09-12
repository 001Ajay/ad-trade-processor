package org.dev.ad.controller;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dev.ad.feign.TradeStoreClient;
import org.dev.ad.model.pojo.SecurityObj;
import org.dev.ad.model.pojo.TradeObj;
import org.dev.ad.model.tradeStore.Security;
import org.dev.ad.service.InstrumentService;
import org.dev.ad.service.TradeCalculator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/security")
public class PortfolioController {

    private TradeStoreClient tradeStoreClient;
    private TradeCalculator tradeCalculator;
    private InstrumentService instrumentService;
    private List<SecurityObj> securityObjList;

    public PortfolioController(TradeStoreClient tradeStoreClient,
                               TradeCalculator tradeCalculator,
                               InstrumentService instrumentService) {
        this.tradeStoreClient = tradeStoreClient;
        this.tradeCalculator = tradeCalculator;
        this.instrumentService = instrumentService;
        this.securityObjList = Lists.newArrayList();
        reloadInformation();
    }

    @GetMapping(value="/reload")
    public List<SecurityObj> reloadInformation(){
        log.debug("reloading information...");
        // Load Last Traded Prices
        instrumentService.loadLTPsFromFile();
        // Load Trades
        List<Security> securities = tradeStoreClient.securities();
        // ReCalculate positions
        securityObjList.clear();
        for(Security security: securities){
            String ticker = security.getTicker();
            Optional<BigDecimal> aPrice = instrumentService.getPrice(ticker);
            BigDecimal ltp = aPrice.orElse(BigDecimal.ZERO);

            Set<TradeObj> tradeObjs = security.getTrades().stream().map(trade -> {
                BigDecimal investment = trade.getAmount();
                BigInteger qty = trade.getQty();
                BigDecimal valuation = tradeCalculator.findValuation(qty, ltp);
                BigDecimal stopLossPercent = tradeCalculator.findStopLoss(investment);
                BigDecimal breakEvenPercent = tradeCalculator.findBreakEven(investment);
                BigDecimal profitPercent1 = tradeCalculator.findProfitPercent1(investment);
                BigDecimal profitPercent2 = tradeCalculator.findProfitPercent2(investment);
                BigDecimal profitPercent3 = tradeCalculator.findProfitPercent3(investment);

                return TradeObj.builder()
                        .id(trade.getId())
                        .stock(security.getName())
                        .ticker(ticker)
                        .type(trade.getType())
                        .demat(security.getDemat())
                        .qty(qty)
                        .price(trade.getPrice())
                        .amount(investment)
                        .date(trade.getDate())
                        .stopLossPercent(stopLossPercent)
                        .breakEvenPercent(breakEvenPercent)
                        .profitPercent1(profitPercent1)
                        .profitPercent2(profitPercent2)
                        .profitPercent3(profitPercent3)
                        .ltp(ltp)
                        .valuation(valuation)
                        .profit(valuation.subtract(investment))
                        .build();
            })
                    .collect(Collectors.toSet());

            BigDecimal valuation = tradeCalculator.findValuation(security.getQty(), ltp);
            BigDecimal invested = security.getInvested();
            BigDecimal profit = valuation.subtract(invested);
            BigDecimal profitPercent = tradeCalculator.calculateProfitPerc(profit, invested);
            SecurityObj securityObj = SecurityObj.builder()
                    .id(security.getId())
                    .name(security.getName())
                    .ticker(ticker)
                    .invested(invested)
                    .qty(security.getQty())
                    .avgPrice(security.getAvgPrice())
                    .trades(tradeObjs)
                    .ltp(ltp)
                    .valuation(valuation)
                    .profit(profit)
                    .profitPercent(profitPercent)
                    .build();
            securityObjList.add(securityObj);
        }
        return securityObjList;
    }

    @GetMapping
    public List<SecurityObj> getAllSecurities(){
        log.debug("getAllSecurities...");
        return securityObjList;
    }
}
