/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
 * ============================================================= 
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/ 
 * 
 * This program is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */  
 
/* $Id$ */  
package org.lamsfoundation.lams.selenium;

/**
 *
 *
 * @author Andrey Balan
 */
public class SeleniumException extends RuntimeException {


	private static final long serialVersionUID = -5273169461546526467L;

	/**
	 * Constructor for TestFrameworkException.
	 */
	public SeleniumException() {
		super();
	}

	/**
	 * Constructor for TestFrameworkException.
	 * @param message
	 */
	public SeleniumException(String message) {
		super(message);
	}

	/**
	 * Constructor for TestFrameworkException.
	 * @param message
	 * @param cause
	 */
	public SeleniumException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for TestFrameworkException.
	 * @param cause
	 */
	public SeleniumException(Throwable cause) {
		super(cause);
	}

}
