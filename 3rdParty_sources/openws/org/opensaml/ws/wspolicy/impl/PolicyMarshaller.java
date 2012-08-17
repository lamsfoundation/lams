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

package org.opensaml.ws.wspolicy.impl;

import org.opensaml.ws.wspolicy.Policy;
import org.opensaml.ws.wssecurity.IdBearing;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;


/**
 * Marshaller for the wsp:Policy element.
 * 
 */
public class PolicyMarshaller extends OperatorContentTypeMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        Policy policy = (Policy) xmlObject;
        
        if (policy.getName() != null) {
            domElement.setAttributeNS(null, Policy.NAME_ATTRIB_NAME, policy.getName());
        }
        
        if (policy.getWSUId() != null) {
            XMLHelper.marshallAttribute(IdBearing.WSU_ID_ATTR_NAME, policy.getWSUId(), domElement, true);
        }
        
        XMLHelper.marshallAttributeMap(policy.getUnknownAttributes(), domElement);
    }

}
