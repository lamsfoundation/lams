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

import org.opensaml.xacml.ctx.AttributeType;
import org.opensaml.xacml.ctx.SubjectType;
import org.opensaml.xacml.impl.AbstractXACMLObject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/** Concrete implementation of {@link SubjectType}. */
public class SubjectTypeImpl extends AbstractXACMLObject implements SubjectType {

    /** Subject category of the Subject. */
    private String subjectCategory;

    /** Lists of the attributes in the subject. */
    private XMLObjectChildrenList<AttributeType> attributes;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected SubjectTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        attributes = new XMLObjectChildrenList<AttributeType>(this);
        subjectCategory = "urn:oasis:names:tc:xacml:1.0:subject-category:access-subject";
    }

    /** {@inheritDoc} */
    public String getSubjectCategory() {
        return subjectCategory;
    }

    /** {@inheritDoc} */
    public void setSubjectCategory(String newSubjectCategory) {
        subjectCategory = prepareForAssignment(this.subjectCategory, newSubjectCategory);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(attributes);

        return Collections.unmodifiableList(children);
    }

    /** {@inheritDoc} */
    public List<AttributeType> getAttributes() {
        return attributes;
    }
}