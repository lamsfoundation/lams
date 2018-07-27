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

import javax.xml.namespace.QName;

/**
 * This interface defines how the object representing a WS Applies To <code> AppliesTo </code> element behaves.
 */
public interface AppliesTo extends WSFedObject {

    /** Element name, no namespace. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "AppliesTo";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(WSFedConstants.WSPOLICY_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            WSFedConstants.WSPOLICY_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "AppliesToType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(WSFedConstants.WSPOLICY_NS, TYPE_LOCAL_NAME,
            WSFedConstants.WSPOLICY_PREFIX);

    /**
     * Gets the endpoint reference of the entity applicable entity.
     * 
     * @return the endpoint reference of the entity applicable entity
     */
    public EndPointReference getEndPointReference();

    /**
     * Sets the endpoint reference of the entity applicable entity.
     * 
     * @param newEndPointReference the endpoint reference of the entity applicable entity
     */
    public void setEndPointReference(EndPointReference newEndPointReference);
}