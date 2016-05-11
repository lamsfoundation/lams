/****************************************************************
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
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.qa.util;

/**
 * <p>
 * This exception wraps all basic exception occured in the q/a tool. It is not
 * suppose to be try and catched in any level. The struts should be taking care
 * of handling this exception.
 * </p>
 *
 * @author Ozgur Demirtas
 *
 */
public class QaApplicationException extends RuntimeException {
    /**
     * Default Constructor
     */
    public QaApplicationException() {
	super();
    }

    /**
     * Constructor for customized error message
     *
     * @param message
     */
    public QaApplicationException(String message) {
	super(message);
    }

    /**
     * Constructor for wrapping the throwable object
     *
     * @param cause
     */
    public QaApplicationException(Throwable cause) {
	super(cause);
    }

    /**
     * Constructor for wrapping both the customized error message and throwable
     * exception object.
     *
     * @param message
     * @param cause
     */
    public QaApplicationException(String message, Throwable cause) {
	super(message, cause);
    }

}
