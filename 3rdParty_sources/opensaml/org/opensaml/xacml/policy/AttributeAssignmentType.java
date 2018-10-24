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

/** XACML AttributeAssignment schema type. */
public interface AttributeAssignmentType extends AttributeValueType {

    /** Local name of the element AttributeAssignment. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "AttributeAssignment";

    /** QName of the element AttributeAssignment. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "AttributeAssignmentType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);
    
    /** Id of the attribute to be assign . */
    public static final String ATTR_ID_ATTRIB_NAME = "AttributeId";

    /**
     * Gets the ID of the attribute to be assigned.
     * 
     * @return ID of the attribute to be assigned
     */
    public String getAttributeId();

    /**
     * Sets the ID of the attribute to be assigned.
     * 
     * @param id ID of the attribute to be assigned
     */
    public void setAttributeId(String id);
}