package com.concurrency.control.domain.trade.repository;

import com.concurrency.control.domain.trade.entity.Trade;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

//	@Lock(LockModeType.OPTIMISTIC)
	Optional<Trade> findByTradeId(Long tradeId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Trade> findTradeByTradeId(Long tradeId);
}
