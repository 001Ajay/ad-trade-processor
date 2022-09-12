package org.dev.ad.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityObj {
    private Long id;
    private String name;
    private String ticker;
    private BigDecimal invested;
    private BigDecimal ltp;
    private BigDecimal valuation;
    private BigInteger qty;
    private BigDecimal avgPrice;
    private Set<TradeObj> trades;
    private BigDecimal profit;
    private BigDecimal profitPercent;
}
