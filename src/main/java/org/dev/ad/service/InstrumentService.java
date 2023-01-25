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

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class InstrumentService {

    private static Path LAST_TRADED_PRICE_FILEPATH = Paths.get("src/main/resources/lastTradedPrice.json");
    private static ObjectMapper mapper = new ObjectMapper();

    @Getter private Map<String, BigDecimal> instrumentPrices;

    public InstrumentService() {
        instrumentPrices = Maps.newHashMap();
        loadLTPsFromFile();
    }

    public void loadLTPsFromFile() {
        instrumentPrices.clear();
        try {
            String fileContent = FileUtils.readFileContent(LAST_TRADED_PRICE_FILEPATH);
            log.debug("Loading fileContent : \n{}", fileContent);
            HashMap<String, Object> hashMap = mapper.readValue(fileContent, HashMap.class);
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                instrumentPrices.put(entry.getKey(), new BigDecimal(entry.getValue().toString()));
            }
            log.debug(String.valueOf(instrumentPrices));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Optional<BigDecimal> getPrice(@NonNull String ticker) {
        return Optional.ofNullable(instrumentPrices.get(ticker.trim()));
    }

    public void setPrice(String ticker, String ltp) {
        instrumentPrices.put(ticker, new BigDecimal(ltp));
        saveAllInstrumentPrices();
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
