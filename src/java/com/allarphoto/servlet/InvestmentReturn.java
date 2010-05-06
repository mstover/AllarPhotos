package com.lazerinc.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InvestmentReturn extends HttpServlet {
	private static final long serialVersionUID = 1;

	// Initialize global variables

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	// Service the request

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//
		float rate = 0;
		try {
			rate = Float.valueOf(request.getParameter("rate")).floatValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		float yearlyInvestment = 0;
		try {
			yearlyInvestment = Float.valueOf(
					request.getParameter("yearly_investment")).floatValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Get Servlet information

	public String getServletInfo() {
		return "com.lazerinc.servlet.InvestmentReturn Information";
	}
}
