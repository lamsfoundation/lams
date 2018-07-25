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

import java.util.List;

import javax.xml.namespace.QName;

/**
 * The wst:RequestSecurityTokenCollection element.
 * 
 * @see "WS-Trust 1.3 Specification"
 * 
 */
public interface RequestSecurityTokenCollection extends WSTrustObject {

    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "RequestSecurityTokenCollection";

    /** Default element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSTrustConstants.WST_NS, ELEMENT_LOCAL_NAME, WSTrustConstants.WST_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RequestSecurityTokenCollectionType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSTrustConstants.WST_NS, TYPE_LOCAL_NAME, WSTrustConstants.WST_PREFIX);

    /**
     * Returns the list of wst:RequestSecurityToken child elements
     * contained in the RSTC.
     * 
     * @return the list of {@link RequestSecurityToken}s.
     */
    public List<RequestSecurityToken> getRequestSecurityTokens();

}
