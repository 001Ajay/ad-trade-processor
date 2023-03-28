package org.dev.ad.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.dev.ad.utils.FileUtils;
import org.dev.ad.utils.JsonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LTPService {

    private static Path LAST_TRADED_PRICE_FILEPATH = Paths.get("src/main/resources/lastTradedPrice.json");
    private static ObjectMapper mapper = new ObjectMapper();
    @Getter
    private Map<String, BigDecimal> instrumentPrices = Maps.newHashMap();

    @PostConstruct
    public void loadLTPsFromFile() {
        instrumentPrices.clear();
        try {
            String fileContent = FileUtils.readFileContent(LAST_TRADED_PRICE_FILEPATH);
            log.debug("Loading fileContent : \n{}", fileContent);
            HashMap<String, Object> hashMap = mapper.readValue(fileContent, HashMap.class);
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                String ticker = entry.getKey().toUpperCase();
                BigDecimal ltp = new BigDecimal(entry.getValue().toString());
                instrumentPrices.put(ticker, ltp);
            }
            log.debug(String.valueOf(instrumentPrices));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public BigDecimal getLtp(@NonNull String ticker) {
        BigDecimal ltp = instrumentPrices.get(ticker.toUpperCase());
        return ltp != null ? ltp : BigDecimal.ZERO;
    }

    public void setLtp(@NonNull String ticker, BigDecimal ltp) {
        if (ltp != null)
            instrumentPrices.put(ticker, ltp);
    }

    public String saveAllInstrumentPrices() {
        log.debug("saveAllInstrumentPrices...");
        try {
            String json = mapper.writeValueAsString(instrumentPrices);
            json = JsonUtils.pretifyJsonString(mapper, json);
            log.debug(json);
            FileUtils.writeToFile(LAST_TRADED_PRICE_FILEPATH, json);
            return json;
        } catch (JsonProcessingException e) {
            return instrumentPrices.toString();
        }
    }

}
