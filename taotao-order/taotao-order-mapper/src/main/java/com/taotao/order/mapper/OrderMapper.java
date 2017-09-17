package com.taotao.order.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.taotao.order.pojo.Order;

public interface OrderMapper {

	void saveOrder(Order order);

	/**
	 * 根据订单号查询订单
	 * @param orderId 订单号
	 * @return
	 */
	Order queryOrderByOrderId(@Param("orderId")String orderId);

	void autoCloseOrder(@Param("date")Date date);

}
