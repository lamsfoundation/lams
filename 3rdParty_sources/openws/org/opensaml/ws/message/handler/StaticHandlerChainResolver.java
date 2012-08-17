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

import java.util.Collections;
import java.util.List;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.xml.util.LazyList;

/** A simple handler chain resolver implementation that returns a static list of handler chains. */
public class StaticHandlerChainResolver implements HandlerChainResolver {
    
    /** Registered handler chains. */
    private List<HandlerChain> handlerChains;

    /**
     * Constructor.
     * 
     * @param newHandlerChain the static handler chain returned by this resolver
     */
    public StaticHandlerChainResolver(HandlerChain newHandlerChain) {
        handlerChains = new LazyList<HandlerChain>();
        if(newHandlerChain != null){
            handlerChains.add(newHandlerChain);
        }
    }
    
    /**
     * Constructor.
     * 
     * @param newHandlerChains the static list of handler chains returned by this resolver
     */
    public StaticHandlerChainResolver(List<HandlerChain> newHandlerChains) {
        handlerChains = new LazyList<HandlerChain>();
        if(newHandlerChains != null){
            handlerChains.addAll(newHandlerChains);
        }
    }

    /** {@inheritDoc} */
    public Iterable<HandlerChain> resolve(MessageContext messageContext) throws HandlerException {
        return Collections.unmodifiableList(handlerChains);
    }

}
