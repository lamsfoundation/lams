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

import org.opensaml.ws.soap.soap11.ActorBearing;
import org.opensaml.ws.soap.soap11.MustUnderstandBearing;
import org.opensaml.ws.soap.soap12.RelayBearing;
import org.opensaml.ws.soap.soap12.RoleBearing;
import org.opensaml.xml.encryption.EncryptedData;

/**
 * The &lt;wsse:EncryptedHeader&gt; element.
 * 
 * @see EncryptedHeader
 * @see "WS-Security, Chapter 9.3 EncryptedHeader"
 * 
 */
public interface EncryptedHeader extends IdBearing, MustUnderstandBearing, ActorBearing,
        org.opensaml.ws.soap.soap12.MustUnderstandBearing, RoleBearing, RelayBearing, WSSecurityObject {
    
    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "EncryptedHeader";

    /** Qualified element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSSecurityConstants.WSSE11_NS, ELEMENT_LOCAL_NAME, WSSecurityConstants.WSSE11_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "EncryptedHeaderType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSSecurityConstants.WSSE11_NS, TYPE_LOCAL_NAME, WSSecurityConstants.WSSE11_PREFIX);
    
    /**
     * Gets the EncryptedData child element.
     * 
     * @return the EncryptedData child element
     */
    public EncryptedData getEncryptedData();
    
    /**
     * Sets the EncryptedData child element.
     * 
     * @param newEncryptedData the new EncryptedData child element
     */
    public void setEncryptedData(EncryptedData newEncryptedData);

}
