/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

package org.lamsfoundation.lams.util.wddx;

/**
 * @author Manpreet Minhas
 * 
 * Thrown if an error occurs trying to get the numeric data from the wddx generated objects.
 * 
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
