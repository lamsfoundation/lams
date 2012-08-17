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

/**
 * Interface for element having a <code>@wsse11:TokenType</code>; attribute.
 * 
 * @see "WS-Security 2004, Chapter 7.1 SecurityTokenReference Element."
 * 
 */
public interface TokenTypeBearing {

    /**
     * The <code>@wsse11:TokenType</code> attribute local name.
     */
    public static final String WSSE11_TOKEN_TYPE_ATTR_LOCAL_NAME = "TokenType";

    /**
     * The <code>@wsse11:TokenType</code> qualified attribute name.
     */
    public static final QName WSSE11_TOKEN_TYPE_ATTR_NAME =
        new QName(WSSecurityConstants.WSSE11_NS, WSSE11_TOKEN_TYPE_ATTR_LOCAL_NAME, WSSecurityConstants.WSSE11_PREFIX);

    /**
     * Returns the <code>@wsse11:TokenType</code> attribute value.
     * 
     * @return the <code>@wsse11:TokenType</code> attribute value or <code>null</code>.
     */
    public String getWSSE11TokenType();

    /**
     * Sets the <code>@wsse11:TokenType</code> attribute value.
     * 
     * @param tokenType the <code>@wsse11:TokenType</code> attribute value to set.
     */
    public void setWSSE11TokenType(String tokenType);

}
