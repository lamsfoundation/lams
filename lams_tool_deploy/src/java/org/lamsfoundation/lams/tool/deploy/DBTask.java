/*
 * DBTask.java
 *
 * Created on 29 March 2005, 17:02
 */

package org.lamsfoundation.lams.tool.deploy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author chris
 */
public abstract class DBTask implements Task
{

    /**
     * Holds value of property dbDriverClass.
     */
    private String dbDriverClass;

    /**
     * Holds value of property dbDriverUrl.
     */
    private String dbDriverUrl;

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
    public void setDbDriverClass(java.lang.String dbDriverClass)
    
    
    {

        this.dbDriverClass = dbDriverClass;
    }

    /**
     * Setter for property jdbcUrl.
     * @param jdbcUrl New value of property jdbcUrl.
     */
    public void setDbDriverUrl(java.lang.String dbDriverUrl)
    
    {

        this.dbDriverUrl = dbDriverUrl;
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
    
    /**
     * Get a JDBC Connection from the set properties.
     * @return Connection
     * @throws DeployException if the connection cannot be established.
     */
    protected Connection getConnection() throws DeployException
    {
        try
        {
            Class.forName(dbDriverClass);
            return DriverManager.getConnection(dbDriverUrl, dbUsername, dbPassword);
        }
        catch (Exception ex)
        {
            throw new DeployException("Could not get connection", ex);
        }
    }
    
    protected String readFile(File file) throws DeployException
    {
        
        try
        {
            return FileUtils.readFileToString(file, "UTF8");
        }
        catch (IOException ioex)
        {
            throw new DeployException("Could not read file", ioex);
        }

    }
    
}
