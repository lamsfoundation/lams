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
 * This interface defines how the object representing a WS End Point Reference <code> EndPointReference </code> element
 * behaves.
 */
public interface EndPointReference extends WSFedObject {

    /** Element name, no namespace. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "EndPointReference";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(WSFedConstants.WSADDRESS_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            WSFedConstants.WSADDRESS_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "EndPointReferenceType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(WSFedConstants.WSADDRESS_NS, TYPE_LOCAL_NAME,
            WSFedConstants.WSADDRESS_PREFIX);

    /**
     * Return the object representing the <code>Address</code> (element).
     * 
     * @return the end point address
     */
    public Address getAddress();

    /**
     * Sets the end point address as an object.
     * 
     * @param address the end point address
     */
    public void setAddress(Address address);
}