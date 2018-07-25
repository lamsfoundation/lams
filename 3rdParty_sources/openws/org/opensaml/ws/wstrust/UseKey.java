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

import org.opensaml.xml.XMLObject;

/**
 * The wst:UseKey element.
 * 
 * @see "WS-Trust 1.3, Chapter 9.2 Key and Encryption Requirements."
 * 
 */
public interface UseKey extends WSTrustObject {

    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "UseKey";

    /** Default element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSTrustConstants.WST_NS, ELEMENT_LOCAL_NAME, WSTrustConstants.WST_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "UseKeyType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSTrustConstants.WST_NS, TYPE_LOCAL_NAME, WSTrustConstants.WST_PREFIX);

    /** The wst:UseKey/@Sig attribute local name. */
    public static final String SIG_ATTRIB_NAME = "Sig";
    
    /**
     * Get the unknown child element.
     * 
     * @return the child element
     */
    public XMLObject getUnknownXMLObject();
    
    /**
     * Set the unknown child element.
     * 
     * @param unknownObject the new child element
     */
    public void setUnknownXMLObject(XMLObject unknownObject);

    /**
     * Returns the wst:UseKey/@Sig attribute value.
     * 
     * @return the Sig attribute value or <code>null</code>
     */
    public String getSig();

    /**
     * Sets the wst:UseKey/@Sig attribute value.
     * 
     * @param sig the Sig attribute value to set.
     */
    public void setSig(String sig);

}
