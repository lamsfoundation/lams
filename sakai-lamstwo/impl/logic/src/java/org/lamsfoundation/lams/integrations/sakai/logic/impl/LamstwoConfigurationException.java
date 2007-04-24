/******************************************************************************
 * LamstwoConfigurationException.java
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.logic.impl;

public class LamstwoConfigurationException extends Exception {

	private static final String EXCEPTION_MSG = "Lamstwo Configuration Exception.  Please verify sakai.properties contains required values";

	public LamstwoConfigurationException() {
		super(EXCEPTION_MSG);
	}

	public LamstwoConfigurationException(String message) {
		super(EXCEPTION_MSG + '\n' + message);
	}
}
