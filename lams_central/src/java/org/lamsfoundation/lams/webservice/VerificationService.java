/**
 * VerificationService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

public interface VerificationService extends javax.xml.rpc.Service {
    public java.lang.String getVerificationServiceAddress();

    public Verification getVerificationService() throws javax.xml.rpc.ServiceException;

    public Verification getVerificationService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
