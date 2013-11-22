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

package org.opensaml.xml.signature;

import org.opensaml.xml.AbstractXMLObject;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

/**
 * Base for signable XMLObjects.
 */
public abstract class AbstractSignableXMLObject extends AbstractXMLObject implements SignableXMLObject {

    /** Signature object. */
    private Signature signature;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AbstractSignableXMLObject(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public boolean isSigned() {
        Element domElement = getDOM();

        if (domElement == null) {
            return false;
        }
        

        Element childElement = XMLHelper.getFirstChildElement(domElement);
        while (childElement != null) {
            if (XMLHelper.isElementNamed(childElement, XMLConstants.XMLSIG_NS, Signature.DEFAULT_ELEMENT_LOCAL_NAME)) {
                return true;
            }
            
            childElement = XMLHelper.getNextSiblingElement(childElement);
        }

        return false;
    }

    /** {@inheritDoc} */
    public Signature getSignature() {
        return signature;
    }

    /** {@inheritDoc} */
    public void setSignature(Signature newSignature) {
        signature = prepareForAssignment(signature, newSignature);
    }
}