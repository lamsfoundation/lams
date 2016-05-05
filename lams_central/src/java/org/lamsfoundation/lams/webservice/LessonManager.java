/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.webservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * LessonManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
public interface LessonManager extends Remote {
    public Long startLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, String countryIsoCode, String langIsoCode, String customCSV)
	    throws RemoteException;

    public Long scheduleLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, String startDate, String countryIsoCode, String langIsoCode,
	    String customCSV) throws RemoteException;

    public boolean deleteLesson(String serverId, String datetime, String hashValue, String username, long lsId)
	    throws RemoteException;
}
