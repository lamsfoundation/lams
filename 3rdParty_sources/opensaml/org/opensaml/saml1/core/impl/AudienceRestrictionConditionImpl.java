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

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml1.core.Audience;
import org.opensaml.saml1.core.AudienceRestrictionCondition;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of the org.opensaml.saml1.core.AudienceRestrictionCondition
 */
public class AudienceRestrictionConditionImpl extends AbstractSAMLObject implements AudienceRestrictionCondition {

    /** Audiences */
    private final XMLObjectChildrenList<Audience> audiences;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AudienceRestrictionConditionImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        audiences = new XMLObjectChildrenList<Audience>(this);
    }

    /** {@inheritDoc} */
    public List<Audience> getAudiences() {
        return audiences;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {

        if (audiences.size() == 0) {
            return null;
        }
        ArrayList<XMLObject> children = new ArrayList<XMLObject>(audiences);
        return Collections.unmodifiableList(children);
    }
}