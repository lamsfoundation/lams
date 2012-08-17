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

import org.opensaml.ws.wssecurity.Created;
import org.opensaml.ws.wssecurity.Expires;
import org.opensaml.ws.wstrust.Lifetime;
import org.opensaml.xml.XMLObject;

/**
 * LifetimeImpl.
 * 
 */
public class LifetimeImpl extends AbstractWSTrustObject implements Lifetime {

    /** The wsu:Created child element. */
    private Created created;

    /** The wsu:Expires child element. */
    private Expires expires;

    /**
     * Constructor.
     * 
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     */
    public LifetimeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public Created getCreated() {
        return created;
    }

    /** {@inheritDoc} */
    public Expires getExpires() {
        return expires;
    }

    /** {@inheritDoc} */
    public void setCreated(Created newCreated) {
        created = prepareForAssignment(created, newCreated);
    }

    /** {@inheritDoc} */
    public void setExpires(Expires newExpires) {
        expires = prepareForAssignment(expires, newExpires);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        if (created != null) {
            children.add(created);
        }
        if (expires != null) {
            children.add(expires);
        }
        return Collections.unmodifiableList(children);
    }

}
