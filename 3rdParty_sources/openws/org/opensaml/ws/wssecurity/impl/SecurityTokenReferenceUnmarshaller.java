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

import org.opensaml.ws.wssecurity.SecurityTokenReference;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * SecurityTokenReferenceUnmarshaller.
 */
public class SecurityTokenReferenceUnmarshaller extends AbstractWSSecurityObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        SecurityTokenReference str = (SecurityTokenReference) parentXMLObject;

        str.getUnknownXMLObjects().add(childXMLObject);
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        SecurityTokenReference str = (SecurityTokenReference) xmlObject;
        
        QName attribQName = 
            XMLHelper.constructQName(attribute.getNamespaceURI(), attribute.getLocalName(), attribute.getPrefix());
        if (SecurityTokenReference.WSU_ID_ATTR_NAME.equals(attribQName)) {
            str.setWSUId(attribute.getValue());
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        } else if (SecurityTokenReference.WSSE_USAGE_ATTR_NAME.equals(attribQName)) {
            str.setWSSEUsages(XMLHelper.getAttributeValueAsList(attribute));
        } else {
            XMLHelper.unmarshallToAttributeMap(str.getUnknownAttributes(), attribute);
        }
    }

}
