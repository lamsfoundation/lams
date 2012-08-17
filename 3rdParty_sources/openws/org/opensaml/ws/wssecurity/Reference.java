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

package org.opensaml.ws.wssecurity;

import javax.xml.namespace.QName;

import org.opensaml.xml.AttributeExtensibleXMLObject;

/**
 * The &lt;wsse:Reference&gt; empty element.
 * 
 * @see "WS-Security 2004, Chapter 7.2"
 * 
 */
public interface Reference extends AttributeExtensibleXMLObject, WSSecurityObject {

    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "Reference";

    /** Qualified element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSSecurityConstants.WSSE_NS, ELEMENT_LOCAL_NAME, WSSecurityConstants.WSSE_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "ReferenceType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSSecurityConstants.WSSE_NS, TYPE_LOCAL_NAME, WSSecurityConstants.WSSE_PREFIX);

    /** The wsse:Reference/@URI attribute local name. */
    public static final String URI_ATTRIB_NAME= "URI";
    
    /** The wsse:Reference/@ValueType attribute local name. */
    public static final String VALUE_TYPE_ATTRIB_NAME= "ValueType";

    /**
     * Returns the wsse:Reference/@URI attribute value.
     * 
     * @return the URI attribute value.
     */
    public String getURI();

    /**
     * Sets the wsse:Reference/@URI attribute value.
     * 
     * @param newURI the URI to set.
     */
    public void setURI(String newURI);
    
    /**
     * Returns the ValueType attribute URI value.
     * 
     * @return the ValueType attribute value or <code>null</code>.
     */
    public String getValueType();

    /**
     * Sets the ValueType attribute URI value.
     * 
     * @param newValueType the ValueType attribute value.
     */
    public void setValueType(String newValueType);
    
}
