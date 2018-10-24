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

package org.opensaml.xml.encryption.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.DataReference;
import org.opensaml.xml.encryption.KeyReference;
import org.opensaml.xml.encryption.ReferenceList;
import org.opensaml.xml.encryption.ReferenceType;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.encryption.ReferenceList}
 */
public class ReferenceListImpl extends AbstractValidatingXMLObject implements ReferenceList {
    
    /** ReferenceType child elements */
    private final IndexedXMLObjectChildrenList indexedChildren;
    
    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected ReferenceListImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        indexedChildren = new IndexedXMLObjectChildrenList<ReferenceType>(this);
    }

    /** {@inheritDoc} */
    public List<ReferenceType> getReferences() {
        return (List<ReferenceType>) indexedChildren;
    }

    /** {@inheritDoc} */
    public List<DataReference> getDataReferences() {
        return (List<DataReference>) indexedChildren.subList(DataReference.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<KeyReference> getKeyReferences() {
        return (List<KeyReference>) indexedChildren.subList(KeyReference.DEFAULT_ELEMENT_NAME);
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
