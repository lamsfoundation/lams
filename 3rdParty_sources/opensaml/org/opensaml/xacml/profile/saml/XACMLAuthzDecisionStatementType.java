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

package org.opensaml.xacml.profile.saml;

import javax.xml.namespace.QName;

import org.opensaml.saml2.core.Statement;
import org.opensaml.xacml.XACMLObject;
import org.opensaml.xacml.ctx.RequestType;
import org.opensaml.xacml.ctx.ResponseType;

/** A SAML XACML profile XACMLAuthzDecisionStatement schema type. */
public interface XACMLAuthzDecisionStatementType extends Statement, XACMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "XACMLAuthzDecisionStatement";

    /** Default element name for XACML 1.0. */
    public static final QName DEFAULT_ELEMENT_NAME_XACML10 = new QName(SAMLProfileConstants.SAML20XACML10_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, SAMLProfileConstants.SAML20XACMLASSERTION_PREFIX);

    /** Default element name for XACML 1.1. */
    public static final QName DEFAULT_ELEMENT_NAME_XACML11 = new QName(SAMLProfileConstants.SAML20XACML1_1_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, SAMLProfileConstants.SAML20XACMLASSERTION_PREFIX);

    /** Default element name for XACML 2.0. */
    public static final QName DEFAULT_ELEMENT_NAME_XACML20 = new QName(SAMLProfileConstants.SAML20XACML20_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, SAMLProfileConstants.SAML20XACMLASSERTION_PREFIX);

    /** Default element name for XACML 3.0. */
    public static final QName DEFAULT_ELEMENT_NAME_XACML30 = new QName(SAMLProfileConstants.SAML20XACML30_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, SAMLProfileConstants.SAML20XACMLASSERTION_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "XACMLAuthzDecisionStatementType";

    /** QName of the XSI type.XACML1.0. */
    public static final QName TYPE_NAME_XACML10 = new QName(SAMLProfileConstants.SAML20XACML10_NS, TYPE_LOCAL_NAME,
            SAMLProfileConstants.SAML20XACMLASSERTION_PREFIX);

    /** QName of the XSI type.XACML1.1. */
    public static final QName TYPE_NAME_XACML11 = new QName(SAMLProfileConstants.SAML20XACML1_1_NS, TYPE_LOCAL_NAME,
            SAMLProfileConstants.SAML20XACMLASSERTION_PREFIX);

    /** QName of the XSI type.XACML2.0. */
    public static final QName TYPE_NAME_XACML20 = new QName(SAMLProfileConstants.SAML20XACML20_NS, TYPE_LOCAL_NAME,
            SAMLProfileConstants.SAML20XACMLASSERTION_PREFIX);

    /** QName of the XSI type.XACML3.0 . */
    public static final QName TYPE_NAME_XACML30 = new QName(SAMLProfileConstants.SAML20XACML30_NS, TYPE_LOCAL_NAME,
            SAMLProfileConstants.SAML20XACMLASSERTION_PREFIX);

    /**
     * Get's the {@link RequestType} from the <code>XACMLAuthzDecisionStatement</code>.
     * 
     * @return the {@link RequestType} inside the <code>XACMLAuthzDecisionStatement</code>
     */
    public RequestType getRequest();

    /**
     * Get's the {@link ResponseType} from the <code>XACMLAuthzDecisionStatement</code>.
     * 
     * @return the {@link ResponseType} inside the <code>XACMLAuthzDecisionStatement</code>
     */
    public ResponseType getResponse();

    /**
     * Sets a {@link ResponseType} to the <code>XACMLAuthzDecisionStatement</code>.
     * 
     * @param request {@link RequestType}
     */
    public void setRequest(RequestType request);

    /**
     * Sets a {@link ResponseType} to the <code>XACMLAuthzDecisionStatement</code>.
     * 
     * @param response {@link ResponseType}
     */
    public void setResponse(ResponseType response);
}
