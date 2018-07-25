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

package org.opensaml.saml1.binding;

import java.util.Collection;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml1.core.Assertion;

/**
 * Extensions to the base SAML message context that carries artifact related information.
 * 
 * @param <InboundMessageType> type of inbound SAML message
 * @param <OutboundMessageType> type of outbound SAML message
 * @param <NameIdentifierType> type of name identifier used for subjects
 */
public interface SAML1ArtifactMessageContext<InboundMessageType extends SAMLObject, OutboundMessageType extends SAMLObject, NameIdentifierType extends SAMLObject>
        extends SAMLMessageContext<InboundMessageType, OutboundMessageType, NameIdentifierType> {

    /**
     * Gets the Base64 encoded artifacts to be resolved.
     * 
     * @return artifacts to be resolved
     */
    public Collection<String> getArtifacts();

    /**
     * Sets the Base64 encoded artifacts to be resolved.
     * 
     * @param artifacts artifacts to be resolved
     */
    public void setArtifacts(Collection<String> artifacts);

    /**
     * Gets the assertions dereferenced from the artifacts.
     * 
     * @return assertions dereferenced from the artifacts
     */
    public Collection<Assertion> getDereferencedAssertions();

    /**
     * Sets the assertions dereferenced from the artifacts.
     * 
     * @param assertions assertions dereferenced from the artifacts
     */
    public void setDereferencedAssertions(Collection<Assertion> assertions);
}
