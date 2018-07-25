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

package org.opensaml.xacml.policy.impl;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.xacml.XACMLObject;
import org.opensaml.xacml.impl.AbstractXACMLObject;
import org.opensaml.xacml.policy.CombinerParametersType;
import org.opensaml.xacml.policy.DefaultsType;
import org.opensaml.xacml.policy.DescriptionType;
import org.opensaml.xacml.policy.IdReferenceType;
import org.opensaml.xacml.policy.ObligationsType;
import org.opensaml.xacml.policy.PolicyCombinerParametersType;
import org.opensaml.xacml.policy.PolicySetCombinerParametersType;
import org.opensaml.xacml.policy.PolicySetType;
import org.opensaml.xacml.policy.PolicyType;
import org.opensaml.xacml.policy.TargetType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/** Concrete implementation of {@link PolicySetType}. */
public class PolicySetTypeImpl extends AbstractXACMLObject implements PolicySetType {

    /** Policy set description. */
    private DescriptionType description;

    /** Policy set defaults. */
    private DefaultsType policySetDefaults;

    /** Policy set target. */
    private TargetType target;

    /** Elements within the choice group. */
    private IndexedXMLObjectChildrenList<? extends XACMLObject> choiceGroup;

    /** Policy obligations. */
    private ObligationsType obligations;

    /** ID of this policy set. */
    private String policySetId;

    /** Version of this policy set. */
    private String version;

    /** Policy combinging algorithm ID. */
    private String combiningAlgo;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected PolicySetTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        choiceGroup = new IndexedXMLObjectChildrenList<XACMLObject>(this);
    }

    /** {@inheritDoc} */
    public List<CombinerParametersType> getCombinerParameters() {
        return (List<CombinerParametersType>) choiceGroup.subList(CombinerParametersType.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public DescriptionType getDescription() {
        return description;
    }

    /** {@inheritDoc} */
    public ObligationsType getObligations() {
        return obligations;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        if (description != null) {
            children.add(description);
        }

        if (policySetDefaults != null) {
            children.add(policySetDefaults);
        }

        children.add(target);

        if (!choiceGroup.isEmpty()) {
            children.addAll(choiceGroup);
        }

        if (obligations != null) {
            children.add(obligations);
        }

        return children;
    }

    /** {@inheritDoc} */
    public List<PolicyType> getPolicies() {
        return (List<PolicyType>) choiceGroup.subList(PolicyType.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<PolicyCombinerParametersType> getPolicyCombinerParameters() {
        return (List<PolicyCombinerParametersType>) choiceGroup
                .subList(PolicyCombinerParametersType.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public String getPolicyCombiningAlgoId() {
        return combiningAlgo;
    }

    /** {@inheritDoc} */
    public List<IdReferenceType> getPolicyIdReferences() {
        return (List<IdReferenceType>) choiceGroup.subList(IdReferenceType.POLICY_ID_REFERENCE_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<PolicySetCombinerParametersType> getPolicySetCombinerParameters() {
        return (List<PolicySetCombinerParametersType>) choiceGroup
                .subList(PolicySetCombinerParametersType.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public DefaultsType getPolicySetDefaults() {
        return policySetDefaults;
    }

    /** {@inheritDoc} */
    public String getPolicySetId() {
        return policySetId;
    }

    /** {@inheritDoc} */
    public List<IdReferenceType> getPolicySetIdReferences() {
        return (List<IdReferenceType>) choiceGroup.subList(IdReferenceType.POLICY_SET_ID_REFERENCE_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<PolicySetType> getPolicySets() {
        return (List<PolicySetType>) choiceGroup.subList(PolicySetType.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public TargetType getTarget() {
        return target;
    }

    /** {@inheritDoc} */
    public String getVersion() {
        return version;
    }

    /** {@inheritDoc} */
    public void setDescription(DescriptionType newDescription) {
        this.description = prepareForAssignment(this.description, newDescription);
    }

    /** {@inheritDoc} */
    public void setObligations(ObligationsType newObligations) {
        this.obligations = prepareForAssignment(this.obligations, newObligations);
    }

    /** {@inheritDoc} */
    public void setPolicyCombiningAlgoId(String id) {
        combiningAlgo = prepareForAssignment(combiningAlgo, id);
    }

    /** {@inheritDoc} */
    public void setPolicySetDefaults(DefaultsType defaults) {
        policySetDefaults = prepareForAssignment(policySetDefaults, defaults);
    }

    /** {@inheritDoc} */
    public void setPolicySetId(String id) {
        policySetId = prepareForAssignment(policySetId, id);
    }

    /** {@inheritDoc} */
    public void setTarget(TargetType newTarget) {
        this.target = prepareForAssignment(this.target, newTarget);
    }

    /** {@inheritDoc} */
    public void setVersion(String newVersion) {
        this.version = prepareForAssignment(this.version, newVersion);
    }

    /** {@inheritDoc} */
    public IndexedXMLObjectChildrenList<XACMLObject> getPolicyChoiceGroup() {
        return (IndexedXMLObjectChildrenList<XACMLObject>) choiceGroup;
    }
}