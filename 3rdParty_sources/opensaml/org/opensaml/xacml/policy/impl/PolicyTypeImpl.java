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
import org.opensaml.xacml.policy.ObligationsType;
import org.opensaml.xacml.policy.PolicyType;
import org.opensaml.xacml.policy.RuleCombinerParametersType;
import org.opensaml.xacml.policy.RuleType;
import org.opensaml.xacml.policy.TargetType;
import org.opensaml.xacml.policy.VariableDefinitionType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/** Concrete implemenation of {@link PolicyType}. */
public class PolicyTypeImpl extends AbstractXACMLObject implements PolicyType {

    /** Policy description. */
    private DescriptionType description;

    /** Policy defaults. */
    private DefaultsType policyDefaults;

    /** Policy target. */
    private TargetType target;

    /** Elements within the choice group. */
    private IndexedXMLObjectChildrenList<? extends XACMLObject> choiceGroup;

    /** Policy obligations. */
    private ObligationsType obligations;

    /** ID of this policy. */
    private String policyId;

    /** Version of this policy. */
    private String version;

    /** Rule combinging algorithm ID. */
    private String ruleCombiningAlgo;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected PolicyTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
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
    public DefaultsType getPolicyDefaults() {
        return policyDefaults;
    }

    /** {@inheritDoc} */
    public String getPolicyId() {
        return policyId;
    }

    /** {@inheritDoc} */
    public List<RuleCombinerParametersType> getRuleCombinerParameters() {
        return (List<RuleCombinerParametersType>) choiceGroup.subList(RuleCombinerParametersType.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public String getRuleCombiningAlgoId() {
        return ruleCombiningAlgo;
    }

    /** {@inheritDoc} */
    public List<RuleType> getRules() {
       	return (List<RuleType>) choiceGroup.subList(RuleType.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public TargetType getTarget() {
        return target;
    }

    /** {@inheritDoc} */
    public List<VariableDefinitionType> getVariableDefinitions() {
        return (List<VariableDefinitionType>) choiceGroup.subList(VariableDefinitionType.DEFAULT_ELEMENT_NAME);
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
    public void setPolicyDefaults(DefaultsType defaults) {
        policyDefaults = prepareForAssignment(policyDefaults, defaults);
    }

    /** {@inheritDoc} */
    public void setPolicyId(String id) {
        policyId = prepareForAssignment(policyId, id);
    }

    /** {@inheritDoc} */
    public void setRuleCombiningAlgoId(String id) {
        ruleCombiningAlgo = prepareForAssignment(ruleCombiningAlgo, id);
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
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        if (description != null) {
            children.add(description);
        }

        if (policyDefaults != null) {
            children.add(policyDefaults);
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
}