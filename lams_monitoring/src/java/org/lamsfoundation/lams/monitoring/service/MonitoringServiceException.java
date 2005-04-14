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
 * ***********************************************************************/

package org.lamsfoundation.lams.monitoring.service;


/**
 * Runtime exception that wraps the error condition occurred at monitoring 
 * side.
 * @author Jacky Fang
 * @since  2005-4-14
 * @version 1.1
 * 
 */
public class MonitoringServiceException extends RuntimeException
{

    /**
     * 
     */
    public MonitoringServiceException()
    {
        super();
    }

    /**
     * @param message
     */
    public MonitoringServiceException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    public MonitoringServiceException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public MonitoringServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
