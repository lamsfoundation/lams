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

package org.opensaml.ws.soap.soap11.decoder ;

import java.util.List;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.message.handler.BaseHandlerChainAwareMessageDecoder;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.ws.soap.soap11.Header;
import org.opensaml.ws.transport.InTransport;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic SOAP 1.1 decoder.
 */
public class SOAP11Decoder extends BaseHandlerChainAwareMessageDecoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(SOAP11Decoder.class);
    
    /** Constructor. */
    public SOAP11Decoder() {
        super();
    }
    
    /**
     * Constructor.
     * 
     * @param pool parser pool used to deserialize messages
     */
    public SOAP11Decoder(ParserPool pool) {
        super(pool);
    }

    /** {@inheritDoc} */
    protected void doDecode(MessageContext messageContext) throws MessageDecodingException {

        InTransport inTransport = messageContext.getInboundMessageTransport();

        log.debug("Unmarshalling SOAP message");
        Envelope soapMessage = (Envelope) unmarshallMessage(inTransport.getIncomingStream());
        messageContext.setInboundMessage(soapMessage);
    }
    
    
    /** {@inheritDoc} */
    public void decode(MessageContext messageContext) throws MessageDecodingException, SecurityException {
            super.decode(messageContext);
        
            // TODO enable once header checking support is completed
            // checkUnderstoodSOAPHeaders(messageContext);
    }
    
    /**
     * Check that all headers which carry the <code>soap11:mustUnderstand</code> attribute
     * and which are targeted to this SOAP node via the <code>soap11:actor</code> were understood by the
     * decoder.
     * 
     * @param messageContext the message context being processed
     * 
     * @throws MessageDecodingException thrown if a SOAP header requires understanding by 
     *              this node but was not understood
     */
    private void checkUnderstoodSOAPHeaders(MessageContext messageContext)
            throws MessageDecodingException {
        
        Envelope envelope = (Envelope) messageContext.getInboundMessage();
        Header soapHeader = envelope.getHeader();
        if (soapHeader == null) {
            log.debug("SOAP Envelope contained no Header");
            return;
        }
        List<XMLObject> headers = soapHeader.getUnknownXMLObjects();
        if (headers == null || headers.isEmpty()) {
            log.debug("SOAP Envelope header list was either null or empty");
            return;
        }

        for (XMLObject header : headers) {
            //TODO
        }
    }

}