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

package org.opensaml.ws.wspolicy;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.ElementExtensibleXMLObject;

/**
 * The wsp:PolicyAttachment element.
 * 
 * @see "WS-Policy (http://schemas.xmlsoap.org/ws/2004/09/policy)"
 */
public interface PolicyAttachment extends WSPolicyObject, ElementExtensibleXMLObject, AttributeExtensibleXMLObject {
    
    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "PolicyAttachment";

    /** Default element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSPolicyConstants.WSP_NS, ELEMENT_LOCAL_NAME, WSPolicyConstants.WSP_PREFIX);
    
    /**
     * Get the AppliesTo child element.
     * 
     * @return the child element
     */
    public AppliesTo getAppliesTo();
    
    /**
     * Set the AppliesTo child element.
     * 
     * @param newAppliesTo the new child element
     */
    public void setAppliesTo(AppliesTo newAppliesTo);
    
    /**
     * Get the list of Policy child elements.
     * 
     * @return the list of child elements
     */
    public List<Policy> getPolicies();
    
    /**
     * Get the list of PolicyReference child elements.
     * 
     * @return the list of child elements
     */
    public List<PolicyReference> getPolicyReferences();

}
