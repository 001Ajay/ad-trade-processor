package org.dev.ad.service;

import lombok.extern.slf4j.Slf4j;
import org.dev.ad.model.Trade;
import org.dev.ad.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class TradeServiceImpl implements TradeService{

    private TradeRepository repo;

    public TradeServiceImpl(TradeRepository TradeRepository){
        this.repo = TradeRepository;
    }

    @Override
    public Trade create(Trade trade) {
        return repo.save(trade);
    }

    @Override
    public List<Trade> fetchTrades() {
        return (List<Trade>) repo.findAll();
    }

    @Override
    public Optional<Trade> fetchTrade(Long tradeId) {
        return repo.findById(tradeId);
    }

    @Override
    public Trade update(Trade trade) {
        repo.deleteById(trade.getTradeId());
        return repo.save(trade);
    }

    @Override
    public void delete(Long tradeId) {
        repo.deleteById(tradeId);
    }
}