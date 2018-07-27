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

package org.opensaml.ws.wssecurity;

import javax.xml.namespace.QName;

/**
 * Constants for the WS-Security 2004.
 * 
 * @see "WS-Security 2004 Specification"
 * 
 */
public final class WSSecurityConstants {
    
    // Namespaces and prefixes.

    /** WS-Security SOAP Message Security 1.0 namespace. */
    public static final String WS_SECURITY_NS =
        "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0";

    /** WS-Security SOAP Message Security 1.1 namespace. */
    public static final String WS_SECURITY11_NS =
        "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1";

    /** WS-Security Utility 1.0 namespace. */
    public static final String WSU_NS =
        "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";

    /** WS-Security Utility 1.0 prefix. */
    public static final String WSU_PREFIX = "wsu";

    /** WS-Security Security Extension 1.0 namespace. */
    public static final String WSSE_NS =
        "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    /** WS-Security Security Extension 1.0 prefix. */
    public static final String WSSE_PREFIX = "wsse";

    /** WS-Security Security Extension 1.1 namespace. */
    public static final String WSSE11_NS =
        "http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd";

    /** WS-Security Security Extension 1.1 prefix. */
    public static final String WSSE11_PREFIX = "wsse11";

    /** WS-Security SAML Token Profile 1.0 namespace. */
    public static final String WSSE_SAML_TOKEN_PROFILE_NS =
        "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0";

    /** WS-Security SAML Token Profile 1.1 namespace. */
    public static final String WSSE11_SAML_TOKEN_PROFILE_NS =
        "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1";

    /** WS-Security Username Token Profile 1.0 namespace. */
    public static final String WSSE_USERNAME_TOKEN_PROFILE_NS =
        "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0";

    /** WS-Security X509 Token Profile 1.0 namespace. */
    public static final String WSSE_X509_TOKEN_PROFILE_NS =
        "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0";

    /** WS-Security Kerberos Token Profile 1.1 namespace. */
    public static final String WSSE_KERBEROS_TOKEN_PROFILE_NS =
        "http://docs.oasis-open.org/wss/oasis-wss-kerberos-token-profile-1.1";
    
    // SOAP fault codes.
    
    /** WS-Security SOAP fault code: SOAP"wsse:UnsupportedSecurityToken". */
    public static final QName SOAP_FAULT_UNSUPPORTED_SECURITY_TOKEN =
        new QName(WSSE_NS, "UnsupportedSecurityToken", WSSE_PREFIX);
    
    /** WS-Security SOAP fault code: "wsse:UnsupportedAlgorithm". */
    public static final QName SOAP_FAULT_UNSUPPORTED_ALGORITHM =
        new QName(WSSE_NS, "UnsupportedAlgorithm", WSSE_PREFIX);
    
    /** WS-Security SOAP fault code: "wsse:InvalidSecurity". */
    public static final QName SOAP_FAULT_INVALID_SECURITY =
        new QName(WSSE_NS, "InvalidSecurity", WSSE_PREFIX);
    
    /** WS-Security SOAP fault code: "wsse:InvalidSecurityToken". */
    public static final QName SOAP_FAULT_INVALID_SECURITY_TOKEN =
        new QName(WSSE_NS, "InvalidSecurityToken", WSSE_PREFIX);
    
    /** WS-Security SOAP fault code: "wsse:FailedAuthentication". */
    public static final QName SOAP_FAULT_FAILED_AUTHENTICATION =
        new QName(WSSE_NS, "FailedAuthentication", WSSE_PREFIX);
    
    /** WS-Security SOAP fault code: "wsse:FailedCheck". */
    public static final QName SOAP_FAULT_FAILED_CHECK =
        new QName(WSSE_NS, "FailedCheck", WSSE_PREFIX);
    
    /** WS-Security SOAP fault code: "wsse:SecurityTokenUnavailable". */
    public static final QName SOAP_FAULT_SECURITY_TOKEN_UNAVAILABLE =
        new QName(WSSE_NS, "SecurityTokenUnavailable", WSSE_PREFIX);
    
    /** WS-Security SOAP fault code: "wsu:MessageExpired". */
    public static final QName SOAP_FAULT_MESSAGE_EXPIRED =
        new QName(WSU_NS, "MessageExpired", WSU_PREFIX);
    
    // Other constants
    
    /** WS-Security - Username Token Profile - UsernameToken. */
    public static final String USERNAME_TOKEN =
        WSSE_USERNAME_TOKEN_PROFILE_NS + "#UsernameToken";

    /** WS-Security - X.509 Token Profile - X509V3. */
    public static final String X509_V3 =
        WSSE_X509_TOKEN_PROFILE_NS + "#X509v3";

    /** WS-Security - X.509 Token Profile - X509V1. */
    public static final String X509_V1 =
        WSSE_X509_TOKEN_PROFILE_NS + "#X509v1";

    /** WS-Security - X.509 Token Profile - X509PKIPathv1. */
    public static final String X509_PKI_PATH_V1 =
        WSSE_X509_TOKEN_PROFILE_NS + "#X509PKIPathv1";

    /** WS-Security - X.509 Token Profile - PKCS7. */
    public static final String X509_PKCS7 =
        WSSE_X509_TOKEN_PROFILE_NS + "#PKCS7";
    
    /** WS-Security - X.509 Token Profile - X509SubjectKeyIdentifier. */
    public static final String X509_SUBJECT_KEY_IDENTIFIER =
        WSSE_X509_TOKEN_PROFILE_NS + "#X509SubjectKeyIdentifier";

    /** WS-Security - Kerberos Token Profile - Kerberosv5_AP_REQ. */
    public static final String KERBEROS_AP_REQ =
        WSSE_KERBEROS_TOKEN_PROFILE_NS + "#Kerberosv5_AP_REQ";

    /** WS-Security - Kerberos Token Profile - GSS_Kerberosv5_AP_REQ. */
    public static final String GSS_KERBEROS_AP_REQ =
        WSSE_KERBEROS_TOKEN_PROFILE_NS + "#GSS_Kerberosv5_AP_REQ";

    /** WS-Security - Kerberos Token Profile - Kerberosv5_AP_REQ1510. */
    public static final String KERBEROS_AP_REQ_1510 =
        WSSE_KERBEROS_TOKEN_PROFILE_NS + "#Kerberosv5_AP_REQ1510";

    /** WS-Security - Kerberos Token Profile - GSS_Kerberosv5_AP_REQ1510. */
    public static final String GSS_KERBEROS_AP_REQ_1510 =
        WSSE_KERBEROS_TOKEN_PROFILE_NS + "#GSS_Kerberosv5_AP_REQ1510";

    /** WS-Security - KeyIdentifier - ThumbPrintSHA1.*/
    public static final String THUMB_PRINT_SHA1 =
        WS_SECURITY_NS + "#ThumbPrintSHA1";

    /** WS-Security - KeyIdentifier - EncryptedKeySHA1.*/
    public static final String ENCRYPTED_KEY_SHA1 =
        WS_SECURITY_NS + "#EncryptedKeySHA1";
    
    /**
     * Prevents instantiation.
     */
    private WSSecurityConstants() {
    }

}
