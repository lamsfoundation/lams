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

package org.opensaml.common.binding;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLObject;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.xml.security.credential.Credential;

/**
 * SAML specific extension to the more basic {@link MessageContext}.
 * 
 * @param <InboundMessageType> type of inbound SAML message
 * @param <OutboundMessageType> type of outbound SAML message
 * @param <NameIdentifierType> type of name identifier used for subjects
 */
public interface SAMLMessageContext<InboundMessageType extends SAMLObject, OutboundMessageType extends SAMLObject, NameIdentifierType extends SAMLObject>
        extends MessageContext {

    /**
     * Gets the inbound SAML message. This may not be the same as the message returned from
     * {@link MessageContext#getInboundMessage()} if the SAML message was carried in another protocol (e.g. SOAP).
     * 
     * @return inbound SAML message
     */
    public InboundMessageType getInboundSAMLMessage();

    /**
     * Gets the ID of the inbound SAML message.
     * 
     * @return ID of the inbound SAML message
     */
    public String getInboundSAMLMessageId();

    /**
     * Gets the issue instant of the incomming SAML message.
     * 
     * @return issue instant of the incomming SAML message
     */
    public DateTime getInboundSAMLMessageIssueInstant();

    /**
     * Gets the protocol used by the peer entity to communicate with the local entity.
     * 
     * @return protocol used by the peer entity to communicate with the local entity
     */
    public String getInboundSAMLProtocol();

    /**
     * Gets the local entity's ID.
     * 
     * @return local entity's ID
     */
    public String getLocalEntityId();

    /**
     * Gets the local entity metadata.
     * 
     * @return local entity metadata
     */
    public EntityDescriptor getLocalEntityMetadata();

    /**
     * Gets the role of the local entity.
     * 
     * @return role of the local entity
     */
    public QName getLocalEntityRole();
    
    /**
     * Gets the role metadata of the local entity.
     * 
     * @return role metadata of the local entity
     */
    public RoleDescriptor getLocalEntityRoleMetadata();

    /**
     * Gets the metadata provider used to lookup information entity information.
     * 
     * @return metadata provider used to lookup information entity information
     */
    public MetadataProvider getMetadataProvider();

    /**
     * Gets the credential used to sign the outbound SAML message.
     * 
     * @return credential used to sign the outbound SAML message
     */
    public Credential getOuboundSAMLMessageSigningCredential();

    /**
     * Gets the artifact type to use for the outbound message.
     * 
     * @return artifact type to use for the outbound message
     */
    public byte[] getOutboundMessageArtifactType();

    /**
     * Gets the outbound SAML message. This may not be the same as the message returned from
     * {@link MessageContext#getOutboundMessage()} if the SAML message was carried in another protocol (e.g. SOAP).
     * 
     * @return outbound SAML message
     */
    public OutboundMessageType getOutboundSAMLMessage();

    /**
     * Gets the ID of the outbound SAML message.
     * 
     * @return ID of the outbound SAML message
     */
    public String getOutboundSAMLMessageId();

    /**
     * Gets the issue instant of the outbound SAML message.
     * 
     * @return issue instant of the outbound SAML message
     */
    public DateTime getOutboundSAMLMessageIssueInstant();

    /**
     * Gets the protocol used by the local entity to communicate with the peer entity.
     * 
     * @return protocol used by the local entity to communicate with the peer entity
     */
    public String getOutboundSAMLProtocol();

    /**
     * Gets the endpoint of for the peer entity.
     * 
     * @return endpoint of for the peer entity
     */
    public Endpoint getPeerEntityEndpoint();

    /**
     * Gets the peer's entity ID.
     * 
     * @return peer's entity ID
     */
    public String getPeerEntityId();

    /**
     * Gets the peer entity metadata.
     * 
     * @return peer entity metadata
     */
    public EntityDescriptor getPeerEntityMetadata();

    /**
     * Gets the role of the peer entity.
     * 
     * @return role of the peer entity
     */
    public QName getPeerEntityRole();

    /**
     * Gets the role of the peer entity.
     * 
     * @return role of the peer entity
     */
    public RoleDescriptor getPeerEntityRoleMetadata();

    /**
     * Gets the relay state associated with the message.
     * 
     * @return relay state associated with the message
     */
    public String getRelayState();

    /**
     * Gets the subject's SAML name identifier.
     * 
     * @return subject's SAML name identifier
     */
    public NameIdentifierType getSubjectNameIdentifier();

    /**
     * Gets whether the inbound SAML message has been authenticated.
     * 
     * @return whether the inbound SAML message has been authenticated
     */
    public boolean isInboundSAMLMessageAuthenticated();

    /**
     * Sets the inbound SAML message.
     * 
     * @param message inbound SAML message
     */
    public void setInboundSAMLMessage(InboundMessageType message);

    /**
     * Sets whether the inbound SAML message has been authenticated.
     * 
     * @param isAuthenticated whether the inbound SAML message has been authenticated
     */
    public void setInboundSAMLMessageAuthenticated(boolean isAuthenticated);

    /**
     * Sets the ID of the inbound SAML message.
     * 
     * @param id ID of the inbound SAML message
     */
    public void setInboundSAMLMessageId(String id);

    /**
     * Sets the issue instant of the incomming SAML message.
     * 
     * @param instant issue instant of the incomming SAML message
     */
    public void setInboundSAMLMessageIssueInstant(DateTime instant);

    /**
     * Sets the protocol used by the peer entity to communicate with the local entity.
     * 
     * @param protocol protocol used by the peer entity to communicate with the local entity
     */
    public void setInboundSAMLProtocol(String protocol);

    /**
     * Sets the local entity's ID.
     * 
     * @param id local entity's ID
     */
    public void setLocalEntityId(String id);

    /**
     * Sets the local entity metadata.
     * 
     * @param metadata local entity metadata
     */
    public void setLocalEntityMetadata(EntityDescriptor metadata);

    /**
     * Sets the role of the local entity.
     * 
     * @param role role of the local entity
     */
    public void setLocalEntityRole(QName role);

    /**
     * Sets the role metadata for the local entity.
     * 
     * @param role role metadata for the local entity
     */
    public void setLocalEntityRoleMetadata(RoleDescriptor role);

    /**
     * Sets the metadata provider used to lookup information entity information.
     * 
     * @param provider metadata provider used to lookup information entity information
     */
    public void setMetadataProvider(MetadataProvider provider);

    /**
     * Sets the artifact type to use for the outbound message.
     * 
     * @param type artifact type to use for the outbound message
     */
    public void setOutboundMessageArtifactType(byte[] type);

    /**
     * Sets the outbound SAML message.
     * 
     * @param message outbound SAML message
     */
    public void setOutboundSAMLMessage(OutboundMessageType message);

    /**
     * Sets the ID of the outbound SAML message.
     * 
     * @param id ID of the outbound SAML message
     */
    public void setOutboundSAMLMessageId(String id);

    /**
     * Sets the issue instant of the outbound SAML message.
     * 
     * @param instant issue instant of the outbound SAML message
     */
    public void setOutboundSAMLMessageIssueInstant(DateTime instant);

    /**
     * Sets the credential used to sign the outbound SAML message.
     * 
     * @param credential credential used to sign the outbound SAML message
     */
    public void setOutboundSAMLMessageSigningCredential(Credential credential);

    /**
     * Sets the protocol used by the local entity to communicate with the peer entity.
     * 
     * @param protocol protocol used by the local entity to communicate with the peer entity
     */
    public void setOutboundSAMLProtocol(String protocol);

    /**
     * Sets the endpoint of for the peer entity.
     * 
     * @param endpoint endpoint of for the peer entity
     */
    public void setPeerEntityEndpoint(Endpoint endpoint);

    /**
     * Sets the peer's entity ID.
     * 
     * @param id peer's entity ID
     */
    public void setPeerEntityId(String id);

    /**
     * Sets the peer entity metadata.
     * 
     * @param metadata peer entity metadata
     */
    public void setPeerEntityMetadata(EntityDescriptor metadata);

    /**
     * Sets the role of the peer entity.
     * 
     * @param role role of the peer entity
     */
    public void setPeerEntityRole(QName role);

    /**
     * Sets the role metadata for the peer entity.
     * 
     * @param role role metadata for the peer entity
     */
    public void setPeerEntityRoleMetadata(RoleDescriptor role);

    /**
     * Sets the relay state associated with the message.
     * 
     * @param relayState relay state associated with the message
     */
    public void setRelayState(String relayState);

    /**
     * Sets the subject's SAML name identifier.
     * 
     * @param identifier subject's SAML name identifier
     */
    public void setSubjectNameIdentifier(NameIdentifierType identifier);
}