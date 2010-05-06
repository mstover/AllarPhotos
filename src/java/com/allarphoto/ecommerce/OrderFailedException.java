package com.lazerinc.ecommerce;

public class OrderFailedException extends RuntimeException {

	private static final long serialVersionUID = 1;

	public static final String APPROVAL = "Approval";

	public static final String FULFILLMENT = "Fulfillment";

	public static final String MERCHANT = "Merchant";

	public static final String CONFIRMATION = "Confirmation";

	public static final String REJECT = "Reject";

	public static final String CANCELLED = "Cancelled";

	public OrderFailedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderFailedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public OrderFailedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public OrderFailedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
