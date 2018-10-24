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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.opensaml.Configuration;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.soap.common.SOAPObjectBuilder;
import org.opensaml.ws.soap.soap11.Body;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.ws.transport.http.HTTPOutTransport;
import org.opensaml.ws.transport.http.HTTPTransportUtils;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * SAML 1.X HTTP SOAP 1.1 binding message encoder.
 */
public class HTTPSOAP11Encoder extends BaseSAML1MessageEncoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPSOAP11Encoder.class);

    /** Constructor. */
    public HTTPSOAP11Encoder() {
        super();
    }

    /** {@inheritDoc} */
    public String getBindingURI() {
        return SAMLConstants.SAML1_SOAP11_BINDING_URI;
    }

    /** {@inheritDoc} */
    public boolean providesMessageConfidentiality(MessageContext messageContext) throws MessageEncodingException {
        if (messageContext.getOutboundMessageTransport().isConfidential()) {
            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    public boolean providesMessageIntegrity(MessageContext messageContext) throws MessageEncodingException {
        if (messageContext.getOutboundMessageTransport().isIntegrityProtected()) {
            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    protected void doEncode(MessageContext messageContext) throws MessageEncodingException {
        validateMessageContent(messageContext);
        SAMLMessageContext samlMsgCtx = (SAMLMessageContext) messageContext;

        SAMLObject samlMessage = samlMsgCtx.getOutboundSAMLMessage();
        if (samlMessage == null) {
            throw new MessageEncodingException("No outbound SAML message contained in message context");
        }

        signMessage(samlMsgCtx);
        Envelope envelope = buildSOAPMessage(samlMsgCtx, samlMessage);

        Element envelopeElem = marshallMessage(envelope);
        try {
            HTTPOutTransport outTransport = (HTTPOutTransport) messageContext.getOutboundMessageTransport();
            HTTPTransportUtils.addNoCacheHeaders(outTransport);
            HTTPTransportUtils.setUTF8Encoding(outTransport);
            HTTPTransportUtils.setContentType(outTransport, "text/xml");
            outTransport.setHeader("SOAPAction", "http://www.oasis-open.org/committees/security");

            Writer out = new OutputStreamWriter(outTransport.getOutgoingStream(), "UTF-8");
            XMLHelper.writeNode(envelopeElem, out);
            out.flush();
        } catch (UnsupportedEncodingException e) {
            log.error("JVM does not support required UTF-8 encoding");
            throw new MessageEncodingException("JVM does not support required UTF-8 encoding");
        } catch (IOException e) {
            log.error("Unable to write message content to outbound stream", e);
            throw new MessageEncodingException("Unable to write message content to outbound stream", e);
        }
    }

    /**
     * Builds the SOAP message to be encoded.
     * 
     * <p>If {@link SAMLMessageContext#getOutboundMessage()} contains
     * a pre-existing SOAP envelope, it will be used.  Any existing body content will be removed
     * before the SAML protocol message is added.
     * </p>
     * 
     * <p>
     * Otherwise, a new Envelope will be constructed.
     * </p>
     * 
     * @param samlMsgCtx the SAML message context
     * @param samlMessage body of the SOAP message
     * 
     * @return the SOAP message
     */
    @SuppressWarnings("unchecked")
    protected Envelope buildSOAPMessage(SAMLMessageContext samlMsgCtx, SAMLObject samlMessage) {
        Envelope envelope = null;
        if (samlMsgCtx.getOutboundMessage() != null && samlMsgCtx.getOutboundMessage() instanceof Envelope) {
            envelope = (Envelope) samlMsgCtx.getOutboundMessage();
            Body body = envelope.getBody();
            if (body == null) {
                XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
                SOAPObjectBuilder<Body> bodyBuilder = (SOAPObjectBuilder<Body>) builderFactory
                        .getBuilder(Body.DEFAULT_ELEMENT_NAME);
                body = bodyBuilder.buildObject();
                envelope.setBody(body);
            } else if (!body.getUnknownXMLObjects().isEmpty()) {
                log.warn("Supplied SOAP Envelope Body was not empty. Existing contents will be removed.");
                body.getUnknownXMLObjects().clear();
            }
            body.getUnknownXMLObjects().add(samlMessage);
        } else {
            envelope = buildSOAPMessage(samlMessage);
            samlMsgCtx.setOutboundMessage(envelope);
        }
        return envelope;
    }
    
    /**
     * Builds the SOAP message to be encoded.
     * 
     * @param samlMessage body of the SOAP message
     * 
     * @return the SOAP message
     */
    @SuppressWarnings("unchecked")
    protected Envelope buildSOAPMessage(SAMLObject samlMessage) {
        log.debug("Building SOAP message");
        XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

        SOAPObjectBuilder<Envelope> envBuilder = (SOAPObjectBuilder<Envelope>) builderFactory
                .getBuilder(Envelope.DEFAULT_ELEMENT_NAME);
        Envelope envelope = envBuilder.buildObject();

        log.debug("Adding SAML message to the SOAP message's body");
        SOAPObjectBuilder<Body> bodyBuilder = (SOAPObjectBuilder<Body>) builderFactory
                .getBuilder(Body.DEFAULT_ELEMENT_NAME);
        Body body = bodyBuilder.buildObject();
        body.getUnknownXMLObjects().add(samlMessage);
        envelope.setBody(body);

        return envelope;
    }

    /**
     * Validates that the message context is a {@link SAMLMessageContext} and that its outbound transport is HTTP.
     * 
     * @param messageContext current message context
     * 
     * @throws MessageEncodingException thrown if the message context conditions are not met
     */
    protected void validateMessageContent(MessageContext messageContext) throws MessageEncodingException {
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
    }
}