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
 * The <code>Password</code> element.
 * 
 * @see "WS-Security UsernameToken Profile 1.1"
 * 
 */
public interface Password extends AttributedString {
    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "Password";

    /** Qualified element name. */
    public static final QName ELEMENT_NAME = 
        new QName(WSSecurityConstants.WSSE_NS, ELEMENT_LOCAL_NAME, WSSecurityConstants.WSSE_PREFIX);

    /** The <code>Type</code> attribute local name. */
    public static final String TYPE_ATTRIB_NAME = "Type";
    
    /**
     * The <code>wsse:Password/@Type</code> attribute URI value <code>#PasswordText</code> (DEFAULT).
     */
    public static final String TYPE_PASSWORD_TEXT = WSSecurityConstants.WSSE_NS + "#PasswordText";

    /**
     * The <code>wsse:Password/@Type</code> attribute URI value <code>#PasswordDigest</code>.
     */
    public static final String TYPE_PASSWORD_DIGEST = WSSecurityConstants.WSSE_NS + "#PasswordDigest";

    /**
     * Returns the <code>wsse:Password/@Type</code> attribute URI value.
     * 
     * @return the <code>Type</code> attribute URI value.
     */
    public String getType();

    /**
     * Sets the <code>wsse:Password/@Type</code> attribute URI value.
     * 
     * @param type the <code>Type</code> attribute URI value to set.
     */
    public void setType(String type);

}
