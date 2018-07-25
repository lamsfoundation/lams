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
import org.opensaml.ws.message.BaseMessageContext;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * Base implemention of {@link SAMLMessageContext}.
 * 
 * @param <InboundMessageType> type of inbound SAML message
 * @param <OutboundMessageType> type of outbound SAML message
 * @param <NameIdentifierType> type of name identifier used for subjects
 */
public class BasicSAMLMessageContext<InboundMessageType extends SAMLObject, OutboundMessageType extends SAMLObject, NameIdentifierType extends SAMLObject>
        extends BaseMessageContext implements SAMLMessageContext<InboundMessageType, OutboundMessageType, NameIdentifierType> {

    /** Gets the artifact type used for outbound messages. */
    private byte[] artifactType;
    
    /** Name identifier for the Subject of the message. */
    private NameIdentifierType subjectNameIdentifer;
    
    /** Local entity's ID. */
    private String localEntityId;

    /** Local entity's metadata. */
    private EntityDescriptor localEntityMetadata;

    /** Asserting entity's role. */
    private QName localEntityRole;

    /** Asserting entity's role metadata. */
    private RoleDescriptor localEntityRoleMetadata;

    /** Inbound SAML message. */
    private InboundMessageType inboundSAMLMessage;

    /** Whether the inbound SAML message has been authenticated. */
    private boolean inboundSAMLMessageAuthenticated;

    /** Inbound SAML message's ID. */
    private String inboundSAMLMessageId;

    /** Inbound SAML message's issue instant. */
    private DateTime inboundSAMLMessageIssueInstant;

    /** Inbound SAML protocol. */
    private String inboundSAMLProtocol;

    /** Metadata provider used to lookup entity information. */
    private MetadataProvider metdataProvider;

    /** Outbound SAML message. */
    private OutboundMessageType outboundSAMLMessage;

    /** Outbound SAML message's ID. */
    private String outboundSAMLMessageId;

    /** Outbound SAML message's issue instant. */
    private DateTime outboundSAMLMessageIssueInstant;

    /** Outboud SAML message signing credential. */
    private Credential outboundSAMLMessageSigningCredential;

    /** Outbound SAML procotol. */
    private String outboundSAMLProtocol;

    /** Message relay state. */
    private String relayState;

    /** Peer entity's endpoint. */
    private Endpoint peerEntityEndpoint;

    /**Peer entity's ID. */
    private String peerEntityId;

    /** Peer entity's metadata. */
    private EntityDescriptor peerEntityMetadata;

    /** Peer entity's role. */
    private QName peerEntityRole;

    /** Peer entity's role metadata. */
    private RoleDescriptor peerEntityRoleMetadata;

    /** {@inheritDoc} */
    public InboundMessageType getInboundSAMLMessage() {
        return inboundSAMLMessage;
    }

    /** {@inheritDoc} */
    public String getInboundSAMLMessageId() {
        return inboundSAMLMessageId;
    }

    /** {@inheritDoc} */
    public DateTime getInboundSAMLMessageIssueInstant() {
        return inboundSAMLMessageIssueInstant;
    }

    /** {@inheritDoc} */
    public String getInboundSAMLProtocol() {
        return inboundSAMLProtocol;
    }

    /** {@inheritDoc} */
    public String getLocalEntityId() {
        return localEntityId;
    }

    /** {@inheritDoc} */
    public EntityDescriptor getLocalEntityMetadata() {
        return localEntityMetadata;
    }

    /** {@inheritDoc} */
    public QName getLocalEntityRole() {
        return localEntityRole;
    }

    /** {@inheritDoc} */
    public RoleDescriptor getLocalEntityRoleMetadata() {
        return localEntityRoleMetadata;
    }

    /** {@inheritDoc} */
    public MetadataProvider getMetadataProvider() {
        return metdataProvider;
    }

    /** {@inheritDoc} */
    public Credential getOuboundSAMLMessageSigningCredential() {
        return outboundSAMLMessageSigningCredential;
    }

    /** {@inheritDoc} */
    public OutboundMessageType getOutboundSAMLMessage() {
        return outboundSAMLMessage;
    }

    /** {@inheritDoc} */
    public String getOutboundSAMLMessageId() {
        return outboundSAMLMessageId;
    }

    /** {@inheritDoc} */
    public DateTime getOutboundSAMLMessageIssueInstant() {
        return outboundSAMLMessageIssueInstant;
    }

    /** {@inheritDoc} */
    public String getOutboundSAMLProtocol() {
        return outboundSAMLProtocol;
    }

    /** {@inheritDoc} */
    public Endpoint getPeerEntityEndpoint() {
        return peerEntityEndpoint;
    }

    /** {@inheritDoc} */
    public String getPeerEntityId() {
        return peerEntityId;
    }

    /** {@inheritDoc} */
    public EntityDescriptor getPeerEntityMetadata() {
        return peerEntityMetadata;
    }

    /** {@inheritDoc} */
    public QName getPeerEntityRole() {
        return peerEntityRole;
    }

    /** {@inheritDoc} */
    public RoleDescriptor getPeerEntityRoleMetadata() {
        return peerEntityRoleMetadata;
    }

    /** {@inheritDoc} */
    public String getRelayState() {
        return relayState;
    }

    /** {@inheritDoc} */
    public NameIdentifierType getSubjectNameIdentifier() {
        return subjectNameIdentifer;
    }

    /** {@inheritDoc} */
    public boolean isInboundSAMLMessageAuthenticated() {
        return inboundSAMLMessageAuthenticated;
    }

    /** {@inheritDoc} */
    public void setInboundSAMLMessage(InboundMessageType message) {
        inboundSAMLMessage = message;
    }

    /** {@inheritDoc} */
    public void setInboundSAMLMessageAuthenticated(boolean isAuthenticated) {
        inboundSAMLMessageAuthenticated = isAuthenticated;
    }

    /** {@inheritDoc} */
    public void setInboundSAMLMessageId(String id) {
        inboundSAMLMessageId = DatatypeHelper.safeTrimOrNullString(id);
    }

    /** {@inheritDoc} */
    public void setInboundSAMLMessageIssueInstant(DateTime instant) {
        inboundSAMLMessageIssueInstant = instant;
    }

    /** {@inheritDoc} */
    public void setInboundSAMLProtocol(String protocol) {
        inboundSAMLProtocol = DatatypeHelper.safeTrimOrNullString(protocol);
    }

    /** {@inheritDoc} */
    public void setLocalEntityId(String id) {
        localEntityId = DatatypeHelper.safeTrimOrNullString(id);
    }

    /** {@inheritDoc} */
    public void setLocalEntityMetadata(EntityDescriptor metadata) {
        localEntityMetadata = metadata;
    }

    /** {@inheritDoc} */
    public void setLocalEntityRole(QName role) {
        localEntityRole = role;
    }

    /** {@inheritDoc} */
    public void setLocalEntityRoleMetadata(RoleDescriptor role) {
        localEntityRoleMetadata = role;
    }

    /** {@inheritDoc} */
    public void setMetadataProvider(MetadataProvider provider) {
        metdataProvider = provider;
    }

    /** {@inheritDoc} */
    public void setOutboundSAMLMessage(OutboundMessageType message) {
        outboundSAMLMessage = message;
    }

    /** {@inheritDoc} */
    public void setOutboundSAMLMessageId(String id) {
        outboundSAMLMessageId = DatatypeHelper.safeTrimOrNullString(id);
    }

    /** {@inheritDoc} */
    public void setOutboundSAMLMessageIssueInstant(DateTime instant) {
        outboundSAMLMessageIssueInstant = instant;
    }

    /** {@inheritDoc} */
    public void setOutboundSAMLMessageSigningCredential(Credential credential) {
        outboundSAMLMessageSigningCredential = credential;
    }

    /** {@inheritDoc} */
    public void setOutboundSAMLProtocol(String protocol) {
        outboundSAMLProtocol = DatatypeHelper.safeTrimOrNullString(protocol);
    }

    /** {@inheritDoc} */
    public void setPeerEntityEndpoint(Endpoint endpoint) {
        peerEntityEndpoint = endpoint;
    }

    /** {@inheritDoc} */
    public void setPeerEntityId(String id) {
        peerEntityId = DatatypeHelper.safeTrimOrNullString(id);
    }

    /** {@inheritDoc} */
    public void setPeerEntityMetadata(EntityDescriptor metadata) {
        peerEntityMetadata = metadata;
    }

    /** {@inheritDoc} */
    public void setPeerEntityRole(QName role) {
        peerEntityRole = role;
    }

    /** {@inheritDoc} */
    public void setPeerEntityRoleMetadata(RoleDescriptor role) {
        peerEntityRoleMetadata = role;
    }

    /** {@inheritDoc} */
    public void setRelayState(String state) {
        relayState = DatatypeHelper.safeTrimOrNullString(state);
    }

    /** {@inheritDoc} */
    public void setSubjectNameIdentifier(NameIdentifierType identifier) {
        subjectNameIdentifer = identifier;
    }
    
    /** {@inheritDoc} */
    public byte[] getOutboundMessageArtifactType() {
        return artifactType;
    }
    
    /** {@inheritDoc} */
    public void setOutboundMessageArtifactType(byte[] type) {
        artifactType = type;
    }

    /** {@inheritDoc} */
    public boolean isIssuerAuthenticated() {
        return isInboundSAMLMessageAuthenticated() || super.isIssuerAuthenticated();
    }
}