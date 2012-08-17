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


import java.io.IOException;
import java.util.Map;
import java.util.Set;


/**
 * Produces requests from incoming queries.
 */
public class ResponseFactory
{
    private ResponseFactory()
    {
    }


    /**
     * Parses a query into a response.
     *
     * @param query the query to parse.
     * @return the parsed response.
     * @throws OpenIdException if the query cannot be parsed into a known
     *  response.
     */
    public static Response parse( String query ) throws OpenIdException
    {
        Map<String,String> map;
        try
        {
            if ( MessageParser.numberOfNewlines( query ) == 1 )
            {
                map = MessageParser.urlEncodedToMap( query );
            }
            else
            {
                map = MessageParser.postedToMap( query );
            }
        }
        catch ( IOException e )
        {
            throw new OpenIdException( "Error parsing " + query + ": "
                      + e.toString() );
        }

        Set<String> set = map.keySet();
        if ( ( set.contains( AssociationResponse.OPENID_SESSION_TYPE ) &&
            set.contains( AssociationResponse.OPENID_ENC_MAC_KEY ) ) ||
            set.contains( AssociationResponse.OPENID_ASSOCIATION_TYPE ) )
        {
            return new AssociationResponse( map );
        }
        else if ( set.contains( AuthenticationResponse.OPENID_SIG ) )
        {
            return new AuthenticationResponse( map );
        }
        else if ( set.contains( CheckAuthenticationResponse.OPENID_IS_VALID ) )
        {
            return new CheckAuthenticationResponse( map );
        }
        else
        {
            throw new OpenIdException( "Cannot parse response from " + query );
        }
    }

}
