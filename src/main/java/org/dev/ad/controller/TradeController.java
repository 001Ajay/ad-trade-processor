package org.dev.ad.controller;

import lombok.extern.slf4j.Slf4j;
import org.dev.ad.model.Trade;
import org.dev.ad.service.TradeService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
public class TradeController {

    private TradeService service;

    public TradeController(TradeService tradeService){
        this.service = tradeService;
    }

    @PostMapping("/trades")
    public Trade save(@RequestBody Trade newTrade){
        log.debug("Creating new Trade");
        return service.create(newTrade);
    }

    @GetMapping("/trades")
    public List<Trade> getTrades(){
        log.debug("Get all trades...");
        List<Trade> trades = service.fetchTrades();
        postProcessTrades(trades);
        log.debug("[%s] trades found...", trades.size());
        return trades;
    }

    private void postProcessTrades(List<Trade> trades) {
        trades.stream().forEach(trade -> {
            trade.setAmount(trade.getPrice().multiply(BigDecimal.valueOf(trade.getQty())));
        });
    }

    @PostMapping("/trades/{id}")
    public Trade update(@RequestBody Trade trade,
                      @PathVariable("id") Long tradeId){
        log.debug("Updating Trade [tradeId={}]", tradeId);
        return service.update(trade);
    }

    @GetMapping("/trades/delete/{id}")
    public String delete(@PathVariable("id") Long tradeId){
        log.debug("Deleting Trade [tradeId={}]", tradeId);
        service.delete(tradeId);
        return "Deleted Trade Successfully";
    }
}
