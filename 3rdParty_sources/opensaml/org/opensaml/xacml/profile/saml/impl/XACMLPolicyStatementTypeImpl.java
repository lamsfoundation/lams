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

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.xacml.XACMLObject;
import org.opensaml.xacml.policy.PolicySetType;
import org.opensaml.xacml.policy.PolicyType;
import org.opensaml.xacml.profile.saml.ReferencedPoliciesType;
import org.opensaml.xacml.profile.saml.XACMLPolicyStatementType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/** Concrete implementation of {@link XACMLPolicyStatementType}. */
public class XACMLPolicyStatementTypeImpl extends AbstractSAMLObject implements XACMLPolicyStatementType {

    /** Choice group in element. */
    private IndexedXMLObjectChildrenList<XACMLObject> choiceGroup;

    /** ReferencedPolicie child. */
    private ReferencedPoliciesType referencedPolicies;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected XACMLPolicyStatementTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        choiceGroup = new IndexedXMLObjectChildrenList<XACMLObject>(this);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(choiceGroup);

        if (referencedPolicies != null) {
            children.add(referencedPolicies);
        }

        return Collections.unmodifiableList(children);
    }

    /** {@inheritDoc} */
    public List<PolicyType> getPolicies() {
        return (List<PolicyType>) choiceGroup.subList(PolicyType.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<PolicySetType> getPolicySets() {
        return (List<PolicySetType>) choiceGroup.subList(PolicySetType.DEFAULT_ELEMENT_NAME);
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
