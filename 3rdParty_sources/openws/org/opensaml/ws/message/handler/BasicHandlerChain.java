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
import org.opensaml.xml.util.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A basic implementation of {@link HandlerChain}.
 */
public class BasicHandlerChain implements HandlerChain {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(BasicHandlerChain.class);
    
    /** The handler chain. */
    private List<Handler> handlers;

    /** Constructor. */
    public BasicHandlerChain() {
        handlers = new LazyList<Handler>();
    }

    /** {@inheritDoc} */
    public List<Handler> getHandlers() {
        return handlers;
    }

    /** {@inheritDoc} */
    public void invoke(MessageContext msgContext) throws HandlerException {
        log.trace("Invoking handler chain");
        for (Handler handler : getHandlers()) {
            log.trace("Invoking handler: {}", handler.getClass().getName());
            handler.invoke(msgContext);
        }
    }

}
