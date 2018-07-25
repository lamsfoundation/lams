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

package org.opensaml.xacml.ctx.impl;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.ctx.StatusMessageType;
import org.opensaml.xacml.impl.AbstractXACMLObjectBuilder;

/**
 * Builder for {@link org.opensaml.xacml.ctx.StatusMessageType}.
 */
public class StatusMessageTypeImplBuilder extends AbstractXACMLObjectBuilder<StatusMessageType> {

    /**
     * Constructor.
     */
    public StatusMessageTypeImplBuilder() {

    }

    /** {@inheritDoc} */
    public StatusMessageType buildObject() {
        return buildObject(XACMLConstants.XACML20CTX_NS, StatusMessageType.DEFAULT_ELEMENT_LOCAL_NAME,
                XACMLConstants.XACMLCONTEXT_PREFIX);
    }

    /** {@inheritDoc} */
    public StatusMessageType buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new StatusMessageTypeImpl(namespaceURI, localName, namespacePrefix);
    }

}
