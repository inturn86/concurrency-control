package com.concurrency.control.domain.trade.service;

import com.concurrency.control.domain.trade.entity.Trade;
import com.concurrency.control.domain.trade.repository.TradeRepository;
import com.concurrency.control.domain.trade.vo.TradeStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

	@Getter
	private final TradeRepository tradeRepository;

	@Transactional(readOnly = true)
	public Trade getTradeByPessimisticId(Long tradeId) {
		return tradeRepository.findTradeByTradeId(tradeId).orElseThrow(() -> new RuntimeException());
	}

	@Transactional(readOnly = true)
	public Trade getTradeByOptimisticId(Long tradeId) {
		return tradeRepository.findByTradeId(tradeId).orElseThrow(() -> new RuntimeException());
	}

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
		if(TradeStatus.COMPLETE.equals(entity.getTradeStatusCd())){
			log.error("이미 완료된 거래입니다.");
			return null;
		}

		entity.confirmTrade(userId);
		return tradeRepository.save(entity);
	}
}
