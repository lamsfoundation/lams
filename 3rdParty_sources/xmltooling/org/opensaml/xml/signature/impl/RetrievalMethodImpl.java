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

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.RetrievalMethod;
import org.opensaml.xml.signature.Transforms;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.signature.RetrievalMethod}
 */
public class RetrievalMethodImpl extends AbstractValidatingXMLObject implements RetrievalMethod {
    
    /** URI attribute value */
    private String uri;
    
    /** Type attribute value */
    private String type;
    
    /** Transforms attribute value */
    private Transforms transforms;

    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected RetrievalMethodImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getURI() {
        return this.uri;
    }

    /** {@inheritDoc} */
    public void setURI(String newURI) {
        this.uri = prepareForAssignment(this.uri, newURI);
    }

    /** {@inheritDoc} */
    public String getType() {
        return this.type;
    }

    /** {@inheritDoc} */
    public void setType(String newType) {
        this.type = prepareForAssignment(this.type, newType);
    }

    /** {@inheritDoc} */
    public Transforms getTransforms() {
        return this.transforms;
    }

    /** {@inheritDoc} */
    public void setTransforms(Transforms newTransforms) {
        this.transforms = prepareForAssignment(this.transforms, newTransforms);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        if (transforms != null) {
            children.add(transforms);
        }
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
