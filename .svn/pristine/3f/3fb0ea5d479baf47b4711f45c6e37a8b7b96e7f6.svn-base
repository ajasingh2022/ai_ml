package com.capgemini.sesp.ast.android.module.communication;

public class CommunicationException extends Exception {
	private static final long serialVersionUID = 4771178533723829560L;
	private int statusCode;
	private String message;

	CommunicationException(int statusCode, String message) {
		super("Status = " + statusCode + ", message = " + message);
		this.statusCode = statusCode;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
