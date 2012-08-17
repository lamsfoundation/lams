/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.verisign.joid.handlers;


import org.verisign.joid.Message;
import org.verisign.joid.Mode;
import org.verisign.joid.OpenIdConstants;


/**
 * TODO MessageDecoder.
 *
 * @author <a href="mailto:akarasulu@apache.org">Alex Karasulu</a>
 */
public class MessageDecoder<E extends Message> implements Decoder<E>
{
    public void decode( DecoderContext<E> context )
    {
        E message = context.getMessage();
        
        message.setMode( Mode.parse( context.getMap().get( OpenIdConstants.OPENID_MODE ) ) );
        message.setNamespace( context.getMap().get( OpenIdConstants.OPENID_NS ) );
    }
}
