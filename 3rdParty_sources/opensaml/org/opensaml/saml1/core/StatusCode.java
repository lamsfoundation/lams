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

package org.opensaml.saml1.core;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * This interface defines how the object representing a SAML 1 <code> StatusCode</code> element behaves.
 */
public interface StatusCode extends SAMLObject {

    /** Element name, no namespace. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "StatusCode";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML10P_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML1P_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "StatusCodeType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML10P_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML1P_PREFIX);

    /** Success status value. */
    public static final QName SUCCESS = new QName(SAMLConstants.SAML10P_NS, "Success", SAMLConstants.SAML1P_PREFIX);

    /** VersionMismatch status value. */
    public static final QName VERSION_MISMATCH = new QName(SAMLConstants.SAML10P_NS, "VersionMismatch",
            SAMLConstants.SAML1P_PREFIX);

    /** Requester status value. */
    public static final QName REQUESTER = new QName(SAMLConstants.SAML10P_NS, "Requester", SAMLConstants.SAML1P_PREFIX);

    /** Responder status value. */
    public static final QName RESPONDER = new QName(SAMLConstants.SAML10P_NS, "Responder", SAMLConstants.SAML1P_PREFIX);

    /** RequestVersionTooHigh status value. */
    public static final QName REQUEST_VERSION_TOO_HIGH = new QName(SAMLConstants.SAML10P_NS, "RequestVersionTooHigh",
            SAMLConstants.SAML1P_PREFIX);

    /** RequestVersionTooLow status value. */
    public static final QName REQUEST_VERSION_TOO_LOW = new QName(SAMLConstants.SAML10P_NS, "RequestVersionTooLow",
            SAMLConstants.SAML1P_PREFIX);

    /** RequestVersionDeprecated status value. */
    public static final QName REQUEST_VERSION_DEPRECATED = new QName(SAMLConstants.SAML10P_NS,
            "RequestVersionDeprecated", SAMLConstants.SAML1P_PREFIX);

    /** 
     * RequestVersionDepricated status value <i>(sic)</i>.
     * 
     * @deprecated due to typo, use {@link #REQUEST_VERSION_DEPRECATED} instead.
     * */
    public static final QName REQUEST_VERSION_DEPRICATED = REQUEST_VERSION_DEPRECATED;
    
    public static final QName TOO_MANY_RESPONSES = new QName(SAMLConstants.SAML10P_NS, "TooManyResponses",
            SAMLConstants.SAML1P_PREFIX);

    /** RequestDenied status value. */
    public static final QName REQUEST_DENIED = new QName(SAMLConstants.SAML10P_NS, "RequestDenied",
            SAMLConstants.SAML1P_PREFIX);

    /** ResourceNotRecognized status value. */
    public static final QName RESOURCE_NOT_RECOGNIZED = new QName(SAMLConstants.SAML10P_NS, "ResourceNotRecognized",
            SAMLConstants.SAML1P_PREFIX);

    /** Name for the attribute which defines the Value. */
    public static final String VALUE_ATTRIB_NAME = "Value";

    /**
     * Gets the value of the status code.
     * 
     * @return value of the status code
     */
    public QName getValue();

    /**
     * Sets the value of the status code.
     * 
     * @param value value of the status code
     */
    public void setValue(QName value);

    /**
     * Gets the second level status code.
     * 
     * @return second level status code
     */
    public StatusCode getStatusCode();

    /**
     * Sets the second level status code.
     * 
     * @param statusCode second level status code
     * @throws IllegalArgumentException
     */
    public void setStatusCode(StatusCode statusCode);
}