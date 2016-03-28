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

/**
 * 
 */
package org.opensaml.saml1.core;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * Interface to describe how the <code> Subject </code> elements work.
 */
public interface Subject extends SAMLObject {
    /** Element name, no namespace. */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "Subject";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML1_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "SubjectType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML1_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);
    
    /** Gets the NameIdentifier of this Subject */
    public NameIdentifier getNameIdentifier();
    
    /** Sets the NameIdentifier of this Subject */
    public void setNameIdentifier(NameIdentifier nameIdentifier) throws IllegalArgumentException;
    
    /** Gets the SubjectConfirmation of this Subject */
    public SubjectConfirmation getSubjectConfirmation();
    
    /** Sets the SubjectConfirmation of this Subject */
    public void setSubjectConfirmation(SubjectConfirmation subjectConfirmation) throws IllegalArgumentException;
    
}
