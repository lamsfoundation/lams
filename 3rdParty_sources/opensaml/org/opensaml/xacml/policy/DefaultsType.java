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
import org.opensaml.xml.schema.XSString;

/** XACML Defaults schema type. */
public interface DefaultsType extends XACMLObject {

    /** Local name of the element PolicySetDefaults. */
    public static final String POLICY_SET_DEFAULTS_ELEMENT_LOCAL_NAME = "PolicySetDefaults";

    /** QName of the element PolicySetDefaults. */
    public static final QName POLICY_SET_DEFAULTS_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            POLICY_SET_DEFAULTS_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the element PolicyDefaults. */
    public static final String POLICY_DEFAULTS_ELEMENT_LOCAL_NAME = "PolicyDefaults";

    /** QName of the element PolicyDefaults. */
    public static final QName POLICY_DEFAULTS_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            POLICY_DEFAULTS_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "DefaultsType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /**
     * Gets the XPath version for this type.
     * 
     * @return XPath version for this type
     */
    public XSString getXPathVersion();

    /**
     * Sets the XPath version for this type.
     * 
     * @param version XPath version for this type
     */
    public void setXPathVersion(XSString version);
}