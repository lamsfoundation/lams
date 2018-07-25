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

/**
 * 
 */

package org.opensaml.saml2.core;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * SAML 2.0 Core StatusCode.
 */
public interface StatusCode extends SAMLObject {

    /** Local Name of StatusCode. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "StatusCode";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20P_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "StatusCodeType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20P_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** Local Name of the Value attribute. */
    public static final String VALUE_ATTRIB_NAME = "Value";

    /** URI for Success status code. */
    public static final String SUCCESS_URI = "urn:oasis:names:tc:SAML:2.0:status:Success";

    /** URI for Requester status code. */
    public static final String REQUESTER_URI = "urn:oasis:names:tc:SAML:2.0:status:Requester";

    /** URI for Responder status code. */
    public static final String RESPONDER_URI = "urn:oasis:names:tc:SAML:2.0:status:Responder";

    /** URI for VersionMismatch status code. */
    public static final String VERSION_MISMATCH_URI = "urn:oasis:names:tc:SAML:2.0:status:VersionMismatch";

    /** URI for AuthnFailed status code. */
    public static final String AUTHN_FAILED_URI = "urn:oasis:names:tc:SAML:2.0:status:AuthnFailed";

    /** URI for InvalidAttrNameOrValue status code. */
    public static final String INVALID_ATTR_NAME_VALUE_URI = "urn:oasis:names:tc:SAML:2.0:status:InvalidAttrNameOrValue";

    /** URI for InvalidNameIDPolicy status code. */
    public static final String INVALID_NAMEID_POLICY_URI = "urn:oasis:names:tc:SAML:2.0:status:InvalidNameIDPolicy";

    /** URI for NoAuthnContext status code. */
    public static final String NO_AUTHN_CONTEXT_URI = "urn:oasis:names:tc:SAML:2.0:status:NoAuthnContext";

    /** URI for NoAvailableIDP status code. */
    public static final String NO_AVAILABLE_IDP_URI = "urn:oasis:names:tc:SAML:2.0:status:NoAvailableIDP";

    /** URI for NoPassive status code. */
    public static final String NO_PASSIVE_URI = "urn:oasis:names:tc:SAML:2.0:status:NoPassive";

    /** URI for NoSupportedIDP status code. */
    public static final String NO_SUPPORTED_IDP_URI = "urn:oasis:names:tc:SAML:2.0:status:NoSupportedIDP";

    /** URI for PartialLogout status code. */
    public static final String PARTIAL_LOGOUT_URI = "urn:oasis:names:tc:SAML:2.0:status:PartialLogout";

    /** URI for ProxyCountExceeded status code. */
    public static final String PROXY_COUNT_EXCEEDED_URI = "urn:oasis:names:tc:SAML:2.0:status:ProxyCountExceeded";

    /** URI for RequestDenied status code. */
    public static final String REQUEST_DENIED_URI = "urn:oasis:names:tc:SAML:2.0:status:RequestDenied";

    /** URI for RequestUnsupported status code. */
    public static final String REQUEST_UNSUPPORTED_URI = "urn:oasis:names:tc:SAML:2.0:status:RequestUnsupported";

    /** URI for RequestVersionDeprecated status code. */
    public static final String REQUEST_VERSION_DEPRECATED_URI = "urn:oasis:names:tc:SAML:2.0:status:RequestVersionDeprecated";

    /** URI for RequestVersionTooHigh status code. */
    public static final String REQUEST_VERSION_TOO_HIGH_URI = "urn:oasis:names:tc:SAML:2.0:status:RequestVersionTooHigh";
    
    /** URI for RequestVersionTooLow status code. */
    public static final String REQUEST_VERSION_TOO_LOW_URI = "urn:oasis:names:tc:SAML:2.0:status:RequestVersionTooLow";

    /** URI for ResourceNotRecognized status code. */
    public static final String RESOURCE_NOT_RECOGNIZED_URI = "urn:oasis:names:tc:SAML:2.0:status:ResourceNotRecognized";

    /** URI for TooManyResponses status code. */
    public static final String TOO_MANY_RESPONSES = "urn:oasis:names:tc:SAML:2.0:status:TooManyResponses";

    /** URI for UnknownAttrProfile status code. */
    public static final String UNKNOWN_ATTR_PROFILE_URI = "urn:oasis:names:tc:SAML:2.0:status:UnknownAttrProfile";

    /** URI for UnknownPrincipal status code. */
    public static final String UNKNOWN_PRINCIPAL_URI = "urn:oasis:names:tc:SAML:2.0:status:UnknownPrincipal";

    /** URI for UnsupportedBinding status code. */
    public static final String UNSUPPORTED_BINDING_URI = "urn:oasis:names:tc:SAML:2.0:status:UnsupportedBinding";

    /**
     * Gets the Status Code of this Status Code.
     * 
     * @return StatusCode StatusCode
     */
    public StatusCode getStatusCode();

    /**
     * Sets the Status Code of this Status Code.
     * 
     * @param newStatusCode the Status Code of this Status Code.
     */
    public void setStatusCode(StatusCode newStatusCode);

    /**
     * Gets the Value of this Status Code.
     * 
     * @return StatusCode Value
     */
    public String getValue();

    /**
     * Sets the Value of this Status Code.
     * 
     * @param newValue the Value of this Status Code
     */
    public void setValue(String newValue);
}
