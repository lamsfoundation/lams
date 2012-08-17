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

package org.opensaml.ws.wspolicy.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.ws.wspolicy.All;
import org.opensaml.ws.wspolicy.ExactlyOne;
import org.opensaml.ws.wspolicy.OperatorContentType;
import org.opensaml.ws.wspolicy.Policy;
import org.opensaml.ws.wspolicy.PolicyReference;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * OperatorContentTypeImpl.
 */
public class OperatorContentTypeImpl extends AbstractWSPolicyObject implements OperatorContentType {
    
    /** All child elements. */
    private IndexedXMLObjectChildrenList<XMLObject> xmlObjects;

    /**
     * Constructor.
     *
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     */
    public OperatorContentTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        xmlObjects = new IndexedXMLObjectChildrenList<XMLObject>(this);
    }

    /** {@inheritDoc} */
    public List<All> getAlls() {
        return (List<All>) xmlObjects.subList(All.ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<ExactlyOne> getExactlyOnes() {
        return (List<ExactlyOne>) xmlObjects.subList(ExactlyOne.ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<Policy> getPolicies() {
        return (List<Policy>) xmlObjects.subList(Policy.ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<PolicyReference> getPolicyReferences() {
        return (List<PolicyReference>) xmlObjects.subList(PolicyReference.ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getXMLObjects() {
        return xmlObjects;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getXMLObjects(QName typeOrName) {
        return (List<XMLObject>) xmlObjects.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        children.addAll(xmlObjects);
        return Collections.unmodifiableList(children);
    }

}
