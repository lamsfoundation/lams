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

package org.opensaml.ws.wstrust;

import javax.xml.namespace.QName;

import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.schema.XSString;

/**
 * The wst:BinaryExchange element.
 * 
 * @see "WS-Trust 1.3, Chapter 8.3 Binary Exchanges and Negotiations."
 * 
 */
public interface BinaryExchange extends XSString, AttributeExtensibleXMLObject, WSTrustObject {

    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "BinaryExchange";

    /** Default element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSTrustConstants.WST_NS, ELEMENT_LOCAL_NAME, WSTrustConstants.WST_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "BinaryExchangeType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSTrustConstants.WST_NS, TYPE_LOCAL_NAME, WSTrustConstants.WST_PREFIX);
    
    /** The ValueType attribute name. */
    public static final String VALUE_TYPE_ATTRIB_NAME = "ValueType";
    
    /** The EncodingType attribute name. */
    public static final String ENCODING_TYPE_ATTRIB_NAME = "EncodingType";
    
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
    
    /**
     * Returns the EncodingType attribute value.
     * 
     * @return the EncodingType attribute value.
     */
    public String getEncodingType();

    /**
     * Sets the EncodingType attribute value.
     * 
     * @param newEncodingType the EncodingType attribute value.
     */
    public void setEncodingType(String newEncodingType);

}
