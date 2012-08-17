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

import org.opensaml.ws.wssecurity.SecurityTokenReference;

/**
 * Interface for RequestedReferenceType complex type.
 * 
 */
public interface RequestedReferenceType extends WSTrustObject {
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RequestedReferenceType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSTrustConstants.WST_NS, TYPE_LOCAL_NAME, WSTrustConstants.WST_PREFIX);
    
    /**
     * Returns the wsse:SecurityTokenReference child element.
     * 
     * @return the {@link SecurityTokenReference} child element or
     *         <code>null</code>.
     */
    public SecurityTokenReference getSecurityTokenReference();

    /**
     * Sets the wsse:SecurityTokenReference child element.
     * 
     * @param securityTokenReference
     *            The {@link SecurityTokenReference} child element to be set.
     */
    public void setSecurityTokenReference(SecurityTokenReference securityTokenReference);

}
