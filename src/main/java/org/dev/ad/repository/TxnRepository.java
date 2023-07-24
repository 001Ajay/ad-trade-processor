package org.dev.ad.repository;


import org.dev.ad.model.Txn;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TxnRepository extends JpaRepository<Txn, Long>{

}