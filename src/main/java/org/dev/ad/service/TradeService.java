package org.dev.ad.service;


import org.dev.ad.model.Trade;

import java.util.List;
import java.util.Optional;

public interface TradeService {

    Trade create(Trade trade);

    List<Trade> fetchTrades();

    Optional<Trade> fetchTrade(Long tradeId);

    Trade update(Trade trade);

    void delete(Long tradeId);
}