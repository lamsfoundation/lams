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

import org.verisign.joid.AssociationRequest;
import org.verisign.joid.AssociationType;
import org.verisign.joid.Crypto;
import org.verisign.joid.OpenIdConstants;
import org.verisign.joid.SessionType;

/**
 * TODO AssociationRequestDecoder.
 *
 * @author <a href="mailto:birkan.duman@gmail.com">Birkan Duman</a>
 */
public class AssociationRequestDecoder<E extends AssociationRequest> extends MessageDecoder<AssociationRequest>
{
    @Override
    public void decode( DecoderContext<AssociationRequest> context )
    {
        super.decode( context );
        
        AssociationRequest message = context.getMessage();
        
        message.setSessionType( SessionType.parse( context.getMap().get( OpenIdConstants.OPENID_SESSION_TYPE ) ) );
        message.setAssociationType( AssociationType.parse( context.getMap().get( OpenIdConstants.OPENID_ASSOCIATION_TYPE ) ) );
        message.setDhModulus( Crypto.convertToBigIntegerFromString( context.getMap().get( OpenIdConstants.OPENID_DH_MODULUS ) ) );
        message.setDhGenerator( Crypto.convertToBigIntegerFromString( context.getMap().get( OpenIdConstants.OPENID_DH_GENERATOR ) ) );
        message.setDhConsumerPublic( Crypto.convertToBigIntegerFromString( context.getMap().get( OpenIdConstants.OPENID_DH_CONSUMER_PUBLIC ) ) );
    }
}
