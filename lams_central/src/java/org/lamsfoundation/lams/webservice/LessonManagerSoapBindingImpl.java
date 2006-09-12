/**
 * LessonManagerSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

import java.rmi.RemoteException;

public class LessonManagerSoapBindingImpl implements LessonManager {
	public Integer startLesson(String serverId, String datetime, String hashValue, String username,
			long ldId, String courseId, String title, String desc) throws RemoteException {
		return null;
	}

	public Integer scheduleLesson(String serverId, String datetime, String hashValue,
			String username, long ldId, String courseId, String title, String desc, String startDate)
			throws RemoteException {
		return null;
	}

	public boolean deleteLearningSession(String serverId, String datetime, String hashValue,
			String username, long lsId) throws RemoteException {
		return false;
	}

}
