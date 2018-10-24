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

/** XACML PolicySetCombineParameters schema type. */
public interface PolicySetCombinerParametersType extends CombinerParametersType {

    /** Local name of the element PolicySetCombineParameters. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "PolicySetCombinerParameters";

    /** QName of the element PolicySetCombineParameters. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "PolicySetCombinerParametersType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** PolicySetIdRef attribute name. */
    public static final String POLICY_SET_ID_REF_ATTRIB_NAME = "PolicySetIdRef";

    /**
     * Gets the referenced policy set's ID.
     * 
     * @return referenced policy set's ID
     */
    public String getPolicySetIdRef();

    /**
     * Sets the referenced policy set's ID.
     * 
     * @param ref referenced policy set's ID
     */
    public void setPolicySetIdRef(String ref);
}