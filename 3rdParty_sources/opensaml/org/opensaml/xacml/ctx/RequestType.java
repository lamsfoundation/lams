/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xacml.ctx;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.XACMLObject;

/** XACML context Request schema type. */
public interface RequestType extends XACMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Request";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20CTX_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, XACMLConstants.XACMLCONTEXT_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RequestType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(XACMLConstants.XACML20CTX_NS, TYPE_LOCAL_NAME,
            XACMLConstants.XACMLCONTEXT_PREFIX);

    /**
     * Gets the subjects from the request.
     * 
     * @return the subjects from the request
     */
    public List<SubjectType> getSubjects();

    /**
     * Gets the resources from the request.
     * 
     * @return the resources from the request
     */
    public List<ResourceType> getResources();

    /**
     * Gets the action from the request.
     * 
     * @return the action from the request
     */
    public ActionType getAction();

    /**
     * Sets the action of the request.
     * 
     * @param newAction action of the request
     */
    public void setAction(ActionType newAction);

    /**
     * Gets the environment from the request.
     * 
     * @return the environment from the request
     */
    public EnvironmentType getEnvironment();

    /**
     * Sets the environment of the request.
     * 
     * @param environment environment of the request
     */
    public void setEnvironment(EnvironmentType environment);
}