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
import org.opensaml.xacml.policy.CombinerParameterType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/** 
 * Implementation of {@link PolicyCombinerParametersTypeUnmarshaller}.
 */
public class PolicyCombinerParametersTypeImpl extends AbstractXACMLObject implements
        org.opensaml.xacml.policy.PolicyCombinerParametersType {

    /**Policy indentity reference.*/
    private String policyIdRef;
    
    /**List or the combiner parameters.*/
    private XMLObjectChildrenList<CombinerParameterType> combinerParameters;
    
    /**
     * Constructor.
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected PolicyCombinerParametersTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix){
        super(namespaceURI,elementLocalName,namespacePrefix);
        combinerParameters = new XMLObjectChildrenList<CombinerParameterType>(this);
    }
    
    /** {@inheritDoc} */
    public String getPolicyIdRef() {
        return policyIdRef;
    }

    /** {@inheritDoc} */
    public void setPolicyIdRef(String ref) {
        this.policyIdRef = prepareForAssignment(this.policyIdRef,ref);
    }

    /** {@inheritDoc} */
    public List<CombinerParameterType> getCombinerParameters() {
        return combinerParameters;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();        
        
        children.addAll(combinerParameters);
                       
        return Collections.unmodifiableList(children);      
    }

}
