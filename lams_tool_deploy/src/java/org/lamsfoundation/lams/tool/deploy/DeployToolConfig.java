/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/

/*
 * Created on 24/11/2005
 *
 */
package org.lamsfoundation.lams.tool.deploy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author mtruong
 *
 * Subclass of DeployConfig.
 * 
 * Encapsulates the configuration data for the Tool Deployer.
 * 
 * The XML outputted by XStream will contain the absolute classname if 
 * an alias is not specified. To make the XML output more concise, the XML elememnt
 * "Deploy" is mapped to this class (org.lamsfoundation.lams.tool.deploy.DeployToolConfig class)
 * 
 * See templateDeployTool.xml for the example XML format/structure
 */
public class DeployToolConfig extends DeployConfig {
    
    private static Log log = LogFactory.getLog(DeployToolConfig.class);
    
    private static final String TOOL_WEB_URI = "toolWebUri";
    private static final String TOOL_CONTEXT = "toolContext";
    private static final String LAMS_EAR_PATH = "lamsEarPath";
    private static final String TOOL_INSERT_SCRIPT_PATH = "toolInsertScriptPath";
    private static final String TOOL_LIBRARY_INSERT_SCRIPT_PATH = "toolLibraryInsertScriptPath";
    private static final String TOOL_TABLES_SCRIPT_PATH = "toolTablesScriptPath";
    private static final String TOOL_TABLES_DELETE_SCRIPT_PATH = "toolTablesDeleteScriptPath";
    private static final String DEPLOY_FILES= "deployFiles";
    private static final String FILE = "file";
  
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
    private String toolContext;
    
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
    private ArrayList deployFiles;
    
    /**
     * Creates an instance of DeployToolConfig object.
     */
    public DeployToolConfig()
    {
        super();
        xstream.alias(ROOT_ELEMENT, DeployToolConfig.class);
    }
    
    /**
     * Creates an instance of DeployToolConfig object, with the values
     * of its properties, set to that specified by the Xml configuration file
     * @param configurationFilePath
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public DeployToolConfig(String configurationFilePath) throws ParserConfigurationException, IOException, SAXException
    {
        super();
        xstream.alias(ROOT_ELEMENT, DeployToolConfig.class);
        updateConfigurationProperties(configurationFilePath);
    }
    
    /** @see org.lamsfoundation.lams.tool.deploy.DeployConfig#updateConfigurationProperties(String) */
    public void updateConfigurationProperties(String configFilePath) throws ParserConfigurationException, IOException, SAXException
    {
        String xml = readFile(configFilePath);
        DeployToolConfig config = (DeployToolConfig)deserialiseXML(xml);
        copyProperties(config);
        //printObjectProperties(); //for testing purposes
    }
     
  
   /**
    * Set an arbitrary property. Note: these will probably have come through
    * ant, and that removes the case of the key, so ignore case.
    */
   protected void setProperty(String key, String value) throws DeployException {
       if ( key == null )
           throw new DeployException("Invalid parameter: Key is null. ");
       
       super.setProperty(key, value);
       //System.out.println("ToolConfig " + key + " is: " + value);
     
       if ( key.equalsIgnoreCase(TOOL_SIGNATURE) ) {
          
           toolSignature = value;
       }

       if ( key.equalsIgnoreCase(TOOL_WEB_URI) ) {
           toolWebUri = value;
       }

       if ( key.equalsIgnoreCase(TOOL_CONTEXT) ) {
           toolContext = value;
       }

       if ( key.equalsIgnoreCase(LAMS_EAR_PATH) ) {
           lamsEarPath  = value;
       }

       if ( key.equalsIgnoreCase(TOOL_INSERT_SCRIPT_PATH) ) {
           toolInsertScriptPath  = value;
       }

       if ( key.equalsIgnoreCase(TOOL_LIBRARY_INSERT_SCRIPT_PATH) ) {
           toolLibraryInsertScriptPath  = value;
       }

       if ( key.equalsIgnoreCase(TOOL_ACTIVITY_INSERT_SCRIPT_PATH) ) {
           toolActivityInsertScriptPath  = value;
       }

       if ( key.equalsIgnoreCase(TOOL_TABLES_SCRIPT_PATH) ) {
           toolTablesScriptPath  = value;
       }

       if ( key.equalsIgnoreCase(TOOL_TABLES_DELETE_SCRIPT_PATH) ) {
           toolTablesDeleteScriptPath  = value;
       }

       if ( key.equalsIgnoreCase(DEPLOY_FILES) ) {
           deployFiles = convertList(value);
       }

   }
   
   /**
    * Converts a String to a List. Entries should be comma separated.
    * @param Input string containing entries.
    * @return List of (String) properties, null if not found.
    */
   protected ArrayList convertList(String input) throws DeployException
   {
       String[] strings = input.split(",");
       ArrayList list = new ArrayList(strings.length);
       for ( int i=0; i<strings.length; i++) {
           list.add(strings[i]);
       }
       return list;
   }
   
   /** Check that all the correct properties exist - tool delete path is optional. */
   public void validateProperties() throws DeployException {
       boolean valid;
       validationError = ""; // object attribute - will be updated by validateProperty() if something is missing.

       valid = validateStringProperty(toolWebUri, TOOL_WEB_URI);
       valid = valid && validateStringProperty(toolContext, TOOL_CONTEXT);
       valid = valid && validateStringProperty(lamsEarPath, LAMS_EAR_PATH);
       valid = valid && validateStringProperty(toolInsertScriptPath, TOOL_INSERT_SCRIPT_PATH);
       valid = valid && validateStringProperty(toolLibraryInsertScriptPath, TOOL_LIBRARY_INSERT_SCRIPT_PATH);
       valid = valid && validateStringProperty(toolActivityInsertScriptPath, TOOL_ACTIVITY_INSERT_SCRIPT_PATH);
       valid = valid && validateStringProperty(toolWebUri, TOOL_TABLES_SCRIPT_PATH);
       valid = valid && validateStringProperty(getDbUsername(), DB_USERNAME);
       valid = valid && validateStringProperty(getDbPassword(), DB_PASSWORD);
       valid = valid && validateStringProperty(getDbDriverClass(), DB_PASSWORD);
       valid = valid && validateStringProperty(getDbDriverUrl(), DB_DRIVER_URL);
       valid = valid && validateListProperty(deployFiles,DEPLOY_FILES);
       
       if (!valid )
           throw new DeployException("Invalid deployment properties: "+validationError);
   }
   
   /**
    * Upon deserialisation of the xml string, a new object will be created. 
    * The properties of this object will be copied to the calling object.
    * Only copy properties if the properties are not null
    * @param config
    */
   private void copyProperties(DeployToolConfig config)
   {
       if (config.getDbUsername() != null)
           this.setDbUsername(config.getDbUsername());
       if (config.getDbPassword() != null)
           this.setDbPassword(config.getDbPassword());
       if (config.getDbDriverUrl() != null)
           this.setDbDriverUrl(config.getDbDriverUrl());
       if (config.getDbDriverClass() != null)
           this.setDbDriverClass(config.getDbDriverClass());
       
       if (config.getToolSignature() != null)
           this.toolSignature = config.getToolSignature();
       if (config.getToolWebUri() != null)
           this.toolWebUri = config.getToolWebUri();
       if (config.getToolContext() != null)
           this.toolContext = config.getToolContext();
       if (config.getLamsEarPath() != null)
           this.lamsEarPath = config.getLamsEarPath();
       if (config.getToolInsertScriptPath() != null)
           this.toolInsertScriptPath = config.getToolInsertScriptPath();
       if (config.getToolLibraryInsertScriptPath() != null)
	       this.toolLibraryInsertScriptPath = config.getToolLibraryInsertScriptPath();
       if (config.getToolActivityInsertScriptPath() != null)
	       this.toolActivityInsertScriptPath = config.getToolActivityInsertScriptPath();
       if (config.getToolTablesScriptPath() != null)
	       this.toolTablesScriptPath = config.getToolTablesScriptPath();
       if (config.getToolTablesDeleteScriptPath() != null)
	       this.toolTablesDeleteScriptPath = config.getToolTablesDeleteScriptPath();
       if (config.getDeployFiles() != null)
	       this.deployFiles = config.getDeployFiles();
   }
   
   /** Used for testing purposes only */
   public void printObjectProperties()
   {
       System.out.println("========Object Properties=======");
       System.out.println("Tool Signature: " + this.toolSignature);
       System.out.println("ToolWebUri: " + this.toolWebUri);
       System.out.println("ToolContext: " + this.toolContext);
       System.out.println("LamsEarPath: " + this.lamsEarPath);
       System.out.println("ToolInsertScriptPath: " + this.toolInsertScriptPath);
       System.out.println("ToolLibraryInsertScriptPath: " + this.toolLibraryInsertScriptPath);
       System.out.println("ToolActivityInsertScriptPath: " + this.toolActivityInsertScriptPath);
       System.out.println("ToolTableScriptPath: " + this.toolTablesScriptPath);
       System.out.println("ToolTableDeleteScriptPath: " + this.toolTablesDeleteScriptPath);
       System.out.println("DbUsername: " + getDbUsername());
       System.out.println("DbPassword: " + getDbPassword());
       System.out.println("DbDriverClass: " + getDbDriverClass());
       System.out.println("DbDriverUrl: " + getDbDriverUrl());  
       ArrayList list = this.deployFiles;
       for(int i=0; i<list.size(); i++)
       {
           System.out.println("DeployFiles: " + list.get(i));
       }
       System.out.println("========End Object Properties=======");
      
   }
   //inherit from parents writePropertiesToFile
 /* public void writePropertiesToFile(Writer writer)
   {
       xstream.toXML(this, writer);       
   } */
  
   /**
     * @return Returns the deployFiles.
     */
    public ArrayList getDeployFiles() {
        return deployFiles;
    }
    /**
     * @param deployFiles The deployFiles to set.
     */
    public void setDeployFiles(ArrayList deployFiles) {
        this.deployFiles = deployFiles;
    }
    /**
     * @return Returns the lamsEarPath.
     */
    public String getLamsEarPath() {
        return lamsEarPath;
    }
    /**
     * @param lamsEarPath The lamsEarPath to set.
     */
    public void setLamsEarPath(String lamsEarPath) {
        this.lamsEarPath = lamsEarPath;
    }
    /**
     * @return Returns the toolActivityInsertScriptPath.
     */
    public String getToolActivityInsertScriptPath() {
        return toolActivityInsertScriptPath;
    }
    /**
     * @param toolActivityInsertScriptPath The toolActivityInsertScriptPath to set.
     */
    public void setToolActivityInsertScriptPath(
            String toolActivityInsertScriptPath) {
        this.toolActivityInsertScriptPath = toolActivityInsertScriptPath;
    }
    /**
     * @return Returns the toolContextRoot.
     */
    public String getToolContext() {
        return toolContext;
    }
    /**
     * @param toolContextRoot The toolContextRoot to set.
     */
    public void setToolContext(String toolContextRoot) {
        this.toolContext = toolContextRoot;
    }
    /**
     * @return Returns the toolInsertScriptPath.
     */
    public String getToolInsertScriptPath() {
        return toolInsertScriptPath;
    }
    /**
     * @param toolInsertScriptPath The toolInsertScriptPath to set.
     */
    public void setToolInsertScriptPath(String toolInsertScriptPath) {
        this.toolInsertScriptPath = toolInsertScriptPath;
    }
    /**
     * @return Returns the toolLibraryInsertScriptPath.
     */
    public String getToolLibraryInsertScriptPath() {
        return toolLibraryInsertScriptPath;
    }
    /**
     * @param toolLibraryInsertScriptPath The toolLibraryInsertScriptPath to set.
     */
    public void setToolLibraryInsertScriptPath(
            String toolLibraryInsertScriptPath) {
        this.toolLibraryInsertScriptPath = toolLibraryInsertScriptPath;
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
     * @return Returns the toolTablesScriptPath.
     */
    public String getToolTablesScriptPath() {
        return toolTablesScriptPath;
    }
    /**
     * @param toolTablesScriptPath The toolTablesScriptPath to set.
     */
    public void setToolTablesScriptPath(String toolTablesScriptPath) {
        this.toolTablesScriptPath = toolTablesScriptPath;
    }
    /**
     * @return Returns the toolWebUri.
     */
    public String getToolWebUri() {
        return toolWebUri;
    }
    /**
     * @param toolWebUri The toolWebUri to set.
     */
    public void setToolWebUri(String toolWebUri) {
        this.toolWebUri = toolWebUri;
    }
}
