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
 * This interface defines how the object representing a SAML1 <code> Conditions</code> element behaves.
 */
public interface Conditions extends SAMLObject {

    /** Element name, no namespace. */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "Conditions";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML1_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "ConditionsType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML1_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);

    /** Name for the NotBefore attribute. */
    public final static String NOTBEFORE_ATTRIB_NAME = "NotBefore";

    /** Name for the NotBefore attribute. */
    public final static String NOTONORAFTER_ATTRIB_NAME = "NotOnOrAfter";

    /** Return the value of the NotBefore attribute. */
    public DateTime getNotBefore();

    /** List the value of the NotBefore attribute. */
    public void setNotBefore(DateTime notBefore);

    /** Return the value of the NotOnOrAfter attribute. */
    public DateTime getNotOnOrAfter();

    /** List the value of the NotOnOrAfter attribute. */
    public void setNotOnOrAfter(DateTime notOnOrAfter);
    
    /**
     * Return the List representing all the <code> Condition </code> sub elements.
     */
    public List<Condition> getConditions();
    
    /**
     * Return the List representing all the <code>Condition</code>s with the given schema type or element name.
     * 
     * @param typeOrName the schema type or element name
     */
    public List<Condition> getConditions(QName typeOrName);

    /**
     * Return the List representing all the <code> AudienceRestrictionCondition </code> sub elements.
     */
    public List<AudienceRestrictionCondition> getAudienceRestrictionConditions();

    /**
     * Return the List representing all the <code> DoNotCacheCondition </code> sub elements.
     */
    public List<DoNotCacheCondition> getDoNotCacheConditions();
}