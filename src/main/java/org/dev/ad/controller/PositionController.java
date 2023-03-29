package org.dev.ad.controller;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dev.ad.model.pojo.SecurityObj;
import org.dev.ad.model.pojo.TradeObj;
import org.dev.ad.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/security")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping(value = "/{ticker}/{ltp}")
    SecurityObj setLtp(@PathVariable String ticker, @PathVariable String ltp) {
        log.info("setLtp : ticker={}, ltp={}", ticker, ltp);
        return positionService.getSecurity(ticker, Optional.of(new BigDecimal(ltp)));
    }

    @GetMapping(value = "/{ticker}")
    SecurityObj getPositionInformation(@PathVariable String ticker) {
        log.info("getPositionInformation : ticker={}", ticker);
        return positionService.getSecurity(ticker, Optional.empty());
    }

    @GetMapping(value = "/trades/{ticker}")
    List<TradeObj> getTrades(@PathVariable String ticker) {
        log.info("getPositionInformation : ticker={}", ticker);
        Set<TradeObj> trades = positionService.getSecurity(ticker, Optional.empty()).getTrades();
        return Lists.newArrayList(trades);
    }

    @GetMapping
    public List<SecurityObj> getAllSecurities() {
        log.debug("getAllSecurities...");
        return positionService.getSecurities();
    }
}
