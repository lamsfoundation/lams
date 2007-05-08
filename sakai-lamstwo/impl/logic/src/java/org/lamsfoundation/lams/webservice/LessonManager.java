/******************************************************************************
 * LessonManager.java
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

/**
 * LessonManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

public interface LessonManager extends java.rmi.Remote {
    public java.lang.Long startLesson(java.lang.String serverId, java.lang.String datetime, java.lang.String hashValue, java.lang.String username, long ldId, java.lang.String courseId, java.lang.String title, java.lang.String desc, java.lang.String countryIsoCode, java.lang.String langIsoCode) throws java.rmi.RemoteException;
    public java.lang.Long scheduleLesson(java.lang.String serverId, java.lang.String datetime, java.lang.String hashValue, java.lang.String username, long ldId, java.lang.String courseId, java.lang.String title, java.lang.String desc, java.lang.String startDate, java.lang.String countryIsoCode, java.lang.String langIsoCode) throws java.rmi.RemoteException;
    public boolean deleteLesson(java.lang.String serverId, java.lang.String datetime, java.lang.String hashValue, java.lang.String username, long lsId) throws java.rmi.RemoteException;
}
