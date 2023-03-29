package org.dev.ad.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dev.ad.config.InstrumentConfig;
import org.dev.ad.model.pojo.SecurityObj;
import org.dev.ad.model.pojo.TradeObj;
import org.dev.ad.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PositionService {

    private static String TRADES_FILE_PATH = "src/main/resources/trades.txt";
    private static List<TradeObj> fileTrades = Lists.newArrayList();
    @Getter
    private Map<String, List<TradeObj>> securityTradeMap = Maps.newHashMap();
    @Getter
    private Map<String, SecurityObj> securityIdObjMap = Maps.newHashMap();
    @Autowired
    private TradeCalculator tradeCalculator;
    @Autowired
    private InstrumentConfig instrumentConfig;


    @PostConstruct
    public void setup() throws IOException {
        loadTradeFromFile();
        sortTradesPerSecurity();
        createPositions();
        sortPositions();
    }

    private void sortPositions() {
        securityIdObjMap = securityIdObjMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private void createPositions() {
        for (Map.Entry<String, List<TradeObj>> entry : securityTradeMap.entrySet()) {
            String ticker = entry.getKey();
            String name = getName(ticker);
            BigDecimal ltp = getLtp(ticker);

            List<TradeObj> trades = entry.getValue();
            SecurityObj securityObj = PositionBuilder.newBuilder()
                    .calculator(tradeCalculator)
                    .name(name)
                    .ticker(ticker)
                    .ltp(ltp)
                    .trades(trades)
                    .build();
            securityIdObjMap.put(ticker, securityObj);
        }
    }

    private String getName(String ticker) {
        String name;
        try {
            name = instrumentConfig.getNames().get(ticker).trim();
            log.info("Ticker:{} --> Name:{}", ticker, name);
            return name;
        } catch (Exception e) {
            log.error("Failed to get name for ticker {} : Error - {}", ticker, e.getMessage());
        }
        return ticker;
    }

    private BigDecimal getLtp(String ticker) {
        try {
            String ltp = instrumentConfig.getLtp().get(ticker).trim();
            return new BigDecimal(ltp);
        } catch (Exception e) {
            log.error("Failed to get ltp for ticker {} : Error - {}", ticker, e.getMessage());
        }
        return BigDecimal.ZERO;
    }


    public SecurityObj getSecurity(String ticker, Optional<BigDecimal> ltp) {
        SecurityObj securityObj = securityIdObjMap.get(ticker);
        if (ltp.isPresent()) {
            securityObj = PositionBuilder.toBuilder(securityObj)
                    .calculator(tradeCalculator)
                    .ltp(ltp.get())
                    .trades(new ArrayList<>(securityObj.getTrades()))
                    .build();
            securityIdObjMap.put(ticker, securityObj);
        }
        return securityObj;
    }

    public List<SecurityObj> getSecurities() {
        return Lists.newArrayList(securityIdObjMap.values());
    }

    public void loadTradeFromFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(TRADES_FILE_PATH));
        int i = 1;
        for (String line : lines) {
            String[] tokens = line.split(",");
            TradeObj tradeObj = PositionRepository.createTradeObjFromLine(i++, tokens);
            fileTrades.add(tradeObj);
        }
    }

    private void sortTradesPerSecurity() {
        for (TradeObj trade : fileTrades) {
            String ticker = trade.getTicker().toUpperCase();
            List<TradeObj> trades = securityTradeMap.get(ticker);
            if (trades == null) {
                trades = Lists.newLinkedList();
            }
            trades.add(trade);
            securityTradeMap.put(ticker, trades);
        }
    }
}
