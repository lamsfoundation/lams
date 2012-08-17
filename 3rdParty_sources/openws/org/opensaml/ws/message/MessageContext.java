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

package org.opensaml.ws.message;

import org.opensaml.ws.message.handler.HandlerChainResolver;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.ws.transport.InTransport;
import org.opensaml.ws.transport.OutTransport;
import org.opensaml.xml.XMLObject;

/**
 * A message context represents the entire context for a given message through the receive, process, and/or response
 * phases. It is a basic unit of work within the library.
 * 
 * Message contexts are <strong>NOT</strong> thread safe.
 */
public interface MessageContext {

    /**
     * Gets the unique id of the communication profile in use.
     * 
     * @return unique id of the communication profile in use
     */
    public String getCommunicationProfileId();

    /**
     * Gets the inbound message.
     * 
     * @return the inbound message
     */
    public XMLObject getInboundMessage();

    /**
     * Gets the issuer of the inbound message.
     * 
     * @return issuer of the inbound message
     */
    public String getInboundMessageIssuer();

    /**
     * Gets the transport used to receive the message.
     * 
     * @return transport used to receive the message
     */
    public InTransport getInboundMessageTransport();

    /**
     * Gets the outbound message.
     * 
     * @return the outbound message
     */
    public XMLObject getOutboundMessage();

    /**
     * Gets the issuer of the outbound message.
     * 
     * @return issuer of the outbound message
     */
    public String getOutboundMessageIssuer();

    /**
     * Gets the transport used to respond to the message.
     * 
     * @return transport used to respond to the message
     */
    public OutTransport getOutboundMessageTransport();

    /**
     * Gets the resolver used to determine active SecurityPolicy.
     * 
     * @return resolver used to determine active SecurityPolicy
     */
    public SecurityPolicyResolver getSecurityPolicyResolver();

    /**
     * Gets whether the issuer of the inbound message represented by this context has been authenticated.
     * What it means for the message issuer to be authenticate will vary by use and 
     * employed authentication mechanisms.
     * 
     * @return whether the issuer of the inbound message represented by this context has been authenticated
     */
    public boolean isIssuerAuthenticated();

    /**
     * Sets the unique id of the communication profile in use.
     * 
     * @param id unique id of the communication profile in use
     */
    public void setCommunicationProfileId(String id);

    /**
     * Sets the inbound message.
     * 
     * @param message the inbound message
     */
    public void setInboundMessage(XMLObject message);

    /**
     * Sets the issuer of the inbound message.
     * 
     * @param issuer issuer of the inbound message
     */
    public void setInboundMessageIssuer(String issuer);

    /**
     * Sets the transport used to used to receive the message.
     * 
     * @param transport the transport used to receive the message
     */
    public void setInboundMessageTransport(InTransport transport);

    /**
     * Sets the outbound message.
     * 
     * @param message the outbound message
     */
    public void setOutboundMessage(XMLObject message);

    /**
     * Sets the issuer of the outbound message.
     * 
     * @param issuer issuer of the outbound message
     */
    public void setOutboundMessageIssuer(String issuer);

    /**
     * Sets the transport used to respond to the message.
     * 
     * @param transport the transport used to respond to the message
     */
    public void setOutboundMessageTransport(OutTransport transport);

    /**
     * Sets the resolver used to determine active SecurityPolicy.
     * 
     * @param resolver resolver used to determine active SecurityPolicy
     */
    public void setSecurityPolicyResolver(SecurityPolicyResolver resolver);
    
    /**
     * Get the pre-SecurityPolicy inbound handler chain resolver.
     * 
     * @return the pre-security inbound handler chain resolver.
     */
    public HandlerChainResolver getPreSecurityInboundHandlerChainResolver();
    
    /**
     * Set the pre-SecurityPolicy inbound handler chain resolver.
     * 
     * @param newHandlerChainResolver the new pre-SecurityPolicy inbound handler chain.
     */
    public void setPreSecurityInboundHandlerChainResolver(HandlerChainResolver newHandlerChainResolver);
    
    /**
     * Get the post-SecurityPolicy inbound handler chain resolver.
     * 
     * @return the pre-SecurityPolicy inbound handler chain resolver.
     */
    public HandlerChainResolver getPostSecurityInboundHandlerChainResolver();
    
    /**
     * Set the post-SecurityPolicy inbound handler chain resolver.
     * 
     * @param newHandlerChainResolver the new post-SecurityPolicy inbound handler chain resolver.
     */
    public void setPostSecurityInboundHandlerChainResolver(HandlerChainResolver newHandlerChainResolver);
    
    /**
     * Get the outbound handler chain resolver.
     * 
     * @return the outbound handler chain resolver.
     */
    public HandlerChainResolver getOutboundHandlerChainResolver();
    
    /**
     * Set the outbound handler chain resolver.
     * 
     * @param newHandlerChainResolver the new outbound handler chain resolver.
     */
    public void setOutboundHandlerChainResolver(HandlerChainResolver newHandlerChainResolver);
}