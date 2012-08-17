package org.verisign.joid.consumer;


/**
 * User: treeder
 * Date: Jun 20, 2007
 * Time: 11:23:55 PM
 */
public class AuthenticationException extends RuntimeException
{
    private static final long serialVersionUID = 2537052042761934028L;

    public AuthenticationException( String s )
    {
        super( s );
    }
}
