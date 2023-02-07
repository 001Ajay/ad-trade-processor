package org.dev.ad.crawler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "crawler.urls")
public class DownloadUrlConfigs {
    @Getter
    @Setter
    private List<String> downloads;
}
