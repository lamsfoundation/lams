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

package org.opensaml.saml1.binding.decoding;

import java.util.List;

import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.binding.artifact.SAMLArtifactMap;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.transport.http.HTTPInTransport;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SAML 1.X HTTP Artifact message decoder.
 * 
 * <strong>NOTE: This decoder is not yet implemented.</strong>
 */
public class HTTPArtifactDecoder extends BaseSAML1MessageDecoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPArtifactDecoder.class);

    /**
     * Constructor.
     * 
     * @param map used to map artifacts to SAML
     * @param pool parser pool used to deserialize messages
     */
    public HTTPArtifactDecoder(SAMLArtifactMap map, ParserPool pool) {
        super(map, pool);
    }

    /** {@inheritDoc} */
    public String getBindingURI() {
        return SAMLConstants.SAML1_ARTIFACT_BINDING_URI;
    }

    /** {@inheritDoc} */
    protected void doDecode(MessageContext messageContext) throws MessageDecodingException {
        if (!(messageContext instanceof SAMLMessageContext)) {
            log.error("Invalid message context type, this decoder only support SAMLMessageContext");
            throw new MessageDecodingException(
                    "Invalid message context type, this decoder only support SAMLMessageContext");
        }

        if (!(messageContext.getInboundMessageTransport() instanceof HTTPInTransport)) {
            log.error("Invalid inbound message transport type, this decoder only support HTTPInTransport");
            throw new MessageDecodingException(
                    "Invalid inbound message transport type, this decoder only support HTTPInTransport");
        }

        SAMLMessageContext samlMsgCtx = (SAMLMessageContext) messageContext;
        
        decodeTarget(samlMsgCtx);
        processArtifacts(samlMsgCtx);

        populateMessageContext(samlMsgCtx);
    }

    /**
     * Decodes the TARGET parameter and adds it to the message context.
     * 
     * @param samlMsgCtx current message context
     * 
     * @throws MessageDecodingException thrown if there is a problem decoding the TARGET parameter.
     */
    protected void decodeTarget(SAMLMessageContext samlMsgCtx) throws MessageDecodingException {
        HTTPInTransport inTransport = (HTTPInTransport) samlMsgCtx.getInboundMessageTransport();

        String target = DatatypeHelper.safeTrim(inTransport.getParameterValue("TARGET"));
        if (target == null) {
            log.error("URL TARGET parameter was missing or did not contain a value.");
            throw new MessageDecodingException("URL TARGET parameter was missing or did not contain a value.");
        }
        samlMsgCtx.setRelayState(target);
    }

    /**
     * Process the incoming artifacts by decoding the artifacts, dereferencing them from the artifact source and 
     * storing the resulting response (with assertions) in the message context.
     * 
     * @param samlMsgCtx current message context
     * 
     * @throws MessageDecodingException thrown if there is a problem decoding or dereferencing the artifacts
     */
    protected void processArtifacts(SAMLMessageContext samlMsgCtx) throws MessageDecodingException {
        HTTPInTransport inTransport = (HTTPInTransport) samlMsgCtx.getInboundMessageTransport();
        List<String> encodedArtifacts = inTransport.getParameterValues("SAMLart");
        if (encodedArtifacts == null || encodedArtifacts.size() == 0) {
            log.error("URL SAMLart parameter was missing or did not contain a value.");
            throw new MessageDecodingException("URL SAMLart parameter was missing or did not contain a value.");
        }
        
        // TODO decode artifact(s); resolve issuer resolution endpoint; dereference using 
        // Request/AssertionArtifact(s) over synchronous backchannel binding;
        // store response as the inbound SAML message.
    }

    /** {@inheritDoc} */
    protected boolean isIntendedDestinationEndpointURIRequired(SAMLMessageContext samlMsgCtx) {
        return false;
    }
    
    /** {@inheritDoc} */
    protected String getIntendedDestinationEndpointURI(SAMLMessageContext samlMsgCtx) throws MessageDecodingException {
        // Not relevant in this binding/profile, there is neither SAML message
        // nor binding parameter with this information
        return null;
    }
}