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

/**
 * SAML 2.0 Metadata EntitiesDescriptor.
 * 
 * @author Chad La Joie
 */
public interface EntitiesDescriptor extends SignableSAMLObject, TimeBoundSAMLObject, CacheableSAMLObject {

    /** Element name, no namespace */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "EntitiesDescriptor";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20MD_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "EntitiesDescriptorType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML20MD_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);

    /** Element QName, no prefix */
    public final static QName ELEMENT_QNAME = new QName(SAMLConstants.SAML20MD_NS, DEFAULT_ELEMENT_LOCAL_NAME);

    /** "ID" attribute name */
    public final static String ID_ATTRIB_NAME = "ID";

    /** "Name" attribute name */
    public final static String NAME_ATTRIB_NAME = "Name";

    /**
     * Gets the name of this entity group.
     * 
     * @return the name of this entity group
     */
    public String getName();

    /**
     * Sets the name of this entity group.
     * 
     * @param name the name of this entity group
     */
    public void setName(String name);

    /**
     * Gets the ID of this entity group.
     * 
     * @return the id of this entity group
     */
    public String getID();

    /**
     * Sets the ID of this entity group.
     * 
     * @param newID the ID of this entity group
     */
    public void setID(String newID);

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
     * Gets a list of child {@link EntitiesDescriptor}s.
     * 
     * @return list of descriptors
     */
    public List<EntitiesDescriptor> getEntitiesDescriptors();

    /**
     * Gets a list of child {@link EntityDescriptor}s.
     * 
     * @return list of child descriptors
     */
    public List<EntityDescriptor> getEntityDescriptors();
}