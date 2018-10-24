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

package org.opensaml.ws.soap.soap11.encoder;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.message.handler.BaseHandlerChainAwareMessageEncoder;
import org.opensaml.ws.soap.common.SOAPObjectBuilder;
import org.opensaml.ws.soap.soap11.Body;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.ws.transport.OutTransport;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Basic SOAP 1.1 encoder.
 */
public class SOAP11Encoder extends BaseHandlerChainAwareMessageEncoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(SOAP11Encoder.class);
    
    /** SOAP Envelope builder. */
    private SOAPObjectBuilder<Envelope> envBuilder;
    
    /** SOAP Body builder. */
    private SOAPObjectBuilder<Body> bodyBuilder;
    

    /** Constructor. */
    @SuppressWarnings("unchecked")
    public SOAP11Encoder() {
        super();
        XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
        envBuilder = (SOAPObjectBuilder<Envelope>) builderFactory.getBuilder(Envelope.DEFAULT_ELEMENT_NAME);
        bodyBuilder = (SOAPObjectBuilder<Body>) builderFactory.getBuilder(Body.DEFAULT_ELEMENT_NAME);
    }
    
    /** {@inheritDoc} */
    public boolean providesMessageConfidentiality(MessageContext messageContext) throws MessageEncodingException {
        return messageContext.getOutboundMessageTransport().isConfidential();
    }

    /** {@inheritDoc} */
    public boolean providesMessageIntegrity(MessageContext messageContext) throws MessageEncodingException {
        return messageContext.getOutboundMessageTransport().isIntegrityProtected();
    }
    
    /** {@inheritDoc} */
    protected void prepareMessageContext(MessageContext messageContext) throws MessageEncodingException {
        if (messageContext.getOutboundMessage() == null) {
            messageContext.setOutboundMessage(buildSOAPEnvelope(messageContext));
        }
    }
    
    /** {@inheritDoc} */
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
     * Perform any processing or fixup on the message context's outbound transport, prior to encoding the actual
     * message.
     * 
     * <p>
     * The default implementation does nothing. Subclasses should override to implement transport-specific 
     * behavior.
     * </p>
     * 
     * @param messageContext the current message context being processed
     * 
     * @throws MessageEncodingException thrown if there is a problem preprocessing the transport
     */
    protected void preprocessTransport(MessageContext messageContext) throws MessageEncodingException {
    }

    /**
     * Builds the SOAP envelope and body skeleton to be encoded.
     * 
     * @param messageContext the message context being processed
     * 
     * @return the minimal SOAP message envelope skeleton
     */
    protected Envelope buildSOAPEnvelope(MessageContext messageContext) {
        log.debug("Building SOAP envelope");

        Envelope envelope = envBuilder.buildObject();

        Body body = bodyBuilder.buildObject();
        envelope.setBody(body);

        return envelope;
    }
 
}