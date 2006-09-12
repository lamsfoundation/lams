/**
 * LessonManagerSoapBindingSkeleton.java
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
public class LessonManagerSoapBindingSkeleton implements LessonManager, Skeleton {
	private LessonManager impl;

	private static Map _myOperations = new Hashtable();

	private static Collection _myOperationsList = new ArrayList();

	/**
	 * Returns List of OperationDesc objects with this name
	 */
	public static List getOperationDescByName(java.lang.String methodName) {
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
				new ParameterDesc(new QName("", "ldId"), ParameterDesc.IN, new QName(
						"http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false),
				new ParameterDesc(new QName("", "courseId"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false),
				new ParameterDesc(new QName("", "title"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false),
				new ParameterDesc(new QName("", "desc"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false), };
		_oper = new OperationDesc("startLesson", _params, new QName("", "startLessonReturn"));
		_oper.setReturnType(new QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
		_oper.setElementQName(new QName("", "startLesson"));
		_oper.setSoapAction("");
		_myOperationsList.add(_oper);
		if (_myOperations.get("startLesson") == null) {
			_myOperations.put("startLesson", new ArrayList());
		}
		((List) _myOperations.get("startLesson")).add(_oper);
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
				new ParameterDesc(new QName("", "ldId"), ParameterDesc.IN, new QName(
						"http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false),
				new ParameterDesc(new QName("", "courseId"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false),
				new ParameterDesc(new QName("", "title"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false),
				new ParameterDesc(new QName("", "desc"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false),
				new ParameterDesc(new QName("", "startDate"), ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class,
						false, false), };
		_oper = new OperationDesc("scheduleLesson", _params, new QName("", "scheduleLessonReturn"));
		_oper.setReturnType(new QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
		_oper.setElementQName(new QName("", "scheduleLesson"));
		_oper.setSoapAction("");
		_myOperationsList.add(_oper);
		if (_myOperations.get("scheduleLesson") == null) {
			_myOperations.put("scheduleLesson", new ArrayList());
		}
		((List) _myOperations.get("scheduleLesson")).add(_oper);
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
				new ParameterDesc(new QName("", "lsId"), ParameterDesc.IN, new QName(
						"http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), };
		_oper = new OperationDesc("deleteLearningSession", _params, new QName("",
				"deleteLearningSessionReturn"));
		_oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		_oper.setElementQName(new QName("", "deleteLearningSession"));
		_oper.setSoapAction("");
		_myOperationsList.add(_oper);
		if (_myOperations.get("deleteLearningSession") == null) {
			_myOperations.put("deleteLearningSession", new ArrayList());
		}
		((List) _myOperations.get("deleteLearningSession")).add(_oper);
	}

	public LessonManagerSoapBindingSkeleton() {
		this.impl = new LessonManagerSoapBindingImpl();
	}

	public LessonManagerSoapBindingSkeleton(LessonManager impl) {
		this.impl = impl;
	}

	public Integer startLesson(String serverId, String datetime, String hashValue, String username,
			long ldId, String courseId, String title, String desc) throws RemoteException {
		Integer ret = impl.startLesson(serverId, datetime, hashValue, username, ldId, courseId,
				title, desc);
		return ret;
	}

	public Integer scheduleLesson(String serverId, String datetime, String hashValue,
			String username, long ldId, String courseId, String title, String desc, String startDate)
			throws RemoteException {
		Integer ret = impl.scheduleLesson(serverId, datetime, hashValue, username, ldId, courseId,
				title, desc, startDate);
		return ret;
	}

	public boolean deleteLearningSession(String serverId, String datetime, String hashValue,
			String username, long lsId) throws RemoteException {
		boolean ret = impl.deleteLearningSession(serverId, datetime, hashValue, username, lsId);
		return ret;
	}

}
