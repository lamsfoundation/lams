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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.xacml.XACMLObject;
import org.opensaml.xacml.ctx.RequestType;
import org.opensaml.xacml.policy.PolicySetType;
import org.opensaml.xacml.policy.PolicyType;
import org.opensaml.xml.schema.XSBooleanValue;

/** A SAML XACML profile XACMLAuthzDecisionQuery schema type. */
public interface XACMLAuthzDecisionQueryType extends RequestAbstractType, XACMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "XACMLAuthzDecisionQuery";

    /** Default element name for XACML 1.0. */
    public static final QName DEFAULT_ELEMENT_NAME_XACML10 = new QName(SAMLProfileConstants.SAML20XACML10P_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, SAMLProfileConstants.SAML20XACMLPROTOCOL_PREFIX);

    /** Default element name for XACML 1.1. */
    public static final QName DEFAULT_ELEMENT_NAME_XACML11 = new QName(SAMLProfileConstants.SAML20XACML1_1P_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, SAMLProfileConstants.SAML20XACMLPROTOCOL_PREFIX);

    /** Default element name for XACML 2.0. */
    public static final QName DEFAULT_ELEMENT_NAME_XACML20 = new QName(SAMLProfileConstants.SAML20XACML20P_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, SAMLProfileConstants.SAML20XACMLPROTOCOL_PREFIX);

    /** Default element name for XACML 3.0. */
    public static final QName DEFAULT_ELEMENT_NAME_XACML30 = new QName(SAMLProfileConstants.SAML20XACML30P_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, SAMLProfileConstants.SAML20XACMLPROTOCOL_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "XACMLAuthzDecisionQueryType";

    /** QName of the XSI type.XACML1.0. */
    public static final QName TYPE_NAME_XACML10 = new QName(SAMLProfileConstants.SAML20XACML10P_NS, TYPE_LOCAL_NAME,
            SAMLProfileConstants.SAML20XACMLPROTOCOL_PREFIX);

    /** QName of the XSI type.XACML1.1. */
    public static final QName TYPE_NAME_XACML11 = new QName(SAMLProfileConstants.SAML20XACML1_1P_NS, TYPE_LOCAL_NAME,
            SAMLProfileConstants.SAML20XACMLPROTOCOL_PREFIX);

    /** QName of the XSI type.XACML2.0. */
    public static final QName TYPE_NAME_XACML20 = new QName(SAMLProfileConstants.SAML20XACML20P_NS, TYPE_LOCAL_NAME,
            SAMLProfileConstants.SAML20XACMLPROTOCOL_PREFIX);

    /** QName of the XSI type.XACML3.0. */
    public static final QName TYPE_NAME_XACML30 = new QName(SAMLProfileConstants.SAML20XACML30P_NS, TYPE_LOCAL_NAME,
            SAMLProfileConstants.SAML20XACMLPROTOCOL_PREFIX);

    /** InputContextOnly attribute name. */
    public static final String INPUTCONTEXTONLY_ATTRIB_NAME = "InputContextOnly";

    /** ReturnContext attribute name. */
    public static final String RETURNCONTEXT_ATTRIB_NAME = "ReturnContext";

    /** CombinePolicies attribute name. */
    public static final String COMBINEPOLICIES_ATTRIB_NAME = "CombinePolicies";

    /**
     * Returns if the PDP can combine policies from the query and local policies.
     * 
     * @return XSBooleanValue true if the PDP can combine policies from the query and locally
     */
    public XSBooleanValue getCombinePoliciesXSBooleanValue();

    /**
     * True then use only information in the XACMLAuthzDecisionQuery, if false could use external XACML attributes.
     * 
     * @return if the use of just attributes in the XACMLAuthzDecisionQuery is allowed
     */
    public XSBooleanValue getInputContextOnlyXSBooleanValue();

    /**
     * Gets the policies to be used while rendering a decision.
     * 
     * @return policies to be used while rendering a decision
     */
    public List<PolicyType> getPolicies();

    /**
     * Gets the policy sets to be used while rendering a decision.
     * 
     * @return policy sets to be used while rendering a decision
     */
    public List<PolicySetType> getPolicySets();

    /**
     * Gets the reference to the policies to be used while rendering a decision.
     * 
     * @return references to the policies to be used while rendering a decision
     */
    public ReferencedPoliciesType getReferencedPolicies();

    /**
     * Sets the reference to the policies to be used while rendering a decision.
     * 
     * @param policies reference to the policies to be used while rendering a decision
     */
    public void setReferencedPolicies(ReferencedPoliciesType policies);

    /**
     * Gets the request of the query.
     * 
     * @return XACMLRequest The request inside the query
     */
    public RequestType getRequest();

    /**
     * If true then include the {@link org.opensaml.xacml.ctx.RequestType} in the response.
     * 
     * @return boolean true if the {@link org.opensaml.xacml.ctx.RequestType} should be included in the response
     */
    public XSBooleanValue getReturnContextXSBooleanValue();

    /**
     * Returns if the PDP can combine policies from the query and local policies.
     * 
     * @return true if the PDP can combine policies from the query and locally
     */
    public Boolean isCombinePolicies();

    /**
     * True then use only information in the XACMLAuthzDecisionQuery, if false could use external XACML attributes.
     * 
     * @return boolean true then use of just attributes in the XACMLAuthzDecisionQuery is allowed
     */
    public Boolean isInputContextOnly();

    /**
     * If true then include the {@link RequestType} in the response.
     * 
     * @return boolean if the {@link RequestType} should be included in the response
     */
    public Boolean isReturnContext();

    /**
     * Sets if the PDP can combine policies from this query and the one locally.
     * 
     * @param combinePolicies If true then the PDP can combine policies from this query and the one locally
     */
    public void setCombinePolicies(Boolean combinePolicies);

    /**
     * Sets if the PDP can combine policies from this query and the one locally.
     * 
     * @param combinePolicies If true then the PDP can combine policies from this query and the one locally
     */
    public void setCombinePolicies(XSBooleanValue combinePolicies);

    /**
     * Sets if external attributes is allowed in the decision, true if it's allowed.
     * 
     * @param inputContextOnly if external attributes is allowed in the decision, true if it's allowed.
     */
    public void setInputContextOnly(Boolean inputContextOnly);

    /**
     * Sets if external attributes is allowed in the decision, true if it's allowed.
     * 
     * @param inputContextOnly if external attributes is allowed in the decision, true if it's allowed.
     */
    public void setInputContextOnly(XSBooleanValue inputContextOnly);

    /**
     * Set's the XACML Request.
     * 
     * @param request The request of the decision query
     */
    public void setRequest(RequestType request);

    /**
     * Set's if the {@link RequestType} should be included inside the request message.
     * 
     * @param returnContext is true if the {@link RequestType} should be included inside the request message
     */
    public void setReturnContext(Boolean returnContext);

    /**
     * Set's if the {@link RequestType} should be included inside the request message.
     * 
     * @param returnContext is true if the {@link RequestType} should be included inside the request message
     */
    public void setReturnContext(XSBooleanValue returnContext);

}
