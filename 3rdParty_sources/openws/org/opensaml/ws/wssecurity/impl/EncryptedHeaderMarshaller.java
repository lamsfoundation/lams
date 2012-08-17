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

package org.opensaml.ws.wssecurity.impl;

import org.opensaml.ws.wssecurity.EncryptedHeader;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

/**
 * Marshaller for instances of {@link EncryptedHeaderMarshaller}.
 */
public class EncryptedHeaderMarshaller extends AbstractWSSecurityObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        EncryptedHeader eh = (EncryptedHeader) xmlObject;
        
        if (eh.getWSUId() != null) {
            XMLHelper.marshallAttribute(EncryptedHeader.WSU_ID_ATTR_NAME, 
                    eh.getWSUId(), domElement, true);
        }
        if (eh.isSOAP11MustUnderstandXSBoolean() != null) {
            XMLHelper.marshallAttribute(EncryptedHeader.SOAP11_MUST_UNDERSTAND_ATTR_NAME, 
                    eh.isSOAP11MustUnderstandXSBoolean().toString(), domElement, false);
        }
        if (eh.getSOAP11Actor() != null) {
            XMLHelper.marshallAttribute(EncryptedHeader.SOAP11_ACTOR_ATTR_NAME, 
                    eh.getSOAP11Actor(), domElement, false);
        }
        if (eh.isSOAP12MustUnderstandXSBoolean() != null) {
            XMLHelper.marshallAttribute(EncryptedHeader.SOAP12_MUST_UNDERSTAND_ATTR_NAME, 
                    eh.isSOAP12MustUnderstandXSBoolean().toString(), domElement, false);
        }
        if (eh.getSOAP12Role() != null) {
            XMLHelper.marshallAttribute(EncryptedHeader.SOAP12_ROLE_ATTR_NAME,
                    eh.getSOAP12Role(), domElement, false);
        }
        if (eh.isSOAP12RelayXSBoolean() != null) {
            XMLHelper.marshallAttribute(EncryptedHeader.SOAP12_RELAY_ATTR_NAME, 
                    eh.isSOAP12RelayXSBoolean().toString(), domElement, false);
        }
        
        super.marshallAttributes(xmlObject, domElement);
    }

}
