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

import javax.xml.namespace.QName;

import org.opensaml.ws.wssecurity.EncryptedHeader;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.EncryptedData;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * Unmarshaller for instances of {@link EncryptedHeader}.
 */
public class EncryptedHeaderUnmarshaller extends AbstractWSSecurityObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        EncryptedHeader eh = (EncryptedHeader) xmlObject;
        QName attrName = XMLHelper.getNodeQName(attribute);
        if (EncryptedHeader.WSU_ID_ATTR_NAME.equals(attrName)) {
            eh.setWSUId(attribute.getValue());
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        } else if (EncryptedHeader.SOAP11_MUST_UNDERSTAND_ATTR_NAME.equals(attrName)) {
            eh.setSOAP11MustUnderstand(XSBooleanValue.valueOf(attribute.getValue()));
        } else if (EncryptedHeader.SOAP11_ACTOR_ATTR_NAME.equals(attrName)) {
            eh.setSOAP11Actor(attribute.getValue());
        } else if (EncryptedHeader.SOAP12_MUST_UNDERSTAND_ATTR_NAME.equals(attrName)) {
            eh.setSOAP12MustUnderstand(XSBooleanValue.valueOf(attribute.getValue()));
        } else if (EncryptedHeader.SOAP12_ROLE_ATTR_NAME.equals(attrName)) {
            eh.setSOAP12Role(attribute.getValue());
        } else if (EncryptedHeader.SOAP12_RELAY_ATTR_NAME.equals(attrName)) {
            eh.setSOAP12Relay(XSBooleanValue.valueOf(attribute.getValue()));
        } else {
            super.processAttribute(xmlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        EncryptedHeader eh = (EncryptedHeader) parentXMLObject;
        if (childXMLObject instanceof EncryptedData) {
            eh.setEncryptedData((EncryptedData) childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }
    
}
