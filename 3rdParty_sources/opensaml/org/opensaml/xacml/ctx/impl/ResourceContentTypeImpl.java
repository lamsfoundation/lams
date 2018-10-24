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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xacml.ctx.ResourceContentType;
import org.opensaml.xacml.impl.AbstractXACMLObject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/** Concrete implementation of {@link ResourceContentType}. */
public class ResourceContentTypeImpl extends AbstractXACMLObject implements ResourceContentType {

    /** "any" elements. */
    private IndexedXMLObjectChildrenList<XMLObject> unknownElements;

    /** "any" attributes. */
    private AttributeMap unknownAttributes;
    
    /** String value of the mixed content element. */
    private String value;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected ResourceContentTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
        unknownElements = new IndexedXMLObjectChildrenList<XMLObject>(this);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        children.addAll(unknownElements);
        
        return Collections.unmodifiableList(children);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects() {
        return unknownElements;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects(QName typeOrName) {
        return (List<XMLObject>) unknownElements.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }
    
    /** {@inheritDoc} */
    public String getValue() {
        return value;
    }
    
    /** {@inheritDoc} */
    public void setValue(String newValue) {
        value = prepareForAssignment(value, newValue);
    }
}