package org.dev.ad.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeObj {

    private Long id;
    private String type;
    private BigInteger qty;
    private BigDecimal price;
    private BigDecimal amount;
    private String date;
    private BigDecimal stopLossPercent;
    private BigDecimal breakEvenPercent;
    private BigDecimal profitPercent1;
    private BigDecimal profitPercent2;
    private BigDecimal profitPercent3;

}
