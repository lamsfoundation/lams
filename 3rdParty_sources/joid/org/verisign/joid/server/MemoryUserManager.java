package org.verisign.joid.server;


import java.util.HashMap;
import java.util.Map;


/**
 * User: treeder
 * Date: Jul 17, 2007
 * Time: 5:33:31 PM
 */
public class MemoryUserManager implements UserManager
{
    private Map<String, User> userMap = new HashMap<String, User>();
    private Map<String, String> rememberMeMap = new HashMap<String, String>();


    public User getUser( String username )
    {
        return ( User ) userMap.get( username );
    }


    public void save( User user )
    {
        userMap.put( user.getUsername(), user );
    }


    public void remember( String username, String authKey )
    {
        rememberMeMap.put( username, authKey );
    }


    public String getRememberedUser( String username, String authKey )
    {
        if ( username == null || authKey == null )
            return null;
        String auth = ( String ) rememberMeMap.get( username );
        if ( auth != null )
        {
            if ( authKey.equals( auth ) )
            {
                // then we have a match
                return username;
            }
        }
        return null;
    }


    /**
     *
     *
     * @param username
     * @param claimedId
     * @return
     */
    public boolean canClaim( String username, String claimedId )
    {
        String usernameFromClaimedId = claimedId.substring( claimedId.lastIndexOf( "/" ) + 1 );
        if ( username.equals( usernameFromClaimedId ) )
        {
            return true;
        }
        return false;
    }
}
