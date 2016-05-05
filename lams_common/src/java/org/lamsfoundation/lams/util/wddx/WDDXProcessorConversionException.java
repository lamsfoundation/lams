/***************************************************************************
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util.wddx;

/**
 * @author Manpreet Minhas
 *
 *         Thrown if an error occurs trying to get the numeric data from the wddx generated objects.
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
     * 
     * @param message
     */
    public WDDXProcessorConversionException(String message) {
	super(message);
    }

    /**
     * Constructor for WDDXProcessorConversionException.
     * 
     * @param message
     * @param cause
     */
    public WDDXProcessorConversionException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
     * Constructor for WDDXProcessorConversionException.
     * 
     * @param cause
     */
    public WDDXProcessorConversionException(Throwable cause) {
	super(cause);
    }

}
