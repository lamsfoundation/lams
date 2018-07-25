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

package org.opensaml.saml2.metadata.impl;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.metadata.Company;
import org.opensaml.saml2.metadata.ContactPerson;
import org.opensaml.saml2.metadata.ContactPersonTypeEnumeration;
import org.opensaml.saml2.metadata.EmailAddress;
import org.opensaml.saml2.metadata.GivenName;
import org.opensaml.saml2.metadata.SurName;
import org.opensaml.saml2.metadata.TelephoneNumber;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.metadata.ContactPerson}
 */
public class ContactPersonImpl extends AbstractSAMLObject implements ContactPerson {

    /** Contact person type */
    private ContactPersonTypeEnumeration type;

    /** Extensions child object */
    private Extensions extensions;

    /** Company child element */
    private Company company;

    /** GivenName child objectobject */
    private GivenName givenName;

    /** SurName child object */
    private SurName surName;
    
    /** "anyAttribute" attributes */
    private final AttributeMap unknownAttributes;

    /** Child email address */
    private final XMLObjectChildrenList<EmailAddress> emailAddresses;

    /** Child telephone numbers */
    private final XMLObjectChildrenList<TelephoneNumber> telephoneNumbers;

    /**
     * Constructor
     * 
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected ContactPersonImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
        emailAddresses = new XMLObjectChildrenList<EmailAddress>(this);
        telephoneNumbers = new XMLObjectChildrenList<TelephoneNumber>(this);
    }

    /** {@inheritDoc} */
    public ContactPersonTypeEnumeration getType() {
        return type;
    }

    /** {@inheritDoc} */
    public void setType(ContactPersonTypeEnumeration type) {
        this.type = prepareForAssignment(this.type, type);
    }

    /** {@inheritDoc} */
    public Extensions getExtensions() {
        return extensions;
    }

    /** {@inheritDoc} */
    public void setExtensions(Extensions extensions) throws IllegalArgumentException {
        this.extensions = prepareForAssignment(this.extensions, extensions);
    }

    /** {@inheritDoc} */
    public Company getCompany() {
        return company;
    }

    /** {@inheritDoc} */
    public void setCompany(Company company) {
        this.company = prepareForAssignment(this.company, company);
    }

    /** {@inheritDoc} */
    public GivenName getGivenName() {
        return givenName;
    }

    /** {@inheritDoc} */
    public void setGivenName(GivenName name) {
        givenName = prepareForAssignment(givenName, name);
    }

    /** {@inheritDoc} */
    public SurName getSurName() {
        return surName;
    }

    /** {@inheritDoc} */
    public void setSurName(SurName name) {
        surName = prepareForAssignment(surName, name);
    }

    /** {@inheritDoc} */
    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    /** {@inheritDoc} */
    public List<TelephoneNumber> getTelephoneNumbers() {
        return telephoneNumbers;
    }
    
    /**
     * {@inheritDoc}
     */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.add(extensions);
        children.add(company);
        children.add(givenName);
        children.add(surName);
        children.addAll(emailAddresses);
        children.addAll(telephoneNumbers);

        return children;
    }
}