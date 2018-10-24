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

/**
 * The wst:Status element.
 * 
 * @see "WS-Trust 1.3, Chapter 7 Validation Binding."
 * 
 */
public interface Status extends WSTrustObject {

    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "Status";

    /** Default element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSTrustConstants.WST_NS, ELEMENT_LOCAL_NAME, WSTrustConstants.WST_PREFIX);

    /**
     * Returns the wst:Code child element.
     * 
     * @return the {@link Code} child element or <code>null</code>
     */
    public Code getCode();

    /**
     * Sets the wst:Code child element.
     * 
     * @param code the {@link Code} child element to set.
     */
    public void setCode(Code code);

    /**
     * Returns the wst:Reason child element.
     * 
     * @return the {@link Reason} child element or <code>null</code>.
     */
    public Reason getReason();

    /**
     * Sets the wst:Reason child element.
     * 
     * @param reason the {@link Reason} child element to set.
     */
    public void setReason(Reason reason);

}
