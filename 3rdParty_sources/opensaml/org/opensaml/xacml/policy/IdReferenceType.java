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

/** XACML IdReference schema type. */
public interface IdReferenceType extends XACMLObject, XSString {

    /** Local name of the element PolicySetIdReference. */
    public static final String POLICY_SET_ID_REFERENCE_ELEMENT_LOCAL_NAME = "PolicySetIdReference";

    /** QName of the element PolicySetIdReference. */
    public static final QName POLICY_SET_ID_REFERENCE_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            POLICY_SET_ID_REFERENCE_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the element PolicyIdReference. */
    public static final String POLICY_ID_REFERENCE_ELEMENT_LOCAL_NAME = "PolicyIdReference";

    /** QName of the element PolicyIdReference. */
    public static final QName POLICY_ID_REFERENCE_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            POLICY_ID_REFERENCE_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "IdReferenceType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Version attribute name. */
    public static final String VERSION_ATTRIB_NAME = "Version";

    /** EarliestVersion attribute name. */
    public static final String EARLIEST_VERSION_ATTRIB_NAME = "EarliestVersion";

    /** LatestVersion attribute name. */
    public static final String LATEST_VERSION_ATTRIB_NAME = "LatestVersion";

    /**
     * Gets the version of the reference.
     * 
     * @return version of the reference
     */
    public String getVersion();

    /**
     * Sets the version of the reference.
     * 
     * @param version version of the reference
     */
    public void setVersion(String version);

    /**
     * Gets the earliest version of the reference.
     * 
     * @return earliest version of the reference
     */
    public String getEarliestVersion();

    /**
     * Sets the earliest version of the reference.
     * 
     * @param version earliest version of the reference
     */
    public void setEarliestVersion(String version);

    /**
     * Gets the latest version of the reference.
     * 
     * @return latest version of the reference
     */
    public String getLatestVersion();

    /**
     * Sets the latest version of the reference.
     * 
     * @param version latest version of the reference
     */
    public void setLatestVersion(String version);

}