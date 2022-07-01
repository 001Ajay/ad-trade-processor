package org.dev.ad.feign;

import org.dev.ad.model.tradeStore.Security;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "trade-store-client", url = "localhost:9000", path = "/ad-trade-store")
public interface TradeStoreClient {

    @GetMapping("/security")
    List<Security> securities();
}
