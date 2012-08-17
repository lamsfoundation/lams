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

package org.opensaml.saml2.ecp.impl;

import org.opensaml.saml2.ecp.RelayState;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.schema.impl.XSStringMarshaller;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

/**
 * Marshaller for instances of {@link RelayState}.
 */
public class RelayStateMarshaller extends XSStringMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        RelayState relayState = (RelayState) xmlObject;
        
        if (relayState.isSOAP11MustUnderstandXSBoolean() != null) {
            XMLHelper.marshallAttribute(RelayState.SOAP11_MUST_UNDERSTAND_ATTR_NAME, 
                    relayState.isSOAP11MustUnderstandXSBoolean().toString(), domElement, false);
        }
        if (relayState.getSOAP11Actor() != null) {
            XMLHelper.marshallAttribute(RelayState.SOAP11_ACTOR_ATTR_NAME, 
                    relayState.getSOAP11Actor(), domElement, false);
        }
        
    }

}
