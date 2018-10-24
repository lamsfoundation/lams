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

package org.opensaml.saml2.core.impl;

import javax.xml.namespace.QName;

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.Attribute} objects.
 */
public class AttributeUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {

        Attribute attribute = (Attribute) parentSAMLObject;

        QName childQName = childSAMLObject.getElementQName();
        if (childQName.getLocalPart().equals("AttributeValue")
                && childQName.getNamespaceURI().equals(SAMLConstants.SAML20_NS)) {
            attribute.getAttributeValues().add(childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {

        Attribute attrib = (Attribute) samlObject;

        if (attribute.getLocalName().equals(Attribute.NAME_ATTTRIB_NAME)) {
            attrib.setName(attribute.getValue());
        } else if (attribute.getLocalName().equals(Attribute.NAME_FORMAT_ATTRIB_NAME)) {
            attrib.setNameFormat(attribute.getValue());
        } else if (attribute.getLocalName().equals(Attribute.FRIENDLY_NAME_ATTRIB_NAME)) {
            attrib.setFriendlyName(attribute.getValue());
        } else {
            QName attribQName = XMLHelper.getNodeQName(attribute);
            if (attribute.isId()) {
                attrib.getUnknownAttributes().registerID(attribQName);
            }
            attrib.getUnknownAttributes().put(attribQName, attribute.getValue());
        }
    }
}