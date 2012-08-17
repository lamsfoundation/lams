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

import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.XACMLObject;

/** XACML VariableDefinition schema type. */
public interface VariableDefinitionType extends XACMLObject {

    /** Local name of the element. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "VariableDefinition";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "VariableDefinitionType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(XACMLConstants.XACML20_NS, TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Name of VariableId attribute. */
    public static final String VARIABLE_ID_ATTRIB_NAME = "VariableId";

    /**
     * Gets the expression for this definition.
     * 
     * @return expression for this definition
     */
    public ExpressionType getExpression();

    /**
     * Gets the ID of this defined variable.
     * 
     * @return ID of this defined variable
     */
    public String getVariableId();

    /**
     * Sets the ID of this defined variable.
     * 
     * @param id ID of this defined variable
     */
    public void setVariableId(String id);
}