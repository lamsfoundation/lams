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

package org.opensaml.xml.schema.impl;

import java.util.List;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSInteger;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.schema.XSInteger}.
 */
public class XSIntegerImpl extends AbstractValidatingXMLObject implements XSInteger {

    /** Value of this integer element. */
    private Integer value;
    
    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected XSIntegerImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }
    
    /**
     * {@inheritDoc}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public void setValue(Integer newValue) {
        value = prepareForAssignment(value, newValue);
    }

    /**
     * {@inheritDoc}
     */
    public List<XMLObject> getOrderedChildren() {
        // no children
        return null;
    }
}