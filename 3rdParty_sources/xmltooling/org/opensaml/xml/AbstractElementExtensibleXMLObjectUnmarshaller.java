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

import org.opensaml.xml.io.AbstractXMLObjectUnmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Attr;

/**
 * AbstractElementExtensibleUnmarshaller unmarshalls element of type <code>xs:any</code>, but without
 * <code>xs:anyAttribute</code> attributes or text content.
 */
public abstract class AbstractElementExtensibleXMLObjectUnmarshaller extends AbstractXMLObjectUnmarshaller {
    
    /** Constructor. */
    public AbstractElementExtensibleXMLObjectUnmarshaller(){
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
    public AbstractElementExtensibleXMLObjectUnmarshaller(String targetNamespaceURI, String targetLocalName) {
        super(targetNamespaceURI, targetLocalName);
    }

    /**
     * Unmarshalls all child elements in the <code>xs:any</code> list.
     * 
     * {@inheritDoc}
     */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        ElementExtensibleXMLObject any = (ElementExtensibleXMLObject) parentXMLObject;
        any.getUnknownXMLObjects().add(childXMLObject);
    }

    /**
     * No <code>xs:anyAttribute</code> attribute.
     * 
     * {@inheritDoc}
     */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
    }

    /**
     * No text content.
     * 
     * {@inheritDoc}
     */
    protected void processElementContent(XMLObject xmlObject, String elementContent) {
    }
}