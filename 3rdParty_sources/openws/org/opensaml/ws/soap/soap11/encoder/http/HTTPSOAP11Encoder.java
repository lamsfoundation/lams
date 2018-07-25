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

package org.opensaml.ws.soap.soap11.encoder.http;

import java.util.List;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.ws.soap.soap11.Header;
import org.opensaml.ws.soap.soap11.encoder.SOAP11Encoder;
import org.opensaml.ws.transport.http.HTTPOutTransport;
import org.opensaml.ws.transport.http.HTTPTransportUtils;
import org.opensaml.ws.wsaddressing.Action;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic SOAP 1.1 encoder for HTTP transport.
 */
public class HTTPSOAP11Encoder extends SOAP11Encoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPSOAP11Encoder.class);
    

    /** Constructor. */
    public HTTPSOAP11Encoder() {
        super();
    }

    /** {@inheritDoc} */
    protected void doEncode(MessageContext messageContext) throws MessageEncodingException {
        if (!(messageContext.getOutboundMessageTransport() instanceof HTTPOutTransport)) {
            log.error("Invalid outbound message transport type, this encoder only support HTTPOutTransport");
            throw new MessageEncodingException(
                    "Invalid outbound message transport type, this encoder only support HTTPOutTransport");
        }
        
        super.doEncode(messageContext);
    }
    
    /**
     * <p>
     * This implementation performs the following actions on the context's {@link HTTPOutTransport}:
     * <ol>
     *   <li>Adds the HTTP header: "Cache-control: no-cache, no-store"</li>
     *   <li>Adds the HTTP header: "Pragma: no-cache"</li>
     *   <li>Sets the character encoding to: "UTF-8"</li>
     *   <li>Sets the content type to: "text/xml"</li>
     *   <li>Sets the SOAPAction HTTP header the value returned by {@link #getSOAPAction(MessageContext)}, if
     *   that returns non-null.</li>
     * </ol>
     * </p>
     * 
     * <p>
     * Subclasses should NOT set the SOAPAction HTTP header in this method. Instead, they should override 
     * the method {@link #getSOAPAction(MessageContext)}.
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
        
        String soapAction = getSOAPAction(messageContext);
        if (soapAction != null) {
            outTransport.setHeader("SOAPAction", soapAction);
        } else {
            outTransport.setHeader("SOAPAction", "");
        }
    }
 
    /**
     * Determine the value of the SOAPAction HTTP header to send.
     * 
     * <p>
     * The default behavior is to return the value of the SOAP Envelope's WS-Addressing Action header,
     * if present.
     * </p>
     * 
     * @param messageContext the current message context being processed
     * @return a SOAPAction HTTP header URI value
     */
    protected String getSOAPAction(MessageContext messageContext) {
        Envelope env = (Envelope) messageContext.getOutboundMessage();
        Header header = env.getHeader();
        if (header == null) {
            return null;
        }
        List<XMLObject> objList = header.getUnknownXMLObjects(Action.ELEMENT_NAME);
        if (objList == null || objList.isEmpty()) {
            return null;
        } else {
            return ((Action)objList.get(0)).getValue();
        }
    }
    
}