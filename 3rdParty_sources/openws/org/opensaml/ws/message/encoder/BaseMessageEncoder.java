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

package org.opensaml.ws.message.encoder;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Base class for message decoders.
 */
public abstract class BaseMessageEncoder implements MessageEncoder {
    
    /** Used to log protocol messages. */
    private Logger protocolMessageLog = LoggerFactory.getLogger("PROTOCOL_MESSAGE");

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(BaseMessageEncoder.class);

    /** Constructor. */
    public BaseMessageEncoder() {

    }

    /** {@inheritDoc} */
    public void encode(MessageContext messageContext) throws MessageEncodingException {
        log.debug("Beginning encode message to outbound transport of type: {}", messageContext
                .getOutboundMessageTransport().getClass().getName());

        doEncode(messageContext);

        logEncodedMessage(messageContext);
        
        log.debug("Successfully encoded message.");
    }

    /**
     * Log the encoded message to the protocol message logger.
     * 
     * @param messageContext the message context to process
     */
    protected void logEncodedMessage(MessageContext messageContext) {
        if(protocolMessageLog.isDebugEnabled() && messageContext.getOutboundMessage() != null){
            if (messageContext.getOutboundMessage().getDOM() == null) {
                try {
                    marshallMessage(messageContext.getOutboundMessage());
                } catch (MessageEncodingException e) {
                    log.error("Unable to marshall message for logging purposes: " + e.getMessage());
                    return;
                }
            }
            protocolMessageLog.debug("\n" + XMLHelper.prettyPrintXML(messageContext.getOutboundMessage().getDOM()));
        }
    }

    /**
     * Encodes the outbound message onto the outbound transport.
     * 
     * @param messageContext current message context
     * 
     * @throws MessageEncodingException thrown if there is a problem encoding the message
     */
    protected abstract void doEncode(MessageContext messageContext) throws MessageEncodingException;

    /**
     * Helper method that marshalls the given message.
     * 
     * @param message message the marshall and serialize
     * 
     * @return marshalled message
     * 
     * @throws MessageEncodingException thrown if the give message can not be marshalled into its DOM representation
     */
    protected Element marshallMessage(XMLObject message) throws MessageEncodingException {
        log.debug("Marshalling message");

        try {
            Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(message);
            if (marshaller == null) {
                log.error("Unable to marshall message, no marshaller registered for message object: "
                        + message.getElementQName());
                throw new MessageEncodingException(
                        "Unable to marshall message, no marshaller registered for message object: "
                        + message.getElementQName());
            }
            Element messageElem = marshaller.marshall(message);
            if (log.isTraceEnabled()) {
                log.trace("Marshalled message into DOM:\n{}", XMLHelper.nodeToString(messageElem));
            }
            return messageElem;
        } catch (MarshallingException e) {
            log.error("Encountered error marshalling message to its DOM representation", e);
            throw new MessageEncodingException("Encountered error marshalling message into its DOM representation", e);
        }
    }
}