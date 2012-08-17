package org.verisign.joid.server;


/**
 * User: treeder
 * Date: Jul 17, 2007
 * Time: 5:34:30 PM
 */
public class User
{
    private String password;
    private String username;


    public User()
    {
    }


    public User( String username, String password )
    {

        this.username = username;
        this.password = password;
    }


    public String getPassword()
    {
        return password;
    }


    public void setPassword( String password )
    {
        this.password = password;
    }


    public String getUsername()
    {
        return username;
    }


    public void setUsername( String username )
    {
        this.username = username;
    }


    public String toString()
    {
        return username;
    }
}
