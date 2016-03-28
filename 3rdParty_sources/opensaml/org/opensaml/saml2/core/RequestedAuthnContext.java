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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * SAML 2.0 Core RequestedAuthnContext.
 */
public interface RequestedAuthnContext extends SAMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "RequestedAuthnContext";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20P_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RequestedAuthnContextType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20P_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** SessionIndex attribute name. */
    public static final String COMPARISON_ATTRIB_NAME = "Comparison";

    /**
     * Gets the Comparison attribute value of the requested authn context.
     * 
     * @return the Comparison attribute value of the requested authn context
     */
    public AuthnContextComparisonTypeEnumeration getComparison();

    /**
     * Sets the Comparison attribute value of the requested authn context.
     * 
     * @param newComparison the SessionIndex of this request
     */
    public void setComparison(AuthnContextComparisonTypeEnumeration newComparison);

    /**
     * Gets the AuthnContextClassRefs of this request.
     * 
     * @return the AuthnContextClassRefs of this request
     */
    public List<AuthnContextClassRef> getAuthnContextClassRefs();

    /**
     * Gets the AuthnContextDeclRefs of this request.
     * 
     * @return the AuthnContextDeclRef of this request
     */
    public List<AuthnContextDeclRef> getAuthnContextDeclRefs();

}
