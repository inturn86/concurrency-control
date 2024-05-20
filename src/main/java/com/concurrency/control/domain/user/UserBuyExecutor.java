package com.concurrency.control.domain.user;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class UserBuyExecutor implements Runnable{

	private final Long tradeId;
	private final CountDownLatch countDownLatch;

	private final Consumer<Long> executeConsumer;

	@Override
	public void run() {
		countDownLatch.countDown();
		executeConsumer.accept(tradeId);
	}
}
