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

/**
 * Encodes a message onto the outbound transport.
 * 
 * Message encoders <strong>MUST</strong> must be thread safe and stateless.
 */
public interface MessageEncoder {

    /**
     * Encodes the message in the binding specific manner.
     * 
     * @param messageContext current message context
     * 
     * @throws MessageEncodingException thrown if the problem can not be encoded
     */
    public void encode(MessageContext messageContext) throws MessageEncodingException;

    /**
     * Indicates whether this encoder, given the current message context, provides end-to-end message confidentiality.
     * 
     * @param messageContext the current message context
     * 
     * @return true if the encoder provides end-to-end message confidentiality, false if not
     * 
     * @throws MessageEncodingException thrown if the encoder encounter an error while attempt to evaluate its ability
     *             to provide message confidentiality.
     */
    public boolean providesMessageConfidentiality(MessageContext messageContext) throws MessageEncodingException;

    /**
     * Indicates whether this encoder, given the current message context, provides end-to-end message integrity.
     * 
     * @param messageContext the current message context
     * 
     * @return true if the encoder provides end-to-end message integrity, false if not
     * 
     * @throws MessageEncodingException thrown if the encoder encounter an error while attempt to evaluate its ability
     *             to provide message integrity.
     */
    public boolean providesMessageIntegrity(MessageContext messageContext) throws MessageEncodingException;
}