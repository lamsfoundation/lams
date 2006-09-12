/**
 * LearningDesignRepositoryService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

import java.net.URL;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface LearningDesignRepositoryService extends Service {
	
    public String getLearningDesignRepositoryAddress();

    public LearningDesignRepository getLearningDesignRepository() throws ServiceException;

    public LearningDesignRepository getLearningDesignRepository(URL portAddress) throws ServiceException;
}
