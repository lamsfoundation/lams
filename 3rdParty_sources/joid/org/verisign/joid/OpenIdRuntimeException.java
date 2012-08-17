package org.verisign.joid;


/**
 * For throwing RuntimeExceptions from joid.
 *
 * User: treeder
 * Date: Dec 20, 2007
 * Time: 12:41:14 PM
 */
public class OpenIdRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = 9171669934119626199L;

    
    public OpenIdRuntimeException( String s )
    {
        super( s );
    }

    
    public OpenIdRuntimeException( String s, Throwable t )
    {
        super( s, t );
    }
}
