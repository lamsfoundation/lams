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

package org.opensaml.xacml.profile.saml.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.saml2.core.impl.RequestAbstractTypeImpl;
import org.opensaml.xacml.XACMLObject;
import org.opensaml.xacml.ctx.RequestType;
import org.opensaml.xacml.policy.IdReferenceType;
import org.opensaml.xacml.profile.saml.XACMLPolicyQueryType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/** Concrete implementation of {@link XACMLPolicyQueryType}. */
public class XACMLPolicyQueryTypeImpl extends RequestAbstractTypeImpl implements XACMLPolicyQueryType {

    /** Choice group for the element. */
    private IndexedXMLObjectChildrenList<XACMLObject> choiceGroup;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    public XACMLPolicyQueryTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        setElementNamespacePrefix(namespacePrefix);
        choiceGroup = new IndexedXMLObjectChildrenList<XACMLObject>(this);
    }

    /** {@inheritDoc} */
    public List<RequestType> getRequests() {
        return (List<RequestType>) choiceGroup.subList(RequestType.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<IdReferenceType> getPolicySetIdReferences() {
        return (List<IdReferenceType>) choiceGroup.subList(IdReferenceType.POLICY_SET_ID_REFERENCE_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<IdReferenceType> getPolicyIdReferences() {
        return (List<IdReferenceType>) choiceGroup.subList(IdReferenceType.POLICY_ID_REFERENCE_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
                    
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        if(super.getOrderedChildren() != null){
            children.addAll(super.getOrderedChildren());
        }
        
        children.addAll(choiceGroup);

        return Collections.unmodifiableList(children);
    }
}