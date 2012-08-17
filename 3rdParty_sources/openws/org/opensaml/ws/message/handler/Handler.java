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
import org.opensaml.ws.message.decoder.MessageDecoder;
import org.opensaml.ws.message.encoder.MessageEncoder;

/**
 * A handler is invoked to implement specific business logic on a {@link MessageContext}.
 * 
 * <p>
 * It is most commonly used to invoke logic within {@link MessageEncoder}'s and {@link MessageDecoder}'s,
 * but could also be used outside of that framework.
 * </p>
 * 
 */
public interface Handler {
    
    /**
     * Invoke the handler on the specified message context.
     * 
     * @param msgContext the message context on which to invoke the handler
     * @throws HandlerException if there is a problem handling the message context
     */
    public void invoke(MessageContext msgContext) throws HandlerException;

}
