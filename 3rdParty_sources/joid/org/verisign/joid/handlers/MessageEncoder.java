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
import org.verisign.joid.Message;
import org.verisign.joid.OpenIdConstants;
import org.verisign.joid.OpenIdException;


/**
 * An encoder for general messages adding their common attributes to the query 
 * string.
 *
 * @author <a href="mailto:birkan.duman@gmail.com">Birkan Duman</a>
 * @author <a href="mailto:akarasulu@apache.org">Alex Karasulu</a>
 */
public class MessageEncoder<E extends Message> implements Encoder<E>
{
    private static final Logger LOG = LoggerFactory.getLogger( MessageEncoder.class );
    
    
    /**
     * {@inheritDoc}
     * @throws OpenIdException 
     */
    public StringBuilder encode( E message, EncodingMode mode, StringBuilder sb ) throws OpenIdException
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

        // append the openid.mode key value pair
        sb.append( OpenIdConstants.OPENID_MODE ).append( mode.getKvDelim() ).append( message.getMode().toString() );        
        sb.append( mode.getNewLine() );
        
        // append the openid.ns key value pair
        sb.append( OpenIdConstants.OPENID_NS ).append( mode.getKvDelim() );
        if ( mode == EncodingMode.POST_STRING )
        {
            sb.append( message.getNamespace() );
        }
        else
        {
            if ( message.isVersion2() )
            {
                sb.append( OpenIdConstants.ENCODED_NS_VERSION2 );
            }
            else
            {
                try
                {
                    sb.append( URLEncoder.encode( message.getNamespace(), "UTF-8" ) );
                }
                catch ( UnsupportedEncodingException e )
                {
                    LOG.error( "This error to decode pre-set URL should never fail.", e );
                }
            }
        }
        
        return sb;
    }
}
