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

import org.opensaml.saml2.core.impl.RequestAbstractTypeUnmarshaller;
import org.opensaml.xacml.ctx.RequestType;
import org.opensaml.xacml.policy.PolicySetType;
import org.opensaml.xacml.policy.PolicyType;
import org.opensaml.xacml.profile.saml.ReferencedPoliciesType;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionQueryType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.schema.XSBooleanValue;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.xacml.profile.saml.XACMLAuthzDecisionQueryType} objects.
 */
public class XACMLAuthzDecisionQueryTypeUnmarshaller extends RequestAbstractTypeUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentObject, XMLObject childObject) throws UnmarshallingException {
        XACMLAuthzDecisionQueryType xacmlauthzdecisionquery = (XACMLAuthzDecisionQueryType) parentObject;

        if (childObject instanceof RequestType) {
            xacmlauthzdecisionquery.setRequest((RequestType) childObject);
        } else if (childObject instanceof PolicyType) {
            xacmlauthzdecisionquery.getPolicies().add((PolicyType) childObject);
        } else if (childObject instanceof PolicySetType) {
            xacmlauthzdecisionquery.getPolicySets().add((PolicySetType) childObject);
        } else if (childObject instanceof ReferencedPoliciesType) {
            xacmlauthzdecisionquery.setReferencedPolicies((ReferencedPoliciesType) childObject);
        } else {
            super.processChildElement(parentObject, childObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        XACMLAuthzDecisionQueryType authzDS = (XACMLAuthzDecisionQueryType) samlObject;

        if (attribute.getLocalName().equals(XACMLAuthzDecisionQueryType.INPUTCONTEXTONLY_ATTRIB_NAME)) {
            authzDS.setInputContextOnly(XSBooleanValue.valueOf(attribute.getValue()));
        }

        if (attribute.getLocalName().equals(XACMLAuthzDecisionQueryType.RETURNCONTEXT_ATTRIB_NAME)) {
            authzDS.setReturnContext(XSBooleanValue.valueOf(attribute.getValue()));
        }

        if (attribute.getLocalName().equals(XACMLAuthzDecisionQueryType.COMBINEPOLICIES_ATTRIB_NAME)) {
            authzDS.setCombinePolicies(XSBooleanValue.valueOf(attribute.getValue()));
        }

        super.processAttribute(samlObject, attribute);
    }

}
