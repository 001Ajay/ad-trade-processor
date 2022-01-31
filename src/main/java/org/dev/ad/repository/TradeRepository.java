package org.dev.ad.repository;


import org.dev.ad.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TradeRepository extends JpaRepository<Trade, Long>{
	
}