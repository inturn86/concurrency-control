package com.concurrency.control.domain.trade.facade;

import com.concurrency.control.domain.trade.dto.request.ConfirmTradeRequestDTO;
import com.concurrency.control.domain.trade.entity.Trade;
import com.concurrency.control.domain.trade.service.TradeService;
import com.concurrency.control.domain.user.entity.User;
import com.concurrency.control.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfirmTradeFacade {

	private final TradeService tradeService;

	private final UserService userService;

	@Transactional
	public void confirmTrade(ConfirmTradeRequestDTO req, Function<Long, Trade> fn) {

		//사용자 조회.
		User user = userService.getUserById(req.userId());

		Trade trade = tradeService.getTradeByPessimisticId(req.tradeId());

		//validate
		if(user.getPoint() < trade.getPrice()) {
			log.error("사용자의 Point가 부족하여 거래할 수 없습니다. Point = {}, 거래 가격 Price = {}", user.getPoint(), trade.getPrice());
			throw new RuntimeException();
		}

		//trade 갱신
		if(ObjectUtils.isEmpty(tradeService.confirmTrade(trade, user.getUserId()))) {
			log.error("거래는 취소 됩니다.");
			return;
		}

		//point 차감
		userService.tradeConfirmRequest(user, trade.getPrice());

		log.error("==== FINISH confirmTrade ====, user = {}, trade = {}", user.toString(), trade.toString());
	}
}
