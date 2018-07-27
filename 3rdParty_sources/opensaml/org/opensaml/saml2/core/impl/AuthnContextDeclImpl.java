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

/**
 * 
 */

package org.opensaml.saml2.core.impl;

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.AuthnContextDecl;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * A concrete implementation of {@link org.opensaml.saml2.core.AuthnContextDecl}.
 */
public class AuthnContextDeclImpl extends AbstractSAMLObject implements AuthnContextDecl {

    /** Child XMLObjects. */
    private IndexedXMLObjectChildrenList<XMLObject> unknownXMLObjects;

    /** Attributes for this element. */
    private AttributeMap unknownAttributes;

    /** Text content of the element. */
    private String textContent;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AuthnContextDeclImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);

        unknownXMLObjects = new IndexedXMLObjectChildrenList<XMLObject>(this);
        unknownAttributes = new AttributeMap(this);
    }

    /** {@inheritDoc} */
    public String getTextContent() {
        return textContent;
    }

    /** {@inheritDoc} */
    public void setTextContent(String newContent) {
        textContent = prepareForAssignment(textContent, newContent);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects() {
        return unknownXMLObjects;
    }
    
    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects(QName typeOrName) {
        return (List<XMLObject>) unknownXMLObjects.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return Collections.unmodifiableList(unknownXMLObjects);
    }

    /** {@inheritDoc} */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }
}