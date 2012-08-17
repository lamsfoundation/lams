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

package org.opensaml.xml.io;

import org.opensaml.xml.XMLObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Marshallers are used to marshall a {@link org.opensaml.xml.XMLObject} into a W3C DOM element.
 */
public interface Marshaller {

    /**
     * Marshall this element, and its children, and root them in a newly created Document. The Document is created by a
     * {@link javax.xml.parsers.DocumentBuilder} obtained from a {@link javax.xml.parsers.DocumentBuilderFactory}
     * created without any additional parameters or properties set; that is the system defaults properties are used.
     * 
     * @param xmlObject the object to marshall
     * 
     * @return the W3C DOM element representing this SAML element
     * 
     * @throws MarshallingException thrown if there is a problem marshalling the given object
     */
    public Element marshall(XMLObject xmlObject) throws MarshallingException;

    /**
     * Marshall this element, and its children, into a W3C DOM element. If the document does not have a Document Element
     * the Element resulting from this marshalling will be set as the Document Element.
     * 
     * @param xmlObject the object to marshall
     * @param document the DOM document the marshalled element will be placed in
     * 
     * @return the W3C DOM element representing this XMLObject
     * 
     * @throws MarshallingException thrown if there is a problem marshalling the given object
     */
    public Element marshall(XMLObject xmlObject, Document document) throws MarshallingException;

    /**
     * Marshall the given XMLObject and append it as a child to the given parent element.
     * 
     * <strong>NOTE:</strong> The given Element must be within a DOM tree whose root is the root of the Document owning
     * the given Element.
     * 
     * @param xmlObject the XMLObject to be marshalled
     * @param parentElement the parent of the Element resulting from marshalling the given XMLObject
     * 
     * @return the marshalled XMLObject
     * 
     * @throws MarshallingException thrown if the given XMLObject can not be marshalled.
     */
    public Element marshall(XMLObject xmlObject, Element parentElement) throws MarshallingException;
}