package org.dev.ad.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "instruments")
public class InstrumentConfig {

    @Getter
    @Setter
    private Map<String, String> names;
    @Getter
    @Setter
    private Map<String, String> ltp;

}
