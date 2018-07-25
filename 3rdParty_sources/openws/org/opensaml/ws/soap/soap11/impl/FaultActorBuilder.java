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

package org.opensaml.ws.soap.soap11.impl;

import org.opensaml.ws.soap.common.SOAPObjectBuilder;
import org.opensaml.ws.soap.soap11.FaultActor;
import org.opensaml.xml.AbstractXMLObjectBuilder;

/**
 * A builder of {@link org.opensaml.ws.soap.soap11.impl.FaultActorImpl} objects.
 */
public class FaultActorBuilder extends AbstractXMLObjectBuilder<FaultActor> implements SOAPObjectBuilder<FaultActor> {

    /** {@inheritDoc} */
    public FaultActor buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new FaultActorImpl(namespaceURI, localName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public FaultActor buildObject() {
        return buildObject(null, FaultActor.DEFAULT_ELEMENT_LOCAL_NAME, null);
    }
}