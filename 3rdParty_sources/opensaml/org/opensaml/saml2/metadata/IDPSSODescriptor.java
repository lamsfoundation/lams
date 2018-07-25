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

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * SAML 2.0 Metadata IDPSSODescriptorType
 */
public interface IDPSSODescriptor extends SSODescriptor {

    /** Local name, no namespace */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "IDPSSODescriptor";

    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20MD_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20MD_PREFIX);

    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "IDPSSODescriptorType";

    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML20MD_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20MD_PREFIX);

    /** "WantAuthnRequestSigned" attribute name */
    public final static String WANT_AUTHN_REQ_SIGNED_ATTRIB_NAME = "WantAuthnRequestsSigned";

    /**
     * Checks if the IDP SSO service wants authentication requests signed.
     * 
     * @return true is signing is desired, false if not
     */
    public Boolean getWantAuthnRequestsSigned();

    /**
     * Checks if the IDP SSO service wants authentication requests signed.
     * 
     * @return true is signing is desired, false if not
     */
    public XSBooleanValue getWantAuthnRequestsSignedXSBoolean();

    /**
     * Sets whether the IDP SSO service wants authentication requests signed. Boolean values will be marshalled to
     * either "true" or "false".
     * 
     * @param newWantSigned true if request should be signed, false if not
     */
    public void setWantAuthnRequestsSigned(Boolean newWantSigned);

    /**
     * Sets whether the IDP SSO service wants authentication requests signed.
     * 
     * @param newWantSigned true if request should be signed, false if not
     */
    public void setWantAuthnRequestsSigned(XSBooleanValue newWantSigned);

    /**
     * Gets the list of single sign on services for this IDP.
     * 
     * @return list of single sign on services
     */
    public List<SingleSignOnService> getSingleSignOnServices();

    /**
     * Gets the list of NameID mapping services for this service.
     * 
     * @return the list of NameID mapping services for this service
     */
    public List<NameIDMappingService> getNameIDMappingServices();

    /**
     * Gets the list of assertion ID request services.
     * 
     * @return assertion ID request services
     */
    public List<AssertionIDRequestService> getAssertionIDRequestServices();

    /**
     * Gets the list of attribute profiles supported by this IdP.
     * 
     * @return attribute profiles supported by this IdP
     */
    public List<AttributeProfile> getAttributeProfiles();

    /**
     * Gets the list of attributes supported by this IdP.
     * 
     * @return attributes supported by this IdP
     */
    public List<Attribute> getAttributes();
}