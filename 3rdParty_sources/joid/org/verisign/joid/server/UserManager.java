package org.verisign.joid.server;


/**
 * User: treeder
 * Date: Jul 17, 2007
 * Time: 5:33:09 PM
 */
public interface UserManager
{
    // removing this to get rid of the User dependency
    //    User getUser(String username);
    //    void save(User user);

    /**
     * The implementation should store this relationship so it can retrieve it later
     * for auto login.
     * @param username
     * @param authKey
     */
    void remember( String username, String authKey );


    /**
     * Returns a User based on a generated authKey from a user selecting "Remember Me".
     * @param username
     * @param authKey
     * @return
     */
    String getRememberedUser( String username, String authKey );


    /**
     * todo: This might be better off as an abstract method on OpenIdServlet
     *
     * @param username
     * @param claimedIdentity
     * @return
     */
    boolean canClaim( String username, String claimedIdentity );
}
