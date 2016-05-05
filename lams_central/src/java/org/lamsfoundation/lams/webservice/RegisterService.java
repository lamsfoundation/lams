/**
 * RegisterService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

public interface RegisterService extends javax.xml.rpc.Service {
    public java.lang.String getRegisterServiceAddress();

    public org.lamsfoundation.lams.webservice.Register getRegisterService() throws javax.xml.rpc.ServiceException;

    public org.lamsfoundation.lams.webservice.Register getRegisterService(java.net.URL portAddress)
	    throws javax.xml.rpc.ServiceException;
}
