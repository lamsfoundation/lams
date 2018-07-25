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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.xml.XMLObject;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.StatusCode}.
 */
public class StatusCodeImpl extends AbstractSAMLObject implements StatusCode {

    /** Value attribute URI. */
    private String value;

    /** Nested secondary StatusCode child element. */
    private StatusCode childStatusCode;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected StatusCodeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public StatusCode getStatusCode() {
        return childStatusCode;
    }

    /** {@inheritDoc} */
    public void setStatusCode(StatusCode newStatusCode) {
        this.childStatusCode = prepareForAssignment(this.childStatusCode, newStatusCode);
    }

    /** {@inheritDoc} */
    public String getValue() {
        return value;
    }

    /** {@inheritDoc} */
    public void setValue(String newValue) {
        this.value = prepareForAssignment(this.value, newValue);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        if (childStatusCode != null) {
            ArrayList<XMLObject> children = new ArrayList<XMLObject>();
            children.add(childStatusCode);
            return Collections.unmodifiableList(children);
        } else {
            return null;
        }
    }
}