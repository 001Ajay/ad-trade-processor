package org.dev.ad.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "txn")
public class Txn {
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long txn_id;
	private Long portfolio_id;
	private String stock;
	private String type;
	private BigDecimal qty;
	private BigDecimal price;
	private BigDecimal amount;
}