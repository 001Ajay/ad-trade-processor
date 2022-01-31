package org.dev.ad.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.dev.ad.model.Trade;
import org.dev.ad.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/txn")
public class TradeController extends CrudController<Trade, Long> {
	
	@Autowired
	private TradeRepository repo;

	@Override
	protected JpaRepository<Trade, Long> getRepo() {
		return repo;
	}
	
	@GetMapping("/buy")
	public Trade buy(@ApiParam(value = "Add object in database table", required = true) 
		//@Valid @RequestParam long portfolioId,
		@Valid @RequestParam String stock,
		@Valid @RequestParam BigDecimal qty,
		@Valid @RequestParam BigDecimal price
		) {
		
		long portfolioId = 1;
		Trade trade = createTrade(portfolioId, stock, "BUY", qty, price);
		
		trade = getRepo().save(trade);
		System.out.println("--> "+createInsertSql(trade));
		
		return trade;
	}
	
	@GetMapping("/sell")
	public Trade sell(@ApiParam(value = "Add object in database table", required = true) 
		@Valid @RequestParam long portfolioId,
		@Valid @RequestParam String stock,
		@Valid @RequestParam BigDecimal qty,
		@Valid @RequestParam BigDecimal price
		) {
		
		Trade trade = createTrade(portfolioId, stock, "SELL", qty, price);
		
		trade = getRepo().save(trade);
		System.out.println("--> "+createInsertSql(trade));
		
		return trade;
	}
	
	private static String INSERT_SQL = "insert into txn(stock,type,qty,price,amount,portfolio_id) values('%s','%s',%s,%s,%s,%x);";
	
	@GetMapping("/backup")
	public @ResponseBody String backupTable() {
		List<Trade> all = findAll();
		all.stream().forEach(trade -> {
				System.out.println(createInsertSql(trade));
		});
		return "done";
	}
	
	private String createInsertSql(Trade trade) {
		return String.format(INSERT_SQL, 
				trade.getStock(),
				trade.getType(),
				trade.getQty().toPlainString(),
				trade.getPrice().toPlainString(),
				trade.getQty().multiply(trade.getPrice()).toPlainString(),
				trade.getPortfolio_id());
	}
	

	private Trade createTrade(long portfolioId, String stock, String type, BigDecimal qty, BigDecimal price) {
		Trade trade = new Trade();
		trade.setPortfolio_id(portfolioId);
		trade.setStock(stock);
		trade.setType(type);
		trade.setQty(qty);
		trade.setPrice(price);
		trade.setAmount(price.multiply(qty));
		return trade;
	}

}
