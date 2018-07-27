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

package org.opensaml.ws.soap.client;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.soap.client.SOAPClient.SOAPRequestParameters;

/** Message context for SOAP messages. */
public interface SOAPMessageContext extends MessageContext{

    /**
     * Gets a set of binding/transport-specific request parameters.
     * 
     * @return set of binding/transport-specific request parameters
     */
    public SOAPRequestParameters getSOAPRequestParameters();
    
    /**
     * Sets a set of binding/transport-specific request parameters.
     * 
     * @param parameters a set of binding/transport-specific request parameters
     */
    public void setSOAPRequestParameters(SOAPRequestParameters parameters);
}