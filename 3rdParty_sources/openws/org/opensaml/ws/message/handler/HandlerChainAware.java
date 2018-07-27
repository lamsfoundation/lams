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
 * A marker interface for {@link MessageDecoder} and {@link MessageEncoder} implementations
 * which process a message context's {@link Handler}'s.
 * 
 * <p>
 * Specifically, it identifies those implementations which process the handlers produced by a message context's
 * {@link MessageContext#getPreSecurityInboundHandlerChainResolver()} and 
 * {@link MessageContext#getPostSecurityInboundHandlerChainResolver()},
 * and {@link MessageContext#getOutboundHandlerChainResolver()}, for decoders and encoders respectively.
 * </p>
 */
public interface HandlerChainAware {

}
