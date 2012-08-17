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

package org.opensaml.xacml.policy.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.xacml.impl.AbstractXACMLObject;
import org.opensaml.xacml.policy.ExpressionType;
import org.opensaml.xacml.policy.VariableReferenceType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Implementation of {@link VariableReferenceType}.
 */
public class VariableReferenceTypeImpl extends AbstractXACMLObject implements VariableReferenceType {

    /**List or expressions.*/
    private XMLObjectChildrenList<ExpressionType> expressions;
    
    /**Variable id.*/
    private String valiableId;
    
    /**
     * Constructor.
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected VariableReferenceTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix){
        super(namespaceURI,elementLocalName,namespacePrefix);
        expressions = new XMLObjectChildrenList<ExpressionType>(this);
    }
    
    /** {@inheritDoc} */
    public List<ExpressionType> getExpressions() {
        return expressions;
    }   

    /** {@inheritDoc} */
    public String getVariableId() {
        return valiableId;
    }

    /** {@inheritDoc} */
    public void setVariableId(String id) {
       this.valiableId = prepareForAssignment(this.valiableId,id);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if(!expressions.isEmpty()){
            children.addAll(expressions);
        }            
        return Collections.unmodifiableList(children);
    }

}
