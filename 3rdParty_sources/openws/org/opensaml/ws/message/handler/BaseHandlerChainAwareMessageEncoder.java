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

package org.opensaml.ws.message.handler;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.encoder.BaseMessageEncoder;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for message encoders which are capable of processing the message context's outbound {@link HandlerChain}.
 */
public abstract class BaseHandlerChainAwareMessageEncoder extends BaseMessageEncoder implements HandlerChainAware {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(BaseHandlerChainAwareMessageEncoder.class);

    /** {@inheritDoc} */
    protected void doEncode(MessageContext messageContext) throws MessageEncodingException {
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
    protected abstract void prepareMessageContext(MessageContext messageContext) throws MessageEncodingException;
    
    /**
     * Encode the message context to the transport.
     * 
     * @param messageContext the message context to process
     *  @throws MessageEncodingException thrown if there is a problem encoding the message context
     *              to the transport
     */
    protected abstract void encodeToTransport(MessageContext messageContext) throws MessageEncodingException;

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
