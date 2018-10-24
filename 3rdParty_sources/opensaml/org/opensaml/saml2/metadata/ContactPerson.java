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

package org.opensaml.saml2.metadata;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.xml.AttributeExtensibleXMLObject;

/**
 * SAML 2.0 Metadata ContactPerson
 */
public interface ContactPerson extends SAMLObject, AttributeExtensibleXMLObject{

    /** Element name, no namespace */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "ContactPerson";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20MD_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "ContactPersonType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML20MD_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);
    
    /** "contactType" attribute's local name */
    public final static String CONTACT_TYPE_ATTRIB_NAME = "contactType";

    /**
     * Gets the type of contact this person.
     * 
     * @return the type of contact this person
     */
	public ContactPersonTypeEnumeration getType();
    
    /**
     * Sets the type of contact this person.
     * 
     * @param type the type of contact this person
     */
    public void setType(ContactPersonTypeEnumeration type);
    
    /**
     * Gets the Extensions child of this object.
     * 
     * @return the Extensions child of this object
     */
    public Extensions getExtensions();
    
    /**
     * Sets the Extensions child of this object.
     * 
     * @param extensions the Extensions child of this object
     * 
     * @throws IllegalArgumentException thrown if the given extensions Object is already a child of another SAMLObject 
     */
    public void setExtensions(Extensions extensions) throws IllegalArgumentException;

    /**
     * Gets the company this contact person is associated with.
     * 
     * @return the company this contact person is associated with
     */
	public Company getCompany();
    
    /**
     * Sets the company this contact person is associated with.
     * 
     * @param company the company this contact person is associated with
     */
    public void setCompany(Company company);

    /**
     * Gets the given name for this person.
     * 
     * @return the given name for this person
     */
	public GivenName getGivenName();
    
    /**
     * Sets the given name for this person.
     * 
     * @param name the given name for this person
     */
    public void setGivenName(GivenName name);

	/**
     * Gets the surname for this person.
     *  
     * @return the surname for this person
	 */
    public SurName getSurName();
    
    /**
     * Sets the surname for this person.
     * @param name the surname for this person
     */
    public void setSurName(SurName name);

    /**
     * Gets a list of email addresses for this person.
     * 
     * @return list of email addresses for this person
     */
	public List<EmailAddress> getEmailAddresses();
    
    /**
     * Gets an immutable list of telephone numbers for this person.
     * 
     * @return list of telephone numbers for this person
     */
	public List<TelephoneNumber> getTelephoneNumbers();
}