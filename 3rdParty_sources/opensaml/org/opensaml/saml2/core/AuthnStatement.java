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

import org.joda.time.DateTime;
import org.opensaml.common.xml.SAMLConstants;

/**
 * SAML 2.0 Core AuthnStatement.
 */
public interface AuthnStatement extends Statement {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "AuthnStatement";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "AuthnStatementType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** AuthnInstant attribute name. */
    public static final String AUTHN_INSTANT_ATTRIB_NAME = "AuthnInstant";

    /** SessionIndex attribute name. */
    public static final String SESSION_INDEX_ATTRIB_NAME = "SessionIndex";

    /** SessionNoOnOrAfter attribute name. */
    public static final String SESSION_NOT_ON_OR_AFTER_ATTRIB_NAME = "SessionNotOnOrAfter";

    /**
     * Gets the time when the authentication took place.
     * 
     * @return the time when the authentication took place
     */
    public DateTime getAuthnInstant();

    /**
     * Sets the time when the authentication took place.
     * 
     * @param newAuthnInstant the time when the authentication took place
     */
    public void setAuthnInstant(DateTime newAuthnInstant);

    /**
     * Get the session index between the principal and the authenticating authority.
     * 
     * @return the session index between the principal and the authenticating authority
     */
    public String getSessionIndex();

    /**
     * Sets the session index between the principal and the authenticating authority.
     * 
     * @param newIndex the session index between the principal and the authenticating authority
     */
    public void setSessionIndex(String newIndex);

    /**
     * Get the time when the session between the principal and the SAML authority ends.
     * 
     * @return the time when the session between the principal and the SAML authority ends
     */
    public DateTime getSessionNotOnOrAfter();

    /**
     * Set the time when the session between the principal and the SAML authority ends.
     * 
     * @param newSessionNotOnOrAfter the time when the session between the principal and the SAML authority ends
     */
    public void setSessionNotOnOrAfter(DateTime newSessionNotOnOrAfter);

    /**
     * Get the DNS domain and IP address of the system where the principal was authenticated.
     * 
     * @return the DNS domain and IP address of the system where the principal was authenticated
     */
    public SubjectLocality getSubjectLocality();

    /**
     * Set the DNS domain and IP address of the system where the principal was authenticated.
     * 
     * @param newLocality the DNS domain and IP address of the system where the principal was authenticated
     */
    public void setSubjectLocality(SubjectLocality newLocality);

    /**
     * Gets the context used to authenticate the subject.
     * 
     * @return the context used to authenticate the subject
     */
    public AuthnContext getAuthnContext();

    /**
     * Sets the context used to authenticate the subject.
     * 
     * @param newAuthnContext the context used to authenticate the subject
     */
    public void setAuthnContext(AuthnContext newAuthnContext);
}