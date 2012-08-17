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


import org.opensaml.xacml.impl.AbstractXACMLObjectUnmarshaller;
import org.opensaml.xacml.policy.ActionsType;
import org.opensaml.xacml.policy.EnvironmentsType;
import org.opensaml.xacml.policy.ResourcesType;
import org.opensaml.xacml.policy.SubjectsType;
import org.opensaml.xacml.policy.TargetType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;

/**
 * A unmarshaller for {@link TargetType}.
 */
public class TargetTypeUnmarshaller extends AbstractXACMLObjectUnmarshaller {

    /** Constructor. */
    public TargetTypeUnmarshaller() {
        super();
    }
    
    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        TargetType targetType = (TargetType) parentXMLObject;
        
        if(childXMLObject.getElementQName().equals(ActionsType.DEFAULT_ELEMENT_NAME)){
            targetType.setActions((ActionsType)childXMLObject);
        } else if(childXMLObject.getElementQName().equals(EnvironmentsType.DEFAULT_ELEMENT_NAME)){
            targetType.setEnvironments((EnvironmentsType)childXMLObject);
        } else  if(childXMLObject.getElementQName().equals(ResourcesType.DEFAULT_ELEMENT_NAME)){
            targetType.setResources((ResourcesType)childXMLObject);
        } else  if(childXMLObject.getElementQName().equals(SubjectsType.DEFAULT_ELEMENT_NAME)){
            targetType.setSubjects((SubjectsType)childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }
    
}
