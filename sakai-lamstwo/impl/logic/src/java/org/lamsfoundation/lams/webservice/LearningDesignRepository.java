/******************************************************************************
 * LearningDesignRepository.java
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

/**
 * LearningDesignRepository.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

public interface LearningDesignRepository extends java.rmi.Remote {
    public java.lang.String getLearningDesigns(java.lang.String serverId, java.lang.String datetime, java.lang.String hashValue, java.lang.String username, java.lang.String courseId, java.lang.Integer mode, java.lang.String country, java.lang.String lang) throws java.rmi.RemoteException;
}
