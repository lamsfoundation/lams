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

package org.opensaml.ws.wsfed;

import java.util.List;

import javax.xml.namespace.QName;

/**
 * This interface defines how the object representing a WS RSTR <code> RequestedSecurityTokenResponse </code> element
 * behaves.
 */
public interface RequestSecurityTokenResponse extends WSFedObject {

    /** Element name, no namespace. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "RequestSecurityTokenResponse";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(WSFedConstants.WSFED11P_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            WSFedConstants.WSFED1P_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RequestSecurityTokenResponseType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(WSFedConstants.WSFED11P_NS, TYPE_LOCAL_NAME,
            WSFedConstants.WSFED1P_PREFIX);

    /**
     * Gets the entity to which the token applies.
     * 
     * @return the entity to which the token applies
     */
    public AppliesTo getAppliesTo();

    /**
     * Set the entity to which the token applies.
     * 
     * @param appliesTo the entity to which the token applies
     */
    public void setAppliesTo(AppliesTo appliesTo);

    /**
     * Return the list of Security Token child elements.
     * 
     * @return the list of RequestedSecurityToken child elements.
     */
    public List<RequestedSecurityToken> getRequestedSecurityToken();
}