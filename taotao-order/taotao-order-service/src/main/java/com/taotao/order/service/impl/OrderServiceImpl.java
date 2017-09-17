package com.taotao.order.service.impl;

import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.order.mapper.OrderMapper;
import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;

	@Override
	public String saveOrder(Order order) {
		String orderId = "";
		try {
			//订单号（唯一+可读）：用户id+时间戳
			orderId = order.getUserId() + "" + System.currentTimeMillis();
			order.setOrderId(orderId);
			order.setStatus(1);
			order.setCreateTime(new Date());
			
			orderMapper.saveOrder(order);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return orderId;
	}

	@Override
	public Order queryOrderByOrderId(String orderId) {
		return orderMapper.queryOrderByOrderId(orderId);
	}

	@Override
	public void autoCloseOrder() {
		orderMapper.autoCloseOrder(DateTime.now().minusDays(2).toDate());
	}
}
