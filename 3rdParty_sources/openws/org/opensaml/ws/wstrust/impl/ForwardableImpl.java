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

import java.util.List;

import org.opensaml.ws.wstrust.Forwardable;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * ForwardableImpl.
 * 
 */
public class ForwardableImpl extends AbstractWSTrustObject implements Forwardable {

    /** Default value. */
    private static final Boolean DEFAULT_VALUE = Boolean.TRUE;

    /** The wst:Forwardable content. */
    private XSBooleanValue value;
    
    /**
     * Constructor. Default value is <code>TRUE</code>.
     * 
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     */
    public ForwardableImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        value = new XSBooleanValue(DEFAULT_VALUE, false);
    }

    /** {@inheritDoc} */
    public XSBooleanValue getValue() {
        return value;
    }

    /** {@inheritDoc} */
    public void setValue(XSBooleanValue newValue) {
        if (newValue != null) {
            value = prepareForAssignment(value, newValue);
        } else {
            value = prepareForAssignment(value, new XSBooleanValue(DEFAULT_VALUE, false));
        }
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }

}
