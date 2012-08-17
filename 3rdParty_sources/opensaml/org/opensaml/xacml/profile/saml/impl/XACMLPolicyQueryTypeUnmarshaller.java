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
import org.opensaml.xacml.policy.IdReferenceType;
import org.opensaml.xacml.profile.saml.XACMLPolicyQueryType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;

/** Unmarshaller for {@link XACMLPolicyQueryType}. */
public class XACMLPolicyQueryTypeUnmarshaller extends RequestAbstractTypeUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentObject, XMLObject childObject) throws UnmarshallingException {
        XACMLPolicyQueryType xacmlpolicyquery = (XACMLPolicyQueryType) parentObject;

        if (childObject instanceof RequestType) {
            xacmlpolicyquery.getRequests().add((RequestType) childObject);
        } else if (childObject.getElementQName().equals(IdReferenceType.POLICY_ID_REFERENCE_ELEMENT_NAME)) {
            xacmlpolicyquery.getPolicyIdReferences().add((IdReferenceType) childObject);
        } else if (childObject.getElementQName().equals(IdReferenceType.POLICY_SET_ID_REFERENCE_ELEMENT_NAME)) {
            xacmlpolicyquery.getPolicySetIdReferences().add((IdReferenceType) childObject);
        } else {
            super.processChildElement(parentObject, childObject);
        }
    }

}
