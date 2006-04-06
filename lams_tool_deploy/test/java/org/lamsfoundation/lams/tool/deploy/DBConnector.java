/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.deploy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
/**
 * Utility class for getting a DB connection.
 * INTENDED ONLY TO BE USED WITH JUNIT!.
 * @author chris
 */
public class DBConnector
{

    public static final String DB_USERNAME_KEY = "dbUsername";
    public static final String DB_PASSWORD_KEY = "dbPassword";
    public static final String DB_DRIVER_CLASS_KEY = "dbDriverClass";
    public static final String DB_DRIVER_URL_KEY = "dbDriverUrl";
    /**
     * Holds value of property dbUsername.
     */
    private String dbUsername;

    /**
     * Holds value of property dbPassword.
     */
    private String dbPassword;

    /**
     * Holds value of property dbDriverClass.
     */
    private String dbDriverClass;

    /**
     * Holds value of property dbDriverUrl.
     */
    private String dbDriverUrl;
    
    /** Creates a new instance of DBConnector */
    public DBConnector(String propsFilePath) throws ConfigurationException
    {
        PropertiesConfiguration config = new PropertiesConfiguration(propsFilePath);
        setDbUsername(config.getString(DB_USERNAME_KEY));
        setDbPassword(config.getString(DB_PASSWORD_KEY));
        setDbDriverUrl(config.getString(DB_DRIVER_URL_KEY));
        setDbDriverClass(config.getString(DB_DRIVER_CLASS_KEY));
    }
    
    /** Creates a new instance of DBConnector */
    public DBConnector()
    {
        
    }

    /**
     * Getter for property dbUsername.
     * @return Value of property dbUsername.
     */
    public String getDbUsername()
    {

        return this.dbUsername;
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
     * Getter for property dbPassword.
     * @return Value of property dbPassword.
     */
    public String getDbPassword()
    {

        return this.dbPassword;
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
     * Getter for property dbDriverClass.
     * @return Value of property dbDriverClass.
     */
    public String getDbDriverClass()
    {

        return this.dbDriverClass;
    }

    /**
     * Setter for property dbDriverClass.
     * @param dbDriverClass New value of property dbDriverClass.
     */
    public void setDbDriverClass(String dbDriverClass)
    {

        this.dbDriverClass = dbDriverClass;
    }

    /**
     * Getter for property dbDriverUrl.
     * @return Value of property dbDriverUrl.
     */
    public String getDbDriverUrl()
    {

        return this.dbDriverUrl;
    }

    /**
     * Setter for property dbDriverUrl.
     * @param dbDriverUrl New value of property dbDriverUrl.
     */
    public void setDbDriverUrl(String dbDriverUrl)
    {

        this.dbDriverUrl = dbDriverUrl;
    }
    
    
    public Connection connect() throws SQLException, ClassNotFoundException
    {
        Class.forName(dbDriverClass);
        return DriverManager.getConnection(dbDriverUrl, dbUsername, dbPassword);
    }
    
}
