/*
 * DBTask.java
 *
 * Created on 29 March 2005, 17:02
 */

package org.lamsfoundation.lams.tool.deploy;

/**
 *
 * @author chris
 */
public abstract class DBTask implements Task
{

    /**
     * Holds value of property jdbcDriverClass.
     */
    private String jdbcDriverClass;

    /**
     * Holds value of property jdbcUrl.
     */
    private String jdbcUrl;

    /**
     * Holds value of property dbUsername.
     */
    private String dbUsername;

    /**
     * Holds value of property dbPassword.
     */
    private String dbPassword;
    
    /** Creates a new instance of DBTask */
    public DBTask()
    {
    }
    
    public abstract void execute() throws DeployException;

    /**
     * Setter for property jdbcDriver.
     * @param jdbcDriver New value of property jdbcDriver.
     */
    public void setJdbcDriverClass(java.lang.String jdbcDriverClass)
    
    {

        this.jdbcDriverClass = jdbcDriverClass;
    }

    /**
     * Setter for property jdbcUrl.
     * @param jdbcUrl New value of property jdbcUrl.
     */
    public void setJdbcUrl(String jdbcUrl)
    {

        this.jdbcUrl = jdbcUrl;
    }

    /**
     * Setter for property dbUsername.
     * @param dbUsername New value of property dbUsername.
     */
    public void setDbUsername(String dbUsername)
    {

        this.dbUsername = dbUsername;
    }

    /**
     * Setter for property dbPassword.
     * @param dbPassword New value of property dbPassword.
     */
    public void setDbPassword(String dbPassword)
    {

        this.dbPassword = dbPassword;
    }
    
}
