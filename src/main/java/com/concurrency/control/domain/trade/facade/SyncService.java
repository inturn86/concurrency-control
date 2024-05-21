package com.concurrency.control.domain.trade.facade;

import com.concurrency.control.domain.trade.dto.request.ConfirmTradeRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SyncService {

	private final ConfirmTradeFacade confirmTradeFacade;

	public synchronized void syncConfirm(ConfirmTradeRequestDTO req) {
		log.error("*** syncConfirm ***  sync START service");
		confirmTradeFacade.confirmTrade(req, null);
		log.error("*** syncConfirm ***  sync END service");
	}
}
