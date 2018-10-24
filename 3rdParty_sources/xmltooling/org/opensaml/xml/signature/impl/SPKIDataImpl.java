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

package org.opensaml.xml.signature.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.SPKIData;
import org.opensaml.xml.signature.SPKISexp;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.signature.SPKIData}
 */
public class SPKIDataImpl extends AbstractValidatingXMLObject implements SPKIData {
    
    /** The list of XMLObject child elements */
    private final IndexedXMLObjectChildrenList indexedChildren;

    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected SPKIDataImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        indexedChildren = new IndexedXMLObjectChildrenList(this);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getXMLObjects() {
        return (List<XMLObject>) this.indexedChildren;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getXMLObjects(QName typeOrName) {
        return (List<XMLObject>) this.indexedChildren.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<SPKISexp> getSPKISexps() {
        return (List<SPKISexp>) this.indexedChildren.subList(SPKISexp.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        children.addAll((List<XMLObject>) indexedChildren);
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
