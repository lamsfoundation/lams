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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.exception;

/**
 * Type of ToolException thrown by the LamsToolService
 * 
 * @author chris
 */
public class LamsToolServiceException extends ToolException {

    /**
     * Creates a new instance of <code>LamsToolServiceException</code> without detail message.
     */
    public LamsToolServiceException() {
    }

    /**
     * Constructs an instance of <code>LamsToolServiceException</code> with the specified detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public LamsToolServiceException(String msg) {
	super(msg);
    }

    /**
     * Constructs an instance of <code>LamsToolServiceException</code>
     * for wrapping both the customized error message and
     * throwable exception object.
     * 
     * @param message
     * @param cause
     */
    public LamsToolServiceException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
     * Constructs an instance of <code>LamsToolServiceException</code>
     * for wrapping an throwable exception object.
     * 
     * @param message
     * @param cause
     */
    public LamsToolServiceException(Throwable cause) {
	super(cause);
    }

}
