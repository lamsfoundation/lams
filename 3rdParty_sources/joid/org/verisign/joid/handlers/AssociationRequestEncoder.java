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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.verisign.joid.AssociationRequest;
import org.verisign.joid.OpenIdConstants;
import org.verisign.joid.OpenIdException;


/**
 * An encoder for AssociationRequests.
 *
 * @author <a href="mailto:akarasulu@apache.org">Alex Karasulu</a>
 */
public final class AssociationRequestEncoder extends MessageEncoder<AssociationRequest>
{
    private final static Logger LOG = LoggerFactory.getLogger( AssociationRequestEncoder.class );
    
    
    public final StringBuilder encode( AssociationRequest message, EncodingMode mode, StringBuilder sb ) throws OpenIdException
    {
        if ( sb == null )
        {
            sb = new StringBuilder();
        }
        else 
        {
            if ( sb.charAt( sb.length() - 1 ) != mode.getNewLine() )
            {
                sb.append( mode.getNewLine() );
            }
        }
        
        super.encode( message, mode, sb );
        
        // append the session type
        sb.append( OpenIdConstants.OPENID_SESSION_TYPE );
        sb.append( mode.getKvDelim() );
        sb.append( message.getSessionType().toString() );        
        sb.append( mode.getNewLine() );

        // append the association type
        sb.append( OpenIdConstants.OPENID_ASSOCIATION_TYPE );
        sb.append( mode.getKvDelim() );
        sb.append( message.getAssociationType().toString() );        
        sb.append( mode.getNewLine() );

        // append the consumer's DH public key as a string
        sb.append( OpenIdConstants.OPENID_DH_CONSUMER_PUBLIC );
        sb.append( mode.getKvDelim() );
        if ( mode == EncodingMode.URL_STRING )
        {
            sb.append( message.getDhConsumerPublicString() );        
        }
        else
        {
            try
            {
                sb.append( URLEncoder.encode( message.getDhConsumerPublicString(), "UTF-8" ) );
            }
            catch ( UnsupportedEncodingException e )
            {
                String msg = "Failed to URL encode the " + OpenIdConstants.OPENID_DH_CONSUMER_PUBLIC
                    + " key's value of '" + message.getDhConsumerPublicString() + "'";
                LOG.error( msg, e );
                throw new OpenIdException( msg, e );
            }
        }
        
        return sb;
    }
}
