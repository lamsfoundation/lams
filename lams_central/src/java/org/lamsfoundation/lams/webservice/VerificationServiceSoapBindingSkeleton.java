/**
 * VerificationServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

@SuppressWarnings({ "unchecked", "serial" })
public class VerificationServiceSoapBindingSkeleton implements Verification, org.apache.axis.wsdl.Skeleton {
    private Verification impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
     * Returns List of OperationDesc objects with this name
     */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
	return (java.util.List) _myOperations.get(methodName);
    }

    /**
     * Returns Collection of OperationDescs
     */
    public static java.util.Collection getOperationDescs() {
	return _myOperationsList;
    }

    static {
	org.apache.axis.description.OperationDesc _oper;
	@SuppressWarnings("unused")
	org.apache.axis.description.FaultDesc _fault;
	org.apache.axis.description.ParameterDesc[] _params;
	_params = new org.apache.axis.description.ParameterDesc[] {
		new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serverId"),
			org.apache.axis.description.ParameterDesc.IN,
			new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"),
			java.lang.String.class, false, false),
		new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "datetime"),
			org.apache.axis.description.ParameterDesc.IN,
			new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"),
			java.lang.String.class, false, false),
		new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "hash"),
			org.apache.axis.description.ParameterDesc.IN,
			new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"),
			java.lang.String.class, false, false), };
	_oper = new org.apache.axis.description.OperationDesc("verify", _params,
		new javax.xml.namespace.QName("", "verifyReturn"));
	_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
	_oper.setElementQName(new javax.xml.namespace.QName("http://webservice.lams.lamsfoundation.org", "verify"));
	_oper.setSoapAction("");
	_myOperationsList.add(_oper);
	if (_myOperations.get("verify") == null) {
	    _myOperations.put("verify", new java.util.ArrayList());
	}
	((java.util.List) _myOperations.get("verify")).add(_oper);
    }

    public VerificationServiceSoapBindingSkeleton() {
	this.impl = new VerificationServiceSoapBindingImpl();
    }

    public VerificationServiceSoapBindingSkeleton(Verification impl) {
	this.impl = impl;
    }

    @Override
    public boolean verify(String serverId, String datetime, String hash) throws java.rmi.RemoteException {
	boolean ret = impl.verify(serverId, datetime, hash);
	return ret;
    }

}
