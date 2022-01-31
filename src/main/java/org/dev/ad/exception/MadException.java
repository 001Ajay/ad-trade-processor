package org.dev.ad.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MadException extends Exception {
	private static final long serialVersionUID = 1L;

	public MadException(String message) {
		super(message);
	}
}