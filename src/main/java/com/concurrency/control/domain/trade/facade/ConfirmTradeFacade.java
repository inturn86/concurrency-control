package com.concurrency.control.domain.trade.facade;

import com.concurrency.control.domain.trade.dto.request.ConfirmTradeRequestDTO;
import com.concurrency.control.domain.trade.entity.Trade;
import com.concurrency.control.domain.trade.service.TradeService;
import com.concurrency.control.domain.user.entity.User;
import com.concurrency.control.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmTradeFacade {

	private final TradeService tradeService;

	private final UserService userService;

	@Transactional
	public void confirmTrade(ConfirmTradeRequestDTO req) {

		//사용자 조회.
		User user = userService.getUserById(req.userId());

		Trade trade = tradeService.getTradeById(req.tradeId());

		//사용자의 point 차감.

		//trade 갱신
		tradeService.confirmTrade(trade, user.getUserId());

	}
}
