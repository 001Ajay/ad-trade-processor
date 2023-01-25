package org.dev.ad.controller;

import io.swagger.annotations.ApiParam;
import org.dev.ad.exception.MadException;
import org.dev.ad.model.Txn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

//@RestController
//@RequestMapping("/mock/txn")
public class MockTxnController { // TODO: Possible redundant class

	@GetMapping("/all")
	public @ResponseBody
	String findAll() {
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
	public Txn buy(@ApiParam(value = "Add object in database table", required = true)
		@Valid @RequestParam long portfolioId,
				   @Valid @RequestParam String stock,
				   @Valid @RequestParam BigDecimal qty,
				   @Valid @RequestParam BigDecimal price
		) {
		
		Txn txn = createTrade(portfolioId, stock, "BUY", qty, price);
				
		return txn;
	}
	
	@GetMapping("/sell")
	public Txn sell(@ApiParam(value = "Add object in database table", required = true)
		@Valid @RequestParam long portfolioId,
					@Valid @RequestParam String stock,
					@Valid @RequestParam BigDecimal qty,
					@Valid @RequestParam BigDecimal price
		) {
		
		Txn txn = createTrade(portfolioId, stock, "SELL", qty, price);
				
		return txn;
	}

	private Txn createTrade(long portfolioId, String stock, String type, BigDecimal qty, BigDecimal price) {
		Txn txn = new Txn();
		txn.setPortfolio_id(portfolioId);
		txn.setStock(stock);
		txn.setType(type);
		txn.setQty(qty);
		txn.setPrice(price);
		txn.setAmount(price.multiply(qty));
		return txn;
	}

}
