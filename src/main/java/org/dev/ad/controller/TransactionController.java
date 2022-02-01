package org.dev.ad.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.dev.ad.model.Txn;
import org.dev.ad.repository.TxnRepository;
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
public class TransactionController extends CrudController<Txn, Long> {
	
	@Autowired
	private TxnRepository repo;

	@Override
	protected JpaRepository<Txn, Long> getRepo() {
		return repo;
	}
	
	@GetMapping("/buy")
	public Txn buy(@ApiParam(value = "Add object in database table", required = true)
		//@Valid @RequestParam long portfolioId,
		@Valid @RequestParam String stock,
				   @Valid @RequestParam BigDecimal qty,
				   @Valid @RequestParam BigDecimal price
		) {
		
		long portfolioId = 1;
		Txn trade = createTrade(portfolioId, stock, "BUY", qty, price);
		
		trade = getRepo().save(trade);
		System.out.println("--> "+createInsertSql(trade));
		
		return trade;
	}
	
	@GetMapping("/sell")
	public Txn sell(@ApiParam(value = "Add object in database table", required = true)
		@Valid @RequestParam long portfolioId,
					@Valid @RequestParam String stock,
					@Valid @RequestParam BigDecimal qty,
					@Valid @RequestParam BigDecimal price
		) {
		
		Txn trade = createTrade(portfolioId, stock, "SELL", qty, price);
		
		trade = getRepo().save(trade);
		System.out.println("--> "+createInsertSql(trade));
		
		return trade;
	}
	
	private static String INSERT_SQL = "insert into txn(stock,type,qty,price,amount,portfolio_id) values('%s','%s',%s,%s,%s,%x);";
	
	@GetMapping("/backup")
	public @ResponseBody String backupTable() {
		List<Txn> all = findAll();
		all.stream().forEach(trade -> {
				System.out.println(createInsertSql(trade));
		});
		return "done";
	}
	
	private String createInsertSql(Txn trade) {
		return String.format(INSERT_SQL, 
				trade.getStock(),
				trade.getType(),
				trade.getQty().toPlainString(),
				trade.getPrice().toPlainString(),
				trade.getQty().multiply(trade.getPrice()).toPlainString(),
				trade.getPortfolio_id());
	}
	

	private Txn createTrade(long portfolioId, String stock, String type, BigDecimal qty, BigDecimal price) {
		Txn trade = new Txn();
		trade.setPortfolio_id(portfolioId);
		trade.setStock(stock);
		trade.setType(type);
		trade.setQty(qty);
		trade.setPrice(price);
		trade.setAmount(price.multiply(qty));
		return trade;
	}

}
