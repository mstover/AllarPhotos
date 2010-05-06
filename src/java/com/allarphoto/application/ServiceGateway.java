package com.lazerinc.application;

import java.util.HashMap;
import java.util.Map;

import com.lazerinc.server.ProductService;
import com.lazerinc.server.UserService;

public class ServiceGateway {

	private static ServiceGateway gateway = null;

	private static Map controllers = new HashMap();

	private static UserService userService;

	private static ProductService productService;

	private ServiceGateway() {
	}

	public static ServiceGateway getGateway() {
		if (gateway == null) {
			gateway = new ServiceGateway();
		}
		return gateway;
	}

	public static OrderingService getOrderService(String serviceName) {
		try {
			OrderingService orderService = (OrderingService) Class.forName(
					serviceName).newInstance();
			return orderService;
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		}
		return null;
	}
}