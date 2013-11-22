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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.message.handler.HandlerChain;
import org.opensaml.ws.message.handler.HandlerChainAware;
import org.opensaml.ws.message.handler.HandlerChainResolver;
import org.opensaml.ws.message.handler.HandlerException;
import org.opensaml.ws.soap.common.SOAPObjectBuilder;
import org.opensaml.ws.soap.soap11.Body;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.ws.transport.OutTransport;
import org.opensaml.ws.transport.http.HTTPOutTransport;
import org.opensaml.ws.transport.http.HTTPTransportUtils;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * 
 */
public class HandlerChainAwareHTTPSOAP11Encoder extends BaseSAML2MessageEncoder implements HandlerChainAware {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HandlerChainAwareHTTPSOAP11Encoder.class);
    
    /** SOAP Envelope builder. */
    private SOAPObjectBuilder<Envelope> envBuilder;
    
    /** SOAP Body builder. */
    private SOAPObjectBuilder<Body> bodyBuilder;

    /** Overrides binding confidentiality. */
    private boolean notConfidential = false;
    
    /** Constructor. */
    public HandlerChainAwareHTTPSOAP11Encoder() {
        super();
        XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
        envBuilder = (SOAPObjectBuilder<Envelope>) builderFactory.getBuilder(Envelope.DEFAULT_ELEMENT_NAME);
        bodyBuilder = (SOAPObjectBuilder<Body>) builderFactory.getBuilder(Body.DEFAULT_ELEMENT_NAME);
    }
    
    /**
     * Returns confidentiality override flag.
     * @return true iff the encoder cannot provide confidentiality
     */
    public boolean isNotConfidential() {
        return notConfidential;
    }
    
    /**
     * Sets confidentiality override flag.
     * @param flag  override flag
     */
    public void setNotConfidential(boolean flag) {
        notConfidential = flag;
    }
    
    /** {@inheritDoc} */
    public String getBindingURI() {
        return SAMLConstants.SAML2_SOAP11_BINDING_URI;
    }
    
    /** {@inheritDoc} */
    public boolean providesMessageConfidentiality(MessageContext messageContext) throws MessageEncodingException {
        if (notConfidential) {
            return false;
        }
        return messageContext.getOutboundMessageTransport().isConfidential();
    }

    /** {@inheritDoc} */
    public boolean providesMessageIntegrity(MessageContext messageContext) throws MessageEncodingException {
        return messageContext.getOutboundMessageTransport().isIntegrityProtected();
    }
    
    // TODO: The rest of the methods here are copied from BaseHandlerChainAwareMessageDecoder and
    // the SOAP subclasses on that "branch", and should drop out once the SAML encoders are aligned
    // to that base class.

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

        prepareMessageContext(messageContext);
        
        processOutboundHandlerChain(messageContext);
        
        encodeToTransport(messageContext);
    }
    
    /**
     * Perform final binding-specific processing of message context and prepare it for encoding
     * to the transport.  
     * 
     * <p>
     * This should include constructing and populating all binding-specific structure and data that needs to be
     * reflected by the message context's properties.
     * </p>
     * 
     * <p>
     * This method is called prior to {@link #processOutboundHandlerChain(MessageContext)}.
     * </p>
     * 
     * @param messageContext the message context to process
     * @throws MessageEncodingException thrown if there is a problem preparing the message context
     *              for encoding
     */
    protected void prepareMessageContext(MessageContext messageContext) throws MessageEncodingException {
        SAMLMessageContext samlMsgCtx = (SAMLMessageContext) messageContext;

        SAMLObject samlMessage = samlMsgCtx.getOutboundSAMLMessage();
        if (samlMessage == null) {
            throw new MessageEncodingException("No outbound SAML message contained in message context");
        }

        signMessage(samlMsgCtx);

        log.debug("Building SOAP envelope");

        Envelope envelope = envBuilder.buildObject();
        Body body = bodyBuilder.buildObject();
        envelope.setBody(body);
        body.getUnknownXMLObjects().add(samlMessage);

        messageContext.setOutboundMessage(envelope);
    }
    
    /**
     * Encode the message context to the transport.
     * 
     * @param messageContext the message context to process
     *  @throws MessageEncodingException thrown if there is a problem encoding the message context
     *              to the transport
     */
    protected void encodeToTransport(MessageContext messageContext) throws MessageEncodingException {
        Element envelopeElem = marshallMessage(messageContext.getOutboundMessage());
        
        preprocessTransport(messageContext);
        
        try {
            OutTransport outTransport = messageContext.getOutboundMessageTransport();
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
     * <p>
     * This implementation performs the following actions on the context's {@link HTTPOutTransport}:
     * <ol>
     *   <li>Adds the HTTP header: "Cache-control: no-cache, no-store"</li>
     *   <li>Adds the HTTP header: "Pragma: no-cache"</li>
     *   <li>Sets the character encoding to: "UTF-8"</li>
     *   <li>Sets the content type to: "text/xml"</li>
     *   <li>Sets the SOAPAction HTTP header</li>
     * </ol>
     * </p>
     * 
     * @param messageContext the current message context being processed
     * 
     * @throws MessageEncodingException thrown if there is a problem preprocessing the transport
     */
    protected void preprocessTransport(MessageContext messageContext) throws MessageEncodingException {
        HTTPOutTransport outTransport = (HTTPOutTransport) messageContext.getOutboundMessageTransport();
        HTTPTransportUtils.addNoCacheHeaders(outTransport);
        HTTPTransportUtils.setUTF8Encoding(outTransport);
        HTTPTransportUtils.setContentType(outTransport, "text/xml");
        outTransport.setHeader("SOAPAction", "http://www.oasis-open.org/committees/security");
    }
 
    /**
     * Process the outbound {@link HandlerChain} for the message context, if any.
     * 
     * @param messageContext the message context to process
     * @throws MessageEncodingException thrown if a handler indicates a problem handling the message
     */
    protected void processOutboundHandlerChain(MessageContext messageContext) throws MessageEncodingException {
        HandlerChainResolver outboundHandlerChainResolver = messageContext.getOutboundHandlerChainResolver();
        if (outboundHandlerChainResolver != null) {
            log.debug("Invoking outbound handler chain on message context");
            try {
                for (HandlerChain outboundHandlerChain : outboundHandlerChainResolver.resolve(messageContext)) {
                    if (outboundHandlerChain != null) {
                        invokeHandlerChain(outboundHandlerChain, messageContext);
                    }
                }
            } catch (HandlerException e) {
                log.error("Encountered HandlerException when encoding message: {}", e.getMessage());
                throw new MessageEncodingException("Handler exception while encoding message", e);
            }
        }
    }
    
    /**
     * Invoke a handler chain on the specified message context.
     * 
     * @param handlerChain the handle chain to invoke
     * @param messageContext the message context to process
     * 
     * @throws HandlerException if handler chain encountered a problem handling the message context
     */
    protected void invokeHandlerChain(HandlerChain handlerChain, MessageContext messageContext)
            throws HandlerException {
        if (handlerChain != null && messageContext != null) {
            handlerChain.invoke(messageContext);
        }
    }
}
