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

import org.joda.time.DateTime;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * This interface defines how the object representing a SAML1 <code> AuthenticationStatment </code> element behaves.
 */
public interface AuthenticationStatement extends SAMLObject, SubjectStatement {

    /** Element name, no namespace. */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "AuthenticationStatement";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML1_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "AuthenticationStatementType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML1_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);

    /** Name of the AuthenticationMethod attribute */
    public final static String AUTHENTICATIONMETHOD_ATTRIB_NAME = "AuthenticationMethod";

    /** Name of the AuthenticationInstant attribute */
    public final static String AUTHENTICATIONINSTANT_ATTRIB_NAME = "AuthenticationInstant";

    /** Return the contents of the AuthenticationMethod attribute */
    public String getAuthenticationMethod();

    /** Set the contents of the AuthenticationMethod attribute */
    public void setAuthenticationMethod(String authenticationMethod);

    /** Return the contents of the AuthenticationInstant attribute */
    public DateTime getAuthenticationInstant();

    /** Set the contents of the AuthenticationInstant attribute */
    public void setAuthenticationInstant(DateTime authenticationInstant);

    /** Set the (single) SubjectLocality child element */
    public SubjectLocality getSubjectLocality();

    /** Get the (single) SubjectLocality child element 
     * @throws IllegalArgumentException */
    public void setSubjectLocality(SubjectLocality subjectLocality) throws IllegalArgumentException;

    /** return all the AuthorityBinding subelement */
    public List<AuthorityBinding> getAuthorityBindings();

}
