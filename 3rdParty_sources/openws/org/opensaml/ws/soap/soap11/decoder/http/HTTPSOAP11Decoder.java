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

package org.opensaml.ws.soap.soap11.decoder.http;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.soap.soap11.decoder.SOAP11Decoder;
import org.opensaml.ws.transport.http.HTTPInTransport;
import org.opensaml.xml.parse.ParserPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic SOAP 1.1 over decoder for HTTP transport.
 */
public class HTTPSOAP11Decoder extends SOAP11Decoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPSOAP11Decoder.class);
    
    /** Constructor. */
    public HTTPSOAP11Decoder() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param pool parser pool used to deserialize messages
     */
    public HTTPSOAP11Decoder(ParserPool pool) {
        super(pool);
    }

    /** {@inheritDoc} */
    protected void doDecode(MessageContext messageContext) throws MessageDecodingException {
        
        if (!(messageContext.getInboundMessageTransport() instanceof HTTPInTransport)) {
            log.error("Invalid inbound message transport type, this decoder only support HTTPInTransport");
            throw new MessageDecodingException(
                    "Invalid inbound message transport type, this decoder only support HTTPInTransport");
        }

        HTTPInTransport inTransport = (HTTPInTransport) messageContext.getInboundMessageTransport();
        if (!inTransport.getHTTPMethod().equalsIgnoreCase("POST")) {
            throw new MessageDecodingException("This message decoder only supports the HTTP POST method");
        }
        
        super.doDecode(messageContext);

    }

}