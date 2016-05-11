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


package org.lamsfoundation.lams.tool.wookie.util;

/**
 *
 * @author Anthony Sukkar
 *
 */
public class WookieException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5518806968051758859L;

    public WookieException(String message) {
	super(message);
    }

    public WookieException(String message, Throwable cause) {
	super(message, cause);
    }

    public WookieException() {
	super();

    }

    public WookieException(Throwable cause) {
	super(cause);

    }

}
