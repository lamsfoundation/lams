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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.opensaml.xml.util.LazyList;
import org.opensaml.xml.util.LazyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A basic implementation of {@link PhasedHandlerChain}.
 */
public class BasicPhasedHandlerChain extends BasicHandlerChain implements PhasedHandlerChain {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(BasicPhasedHandlerChain.class);
    
    /** The ordered list of phases to invoke. */
    private List<String> phaseOrder;
    
    /** Map of phases to corresponding Handler chain. */
    private Map<String, HandlerChain> phaseChains;

    /** Constructor. */
    public BasicPhasedHandlerChain() {
        super();
        phaseOrder = new LazyList<String>();
        phaseChains = new LazyMap<String, HandlerChain>();
    }

    /** {@inheritDoc} */
    public List<Handler> getHandlers() {
        ArrayList<Handler> handlers = new ArrayList<Handler>();
        for (String phaseName : getPhaseOrder()) {
            HandlerChain phase = getPhaseChains().get(phaseName);
            if (phase != null) {
                List<Handler> phaseHandlers = phase.getHandlers();
                if (!phaseHandlers.isEmpty()) {
                    handlers.addAll(phaseHandlers);
                } else {
                    log.info("Specified phase name '{}' exists in PhasedHandlerChain, but contains no handlers",
                        phaseName);
                }
            } else {
                log.warn("Specified phase name '{}' does not exist in PhasedHandlerChain: {}",
                        phaseName, getPhaseChains().keySet());
            }
        }
        return Collections.unmodifiableList(handlers);
    }

    /** {@inheritDoc} */
    public Map<String, HandlerChain> getPhaseChains() {
        return phaseChains;
    }

    /** {@inheritDoc} */
    public List<String> getPhaseOrder() {
        return phaseOrder;
    }

    /** {@inheritDoc} */
    public void setPhaseOrder(List<String> newPhaseOrder) {
        phaseOrder = newPhaseOrder;
    }

}
