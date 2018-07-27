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

package org.opensaml.xacml.profile.saml.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.saml2.core.impl.RequestAbstractTypeImpl;
import org.opensaml.xacml.ctx.RequestType;
import org.opensaml.xacml.policy.PolicySetType;
import org.opensaml.xacml.policy.PolicyType;
import org.opensaml.xacml.profile.saml.ReferencedPoliciesType;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionQueryType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.util.XMLObjectChildrenList;

/** A concrete implementation of {@link XACMLAuthzDecisionQueryType}. */
public class XACMLAuthzDecisionQueryTypeImpl extends RequestAbstractTypeImpl implements XACMLAuthzDecisionQueryType {

    /** Policy children. */
    private List<PolicyType> policies;

    /** PolicySet children. */
    private List<PolicySetType> policySets;

    /** ReeferencedPolicies child. */
    private ReferencedPoliciesType referencedPolicies;

    /** The xacml-context:Request. */
    private RequestType request;

    /** InputContextOnly attribute value. Default = false. */
    private XSBooleanValue inputContextOnly;

    /** ReturnContext attribute value.Default = false. */
    private XSBooleanValue returnContext;

    /** CombinePolicies attribute value. Default = true. */
    private XSBooleanValue combinePolicies;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected XACMLAuthzDecisionQueryTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        setElementNamespacePrefix(namespacePrefix);
        policies = new XMLObjectChildrenList<PolicyType>(this);
        policySets = new XMLObjectChildrenList<PolicySetType>(this);
    }

    /** {@inheritDoc} */
    public XSBooleanValue getCombinePoliciesXSBooleanValue() {
        return combinePolicies;
    }

    /** {@inheritDoc} */
    public XSBooleanValue getInputContextOnlyXSBooleanValue() {
        return inputContextOnly;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (super.getOrderedChildren() != null) {
            children.addAll(super.getOrderedChildren());
        }
        if (request != null) {
            children.add(request);
        }

        if (!policies.isEmpty()) {
            children.addAll(policies);
        }

        if (!policySets.isEmpty()) {
            children.addAll(policySets);
        }

        if (referencedPolicies != null) {
            children.add(referencedPolicies);
        }

        return Collections.unmodifiableList(children);
    }

    /** {@inheritDoc} */
    public RequestType getRequest() {
        return request;
    }

    /** {@inheritDoc} */
    public XSBooleanValue getReturnContextXSBooleanValue() {
        return returnContext;
    }

    /** {@inheritDoc} */
    public Boolean isCombinePolicies() {
        if (combinePolicies != null) {
            return combinePolicies.getValue();
        }

        return Boolean.TRUE;
    }

    /** {@inheritDoc} */
    public Boolean isInputContextOnly() {
        if (inputContextOnly != null) {
            return inputContextOnly.getValue();
        }

        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public Boolean isReturnContext() {
        if (returnContext != null) {
            return returnContext.getValue();
        }

        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public void setCombinePolicies(XSBooleanValue combinePolicies) {
        this.combinePolicies = prepareForAssignment(this.combinePolicies, combinePolicies);
    }

    /** {@inheritDoc} */
    public void setCombinePolicies(Boolean combinePolicies) {
        if (combinePolicies != null) {
            this.combinePolicies = prepareForAssignment(this.combinePolicies,
                    new XSBooleanValue(combinePolicies, false));
        } else {
            this.combinePolicies = prepareForAssignment(this.combinePolicies, null);
        }

    }

    /** {@inheritDoc} */
    public void setInputContextOnly(XSBooleanValue inputContextOnly) {
        this.inputContextOnly = prepareForAssignment(this.inputContextOnly, inputContextOnly);
    }

    /** {@inheritDoc} */
    public void setInputContextOnly(Boolean inputContextOnly) {
        if (inputContextOnly != null) {
            this.inputContextOnly = prepareForAssignment(this.inputContextOnly, new XSBooleanValue(inputContextOnly,
                    false));
        } else {
            this.inputContextOnly = prepareForAssignment(this.inputContextOnly, null);
        }
    }

    /** {@inheritDoc} */
    public void setRequest(RequestType request) {
        this.request = prepareForAssignment(this.request, request);
    }

    /** {@inheritDoc} */
    public void setReturnContext(XSBooleanValue returnContext) {
        this.returnContext = prepareForAssignment(this.returnContext, returnContext);
    }

    /** {@inheritDoc} */
    public void setReturnContext(Boolean returnContext) {
        if (returnContext != null) {
            this.returnContext = prepareForAssignment(this.returnContext, new XSBooleanValue(returnContext, false));
        } else {
            this.returnContext = prepareForAssignment(this.returnContext, null);
        }
    }

    /** {@inheritDoc} */
    public List<PolicyType> getPolicies() {
        return policies;
    }

    /** {@inheritDoc} */
    public List<PolicySetType> getPolicySets() {
        return policySets;
    }

    /** {@inheritDoc} */
    public ReferencedPoliciesType getReferencedPolicies() {
        return referencedPolicies;
    }

    /** {@inheritDoc} */
    public void setReferencedPolicies(ReferencedPoliciesType policies) {
        referencedPolicies = prepareForAssignment(referencedPolicies, policies);
    }
}