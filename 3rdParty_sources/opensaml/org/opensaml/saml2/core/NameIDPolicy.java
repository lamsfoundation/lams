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

package org.opensaml.saml2.core;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * SAML 2.0 Core NameIDPolicy.
 */
public interface NameIDPolicy extends SAMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "NameIDPolicy";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20P_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "NameIDPolicyType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20P_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** Format attribute name. */
    public static final String FORMAT_ATTRIB_NAME = "Format";

    /** SPNameQualifier attribute name. */
    public static final String SP_NAME_QUALIFIER_ATTRIB_NAME = "SPNameQualifier";

    /** AllowCreate attribute name. */
    public static final String ALLOW_CREATE_ATTRIB_NAME = "AllowCreate";

    /**
     * Gets the format of the NameIDPolicy.
     * 
     * @return the format of the NameIDPolicy
     */
    public String getFormat();

    /**
     * Sets the format of the NameIDPolicy
     * 
     * @param newFormat the format of the NameIDPolicy
     */
    public void setFormat(String newFormat);

    /**
     * Gets the SPNameQualifier value.
     * 
     * @return the SPNameQualifier value
     */
    public String getSPNameQualifier();

    /**
     * Sets the SPNameQualifier value.
     * 
     * @param newSPNameQualifier the SPNameQualifier value
     */
    public void setSPNameQualifier(String newSPNameQualifier);

    /**
     * Gets the AllowCreate value.
     * 
     * @return the AllowCreate value
     */
    public Boolean getAllowCreate();

    /**
     * Gets the AllowCreate value.
     * 
     * @return the AllowCreate value
     */
    public XSBooleanValue getAllowCreateXSBoolean();

    /**
     * Sets the AllowCreate value. Boolean values will be marshalled to either "true" or "false".
     * 
     * @param newAllowCreate the AllowCreate value
     */
    public void setAllowCreate(Boolean newAllowCreate);

    /**
     * Sets the AllowCreate value.
     * 
     * @param newAllowCreate the AllowCreate value
     */
    public void setAllowCreate(XSBooleanValue newAllowCreate);

}