package org.dev.ad.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InstrumentService {

    private static Path path = Paths.get("src/main/resources/lastTradedPrice.json");
    private static ObjectMapper mapper = new ObjectMapper();

    @Getter private Map<String, BigDecimal> instrumentPrices;

    public InstrumentService() {
        instrumentPrices = Maps.newHashMap();
        loadLTPsFromFile();
    }

    public void loadLTPsFromFile() {
        instrumentPrices.clear();
        try {
            String fileContent = Files.readAllLines(path).stream().collect(Collectors.joining());
            HashMap<String, Double> hashMap = mapper.readValue(fileContent, HashMap.class);
            log.debug(String.valueOf(hashMap));
            for(Map.Entry<String, Double> entry: hashMap.entrySet()){
                instrumentPrices.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
            }
            log.debug(String.valueOf(instrumentPrices));
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public Optional<BigDecimal> getPrice(@NonNull String ticker){
        return Optional.ofNullable(instrumentPrices.get(ticker.trim()));
    }

    public void setPrice(String ticker, BigDecimal ltp) {
        instrumentPrices.put(ticker, ltp);
    }
}
