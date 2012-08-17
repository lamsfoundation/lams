package org.verisign.joid.consumer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.verisign.joid.OpenIdException;
import org.verisign.joid.Request;
import org.verisign.joid.Response;
import org.verisign.joid.ResponseFactory;


public class Util
{
    public static Response send( Request req, String dest )
        throws IOException, OpenIdException
    {
        String toSend = req.toUrlString();
        StringBuffer b = new StringBuffer();

        BufferedReader in = null;
        try
        {
            // TODO: See reply/patch of Sergey: http://groups.google.com/group/joid-dev/browse_thread/thread/962cf46501ea660d

            URL url = new URL( dest + "?" + toSend );
            HttpURLConnection.setFollowRedirects( true );
            HttpURLConnection connection = ( HttpURLConnection ) url.openConnection();

            in = new BufferedReader( new InputStreamReader( connection
                              .getInputStream() ) );
            String str;
            int lines = 0;
            while ( ( str = in.readLine() ) != null )
            {
                b.append( str );
                b.append( '\n' );
                lines += 1;
            }
            if ( lines == 1 )
            {
                // query string
                b.deleteCharAt( b.length() - 1 );
            }
        }
        finally
        {
            if ( in != null )
                in.close();
        }
        return ResponseFactory.parse( b.toString() );
    }

}
