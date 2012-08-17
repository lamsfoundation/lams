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

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.xacml.policy.PolicySetType;
import org.opensaml.xacml.policy.PolicyType;
import org.opensaml.xacml.profile.saml.ReferencedPoliciesType;
import org.opensaml.xacml.profile.saml.XACMLPolicyStatementType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.xacml.profile.saml.XACMLAuthzDecisionStatementType}.
 */
public class XACMLPolicyStatementTypeUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentObject, XMLObject childObject) throws UnmarshallingException {
        XACMLPolicyStatementType xacmlpolicystatement = (XACMLPolicyStatementType) parentObject;

        if (childObject instanceof PolicyType) {
            xacmlpolicystatement.getPolicies().add((PolicyType) childObject);
        } else if (childObject instanceof PolicySetType) {
            xacmlpolicystatement.getPolicySets().add((PolicySetType) childObject);
        } else if (childObject instanceof ReferencedPoliciesType) {
            xacmlpolicystatement.setReferencedPolicies((ReferencedPoliciesType) childObject);
        } else {
            super.processChildElement(parentObject, childObject);
        }
    }

}
