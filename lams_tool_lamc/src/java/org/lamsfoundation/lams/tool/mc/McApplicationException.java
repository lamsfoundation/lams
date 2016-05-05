/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc;

/**
 * <p>
 * This exception wraps all basic exception occured in the mcq tool. It is not suppose to be try and catched in any
 * level. The struts should be taking care of handling this exception.
 * </p>
 *
 * @author Ozgur Demirtas
 *
 */
public class McApplicationException extends RuntimeException {
    /**
     * Default Constructor
     */
    public McApplicationException() {
	super();
    }

    /**
     * Constructor for customized error message
     *
     * @param message
     */
    public McApplicationException(String message) {
	super(message);
    }

    /**
     * Constructor for wrapping the throwable object
     *
     * @param cause
     */
    public McApplicationException(Throwable cause) {
	super(cause);
    }

    /**
     * Constructor for wrapping both the customized error message and throwable exception object.
     *
     * @param message
     * @param cause
     */
    public McApplicationException(String message, Throwable cause) {
	super(message, cause);
    }

}
