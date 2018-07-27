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

import org.opensaml.xacml.impl.AbstractXACMLObjectMarshaller;
import org.opensaml.xacml.policy.EffectType;
import org.opensaml.xacml.policy.ObligationType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Element;

/** Marshaller for {@link ObligationType}. */
public class ObligationTypeMarshaller extends AbstractXACMLObjectMarshaller {

    /** Constructor. */
    public ObligationTypeMarshaller() {
        super();
    }

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject samlElement, Element domElement) throws MarshallingException {
        ObligationType obligation = (ObligationType) samlElement;

        if (!DatatypeHelper.isEmpty(obligation.getObligationId())) {
            domElement.setAttributeNS(null, ObligationType.OBLIGATION_ID_ATTRIB_NAME, obligation.getObligationId());
        }
        if (obligation.getFulfillOn() != null) {
            if (obligation.getFulfillOn().equals(EffectType.Deny)) {
                domElement.setAttributeNS(null, ObligationType.FULFILL_ON_ATTRIB_NAME, EffectType.Deny.toString());
            } else {
                domElement.setAttributeNS(null, ObligationType.FULFILL_ON_ATTRIB_NAME, EffectType.Permit.toString());
            }                     
        }
    }
}