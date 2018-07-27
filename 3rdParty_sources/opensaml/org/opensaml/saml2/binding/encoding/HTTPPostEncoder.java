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

package org.opensaml.saml2.binding.encoding;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.saml2.core.StatusResponseType;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.transport.http.HTTPOutTransport;
import org.opensaml.ws.transport.http.HTTPTransportUtils;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.XMLHelper;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SAML 2.0 HTTP Post binding message encoder.
 */
public class HTTPPostEncoder extends BaseSAML2MessageEncoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPPostEncoder.class);

    /** Velocity engine used to evaluate the template when performing POST encoding. */
    private VelocityEngine velocityEngine;

    /** ID of the Velocity template used when performing POST encoding. */
    private String velocityTemplateId;

    /**
     * Constructor.
     * 
     * @param engine Velocity engine instance used to create POST body
     * @param templateId ID of the template used to create POST body
     */
    public HTTPPostEncoder(VelocityEngine engine, String templateId) {
        super();
        velocityEngine = engine;
        velocityTemplateId = templateId;
    }

    /** {@inheritDoc} */
    public String getBindingURI() {
        return SAMLConstants.SAML2_POST_BINDING_URI;
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

        SAMLMessageContext samlMsgCtx = (SAMLMessageContext) messageContext;

        SAMLObject outboundMessage = samlMsgCtx.getOutboundSAMLMessage();
        if (outboundMessage == null) {
            throw new MessageEncodingException("No outbound SAML message contained in message context");
        }
        String endpointURL = getEndpointURL(samlMsgCtx).buildURL();

        if (samlMsgCtx.getOutboundSAMLMessage() instanceof StatusResponseType) {
            ((StatusResponseType) samlMsgCtx.getOutboundSAMLMessage()).setDestination(endpointURL);
        }

        signMessage(samlMsgCtx);
        samlMsgCtx.setOutboundMessage(outboundMessage);

        postEncode(samlMsgCtx, endpointURL);
    }

    /**
     * Base64 and POST encodes the outbound message and writes it to the outbound transport.
     * 
     * @param messageContext current message context
     * @param endpointURL endpoint URL to which to encode message
     * 
     * @throws MessageEncodingException thrown if there is a problem encoding the message
     */
    protected void postEncode(SAMLMessageContext messageContext, String endpointURL) throws MessageEncodingException {
        log.debug("Invoking Velocity template to create POST body");
        try {
            VelocityContext context = new VelocityContext();

            populateVelocityContext(context, messageContext, endpointURL);

            HTTPOutTransport outTransport = (HTTPOutTransport) messageContext.getOutboundMessageTransport();
            HTTPTransportUtils.addNoCacheHeaders(outTransport);
            HTTPTransportUtils.setUTF8Encoding(outTransport);
            HTTPTransportUtils.setContentType(outTransport, "text/html");

            Writer out = new OutputStreamWriter(outTransport.getOutgoingStream(), "UTF-8");
            velocityEngine.mergeTemplate(velocityTemplateId, "UTF-8", context, out);
            out.flush();
        } catch (Exception e) {
            log.error("Error invoking Velocity template", e);
            throw new MessageEncodingException("Error creating output document", e);
        }
    }

    /**
     * Populate the Velocity context instance which will be used to render the POST body.
     * 
     * @param velocityContext the Velocity context instance to populate with data
     * @param messageContext the SAML message context source of data
     * @param endpointURL endpoint URL to which to encode message
     * @throws MessageEncodingException thrown if there is a problem encoding the message
     */
    protected void populateVelocityContext(VelocityContext velocityContext, SAMLMessageContext messageContext,
            String endpointURL) throws MessageEncodingException {
        
        Encoder esapiEncoder = ESAPI.encoder();

        String encodedEndpointURL = esapiEncoder.encodeForHTMLAttribute(endpointURL);
        log.debug("Encoding action url of '{}' with encoded value '{}'", endpointURL, encodedEndpointURL);
        velocityContext.put("action", encodedEndpointURL);
        velocityContext.put("binding", getBindingURI());

        log.debug("Marshalling and Base64 encoding SAML message");
        if (messageContext.getOutboundSAMLMessage().getDOM() == null) {
            marshallMessage(messageContext.getOutboundSAMLMessage());
        }
        try {
            String messageXML = XMLHelper.nodeToString(messageContext.getOutboundSAMLMessage().getDOM());
            String encodedMessage = Base64.encodeBytes(messageXML.getBytes("UTF-8"), Base64.DONT_BREAK_LINES);
            if (messageContext.getOutboundSAMLMessage() instanceof RequestAbstractType) {
                velocityContext.put("SAMLRequest", encodedMessage);
            } else if (messageContext.getOutboundSAMLMessage() instanceof StatusResponseType) {
                velocityContext.put("SAMLResponse", encodedMessage);
            } else {
                throw new MessageEncodingException(
                        "SAML message is neither a SAML RequestAbstractType or StatusResponseType");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("UTF-8 encoding is not supported, this VM is not Java compliant.");
            throw new MessageEncodingException("Unable to encode message, UTF-8 encoding is not supported");
        }

        String relayState = messageContext.getRelayState();
        if (checkRelayState(relayState)) {
            String encodedRelayState = esapiEncoder.encodeForHTMLAttribute(relayState);
            log.debug("Setting RelayState parameter to: '{}', encoded as '{}'", relayState, encodedRelayState);
            velocityContext.put("RelayState", encodedRelayState);
        }
    }
}