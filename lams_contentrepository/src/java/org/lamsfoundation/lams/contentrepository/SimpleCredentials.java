package org.lamsfoundation.lams.contentrepository;

/**
 * 
 * Basic implementation of credentials. Note: the "user id" is
 * actually "tool id", so its not really a password as an validation
 * string.
 * <p>
 * When a tool logs into the repository, it will pass in a simple credentials
 * object which has just the userid and password set.
 * <p>
 * The repository then attaches credential to the ticket.
 * The new credential doesn't have the password and it 
 * is never connected from the database.
 * <p>
 * The credential comparison is done using the DAO class - it
 * does not load the data from the database but gets the
 * password to the db for checking.
 * <p>
 * The original credentials contains attributes. This will be useful
 * for the future, but has not yet been implemented.
 * <p>
 * @see org.lamsfoundation.lams.contentrepository.ICredentials
 * @author Fiona Malikoff
*/
public class SimpleCredentials implements ICredentials {

	private String name = null;
	private char[] password = null; 
	
	/**
	 * Should only be used by hibernate or package calls.
	 * @param name
	 * @param password
	 */
	public SimpleCredentials(String name, char[] password ) {
		this.name = name;
		this.password = (char[]) password.clone(); 
	}

   /**
     * Returns the name of the tool.
     *
     * @return the tool name.
     */
    public String getName() {
    	return name;
    }

    /**
     * Returns the password.
     * <p>
     * Note that this method returns a reference to the password.
     * The password is set when the creditionals are passed in to get a ticket,
     * and then removed.
     * <p>
     * @return the password.
     */
    public char[] getPassword() {
    	return password;
    }

    /** 
     * Clear the current password. Clear it after use to 
     * ensure that it is not left in memory as security hole.
     */
    public void clearPassword() {
    	password = new char[0];
    }
}
