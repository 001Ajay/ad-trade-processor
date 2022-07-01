package org.dev.ad.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Data
public class ApplicationConfig {

    @Value("${trade-processor.stopLossPercent}") private BigDecimal stopLossPercent;
    @Value("${trade-processor.breakEvenPercent}") private BigDecimal breakEvenPercent;
    @Value("${trade-processor.profitPercent1}") private BigDecimal profitPercent1;
    @Value("${trade-processor.profitPercent2}") private BigDecimal profitPercent2;
    @Value("${trade-processor.profitPercent3}") private BigDecimal profitPercent3;

}
