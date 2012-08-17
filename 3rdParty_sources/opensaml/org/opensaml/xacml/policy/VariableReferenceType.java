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

package org.opensaml.xacml.policy;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;

/** XACML VariableReference. */
public interface VariableReferenceType extends ExpressionType {

    /** Local name of the element. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "VariableReference";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME_XACML20 = new QName(XACMLConstants.XACML20_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "VariableReferenceType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME_XACML20 = new QName(XACMLConstants.XACML20_NS, TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Name of VariableId attribute. */
    public static final String VARIABLE_ID_ATTRIB_NAME = "VariableId";

    /**
     * Gets the expressions for this definition.
     * 
     * @return expressions for this definition
     */
    public List<ExpressionType> getExpressions();

    /**
     * Gets the ID of the referenced variable.
     * 
     * @return ID of the referenced variable
     */
    public String getVariableId();

    /**
     * Sets the ID of the referenced variable.
     * 
     * @param id ID of the referenced variable
     */
    public void setVariableId(String id);
}