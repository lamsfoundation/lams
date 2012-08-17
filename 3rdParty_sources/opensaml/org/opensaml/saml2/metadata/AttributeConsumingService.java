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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * SAML 2.0 Metadata AttributeAuthorityDescriptor
 */
public interface AttributeConsumingService extends SAMLObject {

    /** Element name, no namespace */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "AttributeConsumingService";

    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20MD_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20MD_PREFIX);

    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "AttributeConsumingServiceType";

    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML20MD_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20MD_PREFIX);

    /** "index" attribute's local name */
    public final static String INDEX_ATTRIB_NAME = "index";

    /** "isDefault" attribute's local name */
    public final static String IS_DEFAULT_ATTRIB_NAME = "isDefault";

    /**
     * Gets the index for this service.
     * 
     * @return the index for this service
     */
    public int getIndex();

    /**
     * Sets the index for this service.
     * 
     * @param index the index for this service
     */
    public void setIndex(int index);

    /**
     * Checks if this is the default service for the service provider.
     * 
     * @return true if this is the default service, false if not
     */
    public Boolean isDefault();

    /**
     * Checks if this is the default service for the service provider.
     * 
     * @return true if this is the default service, false if not
     */
    public XSBooleanValue isDefaultXSBoolean();

    /**
     * Sets if this is the default service for the service provider. Boolean values will be marshalled to either "true"
     * or "false".
     * 
     * @param newIsDefault true if this is the default service, false if not
     */
    public void setIsDefault(Boolean newIsDefault);

    /**
     * Sets if this is the default service for the service provider.
     * 
     * @param newIsDefault true if this is the default service, false if not
     */
    public void setIsDefault(XSBooleanValue newIsDefault);

    /**
     * Gets the list of names this service has.
     * 
     * @return list of names this service has
     */
    public List<ServiceName> getNames();

    /**
     * Gets the descriptions for this service.
     * 
     * @return descriptions for this service
     */
    public List<ServiceDescription> getDescriptions();

    /**
     * Gets the attributes this service requests.
     * 
     * @return attributes this service requests
     */
    public List<RequestedAttribute> getRequestAttributes();
}