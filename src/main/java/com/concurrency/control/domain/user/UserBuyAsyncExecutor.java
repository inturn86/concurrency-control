package com.concurrency.control.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public class UserBuyAsyncExecutor implements Runnable{

	private final Long tradeId;

	private final Consumer<Long> executeConsumer;

	@Override
	public void run() {
		executeConsumer.accept(tradeId);
	}
}
