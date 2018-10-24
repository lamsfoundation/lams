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

package org.opensaml.xml.encryption.impl;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.EncryptionProperty;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.xml.encryption.EncryptionProperty} objects.
 */
public class EncryptionPropertyUnmarshaller extends AbstractXMLEncryptionUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        EncryptionProperty ep = (EncryptionProperty) xmlObject;

        if (attribute.getLocalName().equals(EncryptionProperty.ID_ATTRIB_NAME)) {
            ep.setID(attribute.getValue());
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        } else if (attribute.getLocalName().equals(EncryptionProperty.TARGET_ATTRIB_NAME)) {
            ep.setTarget(attribute.getValue());
        } else {
            QName attributeName = XMLHelper.getNodeQName(attribute);
            if (attribute.isId()) {
                ep.getUnknownAttributes().registerID(attributeName);
            }
            ep.getUnknownAttributes().put(attributeName, attribute.getValue());
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        EncryptionProperty ep = (EncryptionProperty) parentXMLObject;

        // <any> content model
        ep.getUnknownXMLObjects().add(childXMLObject);
    }

}
