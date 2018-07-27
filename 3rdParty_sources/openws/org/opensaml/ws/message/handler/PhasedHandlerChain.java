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
import java.util.Map;

/**
 * A specialized type of {@link HandlerChain} which supports organizing multiple
 * handler chains into a set of named handler chains called 'phases', which will be
 * invoked in a specified order.
 */
public interface PhasedHandlerChain extends HandlerChain {
    
    /**
     * Modifiable map of phase names to corresponding handler chains.
     * 
     * @return the map of phase names to handler chains
     */
    public Map<String, HandlerChain> getPhaseChains();
    
    /**
     * Get the order of phase invocation.  Handler chains will be invoked in the order
     * determined by this list.
     * 
     * @return the ordered list of phase names
     */
    public List<String> getPhaseOrder();
    
    /**
     * Set the order of phase invocation.  Handler chains will be invoked in the order
     * determined by this list.
     * 
     * @param newPhaseOrder a list of phase names
     */
    public void setPhaseOrder(List<String> newPhaseOrder);
    
    /**
     * Get the complete effective list of ordered handlers in the handler chain.
     * 
     * <p>
     * Note that unlike {@link HandlerChain}, the returned list is <b>NOT</b>modifiable.
     * {@link Handler} instances in the effective chain should be added and removed via
     * membership in the appropriate handler chain phase, obtained via {@link #getPhaseChains()}.
     * </p>
     * 
     * @return list of handlers
     */
    public List<Handler> getHandlers();

}
