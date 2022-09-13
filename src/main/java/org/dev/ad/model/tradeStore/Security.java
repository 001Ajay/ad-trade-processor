package org.dev.ad.model.tradeStore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Security {
    private Long id;
    private String name;
    private String ticker;
    private BigDecimal invested;
    private BigDecimal valuation;
    private BigInteger qty;
    private BigDecimal avgPrice;
    private Set<Trade> trades;
}
