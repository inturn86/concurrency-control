package com.concurrency.control.domain.trade.facade;

import com.concurrency.control.domain.trade.dto.request.ConfirmTradeRequestDTO;
import com.concurrency.control.domain.trade.entity.Trade;
import com.concurrency.control.domain.trade.service.TradeService;
import com.concurrency.control.domain.trade.vo.TradeStatus;
import com.concurrency.control.domain.user.UserBuyExecutor;
import com.concurrency.control.domain.user.entity.User;
import com.concurrency.control.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.plugins.tiff.TIFFImageReadParam;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfirmTradeFacadeTest {

	@Autowired
	private ConfirmTradeFacade confirmTradeFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private TradeService tradeService;

	static long tradeId;

	@BeforeEach
	void testStart() {

		//사용자 저장
		Trade trade = Trade.builder()
				.tradeStatusCd(TradeStatus.READY)
				.itemId("NIKE Air max 97")
				.qty(1)
				.price(199000)
				.build();

		tradeId = tradeService.save(trade).getTradeId();


	}

	@Test
	@Transactional
	@DisplayName("낙관적 락 성공")
	void optimisticLock_Success() throws InterruptedException {

		final int concurrencyCnt = 20;

		CountDownLatch countDownLatch = new CountDownLatch(concurrencyCnt);

		List<User> executorUserList = new ArrayList<>();
		//사용자 저장
		for (int i = 0; i < concurrencyCnt; i++){
			User user = User.builder()
					.userName("BUYER")
					.point(10000000l)
					.build();
			executorUserList.add(userService.save(user));
		}

		List<Thread> userBuyExecutorList = executorUserList.stream().map(o -> {
			return new Thread(new UserBuyExecutor(tradeId, countDownLatch, consumer -> confirmTradeFacade.confirmTrade(ConfirmTradeRequestDTO.builder()
					.userId(o.getUserId())
					.tradeId(consumer)
					.build()
			)));
		}).collect(Collectors.toList());

		countDownLatch.await();


	}

	@Test
	@Transactional
	@DisplayName("낙관적 락 실패")
	void optimisticLock_Fail() {

	}
}