/**
 * LearningDesignRepository.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LearningDesignRepository extends Remote {
    public String getLearningDesigns(String serverId, String datetime, String hashValue, String username, Integer mode) throws RemoteException;
}
