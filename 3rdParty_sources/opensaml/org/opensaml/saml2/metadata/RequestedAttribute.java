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

package org.opensaml.saml2.metadata;

import javax.xml.namespace.QName;

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * SAML 2.0 Metadata RequestedAttribute
 * 
 */
public interface RequestedAttribute extends Attribute {

    /** Local name, no namespace */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "RequestedAttribute";

    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20MD_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20MD_PREFIX);

    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "RequestedAttributeType";

    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML20MD_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20MD_PREFIX);

    /** "isRequired" attribute's local name */
    public final static String IS_REQUIRED_ATTRIB_NAME = "isRequired";

    /**
     * Checks to see if this requested attribute is also required.
     * 
     * @return true if this attribute is required
     */
    public Boolean isRequired();

    /**
     * Checks to see if this requested attribute is also required.
     * 
     * @return true if this attribute is required
     */
    public XSBooleanValue isRequiredXSBoolean();

    /**
     * Sets if this requested attribute is also required. Boolean values will be marshalled to either "true" or "false".
     * 
     * @param newIsRequire true if this attribute is required
     */
    public void setIsRequired(Boolean newIsRequire);

    /**
     * Sets if this requested attribute is also required.
     * 
     * @param newIsRequire true if this attribute is required
     */
    public void setIsRequired(XSBooleanValue newIsRequire);
}