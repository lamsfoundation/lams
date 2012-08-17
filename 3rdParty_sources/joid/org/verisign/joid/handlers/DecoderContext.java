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


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.verisign.joid.Message;
import org.verisign.joid.MessageParser;
import org.verisign.joid.OpenIdException;


/**
 * A context used to store information while decoding.
 *
 * @author <a href="mailto:akarasulu@apache.org">Alex Karasulu</a>
 */
public class DecoderContext<E extends Message>
{
    private static final Logger LOG = LoggerFactory.getLogger( DecoderContext.class );
    
    private final EncodingMode mode;
    private final String query;

    private E message;
    private Map<String,String> map;

    
    public DecoderContext( String query, EncodingMode mode ) throws OpenIdException
    {
        this.query = query;
        this.mode = mode;
        
        if ( mode == EncodingMode.URL_STRING )
        {
            try
            {
                this.map = MessageParser.urlEncodedToMap( query );
            }
            catch ( UnsupportedEncodingException e )
            {
                String msg = "Could not parse query string: '" + query + "'";
                LOG.error( msg, e );
                throw new OpenIdException( msg, e );
            }
        }
        else
        {
            try
            {
                this.map = MessageParser.postedToMap( query );
            }
            catch ( IOException e )
            {
                String msg = "Could not parse query string: '" + query + "'";
                LOG.error( msg, e );
                throw new OpenIdException( msg, e );
            }
        }
    }
    
    
    public EncodingMode getEncodingMode()
    {
        return mode;
    }
    
    
    public E getMessage()
    {
        return message;
    }
    
    
    public void setMessage( E message )
    {
        this.message = message;
    }
    

    public String getQuery()
    {
        return query;
    }
    
    
    public Map<String, String> getMap()
    {
        return map;
    }
}
