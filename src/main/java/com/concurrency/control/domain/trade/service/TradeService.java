package com.concurrency.control.domain.trade.service;

import com.concurrency.control.domain.trade.entity.Trade;
import com.concurrency.control.domain.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TradeService {

	private final TradeRepository tradeRepository;

	@Transactional(readOnly = true)
	public Trade getTradeById(Long tradeId) {
		return tradeRepository.findById(tradeId).orElseThrow(() -> new RuntimeException());
	}

	@Transactional
	public Trade save(Trade entity) {
		return tradeRepository.save(entity);
	}

	@Transactional
	public Trade confirmTrade(Trade entity, Long userId) {
		entity.confirmTrade(userId);
		return tradeRepository.save(entity);
	}
}
