package org.verisign.joid.consumer;


import org.verisign.joid.AuthenticationResponse;


/**
 * This class is returned by JoidConsumer.authenticate() and is just a holder for
 * the results.
 *
 * User: treeder
 * Date: Aug 13, 2007
 * Time: 11:09:36 PM
 */
public class AuthenticationResult
{
    private boolean successful;
    private String identity;
    private AuthenticationResponse response;


    public AuthenticationResult( String identity, AuthenticationResponse response )
    {
        this.identity = identity;
        this.response = response;
        if ( identity != null )
        {
            successful = true;
        }
    }


    public AuthenticationResponse getResponse()
    {
        return response;
    }


    public void setResponse( AuthenticationResponse response )
    {
        this.response = response;
    }


    public String getIdentity()
    {
        return identity;
    }


    public void setIdentity( String identity )
    {
        this.identity = identity;
    }


    public boolean isSuccessful()
    {
        return successful;
    }


    public void setSuccessful( boolean successful )
    {
        this.successful = successful;
    }
}
