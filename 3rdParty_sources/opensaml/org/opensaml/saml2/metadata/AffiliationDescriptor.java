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

import org.opensaml.common.SignableSAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.common.CacheableSAMLObject;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.common.TimeBoundSAMLObject;
import org.opensaml.xml.AttributeExtensibleXMLObject;

/**
 * SAML 2.0 Metadata AffiliationDescriptorType
 */
public interface AffiliationDescriptor extends SignableSAMLObject, TimeBoundSAMLObject, CacheableSAMLObject,
        AttributeExtensibleXMLObject {

    /** Element name, no namespace */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "AffiliationDescriptor";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20MD_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "AffiliationDescriptorType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML20MD_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);

    /** "affiliationOwnerID" attribute's local name */
    public final static String OWNER_ID_ATTRIB_NAME = "affiliationOwnerID";

    /** ID attribute's local name */
    public final static String ID_ATTRIB_NAME = "ID";

    /**
     * Gets the ID of the owner of this affiliation. The owner may, or may not, be a memeber of the affiliation.
     * 
     * @return the ID of the owner of this affiliation
     */
    public String getOwnerID();

    /**
     * Gets the ID of this Descriptor.
     * 
     * @return the ID of this Descriptor
     */
    public String getID();

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
     * Sets the ID of the owner of this affiliation.
     * 
     * @param ownerID the ID of the owner of this affiliation
     */
    public void setOwnerID(String ownerID);

    /**
     * Sets the ID of this descriptor.
     * 
     * @param newID the ID of this descriptor
     */
    public void setID(String newID);

    /**
     * Gets a list of the members of this affiliation.
     * 
     * @return a list of affiliate members
     */
    public List<AffiliateMember> getMembers();

    /**
     * Gets an immutable list of KeyDescriptors for this affiliation.
     * 
     * @return list of {@link KeyDescriptor}s for this affiliation
     */
    public List<KeyDescriptor> getKeyDescriptors();
}