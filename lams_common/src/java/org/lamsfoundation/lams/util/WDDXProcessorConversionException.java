package org.lamsfoundation.lams.util;

/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of WebMCQ Pty Ltd. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2002
 ******************************************************************************** */

/**
 * Thrown if an error occurs trying to get the numeric data from the wddx generated objects.
 * @author fmalikoff
 */
public class WDDXProcessorConversionException extends Exception {

	/**
	 * Constructor for WDDXProcessorConversionException.
	 */
	public WDDXProcessorConversionException() {
		super();
	}

	/**
	 * Constructor for WDDXProcessorConversionException.
	 * @param message
	 */
	public WDDXProcessorConversionException(String message) {
		super(message);
	}

	/**
	 * Constructor for WDDXProcessorConversionException.
	 * @param message
	 * @param cause
	 */
	public WDDXProcessorConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for WDDXProcessorConversionException.
	 * @param cause
	 */
	public WDDXProcessorConversionException(Throwable cause) {
		super(cause);
	}

}
