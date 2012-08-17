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

package org.opensaml.saml1.binding.encoding;

import java.util.List;

import org.opensaml.Configuration;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.binding.artifact.SAMLArtifactMap;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml1.binding.artifact.AbstractSAML1Artifact;
import org.opensaml.saml1.binding.artifact.SAML1ArtifactBuilder;
import org.opensaml.saml1.binding.artifact.SAML1ArtifactType0001;
import org.opensaml.saml1.core.Assertion;
import org.opensaml.saml1.core.NameIdentifier;
import org.opensaml.saml1.core.Response;
import org.opensaml.util.URLBuilder;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.transport.http.HTTPOutTransport;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SAML 1.X HTTP Artifact message encoder.
 */
public class HTTPArtifactEncoder extends BaseSAML1MessageEncoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPArtifactEncoder.class);

    /** SAML artifact map used to store created artifacts for later retrival. */
    private SAMLArtifactMap artifactMap;

    /** Default artifact type to use when encoding messages. */
    private byte[] defaultArtifactType;

    /**
     * Constructor.
     * 
     * @param map SAML artifact map used to store created artifacts for later retrival
     */
    public HTTPArtifactEncoder(SAMLArtifactMap map) {
        artifactMap = map;
        defaultArtifactType = SAML1ArtifactType0001.TYPE_CODE;
    }

    /** {@inheritDoc} */
    public String getBindingURI() {
        return SAMLConstants.SAML1_ARTIFACT_BINDING_URI;
    }

    /** {@inheritDoc} */
    public boolean providesMessageConfidentiality(MessageContext messageContext) throws MessageEncodingException {
        return false;
    }

    /** {@inheritDoc} */
    public boolean providesMessageIntegrity(MessageContext messageContext) throws MessageEncodingException {
        return false;
    }

    /** {@inheritDoc} */
    protected void doEncode(MessageContext messageContext) throws MessageEncodingException {
        if (!(messageContext instanceof SAMLMessageContext)) {
            log.error("Invalid message context type, this encoder only support SAMLMessageContext");
            throw new MessageEncodingException(
                    "Invalid message context type, this encoder only support SAMLMessageContext");
        }

        if (!(messageContext.getOutboundMessageTransport() instanceof HTTPOutTransport)) {
            log.error("Invalid outbound message transport type, this encoder only support HTTPOutTransport");
            throw new MessageEncodingException(
                    "Invalid outbound message transport type, this encoder only support HTTPOutTransport");
        }

        SAMLMessageContext<SAMLObject, Response, NameIdentifier> artifactContext = (SAMLMessageContext) messageContext;
        HTTPOutTransport outTransport = (HTTPOutTransport) artifactContext.getOutboundMessageTransport();

        URLBuilder urlBuilder = getEndpointURL(artifactContext);

        List<Pair<String, String>> params = urlBuilder.getQueryParams();

        params.add(new Pair<String, String>("TARGET", artifactContext.getRelayState()));

        SAML1ArtifactBuilder artifactBuilder;
        if (artifactContext.getOutboundMessageArtifactType() != null) {
            artifactBuilder = Configuration.getSAML1ArtifactBuilderFactory().getArtifactBuilder(
                    artifactContext.getOutboundMessageArtifactType());
        } else {
            artifactBuilder = Configuration.getSAML1ArtifactBuilderFactory().getArtifactBuilder(defaultArtifactType);
            artifactContext.setOutboundMessageArtifactType(defaultArtifactType);
        }

        AbstractSAML1Artifact artifact;
        String artifactString;
        for (Assertion assertion : artifactContext.getOutboundSAMLMessage().getAssertions()) {
            artifact = artifactBuilder.buildArtifact(artifactContext, assertion);
            if(artifact == null){
                log.error("Unable to build artifact for message to relying party");
                throw new MessageEncodingException("Unable to builder artifact for message to relying party");
            }

            try {
                artifactMap.put(artifact.base64Encode(), messageContext.getInboundMessageIssuer(), messageContext
                        .getOutboundMessageIssuer(), assertion);
            } catch (MarshallingException e) {
                log.error("Unable to marshall assertion to be represented as an artifact", e);
                throw new MessageEncodingException("Unable to marshall assertion to be represented as an artifact", e);
            }
            artifactString = artifact.base64Encode();
            params.add(new Pair<String, String>("SAMLart", artifactString));
        }

        String redirectUrl = urlBuilder.buildURL();

        log.debug("Sending redirect to URL {} to relying party {}", redirectUrl, artifactContext
                .getInboundMessageIssuer());
        outTransport.sendRedirect(urlBuilder.buildURL());
    }
}