package com.concurrency.control.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public class UserBuyExecutor implements Runnable{

	private final Long tradeId;
	private final CountDownLatch countDownLatch;

	private final Consumer<Long> executeConsumer;

	@Override
	public void run() {
		//순서가 중요...
		try {
			executeConsumer.accept(tradeId);
		}
		catch (Throwable e) {
			log.error("@@Throwable@@ e = {}", e.toString());
		}
		countDownLatch.countDown();
	}
}
