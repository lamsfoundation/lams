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

package org.opensaml.ws.wsaddressing;

import javax.xml.namespace.QName;

import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.ElementExtensibleXMLObject;

/**
 * Interface for element of type {@link EndpointReferenceType}.
 * 
 * @see "WS-Addressing 1.0 - Core"
 * 
 */
public interface EndpointReferenceType extends AttributeExtensibleXMLObject, ElementExtensibleXMLObject, WSAddressingObject {
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "EndpointReferenceType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSAddressingConstants.WSA_NS, TYPE_LOCAL_NAME, WSAddressingConstants.WSA_PREFIX);

    /**
     * Returns the &lt;wsa:Address&gt; child element.
     * 
     * @return the {@link Address} child element or <code>null</code>
     */
    public Address getAddress();

    /**
     * Sets the &lt;wsa:Address&gt; child element.
     * 
     * @param address the {@link Address} child element to set.
     */
    public void setAddress(Address address);

    /**
     * Returns the optional &lt;wsa:Metadata&gt; child element.
     * 
     * @return the {@link Metadata} child element or <code>null</code>.
     */
    public Metadata getMetadata();

    /**
     * Sets the &lt;wsa:Metadata&gt; child element.
     * 
     * @param metadata the {@link Metadata} child element to set.
     */
    public void setMetadata(Metadata metadata);

    /**
     * Returns the optional &lt;wsa:ReferenceParameters&gt; child element.
     * 
     * @return the {@link ReferenceParameters} child element or <code>null</code>.
     */
    public ReferenceParameters getReferenceParameters();

    /**
     * Sets the &lt;wsa:ReferenceParameters&gt; child element.
     * 
     * @param referenceParameters the {@link ReferenceParameters} child element to set.
     */
    public void setReferenceParameters(ReferenceParameters referenceParameters);

}
