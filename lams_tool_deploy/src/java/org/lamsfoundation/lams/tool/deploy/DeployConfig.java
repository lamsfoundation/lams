/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */

package org.lamsfoundation.lams.tool.deploy;

import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Encapsulates configuration data for the Deployer.
 * @author chris
 */
public class DeployConfig
{
    public static final String TOOL_SIGNATURE_KEY = "toolSignature";
    public static final String TOOL_WEB_URI_KEY = "toolWebUri";
    public static final String TOOL_CONTEXT_KEY = "toolContext";
    public static final String LAMS_EAR_PATH_KEY = "lamsEarPath";
    public static final String TOOL_INSERT_SCRIPT_PATH_KEY = "toolInsertScriptPath";
    public static final String TOOL_LIBRARY_INSERT_SCRIPT_PATH_KEY = "toolLibraryInsertScriptPath";
    public static final String TOOL_ACTIVITY_INSERT_SCRIPT_PATH_KEY = "toolActivityInsertScriptPath";
    public static final String TOOL_TABLES_SCRIPT_PATH_KEY = "toolTablesScriptPath";
    public static final String TOOL_TABLES_DELETE_SCRIPT_PATH_KEY = "toolTablesDeleteScriptPath";
    public static final String DB_USERNAME_KEY = "dbUsername";
    public static final String DB_PASSWORD_KEY = "dbPassword";
    public static final String DB_DRIVER_CLASS_KEY = "dbDriverClass";
    public static final String DB_DRIVER_URL_KEY = "dbDriverUrl";
    public static final String DEPLOY_FILES_KEY = "deployFiles";
    
    private PropertiesConfiguration props = null;
    private String validationError = "";
    
 
    /**
     * Holds value of property toolSignature.
     */
    private String toolSignature;

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
     * Holds value of property toolInsertScriptPath.
     */
    private String toolInsertScriptPath;
    
    /**
     * Holds value of property toolLibraryInsertScriptPath.
     */
    private String toolLibraryInsertScriptPath;
    
    /**
     * Holds value of property toolActivityInsertScriptPath.
     */
    private String toolActivityInsertScriptPath;
    
    /**
     * Holds value of property toolTablesScriptPath.
     */
    private String toolTablesScriptPath;
    
    /**
     * Holds value of property toolTablesDeleteScriptPath.
     */
    private String toolTablesDeleteScriptPath;

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
    
    /** Creates a new instance of DeployConfig 
     * @param deployPropertiesFilePath file to load
     * @param validate validate the properties after loading. Set to false if 
     * intending to load some more properties later.
     * @throws DeployException if a configuration error occurs or if the validation fails.
     */
    public DeployConfig(final String deployPropertiesFilePath, final boolean validate) throws DeployException
    {
        updateConfiguration(deployPropertiesFilePath, validate);
    }
    /** Creates a new instance of DeployConfig 
     * @param deployPropertiesFilePath file to load
     * @param validate validate the properties after loading. Set to false if 
     * intending to load some more properties later.
     * @throws DeployException if a configuration error occurs or if the validation fails.
     */
    public DeployConfig(final URL deployPropertiesFilePath, final boolean validate) throws DeployException
    {
        updateConfiguration(deployPropertiesFilePath, validate);
    }
    
    /** Update the configuration. Only updates a value if a particular property is found.
     * @param deployPropertiesFilePath: file to load
     * @param validate validate the properties after loading. Set to false if 
     * intending to load some more properties later.
     * @throws DeployException if a configuration error occurs or if the validation fails.
     */
    public void updateConfiguration(final URL deployPropertiesFilePath, final boolean validate) 
    	throws DeployException
    {
        try {
	        props = new PropertiesConfiguration(deployPropertiesFilePath);
	        updateConfigurationFromProps(validate);
        } catch (ConfigurationException confex) {
	            throw new DeployException("Failed to load configuration", confex);
	    }
    }
    
    /** Update the configuration. Only updates a value if a particular property is found.
     * @param deployPropertiesFilePath: file to load
     * @param validate validate the properties after loading. Set to false if 
     * intending to load some more properties later.
     * @throws DeployException if a configuration error occurs or if the validation fails.
     */
    public void updateConfiguration(final String deployPropertiesFilePath, final boolean validate) 
    	throws DeployException
    {
        try {
	        props = new PropertiesConfiguration(deployPropertiesFilePath);
	        updateConfigurationFromProps(validate);
        } catch (ConfigurationException confex) {
	            throw new DeployException("Failed to load configuration", confex);
	    }
    }


	/** Update the current configuration from the props variable - which should
	 * have already been set by the calling function.
     * @param validate validate the properties after loading. Set to false if 
     * intending to load some more properties later.
	 */
	private void updateConfigurationFromProps (final boolean validate) {
	    
        String value = getProperty(TOOL_SIGNATURE_KEY);
        if ( value != null ) {
            toolSignature = value;
        }

        value = getProperty(TOOL_WEB_URI_KEY);
        if ( value != null ) {
            toolWebUri = value;
        }
        
        value = getProperty(TOOL_CONTEXT_KEY);
        if ( value != null ) {
            toolContextRoot = value;
        }
        
        value = getProperty(LAMS_EAR_PATH_KEY);
        if ( value != null ) {
            lamsEarPath  = value;
        }
        
        value = getProperty(TOOL_INSERT_SCRIPT_PATH_KEY);
        if ( value != null ) {
            toolInsertScriptPath  = value;
        }
        
        value = getProperty(TOOL_LIBRARY_INSERT_SCRIPT_PATH_KEY);
        if ( value != null ) {
            toolLibraryInsertScriptPath  = value;
        }
        
        value = getProperty(TOOL_ACTIVITY_INSERT_SCRIPT_PATH_KEY);
        if ( value != null ) {
            toolActivityInsertScriptPath  = value;
        }
        
        value = getProperty(TOOL_TABLES_SCRIPT_PATH_KEY);
        if ( value != null ) {
            toolTablesScriptPath  = value;
        }
        
        value = getProperty(TOOL_TABLES_DELETE_SCRIPT_PATH_KEY);
        if ( value != null ) {
            toolTablesDeleteScriptPath  = value;
        }
        
        value = getProperty(DB_USERNAME_KEY);
        if ( value != null ) {
            dbUsername  = value;
        }
        
        value = getProperty(DB_PASSWORD_KEY);
        if ( value != null ) {
            dbPassword  = value;
        }
        
        value = getProperty(DB_DRIVER_CLASS_KEY);
        if ( value != null ) {
            dbDriverClass  = value;
        }
        
        value = getProperty(DB_DRIVER_URL_KEY);
        if ( value != null ) {
            dbDriverUrl = value;
        }

        List listValue = getPropertyList(DEPLOY_FILES_KEY);
        if ( listValue != null && listValue.size() > 0) {
            deployFiles = listValue;
        }

        if ( validate )
            validateProperties();
    }
       
    /** 
     * Write out the current settings to the supplied stream.
     * 
     * @param outputStream 
     */
    public void writeProperties(OutputStream os) {
        if ( os == null ) {
            throw new DeployException("Invalid parameter - outputStream os is null");
        }
        
        PropertiesConfiguration newProps = new PropertiesConfiguration();
        newProps.setProperty(TOOL_SIGNATURE_KEY,toolSignature);
        newProps.setProperty(TOOL_WEB_URI_KEY,toolWebUri);
        newProps.setProperty(TOOL_CONTEXT_KEY,toolContextRoot);
        newProps.setProperty(LAMS_EAR_PATH_KEY,lamsEarPath);
        newProps.setProperty(TOOL_INSERT_SCRIPT_PATH_KEY,toolInsertScriptPath);
        newProps.setProperty(TOOL_LIBRARY_INSERT_SCRIPT_PATH_KEY,toolLibraryInsertScriptPath);
        newProps.setProperty(TOOL_ACTIVITY_INSERT_SCRIPT_PATH_KEY,toolActivityInsertScriptPath);
        newProps.setProperty(TOOL_TABLES_SCRIPT_PATH_KEY,toolTablesScriptPath);
        newProps.setProperty(TOOL_TABLES_DELETE_SCRIPT_PATH_KEY,toolTablesDeleteScriptPath);
        newProps.setProperty(DB_USERNAME_KEY,dbUsername);
        newProps.setProperty(DB_PASSWORD_KEY,dbPassword);
        newProps.setProperty(DB_DRIVER_CLASS_KEY,dbDriverClass);
        newProps.setProperty(DB_DRIVER_URL_KEY,dbDriverUrl);
        newProps.setProperty(DEPLOY_FILES_KEY,deployFiles);
        
        try {
            newProps.save(os);
        } catch (ConfigurationException e) {
            throw new DeployException("Unable to create generate properties: "+e.getMessage(), e);
        }
   }
    
    /**
     * The value to be used in the web-uri element of the application xml
     * for the tool being deployed.
     * @return Value of property toolWebAppUri.
     */
    public String getToolWebUri()
    {

        return this.toolWebUri;
    }
    
    /**
     * The value to be used in the context-root element of the application xml
     * for the tool being deployed.
     * @return Value of property toolContextRoot.
     */
    public String getToolContextRoot()
    {
        
        return this.toolContextRoot;
    }
    
    /**
     * The path to the lasm ear.
     * @return Value of property lamsEarPath.
     */
    public String getLamsEarPath()
    {
        
        return this.lamsEarPath;
    }
    
    /**
     * The path to the sql script that inserts the lams_tool record.
     * @return Value of property toolIinsertScriptPath.
     */
    public String getToolInsertScriptPath()
    
    
    {

        return this.toolInsertScriptPath;
    }
    
    /**
     * The path to the sql script that inserts that lams_learning_library record.
     * @return Value of property libraryInsertScriptPath.
     */
    public String getToolLibraryInsertScriptPath()
    
    
    {
        
        return this.toolLibraryInsertScriptPath;
    }
    
    /**
     * The path to the sql script that inserts the lams_learning_activity record.
     * @return Value of property toolActivityInsertScriptPath.
     */
    public String getToolActivityInsertScriptPath()
    
    {
        
        return this.toolActivityInsertScriptPath;
    }
    
    /**
     * The path to the sql script that creates the tools tables and inserts
     * any required data.
     * @return Value of property toolTablesScriptPath.
     */
    public String getToolTablesScriptPath()
    
    {
        
        return this.toolTablesScriptPath;
    }
    
    /**
     * The list of files that need to be nmoved to the lams ear
     * to deploy the tool.
     * @return Value of property deployFiles.
     */
    public List getDeployFiles()
    {
        
        return this.deployFiles;
    }
    
    /**
     * The username for the lams db.
     * @return Value of property dbUsername.
     */
    public String getDbUsername()
    {
        
        return this.dbUsername;
    }
    
    /**
     * The password for the lams db.
     * @return Value of property dbPassword.
     */
    public String getDbPassword()
    {
        
        return this.dbPassword;
    }
    
    /**
     * The class name of the JDBC driver to use.
     * @return Value of property dbDriverClass.
     */
    public String getDbDriverClass()
    {
        
        return this.dbDriverClass;
    }
    
    /**
     * The URL for the JDBC driver to connect with.
     * @return Value of property dbDriverURL.
     */
    public String getDbDriverUrl()
    
    {
        
        return this.dbDriverUrl;
    }
    
    /**
     * Set an arbitrary property. Note: these will probably have come through
     * ant, and that removes the case of the key, so ignore case.
     */
    protected void setProperty(String key, String value) throws DeployException {
        if ( key == null )
            throw new DeployException("Invalid parameter: Key is null. ");
        
        if ( key.equalsIgnoreCase(TOOL_SIGNATURE_KEY) ) {
            toolSignature = value;
        }

        if ( key.equalsIgnoreCase(TOOL_WEB_URI_KEY) ) {
            toolWebUri = value;
        }

        if ( key.equalsIgnoreCase(TOOL_CONTEXT_KEY) ) {
            toolContextRoot = value;
        }

        if ( key.equalsIgnoreCase(LAMS_EAR_PATH_KEY) ) {
            lamsEarPath  = value;
        }

        if ( key.equalsIgnoreCase(TOOL_INSERT_SCRIPT_PATH_KEY) ) {
            toolInsertScriptPath  = value;
        }

        if ( key.equalsIgnoreCase(TOOL_LIBRARY_INSERT_SCRIPT_PATH_KEY) ) {
            toolLibraryInsertScriptPath  = value;
        }

        if ( key.equalsIgnoreCase(TOOL_ACTIVITY_INSERT_SCRIPT_PATH_KEY) ) {
            toolActivityInsertScriptPath  = value;
        }

        if ( key.equalsIgnoreCase(TOOL_TABLES_SCRIPT_PATH_KEY) ) {
            toolTablesScriptPath  = value;
        }

        if ( key.equalsIgnoreCase(TOOL_TABLES_DELETE_SCRIPT_PATH_KEY) ) {
            toolTablesDeleteScriptPath  = value;
        }

        if ( key.equalsIgnoreCase(DB_USERNAME_KEY) ) {
            dbUsername  = value;
        }

        if ( key.equalsIgnoreCase(DB_PASSWORD_KEY) ) {
            dbPassword  = value;
        }

        if ( key.equalsIgnoreCase(DB_DRIVER_CLASS_KEY) ) {
            dbDriverClass  = value;
        }

        if ( key.equalsIgnoreCase(DB_DRIVER_URL_KEY) ) {
            dbDriverUrl = value;
        }

        if ( key.equalsIgnoreCase(DEPLOY_FILES_KEY) ) {
            deployFiles = convertList(value);
        }

    }
    /**
     * @param dbDriverUrl The dbDriverUrl to set.
     */
    protected void setDbDriverUrl(String dbDriverUrl) {
        this.dbDriverUrl = dbDriverUrl;
    }
    /**
     * @param dbPassword The dbPassword to set.
     */
    protected void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
    /**
     * @param dbUsername The dbUsername to set.
     */
    protected void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }
    /**
     * @param deployFiles The deployFiles to set.
     */
    protected void setDeployFiles(List deployFiles) {
        this.deployFiles = deployFiles;
    }
    /**
     * @param lamsEarPath The lamsEarPath to set.
     */
    protected void setLamsEarPath(String lamsEarPath) {
        this.lamsEarPath = lamsEarPath;
    }
    /**
     * @param toolActivityInsertScriptPath The toolActivityInsertScriptPath to set.
     */
    protected void setToolActivityInsertScriptPath(
            String toolActivityInsertScriptPath) {
        this.toolActivityInsertScriptPath = toolActivityInsertScriptPath;
    }
    /**
     * @param toolContextRoot The toolContextRoot to set.
     */
    protected void setToolContextRoot(String toolContextRoot) {
        this.toolContextRoot = toolContextRoot;
    }
    /**
     * @param toolInsertScriptPath The toolInsertScriptPath to set.
     */
    protected void setToolInsertScriptPath(String toolInsertScriptPath) {
        this.toolInsertScriptPath = toolInsertScriptPath;
    }
    /**
     * @param toolLibraryInsertScriptPath The toolLibraryInsertScriptPath to set.
     */
    protected void setToolLibraryInsertScriptPath(
            String toolLibraryInsertScriptPath) {
        this.toolLibraryInsertScriptPath = toolLibraryInsertScriptPath;
    }
    /**
     * @param toolTablesScriptPath The toolTablesScriptPath to set.
     */
    protected void setToolTablesScriptPath(String toolTablesScriptPath) {
        this.toolTablesScriptPath = toolTablesScriptPath;
    }
    /**
     * @param toolWebUri The toolWebUri to set.
     */
    protected void setToolWebUri(String toolWebUri) {
        this.toolWebUri = toolWebUri;
    }
    
    /**
     * Tries to load the property for the given key from the props object.
     * @param key the key to the desired property.
     * @return String the property value or null if not found in props.
     */
    protected String getProperty(final String key) throws DeployException
    {
        String property =  props.getString(key);
        if ( property !=null && property.length() > 0 ) {
            return property;
        } else { 
            return null;
        }
    }

    /**
     * Returns a list of property strings for the given key from the props object.
     * @param key identifies the property to locate
     * @return List of (String) properties, null if not found.
     */
    protected List getPropertyList(final String key) throws DeployException
    {
        List propsList = props.getList(key);
        if ((propsList != null) && !propsList.isEmpty())
        {
            return propsList;
        } else {
            return null;
        }
    }
    
    /**
     * Converts a String to a List. Entries should be comma separated.
     * @param Input string containing entries.
     * @return List of (String) properties, null if not found.
     */
    protected List convertList(String input) throws DeployException
    {
        String[] strings = input.split(",");
        List list = new ArrayList(strings.length);
        for ( int i=0; i<strings.length; i++) {
            list.add(strings[i]);
        }
        return list;
    }
    
    /** Check that all the correct properties exist - tool delete path is optional. */
    public void validateProperties() throws DeployException {
        boolean valid;
        validationError = ""; // object attribute - will be updated by validateProperty() if something is missing.

        valid = validateStringProperty(toolWebUri, TOOL_WEB_URI_KEY);
        valid = valid && validateStringProperty(toolContextRoot, TOOL_CONTEXT_KEY);
        valid = valid && validateStringProperty(lamsEarPath, LAMS_EAR_PATH_KEY);
        valid = valid && validateStringProperty(toolInsertScriptPath, TOOL_INSERT_SCRIPT_PATH_KEY);
        valid = valid && validateStringProperty(toolLibraryInsertScriptPath, TOOL_LIBRARY_INSERT_SCRIPT_PATH_KEY);
        valid = valid && validateStringProperty(toolActivityInsertScriptPath, TOOL_ACTIVITY_INSERT_SCRIPT_PATH_KEY);
        valid = valid && validateStringProperty(toolWebUri, TOOL_TABLES_SCRIPT_PATH_KEY);
        valid = valid && validateStringProperty(dbUsername, DB_USERNAME_KEY);
        valid = valid && validateStringProperty(dbPassword, DB_PASSWORD_KEY);
        valid = valid && validateStringProperty(dbDriverClass, DB_PASSWORD_KEY);
        valid = valid && validateStringProperty(dbDriverUrl, DB_DRIVER_URL_KEY);
        valid = valid && validateListProperty(deployFiles,DEPLOY_FILES_KEY);
        
        if ( ! valid )
            throw new DeployException("Invalid deployment properties: "+validationError);
    }
    
    protected boolean validateStringProperty(String property, String key) {
        if ( ((property == null) || (property.length() < 1)) ) {
            validationError = validationError + "Property "+key+" is missing or has no value.";
            return false;
        }
        return true;
    }

    protected boolean validateListProperty(List property, String key) {
        if ( ((property == null) || (property.isEmpty())) ) {
            validationError = validationError + "Property "+key+" is missing or has no value.";
            return false;
        }
        return true;
    }


    /**
     * @return Returns the toolTablesDeleteScriptPath.
     */
    public String getToolTablesDeleteScriptPath() {
        return toolTablesDeleteScriptPath;
    }
    /**
     * @param toolTablesDeleteScriptPath The toolTablesDeleteScriptPath to set.
     */
    public void setToolTablesDeleteScriptPath(String toolTablesDeleteScriptPath) {
        this.toolTablesDeleteScriptPath = toolTablesDeleteScriptPath;
    }
    /**
     * @return Returns the toolSignature.
     */
    public String getToolSignature() {
        return toolSignature;
    }
    /**
     * @param toolSignature The toolSignature to set.
     */
    public void setToolSignature(String toolSignature) {
        this.toolSignature = toolSignature;
    }
}
