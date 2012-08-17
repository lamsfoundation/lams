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

package org.opensaml.xml;

import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * AbstractExtensibleXMLObjectMarshaller marshalls element of type <code>xs:any</code> and with
 * <code>xs:anyAttribute</code> attributes.
 */
public class AbstractExtensibleXMLObjectMarshaller extends AbstractElementExtensibleXMLObjectMarshaller {
    
    /** Constructor. */
    public AbstractExtensibleXMLObjectMarshaller(){
        super();
    }

    /**
     * Constructor.
     * 
     * @deprecated no replacement
     * 
     * @param targetNamespaceURI the namespace URI of either the schema type QName or element QName of the elements this
     *            unmarshaller operates on
     * @param targetLocalName the local name of either the schema type QName or element QName of the elements this
     *            unmarshaller operates on
     */
    public AbstractExtensibleXMLObjectMarshaller(String targetNamespaceURI, String targetLocalName) {
        super(targetNamespaceURI, targetLocalName);
    }

    /**
     * Marshalls the <code>xs:anyAttribute</code> attributes.
     * 
     * {@inheritDoc}
     */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        AttributeExtensibleXMLObject anyAttribute = (AttributeExtensibleXMLObject) xmlObject;
        Attr attribute;
        Document document = domElement.getOwnerDocument();
        for (Entry<QName, String> entry : anyAttribute.getUnknownAttributes().entrySet()) {
            attribute = XMLHelper.constructAttribute(document, entry.getKey());
            attribute.setValue(entry.getValue());
            domElement.setAttributeNodeNS(attribute);
            if (Configuration.isIDAttribute(entry.getKey())
                    || anyAttribute.getUnknownAttributes().isIDAttribute(entry.getKey())) {
                attribute.getOwnerElement().setIdAttributeNode(attribute, true);
            }
        }
    }

}
