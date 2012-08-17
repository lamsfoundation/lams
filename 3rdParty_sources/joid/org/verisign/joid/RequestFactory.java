//
// (C) Copyright 2007 VeriSign, Inc.  All Rights Reserved.
//
// VeriSign, Inc. shall have no responsibility, financial or
// otherwise, for any consequences arising out of the use of
// this material. The program material is provided on an "AS IS"
// BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied.
//
// Distributed under an Apache License
// http://www.apache.org/licenses/LICENSE-2.0
//
package org.verisign.joid;


import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Produces requests from incoming queries.
 */
public class RequestFactory
{
    public static String OPENID_MODE = "openid.mode";


    /**
     * Parses a query into a request.
     *
     * @param query the query to parse.
     * @return the parsed request.
     * @throws OpenIdException if the query cannot be parsed into a known
     *  request.
     * @throws InvalidOpenIdQueryException 
     */
    public static Request parse( String query )
        throws UnsupportedEncodingException, OpenIdException, InvalidOpenIdQueryException
    {
        Map<String,String> map;
        
        try
        {
            map = parseQuery( query );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new OpenIdException( "Error parsing " + query + ": " + e.toString() );
        }

        Mode mode = Mode.parse( map.get( OPENID_MODE ) );
        
        if ( Mode.ASSOCIATE == mode )
        {
            return new AssociationRequest( map, Mode.ASSOCIATE );
        }
        else if ( Mode.CHECKID_IMMEDIATE == mode || Mode.CHECKID_SETUP == mode ) 
        {
            return new AuthenticationRequest( map, mode );
        }
        else if ( Mode.CHECK_AUTHENTICATION == mode )
        {
            return new CheckAuthenticationRequest( map, Mode.CHECK_AUTHENTICATION );
        }
        else
        {
            throw new InvalidOpenIdQueryException( new StringBuilder( "Cannot parse request from " ).append( query ).toString() );
        }
    }


    /**
     * Parses a query into a map. 
     *
     * @param query the query to parse.
     * @return the parsed request.
     * @throws UnsupportedEncodingException if the string is not properly 
     *  UTF-8 encoded.
     */
    public static Map<String,String> parseQuery( String query ) throws UnsupportedEncodingException
    {
        return MessageParser.urlEncodedToMap( query );
    }
}
