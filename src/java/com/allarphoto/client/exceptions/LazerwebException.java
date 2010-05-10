package com.allarphoto.client.exceptions;

import strategiclibrary.service.webaction.ActionException;

public class LazerwebException extends ActionException {
	private static final long serialVersionUID = 1;

	public final static String USER_NOT_LOGGED_IN = "UserNotLoggedInError";

	public final static String INVALID_LOG_IN = "InvalidLogInError";

	public final static String INCOMPLETE_USER_INFO = "IncompleteUserInfoError";

	public final static String INVALID_EMAIL = "InvalidEmailError";

	public final static String NO_RESPONSE_FROM_SERVER = "NoResponseFromServerError";

	public final static String EMAIL_FAILURE = "EmailFailureError";

	public final static String INCOMPLETE_INFO = "IncompleteInfoError";

	public final static String BAD_DOWNLOAD_FILE = "BadDownloadFileError";

	public final static String INVALID_DATE_FORMAT = "InvalidDateFormatError";

	public final static String INVALID_PERMISSION = "InvalidPermission";

	public final static String EMPTY_ORDER = "EmptyOrderError";

	public final static String EMPTY_CART = "EmptyCartError";

	public final static String PRODUCT_EXPIRED_EXCEPTION = "ProductExpiredException";

	public final static String DUP_USERNAME = "DuplicateUsernameError";

	public final static String DELETE_OWN_GROUP = "DeleteOwnGroupError";

	public final static String INVALID_ORDER = "InvalidOrderException";

	public final static String NO_SUCH_PRODUCT = "NoSuchProductException";

	public final static String NO_SUCH_USER = "NoSuchUserException";

	public LazerwebException(String arg0, boolean arg1) {
		super(arg0, arg1);
	}

	public LazerwebException(String arg0, Throwable arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}

	public LazerwebException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LazerwebException(Throwable arg0) {
		super(arg0);
	}

	public LazerwebException() {
		this("LazerwebException");
	}

	public LazerwebException(String message) {
		super(message);
	}
}