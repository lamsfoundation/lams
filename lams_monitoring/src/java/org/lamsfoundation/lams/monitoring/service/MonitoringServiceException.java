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

/* $$Id$$ */
package org.lamsfoundation.lams.monitoring.service;

/**
 * Runtime exception that wraps the error condition occurred at monitoring
 * side.
 * 
 * @author Jacky Fang
 * @since 2005-4-14
 * @version 1.1
 *
 */
public class MonitoringServiceException extends RuntimeException {

    /**
     *
     */
    public MonitoringServiceException() {
	super();
    }

    /**
     * @param message
     */
    public MonitoringServiceException(String message) {
	super(message);
    }

    /**
     * @param cause
     */
    public MonitoringServiceException(Throwable cause) {
	super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public MonitoringServiceException(String message, Throwable cause) {
	super(message, cause);
    }

}
