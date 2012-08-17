package org.verisign.joid.consumer;


/**
 * User: treeder
 * Date: Jul 17, 2007
 * Time: 4:35:43 PM
 */
public class ServerAndDelegate
{
    private String server;
    private String delegate;


    public String getServer()
    {
        return server;
    }


    public void setServer( String server )
    {
        this.server = server;
    }


    public String getDelegate()
    {
        return delegate;
    }


    public void setDelegate( String delegate )
    {
        this.delegate = delegate;
    }


    public String toString()
    {
        return "ServerAndDelegate[server=" + server + ", delegate=" + delegate + "]";
    }
}
