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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.XMLObject;

/**
 * SAML 2.0 Core Attribute.
 */
public interface Attribute extends SAMLObject, AttributeExtensibleXMLObject {

    /** Local name of the Attribute element. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Attribute";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "AttributeType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Name of the Name attribute. */
    public static final String NAME_ATTTRIB_NAME = "Name";

    /** Name for the NameFormat attribute. */
    public static final String NAME_FORMAT_ATTRIB_NAME = "NameFormat";

    /** Name of the FriendlyName attribute. */
    public static final String FRIENDLY_NAME_ATTRIB_NAME = "FriendlyName";

    /** Unspecified attribute format ID. */
    public static final String UNSPECIFIED = "urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified";

    /** URI reference attribute format ID. */
    public static final String URI_REFERENCE = "urn:oasis:names:tc:SAML:2.0:attrname-format:uri";

    /** Basic attribute format ID. */
    public static final String BASIC = "urn:oasis:names:tc:SAML:2.0:attrname-format:basic";

    /**
     * Get the name of this attribute.
     * 
     * @return the name of this attribute
     */
    public String getName();

    /**
     * Sets the name of this attribute.
     * 
     * @param name the name of this attribute
     */
    public void setName(String name);

    /**
     * Get the name format of this attribute.
     * 
     * @return the name format of this attribute
     */
    public String getNameFormat();

    /**
     * Sets the name format of this attribute.
     * 
     * @param nameFormat the name format of this attribute
     */
    public void setNameFormat(String nameFormat);

    /**
     * Get the friendly name of this attribute.
     * 
     * @return the friendly name of this attribute
     */
    public String getFriendlyName();

    /**
     * Sets the friendly name of this attribute.
     * 
     * @param friendlyName the friendly name of this attribute
     */
    public void setFriendlyName(String friendlyName);

    /**
     * Gets the list of attribute values for this attribute.
     * 
     * @return the list of attribute values for this attribute
     */
    public List<XMLObject> getAttributeValues();
}