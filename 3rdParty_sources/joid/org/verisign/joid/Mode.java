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
package org.verisign.joid;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * TODO Mode.
 *
 * @author <a href="mailto:akarasulu@apache.org">Alex Karasulu</a>
 */
public enum Mode
{
    ERROR( "error" ), 
    ID_RES( "id_res" ),
    CANCEL( "cancel" ),
    ASSOCIATE( "associate"),
    SETUP_NEEDED( "setup_needed" ),
    CHECKID_IMMEDIATE( "checkid_immediate" ), 
    CHECK_AUTHENTICATION( "check_authentication" ),
    CHECKID_SETUP( "checkid_setup" );
    
    
    private final static Map<String, Mode> MAP;
    
    
    static
    {
        Map<String, Mode> m = new HashMap<String, Mode>();
        m.put( ERROR.getValue(), ERROR );
        m.put( ID_RES.getValue(), ID_RES );
        m.put( CANCEL.getValue(), CANCEL );
        m.put( ASSOCIATE.getValue(), ASSOCIATE );
        m.put( SETUP_NEEDED.getValue(), SETUP_NEEDED );
        m.put( CHECKID_IMMEDIATE.getValue(), CHECKID_IMMEDIATE );
        m.put( CHECK_AUTHENTICATION.getValue(), CHECK_AUTHENTICATION );
        m.put( CHECKID_SETUP.getValue(), CHECKID_SETUP );
        
        MAP = Collections.unmodifiableMap( m );
    }
    
    
    public static Mode parse( String value )
    {
        if ( MAP.containsKey( value ) )
        {
            return MAP.get( value );
        }
        
        String lowercase = value.toLowerCase();
        if ( MAP.containsKey( lowercase ) )
        {
            return MAP.get( lowercase );
        }
        
        throw new IllegalArgumentException( "'" + value + "' does not correspond to a value Mode value" );
    }
    
    
    private final String value;
    
    
    private Mode( String value )
    {
        this.value = value;
    }
    
    
    public String toString()
    {
        return value;
    }
    
    
    public String getValue()
    {
        return value;
    }
}
