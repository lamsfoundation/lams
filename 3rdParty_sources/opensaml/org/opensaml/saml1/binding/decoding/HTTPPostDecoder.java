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

import java.io.ByteArrayInputStream;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.binding.artifact.SAMLArtifactMap;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml1.core.ResponseAbstractType;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.transport.http.HTTPInTransport;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SAML 1.X HTTP POST message decoder.
 */
public class HTTPPostDecoder extends BaseSAML1MessageDecoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPPostDecoder.class);

    /** Constructor. */
    public HTTPPostDecoder() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param pool parser pool used to deserialize messages
     */
    public HTTPPostDecoder(ParserPool pool) {
        super(pool);
    }

    /**
     * Constructor.
     * 
     * @param map Artifact to SAML map
     * 
     * @deprecated
     */
    public HTTPPostDecoder(SAMLArtifactMap map) {
        super(map);
    }

    /**
     * Constructor.
     * 
     * @param map used to map artifacts to SAML
     * @param pool parser pool used to deserialize messages
     * 
     * @deprecated
     */
    public HTTPPostDecoder(SAMLArtifactMap map, ParserPool pool) {
        super(map, pool);
    }

    /** {@inheritDoc} */
    public String getBindingURI() {
        return SAMLConstants.SAML1_POST_BINDING_URI;
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

        HTTPInTransport inTransport = (HTTPInTransport) samlMsgCtx.getInboundMessageTransport();
        if (!inTransport.getHTTPMethod().equalsIgnoreCase("POST")) {
            throw new MessageDecodingException("This message decoder only supports the HTTP POST method");
        }

        String relayState = inTransport.getParameterValue("TARGET");
        samlMsgCtx.setRelayState(relayState);
        log.debug("Decoded SAML relay state (TARGET parameter) of: {}", relayState);

        String base64Message = inTransport.getParameterValue("SAMLResponse");
        byte[] decodedBytes = Base64.decode(base64Message);
        if (decodedBytes == null) {
            log.error("Unable to Base64 decode SAML message");
            throw new MessageDecodingException("Unable to Base64 decode SAML message");
        }

        SAMLObject inboundMessage = (SAMLObject) unmarshallMessage(new ByteArrayInputStream(decodedBytes));
        samlMsgCtx.setInboundMessage(inboundMessage);
        samlMsgCtx.setInboundSAMLMessage(inboundMessage);
        log.debug("Decoded SAML message");

        populateMessageContext(samlMsgCtx);
    }

    /** {@inheritDoc} */
    protected boolean isIntendedDestinationEndpointURIRequired(SAMLMessageContext samlMsgCtx) {
        return samlMsgCtx.getInboundSAMLMessage() instanceof ResponseAbstractType;
    }
}