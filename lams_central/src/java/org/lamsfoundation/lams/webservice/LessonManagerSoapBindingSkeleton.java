/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.webservice;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.description.FaultDesc;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.wsdl.Skeleton;

/**
 * LessonManagerSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
@SuppressWarnings({ "serial", "unchecked" })
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
		new ParameterDesc(new QName("", "serverId"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "datetime"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "hashValue"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "username"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "ldId"), ParameterDesc.IN,
			new QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false),
		new ParameterDesc(new QName("", "courseId"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "title"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "desc"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "countryIsoCode"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "langIsoCode"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "customCSV"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false,
			false), };
	_oper = new OperationDesc("startLesson", _params, new QName("", "startLessonReturn"));
	_oper.setReturnType(new QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
	_oper.setElementQName(new QName("", "startLesson"));
	_oper.setSoapAction("");
	_myOperationsList.add(_oper);
	if (_myOperations.get("startLesson") == null) {
	    _myOperations.put("startLesson", new ArrayList());
	}
	((List) _myOperations.get("startLesson")).add(_oper);
	_params = new ParameterDesc[] {
		new ParameterDesc(new QName("", "serverId"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "datetime"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "hashValue"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "username"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "ldId"), ParameterDesc.IN,
			new QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false),
		new ParameterDesc(new QName("", "courseId"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "title"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "desc"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "startDate"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "countryIsoCode"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "langIsoCode"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "customCSV"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false,
			false), };
	_oper = new OperationDesc("scheduleLesson", _params, new QName("", "scheduleLessonReturn"));
	_oper.setReturnType(new QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
	_oper.setElementQName(new QName("", "scheduleLesson"));
	_oper.setSoapAction("");
	_myOperationsList.add(_oper);
	if (_myOperations.get("scheduleLesson") == null) {
	    _myOperations.put("scheduleLesson", new ArrayList());
	}
	((List) _myOperations.get("scheduleLesson")).add(_oper);
	_params = new ParameterDesc[] {
		new ParameterDesc(new QName("", "serverId"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "datetime"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "hashValue"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "username"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "lsId"), ParameterDesc.IN,
			new QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), };
	_oper = new OperationDesc("deleteLesson", _params, new QName("", "deleteLessonReturn"));
	_oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
	_oper.setElementQName(new QName("", "deleteLesson"));
	_oper.setSoapAction("");
	_myOperationsList.add(_oper);
	if (_myOperations.get("deleteLesson") == null) {
	    _myOperations.put("deleteLesson", new ArrayList());
	}
	((List) _myOperations.get("deleteLesson")).add(_oper);
    }

    public LessonManagerSoapBindingSkeleton() {
	this.impl = new LessonManagerSoapBindingImpl();
    }

    public LessonManagerSoapBindingSkeleton(LessonManager impl) {
	this.impl = impl;
    }

    @Override
    public Long startLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, String countryIsoCode, String langIsoCode, String customCSV)
	    throws RemoteException {
	Long ret = impl.startLesson(serverId, datetime, hashValue, username, ldId, courseId, title, desc,
		countryIsoCode, langIsoCode, customCSV);
	return ret;
    }

    @Override
    public Long scheduleLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, String startDate, String countryIsoCode, String langIsoCode,
	    String customCSV) throws RemoteException {
	Long ret = impl.scheduleLesson(serverId, datetime, hashValue, username, ldId, courseId, title, desc, startDate,
		countryIsoCode, langIsoCode, customCSV);
	return ret;
    }

    @Override
    public boolean deleteLesson(String serverId, String datetime, String hashValue, String username, long lsId)
	    throws RemoteException {
	boolean ret = impl.deleteLesson(serverId, datetime, hashValue, username, lsId);
	return ret;
    }

}
