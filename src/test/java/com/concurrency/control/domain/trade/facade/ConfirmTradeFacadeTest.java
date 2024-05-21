package com.concurrency.control.domain.trade.facade;

import com.concurrency.control.domain.trade.dto.request.ConfirmTradeRequestDTO;
import com.concurrency.control.domain.trade.entity.Trade;
import com.concurrency.control.domain.trade.service.TradeService;
import com.concurrency.control.domain.trade.vo.TradeStatus;
import com.concurrency.control.domain.user.UserBuyExecutor;
import com.concurrency.control.domain.user.entity.User;
import com.concurrency.control.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.log.SubSystemLogging;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ConfirmTradeFacadeTest {

	@Autowired
	private ConfirmTradeFacade confirmTradeFacade;

	@Autowired
	private SyncService syncService;

	@Autowired
	private UserService userService;

	@Autowired
	private TradeService tradeService;


	static int price = 199000;

	static long tradeId;

	@BeforeEach
	void testStart() {
		//사용자 저장
		Trade trade = Trade.builder()
				.tradeStatusCd(TradeStatus.READY)
				.itemId("NIKE Air max 97")
				.qty(1)
				.price(price)
				.build();

		tradeId = tradeService.save(trade).getTradeId();
	}

	@AfterEach
	void testAfter() {
		userService.getUserRepository().deleteAll();
		tradeService.getTradeRepository().deleteAll();
	}

	@Test
	@DisplayName("낙관적 락 성공")
	@RepeatedTest(200)
	void optimisticLock_Success() throws InterruptedException {

		final int concurrencyCnt = 10;

		final int userPoint = 10000000;

		CountDownLatch countDownLatch = new CountDownLatch(concurrencyCnt);

		List<User> executorUserList = new ArrayList<>();
		//사용자 저장
		for (int i = 0; i < concurrencyCnt; i++){
			User user = User.builder()
					.userName("BUYER")
					.point(userPoint)
					.build();
			executorUserList.add(userService.save(user));
		}

		List<Thread> userBuyExecutorList = executorUserList.stream().map(o ->
			new Thread(new UserBuyExecutor(tradeId, countDownLatch, consumer -> confirmTradeFacade.confirmTrade(ConfirmTradeRequestDTO.builder()
					.userId(o.getUserId())
					.tradeId(consumer)
					.build()
			, (tradeId) -> tradeService.getTradeByOptimisticId(tradeId))))
		).collect(Collectors.toList());

		userBuyExecutorList.forEach(Thread::start);

		countDownLatch.await();

		System.out.println("===FINISH await===");

		List<User> userList = userService.getUserList();

		Trade trade = tradeService.getTradeById(tradeId);
//
		assertEquals(trade.getTradeStatusCd(), TradeStatus.COMPLETE);

		assertEquals(userList.stream().filter(o -> o.getPoint() == (userPoint - price)).count(), 1);
	}

	@Test
	@DisplayName("비관적 락 성공")
	@RepeatedTest(200)
	void pessimisticLock_Success() throws InterruptedException {

		final int concurrencyCnt = 10;

		final int userPoint = 10000000;

		CountDownLatch countDownLatch = new CountDownLatch(concurrencyCnt);

		List<User> executorUserList = new ArrayList<>();
		//사용자 저장
		for (int i = 0; i < concurrencyCnt; i++){
			User user = User.builder()
					.userName("BUYER")
					.point(userPoint)
					.build();
			executorUserList.add(userService.save(user));
		}

		List<Thread> userBuyExecutorList = executorUserList.stream().map(o ->
				new Thread(new UserBuyExecutor(tradeId, countDownLatch, consumer -> confirmTradeFacade.confirmTrade(ConfirmTradeRequestDTO.builder()
								.userId(o.getUserId())
								.tradeId(consumer)
								.build()
						, (funParam) -> tradeService.getTradeByPessimisticId(funParam))))
		).collect(Collectors.toList());

		userBuyExecutorList.forEach(Thread::start);

		countDownLatch.await();

		System.out.println("===FINISH await===");

		List<User> userList = userService.getUserList();

		Trade trade = tradeService.getTradeById(tradeId);
	//
		assertEquals(trade.getTradeStatusCd(), TradeStatus.COMPLETE);

		assertEquals(userList.stream().filter(o -> o.getPoint() == (userPoint - price)).count(), 1);
	}
}