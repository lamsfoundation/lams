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

/** SAML 2.0 Core AuthnContext. */
public interface AuthnContext extends SAMLObject {

    /** Local Name of AuthnContext. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "AuthnContext";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "AuthnContextType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** URI for Internet Protocol authentication context. */
    public static final String IP_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:InternetProtocol";

    /** URI for Internet Protocol Password authentication context. */
    public static final String IP_PASSWORD_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:InternetProtocolPassword";

    /** URI for Kerberos authentication context. */
    public static final String KERBEROS_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:Kerberos";

    /** URI for Mobile One Factor Unregistered authentication context. */
    public static final String MOFU_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:MobileOneFactorUnregistered";

    /** URI for Mobile Two Factor Unregistered authentication context. */
    public static final String MTFU = "urn:oasis:names:tc:SAML:2.0:ac:classes:MobileTwoFactorUnregistered";

    /** URI for Mobile One Factor Contract authentication context. */
    public static final String MOFC_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:MobileOneFactorContract";

    /** URI for Mobile Two Factor Contract authentication context. */
    public static final String MTFC_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:MobileTwoFactorContract";

    /** URI for Password authentication context. */
    public static final String PASSWORD_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:Password";

    /** URI for Password Protected Transport authentication context. */
    public static final String PPT_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport";

    /** URI for Previous Session authentication context. */
    public static final String PREVIOUS_SESSION_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:PreviousSession";

    /** URI for X509 Public Key authentication context. */
    public static final String X509_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:X509";

    /** URI for PGP authentication context. */
    public static final String PGP_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:PGP";

    /** URI for SPKI authentication context. */
    public static final String SPKI_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:SPKI";

    /** URI for XML Digital Signature authentication context. */
    public static final String XML_DSIG_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:XMLDSig";

    /** URI for Smart Card authentication context. */
    public static final String SMARTCARD_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:Smartcard";

    /** URI for Smart Card PKI authentication context. */
    public static final String SMARTCARD_PKI_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:SmartcardPKI";

    /** URI for Software PKU authentication context. */
    public static final String SOFTWARE_PKI_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:SoftwarePKI";

    /** URI for Telephony authentication context. */
    public static final String TELEPHONY_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:Telephony";

    /** URI for Nomadic Telephony authentication context. */
    public static final String NOMAD_TELEPHONY_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:NomadTelephony";

    /** URI for Personalized Telephony authentication context. */
    public static final String PERSONAL_TELEPHONY_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:PersonalTelephony";

    /** URI for Authenticated Telephony authentication context. */
    public static final String AUTHENTICATED_TELEPHONY_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:AuthenticatedTelephony";

    /** URI for Secure Remote Password authentication context. */
    public static final String SRP_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:SecureRemotePassword";

    /** URI for SSL/TLS Client authentication context. */
    public static final String TLS_CLIENT_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:TLSClient";

    /** URI for Time Synchornized Token authentication context. */
    public static final String TIME_SYNC_TOKEN_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:TimeSyncToken";

    /** URI for unspecified authentication context. */
    public static final String UNSPECIFIED_AUTHN_CTX = "urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified";

    /**
     * Gets the URI identifying the Context Class of this Authentication Context.
     * 
     * @return AuthnContext AuthnContextClassRef
     */
    public AuthnContextClassRef getAuthnContextClassRef();

    /**
     * Sets the URI identifying the Context Class of this Authentication Context.
     * 
     * @param newAuthnContextClassRef the URI of this Authentication Context's Class.
     */
    public void setAuthnContextClassRef(AuthnContextClassRef newAuthnContextClassRef);

    /**
     * Gets Declaration of this Authentication Context.
     * 
     * @return AuthnContext AuthnContextDecl
     */
    public AuthnContextDecl getAuthContextDecl();

    /**
     * Sets the Declaration of this Authentication Context.
     * 
     * @param newAuthnContextDecl the Declaration of this Authentication Context
     */
    public void setAuthnContextDecl(AuthnContextDecl newAuthnContextDecl);

    /**
     * Gets the URI of the Declaration of this Authentication Context.
     * 
     * @return AuthnContext AuthnContextDeclRef
     */
    public AuthnContextDeclRef getAuthnContextDeclRef();

    /**
     * Sets the URI of the Declaration of this Authentication Context.
     * 
     * @param newAuthnContextDeclRef the URI of the Declaration of this Authentication Context
     */
    public void setAuthnContextDeclRef(AuthnContextDeclRef newAuthnContextDeclRef);

    /**
     * Gets the Authenticating Authorities of this Authentication Context.
     * 
     * @return AuthnContext AuthenticatingAuthorities
     */
    public List<AuthenticatingAuthority> getAuthenticatingAuthorities();
}
