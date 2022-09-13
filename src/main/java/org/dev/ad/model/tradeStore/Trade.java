package org.dev.ad.model.tradeStore;

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
public class Trade {
    private Long id;
    private String type;
    private BigInteger qty;
    private BigDecimal price;
    private BigDecimal amount;
    private String date;
    private String demat;
}
