/**
 * LessonWebServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface LessonManagerService extends Service {
	public java.lang.String getLearningServiceAddress();

	public LessonManager getLessonManager() throws ServiceException;

	public LessonManager getLessonManager(java.net.URL portAddress) throws ServiceException;
}
