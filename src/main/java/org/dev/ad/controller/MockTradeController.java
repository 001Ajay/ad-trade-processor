package org.dev.ad.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.dev.ad.exception.MadException;
import org.dev.ad.model.Trade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/mock/txn")
public class MockTradeController  {
	
	@GetMapping("/all")
	public @ResponseBody String findAll() {
		return "{[\n" + 
				"                [\"Dakota Rice\", \"Niger\", \"Oud-Turnhout\", \"$36,738\"],\n" + 
				"                [\"Minerva Hooper\", \"Curaçao\", \"Sinaai-Waas\", \"$23,789\"],\n" + 
				"                [\"Sage Rodriguez\", \"Netherlands\", \"Baileux\", \"$56,142\"],\n" + 
				"                [\"Philip Chaney\", \"Korea, South\", \"Overland Park\", \"$38,735\"],\n" + 
				"                [\"Doris Greene\", \"Malawi\", \"Feldkirchen in Kärnten\", \"$63,542\"],\n" + 
				"                [\"Mason Porter\", \"Chile\", \"Gloucester\", \"$78,615\"]\n" + 
				"              ]}";
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<String> findById(@ApiParam(value = "Id from which object will be retrieved", required = true) 
		@PathVariable(value = "id") String id) throws MadException {
		
		return ResponseEntity.ok().body("");
	}
	
	@PostMapping("/add")
	public String add(@ApiParam(value = "Add object in database table", required = true) 
		@Valid @RequestBody String employee) {
		return employee;
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> update(@ApiParam(value = "Add object in database table", required = true) 
		@Valid @RequestBody String employee) {
		return ResponseEntity.ok().body(employee);
	}
	
	@DeleteMapping("/delete/{id}")
	public @ResponseBody String delete(@ApiParam(value = "Id from which object will be delete from database table", required = true) 
		@PathVariable(value = "id") String id) throws MadException {
		return "";
	}
	
	@GetMapping("/buy")
	public Trade buy(@ApiParam(value = "Add object in database table", required = true) 
		@Valid @RequestParam long portfolioId,
		@Valid @RequestParam String stock,
		@Valid @RequestParam BigDecimal qty,
		@Valid @RequestParam BigDecimal price
		) {
		
		Trade trade = createTrade(portfolioId, stock, "BUY", qty, price);
				
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
				
		return trade;
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
