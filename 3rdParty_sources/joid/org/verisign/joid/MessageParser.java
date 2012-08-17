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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 * Parses an OpenID message. 
 */
public class MessageParser
{
    private final static Log LOG = LogFactory.getLog( MessageParser.class );
    static char newline = '\n';


    /**
     * Unrolls a message as a string. This string will use the
     * <code>name:value</code> format of the specification. See also
     * {@link #toUrlString()}.
     *
     * @return the message as a string.
     */
    static String toPostString( Message message ) throws OpenIdException
    {
        return toStringDelimitedBy( message, ":", newline );
    }


    /**
     * Unrolls a message as a string. This string will use encoding
     * suitable for URLs. See also {@link #toPostString()}.
     *
     * @return the message as a string.
     */
    static String toUrlString( Message message ) throws OpenIdException
    {
        return toStringDelimitedBy( message, "=", '&' );
    }


    private static String toStringDelimitedBy( Message message,
                          String kvDelim, char lineDelim ) throws OpenIdException
    {
        Map<String,String> map = message.toMap();
        Set<Map.Entry<String, String>> set = map.entrySet();
        StringBuffer sb = new StringBuffer();
        try
        {
            for ( Iterator<Map.Entry<String, String>> iter = set.iterator(); iter.hasNext(); )
            {
                Map.Entry<String,String> mapEntry = iter.next();
                String key = mapEntry.getKey();
                String value = mapEntry.getValue();

                if ( lineDelim == newline )
                {
                    sb.append( key + kvDelim + value );
                    sb.append( lineDelim );
                }
                else
                {
                    if ( value != null )
                    {
                        sb.append( URLEncoder.encode( key, "UTF-8" ) + kvDelim
                            + URLEncoder.encode( value, "UTF-8" ) );
                        if ( iter.hasNext() )
                        {
                            sb.append( lineDelim );
                        }
                    }
                    else
                    {
                        StringBuilder msg = new StringBuilder( "Value for key '" );
                        msg.append( key ).append( "' is null in message map" );
                        LOG.error( msg );
                        throw new OpenIdException( msg.toString() );
                    }
                }

            }
            return sb.toString();
        }
        catch ( UnsupportedEncodingException e )
        {
            // should not happen
            throw new RuntimeException( "Internal error" );
        }
    }


    static int numberOfNewlines( String query ) throws IOException
    {
        BufferedReader br = new BufferedReader( new StringReader( query ) );
        int n = 0;
        while ( br.readLine() != null )
        {
            n += 1;
        }
        //log.warn ("number of lines="+n+" for "+query);
        return n;
    }


    /**
     * Translate a query string to a Map. Duplicate values are
     * overwritten, so don't use this routine for general query string
     * parsing.
     *
     * TODO: Made public only for unit tests. Do not use.
     */
    public static Map<String,String> urlEncodedToMap( String query )
        throws UnsupportedEncodingException
    {
        Map<String,String> map = new HashMap<String,String>();
        if ( query == null )
        {
            return map;
        }
        String[] parts = query.split( "[&;]" );
        for ( int i = 0; i < parts.length; i++ )
        {
            String[] nameval = parts[i].split( "=" );
            String name = URLDecoder.decode( nameval[0], "UTF-8" );
            String value = null;
            if ( nameval.length > 1 )
            {
                value = URLDecoder.decode( nameval[1], "UTF-8" );
            }
            map.put( name, value );
        }
        return map;
    }


    public static Map<String,String> postedToMap( String query ) throws IOException
    {
        Map<String,String> map = new HashMap<String, String>();
        if ( query == null )
        {
            return map;
        }
        BufferedReader br = new BufferedReader( new StringReader( query ) );
        String s = br.readLine();
        while ( s != null )
        {
            int index = s.indexOf( ":" );
            if ( index != -1 )
            {
                String name = s.substring( 0, index );
                String value = s.substring( index + 1, s.length() );
                if ( name != null && value != null )
                {
                    map.put( name, value );
                }
            }
            s = br.readLine();
        }
        return map;
    }
}
