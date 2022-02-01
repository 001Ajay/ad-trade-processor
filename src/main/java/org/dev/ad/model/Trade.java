package org.dev.ad.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trade {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private Long tradeId;
     String stock;
     String ticker;
     int qty;
     BigDecimal price;
     BigDecimal amount;
     int portfolioId;
     BigDecimal currentPrice;
     BigDecimal delta;
     String status;
     String trade_date;
}
