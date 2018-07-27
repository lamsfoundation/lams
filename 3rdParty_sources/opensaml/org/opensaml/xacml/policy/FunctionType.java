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

/** XACML Function schema type. */
public interface FunctionType extends ExpressionType {
    
    /** Local name of the element Function. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Function";

    /** QName of the element Function. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "FunctionType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** FunctionId attribute name. */
    public static final String FUNCTION_ID_ATTRIB_NAME = "FunctionId";

    /**
     * Gets the ID of this function.
     * 
     * @return ID of this function
     */
    public String getFunctionId();

    /**
     * Sets the ID of this function.
     * 
     * @param id ID of this function
     */
    public void setFunctionId(String id);
}