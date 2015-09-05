package com.fit2cloud.sdk;

public class PluginException extends Exception {

	private static final long serialVersionUID = -7430603197031343440L;

	public PluginException() {
		super();
	}

	public PluginException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PluginException(String message, Throwable cause) {
		super(message, cause);
	}

	public PluginException(String message) {
		super(message);
	}

	public PluginException(Throwable cause) {
		super(cause);
	}
}
