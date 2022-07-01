package org.dev.ad.controller;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dev.ad.config.ApplicationConfig;
import org.dev.ad.feign.TradeStoreClient;
import org.dev.ad.model.pojo.SecurityObj;
import org.dev.ad.model.pojo.TradeObj;
import org.dev.ad.model.tradeStore.Security;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/security")
public class PortfolioController {

    private TradeStoreClient tradeStoreClient;
    private ApplicationConfig applicationConfig;
    private BigDecimal BD_100 = BigDecimal.valueOf(100);


    public PortfolioController(TradeStoreClient tradeStoreClient, ApplicationConfig applicationConfig) {
        this.tradeStoreClient = tradeStoreClient;
        this.applicationConfig = applicationConfig;
    }

    @GetMapping
    List<SecurityObj> getAllSecurities(){
        List<Security> securities = tradeStoreClient.securities();
        List<SecurityObj> securityObjList = Lists.newArrayList();
        for(Security security: securities){
            Set<TradeObj> tradeObjs = security.getTrades().stream().map(trade -> {

                BigDecimal amount = trade.getAmount();
                BigDecimal stopLossPercent = amount.subtract((amount.multiply(applicationConfig.getStopLossPercent()).divide(BD_100, 2, RoundingMode.HALF_UP)));
                BigDecimal breakEvenPercent = amount.add((amount.multiply(applicationConfig.getBreakEvenPercent()).divide(BD_100, 2, RoundingMode.HALF_UP)));
                BigDecimal profitPercent1 = amount.add((amount.multiply(applicationConfig.getProfitPercent1()).divide(BD_100, 2, RoundingMode.HALF_UP)));
                BigDecimal profitPercent2 = amount.add((amount.multiply(applicationConfig.getProfitPercent2()).divide(BD_100, 2, RoundingMode.HALF_UP)));
                BigDecimal profitPercent3 = amount.add((amount.multiply(applicationConfig.getProfitPercent3()).divide(BD_100, 2, RoundingMode.HALF_UP)));

                return TradeObj.builder()
                        .id(trade.getId())
                        .type(trade.getType())
                        .qty(trade.getQty())
                        .price(trade.getPrice())
                        .amount(trade.getAmount())
                        .date(trade.getDate())
                        .stopLossPercent(stopLossPercent)
                        .breakEvenPercent(breakEvenPercent)
                        .profitPercent1(profitPercent1)
                        .profitPercent2(profitPercent2)
                        .profitPercent3(profitPercent3)
                        .build();
            })
                    .collect(Collectors.toSet());

            SecurityObj securityObj = SecurityObj.builder()
                    .id(security.getId())
                    .name(security.getName())
                    .ticker(security.getTicker())
                    .invested(security.getInvested())
                    .valuation(security.getValuation())
                    .qty(security.getQty())
                    .avgPrice(security.getAvgPrice())
                    .trades(tradeObjs)
                    .build();
            securityObjList.add(securityObj);
        }
        return securityObjList;
    }
}
