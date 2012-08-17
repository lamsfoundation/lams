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

package org.opensaml.samlext.saml2mdattr;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;

/** SAML V2.0 Metadata Extension for Entity Attributes EntityAttributes SAML object. */
public interface EntityAttributes extends SAMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "EntityAttributes";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME =
        new QName(SAMLConstants.SAML20MDATTR_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20MDATTR_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "EntityAttributesType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME =
        new QName(SAMLConstants.SAML20MDATTR_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20MDATTR_PREFIX);
    
    /**
     * Gets the attributes about the entity.
     * 
     * @return attributes about the entity
     */
    public List<Attribute> getAttributes();
    
    /**
     * Gets the assertions about the entity.
     * 
     * @return assertions about the entity
     */
    public List<Assertion> getAssertions();
}