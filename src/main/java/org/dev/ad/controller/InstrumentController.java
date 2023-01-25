package org.dev.ad.controller;

import lombok.extern.slf4j.Slf4j;
import org.dev.ad.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/instrument")
public class InstrumentController {

    @Autowired private InstrumentService instrumentService;

    @GetMapping(value = "/ltp/{ticker}")
    BigDecimal getLastTradedPrice(@PathVariable String ticker){
        log.info("getLastTradedPrice : ticker={}",ticker);
        Optional<BigDecimal> ltp = instrumentService.getPrice(ticker);
        return ltp.isPresent() ? ltp.get() : BigDecimal.ZERO;
    }

    @GetMapping(value = "/ltp/{ticker}/{ltp}")
    String setLastTradedPrice(@PathVariable String ticker, @PathVariable String ltp) {
        log.info("setLastTradedPrice : ticker={}, ltp={}", ticker, ltp);
//        BigDecimal price = new BigDecimal(ltp.contains(".") ? ltp : ltp+".00" );
        instrumentService.setPrice(ticker, ltp);
        return ticker + " ---> " + ltp;
    }

    @GetMapping(value = "/ltp/reload/ltpFile")
    Map<String, BigDecimal> reloadLtpFile(){
        log.info("reloadLtpFile...");
        instrumentService.loadLTPsFromFile();
        return instrumentService.getInstrumentPrices();
    }

    @GetMapping(value = "/ltp")
    Map<String, BigDecimal> getInstrumentPrices() {
        log.info("getInstrumentPrices...");
        return instrumentService.getInstrumentPrices();
    }

    @GetMapping(value = "/ltp/saveAll")
    String saveAllInstrumentPrices() {
        log.info("getInstrumentPrices...");
        return instrumentService.saveAllInstrumentPrices();
    }
}
