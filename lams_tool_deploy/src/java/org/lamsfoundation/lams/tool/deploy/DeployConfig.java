/*
 * DeployConfig.java
 *
 * Created on 04 April 2005
 */

package org.lamsfoundation.lams.tool.deploy;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Encapsulates configuration data for the Deployer.
 * @author chris
 */
public class DeployConfig
{
    //public static final String PROPERTIES_FILE_NAME = "/home/chris/workspace/lams_tool_deploy/test/file/deploy.properties";
    public static final String TOOL_WEB_URI_KEY = "toolWebUri";
    public static final String TOOL_CONTEXT_KEY = "toolContext";
    public static final String LAMS_EAR_PATH_KEY = "lamsEarPath";
    public static final String TOOL_INSERT_SCRIPT_NAME_KEY = "toolInsertScriptName";
    public static final String TOOL_LIBRARY_INSERT_SCRIPT_NAME_KEY = "toolLibraryInsertScriptName";
    public static final String TOOL_ACTIVITY_INSERT_SCRIPT_NAME_KEY = "toolActivityInsertScriptName";
    public static final String TOOL_TABLES_SCRIPT_NAME_KEY = "toolTablesScriptName";
    public static final String DB_USERNAME_KEY = "dbUsername";
    public static final String DB_PASSWORD_KEY = "dbPassword";
    public static final String DB_DRIVER_CLASS_KEY = "dbDriverClass";
    public static final String DB_DRIVER_URL_KEY = "dbDriverUrl";
    public static final String DEPLOY_FILES_KEY = "deployFiles";
    
    private PropertiesConfiguration props = null;
    /**
     * Holds value of property toolWebUri.
     */
    private String toolWebUri;
    
    /**
     * Holds value of property toolContextRoot.
     */
    private String toolContextRoot;
    
    /**
     * Holds value of property lamsEarPath.
     */
    private String lamsEarPath;
    
    /**
     * Holds value of property toolInsertScriptName.
     */
    private String toolInsertScriptName;
    
    /**
     * Holds value of property toolLibraryInsertScriptName.
     */
    private String toolLibraryInsertScriptName;
    
    /**
     * Holds value of property toolActivityInsertScriptName.
     */
    private String toolActivityInsertScriptName;
    
    /**
     * Holds value of property toolTablesScriptName.
     */
    private String toolTablesScriptName;
    
    /**
     * Holds value of property deployFiles.
     */
    private List deployFiles;
    
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
    
    /** Creates a new instance of DeployConfig */
    public DeployConfig(final String deployPropertiesFilePath) throws DeployException
    {
        try
        {
            props = new PropertiesConfiguration(deployPropertiesFilePath);
            toolWebUri = getProperty(TOOL_WEB_URI_KEY);
            toolContextRoot = getProperty(TOOL_CONTEXT_KEY);
            lamsEarPath = getProperty(LAMS_EAR_PATH_KEY);
            toolInsertScriptName = getProperty(TOOL_INSERT_SCRIPT_NAME_KEY);
            toolLibraryInsertScriptName = getProperty(TOOL_LIBRARY_INSERT_SCRIPT_NAME_KEY);
            toolActivityInsertScriptName = getProperty(TOOL_ACTIVITY_INSERT_SCRIPT_NAME_KEY);
            toolTablesScriptName = getProperty(TOOL_TABLES_SCRIPT_NAME_KEY);
            dbUsername = getProperty(DB_USERNAME_KEY);
            dbPassword = getProperty(DB_PASSWORD_KEY);
            dbDriverClass = getProperty(DB_DRIVER_CLASS_KEY);
            dbDriverUrl = getProperty(DB_DRIVER_URL_KEY);
            deployFiles = getPropertyList(DEPLOY_FILES_KEY);
            
        }
        catch (ConfigurationException confex)
        {
            throw new DeployException("Failed to load configuration", confex);
        }
    }
    
    /**
     * Getter for property toolWebAppName.
     * @return Value of property toolWebAppName.
     */
    public String getToolWebUri()
    
    
    {

        return this.toolWebUri;
    }
    
    /**
     * Getter for property toolContextRoot.
     * @return Value of property toolContextRoot.
     */
    public String getToolContextRoot()
    {
        
        return this.toolContextRoot;
    }
    
    /**
     * Getter for property lamsEarPath.
     * @return Value of property lamsEarPath.
     */
    public String getLamsEarPath()
    {
        
        return this.lamsEarPath;
    }
    
    /**
     * Getter for property toolIinsertScriptPath.
     * @return Value of property toolIinsertScriptPath.
     */
    public String getToolInsertScriptName()
    
    
    {

        return this.toolInsertScriptName;
    }
    
    /**
     * Getter for property libraryInsertScriptPath.
     * @return Value of property libraryInsertScriptPath.
     */
    public String getToolLibraryInsertScriptName()
    
    
    {
        
        return this.toolLibraryInsertScriptName;
    }
    
    /**
     * Getter for property toolActivityInsertScriptPath.
     * @return Value of property toolActivityInsertScriptPath.
     */
    public String getToolActivityInsertScriptName()
    
    {
        
        return this.toolActivityInsertScriptName;
    }
    
    /**
     * Getter for property toolTablesScriptPath.
     * @return Value of property toolTablesScriptPath.
     */
    public String getToolTablesScriptName()
    
    {
        
        return this.toolTablesScriptName;
    }
    
    /**
     * Getter for property deployFiles.
     * @return Value of property deployFiles.
     */
    public List getDeployFiles()
    {
        
        return this.deployFiles;
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
     * Getter for property dbPassword.
     * @return Value of property dbPassword.
     */
    public String getDbPassword()
    {
        
        return this.dbPassword;
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
     * Getter for property dbDriverURL.
     * @return Value of property dbDriverURL.
     */
    public String getDbDriverUrl()
    
    {
        
        return this.dbDriverUrl;
    }
    
    /**
     * Tries to load the property for the given key.
     * @param key the key to the desired property.
     * @return String the property value
     * @throws DeployException if the property cannot be found.
     */
    protected String getProperty(final String key) throws DeployException
    {
        String property =  props.getString(key);
        if ((property == null) || (property.length() < 1))
        {
            throw new DeployException("Property "+key+" is missing or has no value");
        }
        return property;
    }
    
    /**
     * Returns a list of property strings for the given key.
     * @param key identifies the property to locate
     * @return List of (String) properties
     * @throws DeployException if there are no matches
     */
    protected List getPropertyList(final String key) throws DeployException
    {
        List propsList = props.getList(key);
        if ((propsList == null) || (propsList.isEmpty()))
        {
            throw new DeployException("Property List "+key+" is missing or empty");
        }
        return propsList;
    }
}
