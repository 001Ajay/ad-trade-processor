package org.dev.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"org.dev.ad"})
@EnableDiscoveryClient
@EnableFeignClients
public class AdTradeProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdTradeProcessorApplication.class, args);
	}

}
