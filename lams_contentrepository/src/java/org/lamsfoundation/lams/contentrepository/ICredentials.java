
package org.lamsfoundation.lams.contentrepository;
import java.io.Serializable;

/**
 * Base interface for all credentials that may be passed to the
 * Repository.login() method.
 */
public interface ICredentials extends Serializable {

    /**
     * Returns the name of the tool.
     *
     * @return the tool name.
     */
    public String getName();

    /**
     * Returns the password.
     * <p>
     * Note that this method returns a reference to the password.
     * The password is set when the creditionals are passed in to get a ticket,
     * and then removed.
     * <p>
     * @return the password.
     */
    public char[] getPassword();

    /**
     * Clear the current password - important not to leave this lying around.
     * 
     * After the credential is used, it should be cleared to ensure the 
     * identification string isn't held in memory.  
     */
    public void clearPassword();

    /* Implement in the future
     * 
     * Stores an attribute in this credentials instance.
     * 
     * @param name  a String specifying the name of the attribute
     * @param value the String value of the attribute to be stored
    public void setAttribute(String name, String value);
     */

    /* Implement in the future
     * 
     * Removes an attribute from this credentials instance.
     *
     * @param name a String specifying the name of the attribute
     *             to remove
    public void removeAttribute(String name);
     */

    /* Implement in the future
     * 
     * Returns the value of the named attribute as a String,
     * or null if no attribute of the given name exists.
     *
     * @param name a String specifying the name of
     *             the attribute
     * @return	an String containing the value of the attribute,
     * or null if the attribute does not exist
    public String getAttribute(String name);
     */

    /* Implement in the future
     * 
     * Returns the names of the attributes available to this
     * credentials instance. This method returns an empty array
     * if the credentials instance has no attributes available to it.
     *
     * @return a string array containing the names of the stored attributes
    public String[] getAttributeNames();
     */
}