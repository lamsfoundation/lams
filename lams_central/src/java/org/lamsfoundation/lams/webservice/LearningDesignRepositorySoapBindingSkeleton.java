/**
 * LearningDesignWebServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

import java.rmi.RemoteException;
import java.util.*;
import javax.xml.namespace.QName;

import org.apache.axis.wsdl.Skeleton;
import org.apache.axis.description.*;

@SuppressWarnings( { "serial", "unchecked" })
public class LearningDesignRepositorySoapBindingSkeleton implements LearningDesignRepository,
		Skeleton {
	private LearningDesignRepository impl;

	private static Map _myOperations = new Hashtable();

	private static Collection _myOperationsList = new ArrayList();

	/**
	 * Returns List of OperationDesc objects with this name
	 */
	public static List getOperationDescByName(String methodName) {
		return (List) _myOperations.get(methodName);
	}

	/**
	 * Returns Collection of OperationDescs
	 */
	public static Collection getOperationDescs() {
		return _myOperationsList;
	}

	static {
		OperationDesc _oper;
		@SuppressWarnings("unused")
		FaultDesc _fault;
		ParameterDesc[] _params;
		_params = new ParameterDesc[] {
				new ParameterDesc(new QName("", "serverId"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false),
				new ParameterDesc(new QName("", "datetime"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false),
				new ParameterDesc(new QName("", "hashValue"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false),
				new ParameterDesc(new QName("", "username"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false),
				new ParameterDesc(new QName("", "mode"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "int"), Integer.class, false,
						false), };
		_oper = new OperationDesc("getLearningDesigns", _params, new QName("",
				"getLearningDesignsReturn"));
		_oper.setReturnType(new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
		_oper.setElementQName(new QName("", "getLearningDesigns"));
		_oper.setSoapAction("");
		_myOperationsList.add(_oper);
		if (_myOperations.get("getLearningDesigns") == null) {
			_myOperations.put("getLearningDesigns", new ArrayList());
		}
		((List) _myOperations.get("getLearningDesigns")).add(_oper);
	}

	public LearningDesignRepositorySoapBindingSkeleton() {
		this.impl = new LearningDesignRepositorySoapBindingImpl();
	}

	public LearningDesignRepositorySoapBindingSkeleton(LearningDesignRepository impl) {
		this.impl = impl;
	}

	public String getLearningDesigns(String serverId, String datetime, String hashValue,
			String username, Integer mode) throws RemoteException {
		String ret = impl.getLearningDesigns(serverId, datetime, hashValue, username, mode);
		return ret;
	}

}
