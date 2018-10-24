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

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.impl.AbstractXACMLObjectUnmarshaller;
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
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Attr;

/** Unmarshaller for {@link PolicySetType} objects. */
public class PolicySetTypeUnmarshaller extends AbstractXACMLObjectUnmarshaller {

    /** Constructor. */
    public PolicySetTypeUnmarshaller() {
        super(XACMLConstants.XACML20_NS, PolicySetType.DEFAULT_ELEMENT_LOCAL_NAME);
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        PolicySetType policySet = (PolicySetType) xmlObject;

        if (attribute.getLocalName().equals(PolicySetType.POLICY_SET_ID_ATTRIB_NAME)) {
            policySet.setPolicySetId(attribute.getValue());
        } else if (attribute.getLocalName().equals(PolicySetType.VERSION_ATTRIB_NAME)) {
            policySet.setVersion(attribute.getValue());
        } else if (attribute.getLocalName().equals(PolicySetType.POLICY_COMBINING_ALG_ID_ATTRIB_NAME)) {
            policySet.setPolicyCombiningAlgoId(attribute.getValue());
        } else {
            super.processAttribute(xmlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        PolicySetType policySet = (PolicySetType) parentXMLObject;

        if (childXMLObject instanceof DescriptionType) {
            policySet.setDescription((DescriptionType) childXMLObject);
        } else if (childXMLObject instanceof DefaultsType) {
            policySet.setPolicySetDefaults((DefaultsType) childXMLObject);
        } else if (childXMLObject instanceof TargetType){
                policySet.setTarget((TargetType) childXMLObject);
        } else if (childXMLObject instanceof PolicySetType) {
            policySet.getPolicySets().add((PolicySetType) childXMLObject);
        } else if (childXMLObject instanceof PolicyType) {
            policySet.getPolicies().add((PolicyType) childXMLObject);
        } else if (childXMLObject.getElementQName().equals(IdReferenceType.POLICY_SET_ID_REFERENCE_ELEMENT_NAME)) {
            policySet.getPolicySetIdReferences().add((IdReferenceType) childXMLObject);
        } else if (childXMLObject.getElementQName().equals(IdReferenceType.POLICY_ID_REFERENCE_ELEMENT_NAME)) {
            policySet.getPolicyIdReferences().add((IdReferenceType) childXMLObject);
        } else if (childXMLObject.getElementQName().equals(CombinerParametersType.DEFAULT_ELEMENT_NAME)) {
            policySet.getCombinerParameters().add((CombinerParametersType) childXMLObject);
        } else if (childXMLObject.getElementQName().equals(PolicyCombinerParametersType.DEFAULT_ELEMENT_NAME)) {
            policySet.getPolicyCombinerParameters().add((PolicyCombinerParametersType) childXMLObject);
        } else if (childXMLObject.getElementQName().equals(PolicySetCombinerParametersType.DEFAULT_ELEMENT_NAME)) {
            policySet.getPolicySetCombinerParameters().add((PolicySetCombinerParametersType) childXMLObject);
        } else if (childXMLObject instanceof ObligationsType) {
            policySet.setObligations((ObligationsType) childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }

}