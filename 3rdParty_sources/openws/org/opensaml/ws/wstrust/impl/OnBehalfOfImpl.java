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

package org.opensaml.ws.wstrust.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.ws.wstrust.OnBehalfOf;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * OnBehalfOfImpl.
 * 
 */
public class OnBehalfOfImpl extends AbstractWSTrustObject implements OnBehalfOf {
    
    /** Wildcard child element.
     * @deprecated This was an schema implementation mistake, should have implemented a sequence.
     * */
    private XMLObject unknownChild;
    
    /** Wildcard child elements. */
    private IndexedXMLObjectChildrenList<XMLObject> unknownChildren;

    /**
     * Constructor.
     * 
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     */
    public OnBehalfOfImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownChildren = new IndexedXMLObjectChildrenList<XMLObject>(this);
    }

    /** {@inheritDoc} */
    public XMLObject getUnknownXMLObject() {
        // Have to do this b/c don't want to break existing code for both the
        // setUnknownXMLObject case as well as the unmarshalling case, which will be in the list.
        if (unknownChild != null) {
            return unknownChild;
        } else if (!unknownChildren.isEmpty()) {
            return unknownChildren.get(0);
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    public void setUnknownXMLObject(XMLObject unknownObject) {
        unknownChild = prepareForAssignment(unknownChild, unknownObject);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        List<XMLObject> children = new ArrayList<XMLObject>();
        if (unknownChild != null) {
            children.add(unknownChild);
        }
        children.addAll(unknownChildren);
        return Collections.unmodifiableList(children);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects() {
        return unknownChildren;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects(QName typeOrName) {
        return (List<XMLObject>) unknownChildren.subList(typeOrName);
    }
}
