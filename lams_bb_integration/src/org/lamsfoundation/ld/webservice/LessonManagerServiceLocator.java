/******************************************************************************
 * LessonManagerServiceLocator.java
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

/**
 * LessonManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.ld.webservice;

public class LessonManagerServiceLocator extends org.apache.axis.client.Service implements org.lamsfoundation.ld.webservice.LessonManagerService {

    public LessonManagerServiceLocator() {
    }


    public LessonManagerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LessonManagerServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LessonManagerService
    private java.lang.String LessonManagerService_address = "http://localhost:8080/lams/services/LessonManagerService";

    public java.lang.String getLessonManagerServiceAddress() {
        return LessonManagerService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LessonManagerServiceWSDDServiceName = "LessonManagerService";

    public java.lang.String getLessonManagerServiceWSDDServiceName() {
        return LessonManagerServiceWSDDServiceName;
    }

    public void setLessonManagerServiceWSDDServiceName(java.lang.String name) {
        LessonManagerServiceWSDDServiceName = name;
    }

    public org.lamsfoundation.ld.webservice.LessonManager getLessonManagerService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LessonManagerService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLessonManagerService(endpoint);
    }

    public org.lamsfoundation.ld.webservice.LessonManager getLessonManagerService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	org.lamsfoundation.ld.webservice.LessonManagerServiceSoapBindingStub _stub = new org.lamsfoundation.ld.webservice.LessonManagerServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getLessonManagerServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLessonManagerServiceEndpointAddress(java.lang.String address) {
        LessonManagerService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.lamsfoundation.ld.webservice.LessonManager.class.isAssignableFrom(serviceEndpointInterface)) {
            	org.lamsfoundation.ld.webservice.LessonManagerServiceSoapBindingStub _stub = new org.lamsfoundation.ld.webservice.LessonManagerServiceSoapBindingStub(new java.net.URL(LessonManagerService_address), this);
                _stub.setPortName(getLessonManagerServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("LessonManagerService".equals(inputPortName)) {
            return getLessonManagerService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.lams.lamsfoundation.org", "LessonManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.lams.lamsfoundation.org", "LessonManagerService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("LessonManagerService".equals(portName)) {
            setLessonManagerServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
