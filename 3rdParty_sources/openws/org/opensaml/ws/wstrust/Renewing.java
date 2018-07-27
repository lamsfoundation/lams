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

import org.opensaml.xml.schema.XSBooleanValue;

/**
 * The wst:Renewing element.
 * 
 * @see "WS-Trust 1.3, Chapter 5 Renewal Binding."
 * 
 */
public interface Renewing extends WSTrustObject {

    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "Renewing";

    /** Default element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSTrustConstants.WST_NS, ELEMENT_LOCAL_NAME, WSTrustConstants.WST_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RenewingType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSTrustConstants.WST_NS, TYPE_LOCAL_NAME, WSTrustConstants.WST_PREFIX);

    /** The wst:Renewing/@Allow attribute local name. */
    public static final String ALLOW_ATTRIB_NAME = "Allow";

    /** The wst:Renewing/@OK attribute local name. */
    public static final String OK_ATTRIB_NAME = "OK";

    /**
     * Returns the wst:Renewing/@Allow attribute value.
     * 
     * @return the Allow attribute value
     */
    public Boolean isAllow();
    
    /**
     * Returns the wst:Renewing/@Allow attribute value.
     * 
     * @return the Allow attribute value
     */
    public XSBooleanValue isAllowXSBoolean();

    /**
     * Sets the wst:Renewing/@Allow attribute value.
     * 
     * @param allow the Allow attribute value.
     */
    public void setAllow(Boolean allow);

    /**
     * Sets the wst:Renewing/@Allow attribute value.
     * 
     * @param allow the Allow attribute value.
     */
    public void setAllow(XSBooleanValue allow);

    /**
     * Returns the wst:Renewing/@OK attribute value.
     * 
     * @return the OK attribute value
     */
    public Boolean isOK();
    
    /**
     * Returns the wst:Renewing/@OK attribute value.
     * 
     * @return the OK attribute value
     */
    public XSBooleanValue isOKXSBoolean();

    /**
     * Sets the wst:Renewing/@OK attribute value.
     * 
     * @param ok the OK attribute value.
     */
    public void setOK(Boolean ok);
    
    /**
     * Sets the wst:Renewing/@OK attribute value.
     * 
     * @param ok the OK attribute value.
     */
    public void setOK(XSBooleanValue ok);
    
}
