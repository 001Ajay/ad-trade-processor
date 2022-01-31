package org.dev.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("org.dev.ad")
@SpringBootApplication
@EnableDiscoveryClient
public class AdTradeProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdTradeProcessorApplication.class, args);
	}

}
