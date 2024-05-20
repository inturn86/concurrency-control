package com.concurrency.control.domain.trade.dto.request;


import lombok.Builder;

@Builder
public record ConfirmTradeRequestDTO (
		Long tradeId,
		Long userId
) {
}
