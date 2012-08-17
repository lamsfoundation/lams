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


/**
 * An enumeration for managing AssociationTypes. 
 *
 * @author <a href="mailto:akarasulu@apache.org">akarasulu at apache.org</a>
 */
public enum AssociationType
{
    HMAC_SHA1( "HMAC-SHA1" ), HMAC_SHA256( "HMAC-SHA256" );

 
    private final String name;
    
    
    private AssociationType( String name )
    {
        this.name = name;
    }
    
    
    public String toString()
    {
        return name;
    }
    
    
    public String getName()
    {
        return name;
    }

    
    public boolean equalsName( String name )
    {
        return name.equalsIgnoreCase( name );
    }
    
    
    public static AssociationType parse( String name )
    {
        if ( name.equalsIgnoreCase( HMAC_SHA1.toString() ) )
        {
            return HMAC_SHA1;
        }
        
        
        if ( name.equalsIgnoreCase( HMAC_SHA256.toString() ) )
        {
            return HMAC_SHA256;
        }
        
        throw new IllegalArgumentException( "The string '" + name + "' does not match any association type." );
    }
}
