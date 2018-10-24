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

package org.opensaml.xacml.ctx.impl;

import org.opensaml.xacml.ctx.AttributeValueType;
import org.opensaml.xacml.ctx.MissingAttributeDetailType;
import org.opensaml.xacml.impl.AbstractXACMLObjectUnmarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Attr;

/** Unmarshaller for {@link MissingAttributeDetailType} objects. */
public class MissingAttributeDetailTypeUnmarshaller extends AbstractXACMLObjectUnmarshaller {

    /** Constructor. */
    public MissingAttributeDetailTypeUnmarshaller() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param targetNamespaceURI the namespace URI of either the schema type QName or element QName of the elements this
     *            unmarshaller operates on
     * @param targetLocalName the local name of either the schema type QName or element QName of the elements this
     *            unmarshaller operates on
     */
    protected MissingAttributeDetailTypeUnmarshaller(String targetNamespaceURI, String targetLocalName) {
        super(targetNamespaceURI, targetLocalName);
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        MissingAttributeDetailType madt = (MissingAttributeDetailType) xmlObject;

        if (attribute.getLocalName().equals(MissingAttributeDetailType.ATTRIBUTE_ID_ATTRIB_NAME)) {
            madt.setAttributeId(attribute.getValue());
        } else if (attribute.getLocalName().equals(MissingAttributeDetailType.DATA_TYPE_ATTRIB_NAME)) {
            madt.setDataType(attribute.getValue());
        } else if (attribute.getLocalName().equals(MissingAttributeDetailType.ISSUER_ATTRIB_NAME)) {
            madt.setIssuer(attribute.getValue());
        } else {
            super.processAttribute(xmlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        MissingAttributeDetailType madt = (MissingAttributeDetailType) parentXMLObject;
        if (childXMLObject instanceof AttributeValueType) {
            madt.getAttributeValues().add((AttributeValueType) childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }

}