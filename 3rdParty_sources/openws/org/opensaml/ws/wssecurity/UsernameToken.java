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
import org.opensaml.xml.ElementExtensibleXMLObject;

/**
 * The &lt;wsse:UsernameToken&gt; element.
 * 
 * @see "WS-Security UsernameToken Profile 1.1"
 * 
 */
public interface UsernameToken extends IdBearing, AttributeExtensibleXMLObject, 
        ElementExtensibleXMLObject, WSSecurityObject {
    
    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "UsernameToken";

    /** Qualified element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSSecurityConstants.WSSE_NS, ELEMENT_LOCAL_NAME, WSSecurityConstants.WSSE_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "UsernameTokenType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSSecurityConstants.WSSE_NS, TYPE_LOCAL_NAME, WSSecurityConstants.WSSE_PREFIX);

    /**
     * Returns the &lt;wsse:Username&gt; child element.
     * 
     * @return the {@link Username} child element.
     */
    public Username getUsername();

    /**
     * Sets the &lt;wsse:Username&gt; child element.
     * 
     * @param username
     *            the {@link Username} child element to set.
     */
    public void setUsername(Username username);

}
