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
 * LearningDesignWebServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
@SuppressWarnings({ "serial", "unchecked" })
public class LearningDesignRepositorySoapBindingSkeleton implements LearningDesignRepository, Skeleton {
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
		new ParameterDesc(new QName("", "serverId"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "datetime"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "hashValue"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "username"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "courseId"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "mode"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), Integer.class, false, false),
		new ParameterDesc(new QName("", "country"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false),
		new ParameterDesc(new QName("", "lang"), ParameterDesc.IN,
			new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false,
			false), };
	_oper = new OperationDesc("getLearningDesigns", _params, new QName("", "getLearningDesignsReturn"));
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

    @Override
    public String getLearningDesigns(String serverId, String datetime, String hashValue, String username,
	    String courseId, Integer mode, String country, String lang) throws RemoteException {
	String ret = impl.getLearningDesigns(serverId, datetime, hashValue, username, courseId, mode, country, lang);
	return ret;
    }

}
