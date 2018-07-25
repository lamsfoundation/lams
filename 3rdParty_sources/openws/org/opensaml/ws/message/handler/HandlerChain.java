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

import java.util.List;

import org.opensaml.ws.message.MessageContext;

/**
 * An ordered chain of {@link Handler} instances which may be invoked on a message context.
 */
public interface HandlerChain {
    
    /**
     * Invoke the handler chain on the specified message context.
     * 
     * @param msgContext the message context on which to invoke the handler chain
     * @throws HandlerException if there is a problem handling the message context
     */
    public void invoke(MessageContext msgContext) throws HandlerException;
    
    
    /**
     * Get the modifiable list of handlers in the handler chain.
     * 
     * @return list of handlers
     */
    public List<Handler> getHandlers();

}
