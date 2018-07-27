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

package org.opensaml.saml1.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml1.core.StatusCode;
import org.opensaml.xml.XMLObject;

/**
 * Concrete implementation of {@link org.opensaml.saml1.core.StatusCode} Object.
 */
public class StatusCodeImpl extends AbstractSAMLObject implements StatusCode {

    /** Contents of the Value attribute. */
    private QName value = null;

    /** The child StatusCode sub element. */
    private StatusCode childStatusCode = null;

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
    public QName getValue() {
        return value;
    }

    /** {@inheritDoc} */
    public void setValue(QName newValue) {
        this.value = prepareAttributeValueForAssignment(StatusCode.VALUE_ATTRIB_NAME, this.value, newValue);
    }

    /** {@inheritDoc} */
    public StatusCode getStatusCode() {
        return childStatusCode;
    }

    /** {@inheritDoc} */
    public void setStatusCode(StatusCode statusCode) {
        childStatusCode = prepareForAssignment(childStatusCode, statusCode);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        if (childStatusCode != null) {
            ArrayList<XMLObject> contents = new ArrayList<XMLObject>(1);
            contents.add(childStatusCode);
            return Collections.unmodifiableList(contents);
        } else {
            return null;
        }
    }
}