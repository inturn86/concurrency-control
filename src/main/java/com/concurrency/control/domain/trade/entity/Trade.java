package com.concurrency.control.domain.trade.entity;

import com.concurrency.control.domain.trade.vo.TradeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "trade")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tradeId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TradeStatus tradeStatusCd;

	@Column(nullable = false)
	private String itemId;

	@Column(nullable = false)
	private Integer qty;

	private Integer price;

	private Long buyerId;

	@Version
	private Integer version;

	public void confirmTrade(Long userId) {
		this.tradeStatusCd = TradeStatus.COMPLETE;
		this.buyerId = userId;
	}

}
