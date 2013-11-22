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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml1.core.ResponseAbstractType;
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
 * SAML 1.X HTTP POST message encoder.
 */
public class HTTPPostEncoder extends BaseSAML1MessageEncoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPPostEncoder.class);

    /** Velocity engine used to evaluate the template when performing POST encoding. */
    private VelocityEngine velocityEngine;

    /** ID of the velocity template used when performing POST encoding. */
    private String velocityTemplateId;

    /**
     * Constructor.
     * 
     * @param engine velocity engine instance used to create POST body
     * @param templateId ID of the template used to create POST body
     */
    public HTTPPostEncoder(VelocityEngine engine, String templateId) {
        super();
        velocityEngine = engine;
        velocityTemplateId = templateId;
    }

    /** {@inheritDoc} */
    public String getBindingURI() {
        return SAMLConstants.SAML1_POST_BINDING_URI;
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

        if (samlMsgCtx.getOutboundSAMLMessage() instanceof ResponseAbstractType) {
            ((ResponseAbstractType) samlMsgCtx.getOutboundSAMLMessage()).setRecipient(endpointURL);
        }

        signMessage(samlMsgCtx);
        samlMsgCtx.setOutboundMessage(outboundMessage);

        postEncode(samlMsgCtx, endpointURL);
    }

    /**
     * Base64 and POST encodes the outbound message and writes it to the outbound transport.
     * 
     * @param messageContext current message context
     * @param endpointURL endpoint URL to encode message to
     * 
     * @throws MessageEncodingException thrown if there is a problem encoding the message
     */
    protected void postEncode(SAMLMessageContext messageContext, String endpointURL) throws MessageEncodingException {
        log.debug("Invoking velocity template to create POST body");

        try {
            VelocityContext context = new VelocityContext();
            Encoder esapiEncoder = ESAPI.encoder();

            String encodedEndpointURL = esapiEncoder.encodeForHTMLAttribute(endpointURL);
            log.debug("Encoding action url of '{}' with encoded value '{}'", endpointURL, encodedEndpointURL);
            context.put("action", encodedEndpointURL);
            context.put("binding", getBindingURI());

            log.debug("Marshalling and Base64 encoding SAML message");
            String messageXML = XMLHelper.nodeToString(marshallMessage(messageContext.getOutboundSAMLMessage()));
            String encodedMessage = Base64.encodeBytes(messageXML.getBytes("UTF-8"), Base64.DONT_BREAK_LINES);
            context.put("SAMLResponse", encodedMessage);

            if (messageContext.getRelayState() != null) {
                String encodedRelayState = esapiEncoder.encodeForHTMLAttribute(messageContext.getRelayState());
                log.debug("Setting TARGET parameter to: '{}', encoded as '{}'", messageContext.getRelayState(), encodedRelayState);
                context.put("TARGET", encodedRelayState);
            }

            HTTPOutTransport outTransport = (HTTPOutTransport) messageContext.getOutboundMessageTransport();
            HTTPTransportUtils.addNoCacheHeaders(outTransport);
            HTTPTransportUtils.setUTF8Encoding(outTransport);
            HTTPTransportUtils.setContentType(outTransport, "text/html");

            OutputStream transportOutStream = outTransport.getOutgoingStream();
            Writer out = new OutputStreamWriter(transportOutStream, "UTF-8");
            velocityEngine.mergeTemplate(velocityTemplateId, "UTF-8", context, out);
            out.flush();
        }catch(UnsupportedEncodingException e){
            log.error("UTF-8 encoding is not supported, this VM is not Java compliant.");
            throw new MessageEncodingException("Unable to encode message, UTF-8 encoding is not supported");
        } catch (Exception e) {
            log.error("Error invoking velocity template", e);
            throw new MessageEncodingException("Error creating output document", e);
        }
    }
}