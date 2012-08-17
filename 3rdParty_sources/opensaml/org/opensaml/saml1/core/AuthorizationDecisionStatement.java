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

package org.opensaml.saml1.core;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * This interface defines how the object representing a SAML1 <code> AuthorizationDecisionStatement </code> element
 * behaves.
 */
public interface AuthorizationDecisionStatement extends SAMLObject, SubjectStatement {

    /** Element name, no namespace. */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "AuthorizationDecisionStatement";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML1_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "AuthorizationDecisionStatementType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML1_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);

    /** Name for Resource attribute */
    public final static String RESOURCE_ATTRIB_NAME = "Resource";
    
    /** Name for Decision attribute */
    public final static String DECISION_ATTRIB_NAME = "Decision";
    
    /** Return the contents of the Resource attribute */
    public String getResource();
    
    /** Set the contents of the Resource attribute */
    public void setResource(String resource);
    
    /** Return the contents of the Decision attribute */
    public DecisionTypeEnumeration getDecision();
    
    /** Set the contents of the Decision attribute */
    public void setDecision(DecisionTypeEnumeration decision);
    
    /** Get the Action Elements */
    public List<Action> getActions();
    
    /** Return the Evidence element */
    public Evidence getEvidence();
    
    /** Set the Evidence element 
     * @throws IllegalArgumentException */
    public void setEvidence(Evidence evidence) throws IllegalArgumentException;
   
}