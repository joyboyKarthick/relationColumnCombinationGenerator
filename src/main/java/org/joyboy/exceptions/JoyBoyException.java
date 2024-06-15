package org.joyboy.exceptions;

public class JoyBoyException extends RuntimeException
{
	public static final Long RESOURCE_NOT_FOUND = 1000L;
	public static final Long INVALID_RESOURCE = 1100L;
	public static final Long INVALID_INPUT =1200L;
	public static final Long INVALID_INPUT_FORMAT =1201L;


	public JoyBoyException(Long errorCode, String errorMsg)
	{
		super("error code :" + errorCode + ", ".concat(errorMsg));
	}
}

