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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.deploy;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * @author mtruong
 *
 *         Subclass of DeployConfig.
 *
 *         Encapsulates the configuration data for the Tool Deployer.
 *
 *         The XML outputted by XStream will contain the absolute classname if
 *         an alias is not specified. To make the XML output more concise, the XML elememnt
 *         "Deploy" is mapped to this class (org.lamsfoundation.lams.tool.deploy.DeployToolConfig class)
 *
 *         See templateDeployTool.xml for the example XML format/structure
 */
public class DeployToolConfig extends DeployConfig {

    // private static Log log = LogFactory.getLog(DeployToolConfig.class);

    private static final String TOOL_WEB_URI = "toolWebUri";
    private static final String TOOL_CONTEXT = "toolContext";
    private static final String TOOL_INSERT_SCRIPT_PATH = "toolInsertScriptPath";
    private static final String TOOL_LIBRARY_INSERT_SCRIPT_PATH = "toolLibraryInsertScriptPath";
    private static final String TOOL_TABLES_SCRIPT_PATH = "toolTablesScriptPath";
    private static final String TOOL_DB_VERSION_SCRIPT_PATH = "toolDBVersionScriptPath";
    private static final String TOOL_APP_CONTEXT_FILE_PATH = "toolApplicationContextPath";
    private static final String TOOL_JAR_FILE_NAME = "toolJarFileName";
    private static final String DEPLOY_FILES = "deployFiles";
    private static final String MIN_SERVER_VERSION_NUMBER = "minServerVersionNumber";
    private static final String GENERATE_XML_FOR_INSTALLERS = "generateForInstallers";
    protected static final String LANGUAGE_FILES = "languageFiles";

    public DeployToolConfig() {
	super();
    }

    /**
     * Holds value of property toolSignature.
     */
    private String toolSignature;

    /**
     * Holds value of property toolVersion.
     */
    //private String toolVersion;

    /**
     * Holds value of property toolWebUri.
     */
    private String toolWebUri;

    /**
     * Holds value of property toolContextRoot.
     */
    private String toolContext;

    /**
     * Holds value of property toolInsertScriptPath.
     */
    private String toolInsertScriptPath;

    /**
     * Holds value of property toolLibraryInsertScriptPath.
     */
    private String toolLibraryInsertScriptPath;

    /**
     * Holds the value of property hideTool
     */
    private boolean hideTool;

    /**
     * Holds value of property toolActivityInsertScriptPath.
     */
    private String toolActivityInsertScriptPath;

    /**
     * Holds value of property toolTablesScriptPath.
     */
    private String toolTablesScriptPath;

    /**
     * Holds value of property toolDBVersionScriptPath.
     */
    private String toolDBVersionScriptPath;

    private String toolApplicationContextPath;

    /**
     * Holds the value of property toolJarFileName.
     */
    private String toolJarFileName;

    /**
     * Holds the value of property minServerVersionNumber.
     */
    private String minServerVersionNumber;

    /**
     * Holds value of property deployFiles.
     */
    private ArrayList<String> deployFiles;

    /**
     * Holds value of property languageFiles.
     */
    private ArrayList<String> languageFiles;

    /**
     * Creates an instance of DeployToolConfig object.
     */
    public DeployToolConfig(String outputPath) {
	super(outputPath);
	xstream.alias(ROOT_ELEMENT, DeployToolConfig.class);
    }

    /**
     * Creates an instance of DeployToolConfig object, with the values
     * of its properties, set to that specified by the Xml configuration file
     *
     * @param configurationFilePath
     *            - only needed when generating the initial deploy.xml
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public DeployToolConfig(String outputPath, String configurationFilePath)
	    throws ParserConfigurationException, IOException, SAXException {
	super(outputPath);
	xstream.alias(ROOT_ELEMENT, DeployToolConfig.class);
	updateConfigurationProperties(configurationFilePath);
    }

    /** @see org.lamsfoundation.lams.tool.deploy.DeployConfig#updateConfigurationProperties(String) */
    @Override
    public void updateConfigurationProperties(String configFilePath)
	    throws ParserConfigurationException, IOException, SAXException {
	String xml = readFile(configFilePath);
	DeployToolConfig config = (DeployToolConfig) deserialiseXML(xml);
	copyProperties(config);
	printObjectProperties(); //for testing purposes
    }

    /**
     * Set an arbitrary property. Note: these will probably have come through
     * ant, and that removes the case of the key, so ignore case.
     */
    @Override
    protected void setProperty(String key, String value) throws DeployException {
	if (key == null) {
	    throw new DeployException("Invalid parameter: Key is null. ");
	}

	super.setProperty(key, value);
	//System.out.println("ToolConfig " + key + " is: " + value);

	if (key.equalsIgnoreCase(GENERATE_XML_FOR_INSTALLERS)) {
	    generateForInstallers = java.lang.Boolean.parseBoolean(value);
	    System.out.print("generateForInstallers" + generateForInstallers);
	}

	if (key.equalsIgnoreCase(TOOL_SIGNATURE)) {

	    toolSignature = value;
	}

	if (key.equalsIgnoreCase(TOOL_VERSION)) {

	    toolVersion = value;
	}

	if (key.equalsIgnoreCase(HIDE_TOOL)) {
	    hideTool = java.lang.Boolean.parseBoolean(value);
	}

	if (key.equalsIgnoreCase(TOOL_WEB_URI)) {
	    toolWebUri = value;
	}

	if (key.equalsIgnoreCase(TOOL_CONTEXT)) {
	    toolContext = value;
	}

	if (key.equalsIgnoreCase(TOOL_INSERT_SCRIPT_PATH)) {
	    toolInsertScriptPath = value;
	}

	if (key.equalsIgnoreCase(TOOL_LIBRARY_INSERT_SCRIPT_PATH)) {
	    toolLibraryInsertScriptPath = value;
	}

	if (key.equalsIgnoreCase(TOOL_ACTIVITY_INSERT_SCRIPT_PATH)) {
	    toolActivityInsertScriptPath = value;
	}

	if (key.equalsIgnoreCase(TOOL_TABLES_SCRIPT_PATH)) {
	    toolTablesScriptPath = value;
	}

	if (key.equalsIgnoreCase(TOOL_DB_VERSION_SCRIPT_PATH)) {
	    toolDBVersionScriptPath = value;
	}

	if (key.equalsIgnoreCase(TOOL_APP_CONTEXT_FILE_PATH)) {
	    toolApplicationContextPath = value;
	}

	if (key.equalsIgnoreCase(TOOL_JAR_FILE_NAME)) {
	    toolJarFileName = value;
	}

	if (key.equalsIgnoreCase(MIN_SERVER_VERSION_NUMBER)) {
	    minServerVersionNumber = value;
	}

	if (key.equalsIgnoreCase(DEPLOY_FILES)) {
	    deployFiles = convertList(value);
	}

    }

    /**
     * Pass through some filename lists to be assigned to a parameter
     */
    @Override
    protected void setFilenames(String key, ArrayList<String> filenames) throws DeployException {
	if (key == null) {
	    throw new DeployException("Invalid parameter: Key is null. ");
	}

	super.setFilenames(key, filenames);

	if (key.equalsIgnoreCase(LANGUAGE_FILES)) {
	    setLanguageFiles(filenames);
	}
    }

    /**
     * Converts a String to a List. Entries should be comma separated.
     *
     * @param Input
     *            string containing entries.
     * @return List of (String) properties, null if not found.
     */
    protected ArrayList<String> convertList(String input) throws DeployException {
	String[] strings = input.split(",");
	ArrayList<String> list = new ArrayList<String>(strings.length);
	for (String value : strings) {
	    list.add(value);
	}
	return list;
    }

    /**
     * Check that all the correct properties exist - tool delete path is optional.
     * toolDBVersionScriptPath is also optional.
     */
    @Override
    public void validateProperties() throws DeployException {
	boolean valid;
	validationError = ""; // object attribute - will be updated by validateProperty() if something is missing.

	valid = validateStringProperty(toolWebUri, TOOL_WEB_URI);
	valid = valid && validateStringProperty(toolContext, TOOL_CONTEXT);
	valid = valid && validateStringProperty(getLamsEarPath(), LAMS_EAR_PATH);
	valid = valid && validateListProperty(getLanguageFiles(), LANGUAGE_FILES);
	valid = valid && validateStringProperty(toolInsertScriptPath, TOOL_INSERT_SCRIPT_PATH);
	valid = valid && validateStringProperty(toolLibraryInsertScriptPath, TOOL_LIBRARY_INSERT_SCRIPT_PATH);
	valid = valid && validateStringProperty(toolActivityInsertScriptPath, TOOL_ACTIVITY_INSERT_SCRIPT_PATH);
	//valid = valid && validateStringProperty(toolDBVersionScriptPath, TOOL_DB_VERSION_SCRIPT_PATH);
	valid = valid && validateStringProperty(toolWebUri, TOOL_TABLES_SCRIPT_PATH);
	valid = valid && validateStringProperty(toolApplicationContextPath, TOOL_APP_CONTEXT_FILE_PATH);
	valid = valid && validateStringProperty(toolJarFileName, TOOL_JAR_FILE_NAME);
	valid = valid && validateStringProperty(minServerVersionNumber, MIN_SERVER_VERSION_NUMBER);
	valid = valid && validateStringProperty(getDbUsername(), DB_USERNAME);
	valid = valid && validateStringProperty(getDbPassword(), DB_PASSWORD);
	valid = valid && validateStringProperty(getDbDriverClass(), DB_PASSWORD);
	valid = valid && validateStringProperty(getDbDriverUrl(), DB_DRIVER_URL);
	valid = valid && validateListProperty(deployFiles, DEPLOY_FILES);

	if (!valid) {
	    throw new DeployException("Invalid deployment properties: " + validationError);
	}
    }

    /**
     * Upon deserialisation of the xml string, a new object will be created.
     * The properties of this object will be copied to the calling object.
     * Only copy properties if the properties are not null
     *
     * @param config
     */
    protected void copyProperties(DeployToolConfig config) {
	super.copyProperties(config);
	if (config.getToolSignature() != null) {
	    this.toolSignature = config.getToolSignature();
	}
	if (config.getToolVersion() != null) {
	    this.toolVersion = config.getToolVersion();
	}
	this.hideTool = config.getHideTool();
	if (config.getToolWebUri() != null) {
	    this.toolWebUri = config.getToolWebUri();
	}
	if (config.getToolContext() != null) {
	    this.toolContext = config.getToolContext();
	}
	if (config.getToolInsertScriptPath() != null) {
	    this.toolInsertScriptPath = config.getToolInsertScriptPath();
	}
	if (config.getToolLibraryInsertScriptPath() != null) {
	    this.toolLibraryInsertScriptPath = config.getToolLibraryInsertScriptPath();
	}
	if (config.getToolActivityInsertScriptPath() != null) {
	    this.toolActivityInsertScriptPath = config.getToolActivityInsertScriptPath();
	}
	if (config.getToolTablesScriptPath() != null) {
	    this.toolTablesScriptPath = config.getToolTablesScriptPath();
	}
	if (config.getToolDBVersionScriptPath() != null) {
	    this.toolDBVersionScriptPath = config.getToolDBVersionScriptPath();
	}
	if (config.getToolApplicationContextPath() != null) {
	    this.toolApplicationContextPath = config.getToolApplicationContextPath();
	}
	if (config.getToolJarFileName() != null) {
	    this.toolJarFileName = config.getToolJarFileName();
	}
	if (config.getMinServerVersionNumber() != null) {
	    this.minServerVersionNumber = config.getMinServerVersionNumber();
	}
	if (config.getDeployFiles() != null) {
	    this.deployFiles = config.getDeployFiles();
	}
	if (config.getLanguageFiles() != null) {
	    this.setLanguageFiles(config.getLanguageFiles());
	}

    }

    /** Used for testing purposes only */
    @Override
    public void printObjectProperties() {
	super.printObjectProperties();
	System.out.println("Tool Signature: " + this.toolSignature);
	System.out.println("Tool Version: " + this.toolVersion);
	System.out.println("Hide Tool: " + this.hideTool);
	System.out.println("ToolWebUri: " + this.toolWebUri);
	System.out.println("ToolContext: " + this.toolContext);
	System.out.println("ToolInsertScriptPath: " + this.toolInsertScriptPath);
	System.out.println("ToolLibraryInsertScriptPath: " + this.toolLibraryInsertScriptPath);
	System.out.println("ToolActivityInsertScriptPath: " + this.toolActivityInsertScriptPath);
	System.out.println("ToolTableScriptPath: " + this.toolTablesScriptPath);
	System.out.println("ToolDBVersionScriptPath: " + this.toolDBVersionScriptPath);
	System.out.println("ToolApplicationContextPath: " + this.toolApplicationContextPath);
	System.out.println("ToolJarFileName: " + this.toolJarFileName);
	System.out.println("MinServerVersionNumber: " + this.minServerVersionNumber);

	ArrayList list = this.deployFiles;
	for (int i = 0; i < list.size(); i++) {
	    System.out.println("DeployFiles: " + list.get(i));
	}
	list = getLanguageFiles();
	for (int i = 0; i < list.size(); i++) {
	    System.out.println("LanguageFiles: " + list.get(i));
	}
    }
    //inherit from parents writePropertiesToFile
    /*
     * public void writePropertiesToFile(Writer writer)
     * {
     * xstream.toXML(this, writer);
     * }
     */

    /**
     * @return Returns the deployFiles.
     */
    public ArrayList<String> getDeployFiles() {
	return deployFiles;
    }

    /**
     * @param deployFiles
     *            The deployFiles to set.
     */
    public void setDeployFiles(ArrayList<String> deployFiles) {
	this.deployFiles = deployFiles;
    }

    /**
     * @return Returns the toolActivityInsertScriptPath.
     */
    public String getToolActivityInsertScriptPath() {
	return toolActivityInsertScriptPath;
    }

    /**
     * @param toolActivityInsertScriptPath
     *            The toolActivityInsertScriptPath to set.
     */
    public void setToolActivityInsertScriptPath(String toolActivityInsertScriptPath) {
	this.toolActivityInsertScriptPath = toolActivityInsertScriptPath;
    }

    /**
     * @return Returns the toolContextRoot.
     */
    public String getToolContext() {
	return toolContext;
    }

    /**
     * @param toolContextRoot
     *            The toolContextRoot to set.
     */
    public void setToolContext(String toolContextRoot) {
	this.toolContext = toolContextRoot;
    }

    /**
     * @return Returns the thideTool.
     */
    public boolean getHideTool() {
	return hideTool;
    }

    /**
     * @param hideTool
     *            The toolContextRoot to set.
     */
    public void setHideTool(boolean hideTool) {
	this.hideTool = hideTool;
    }

    /**
     * @return Returns the toolInsertScriptPath.
     */
    public String getToolInsertScriptPath() {
	return toolInsertScriptPath;
    }

    /**
     * @param toolInsertScriptPath
     *            The toolInsertScriptPath to set.
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
     * @param toolLibraryInsertScriptPath
     *            The toolLibraryInsertScriptPath to set.
     */
    public void setToolLibraryInsertScriptPath(String toolLibraryInsertScriptPath) {
	this.toolLibraryInsertScriptPath = toolLibraryInsertScriptPath;
    }

    /**
     * @return Returns the toolSignature.
     */
    public String getToolSignature() {
	return toolSignature;
    }

    /**
     * @return Returns the toolVersion.
     */
    //public String getToolVersion() {
    //    return toolVersion;
    //}
    /**
     * @param toolSignature
     *            The toolSignature to set.
     */
    public void setToolSignature(String toolSignature) {
	this.toolSignature = toolSignature;
    }

    /**
     * @param toolVersion
     *            The toolVersion to set.
     */
    public void setToolVersion(String toolVersion) {
	this.toolVersion = toolVersion;
    }

    /**
     * @return Returns the toolTablesScriptPath.
     */
    public String getToolTablesScriptPath() {
	return toolTablesScriptPath;
    }

    /**
     * @param toolTablesScriptPath
     *            The toolTablesScriptPath to set.
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
     * @param toolWebUri
     *            The toolWebUri to set.
     */
    public void setToolWebUri(String toolWebUri) {
	this.toolWebUri = toolWebUri;
    }

    public ArrayList<String> getLanguageFiles() {
	return languageFiles;
    }

    /**
     * @return Returns the toolDBVersionScriptPath.
     */
    public String getToolDBVersionScriptPath() {
	return toolDBVersionScriptPath;
    }

    /**
     * @param toolDBVersionScriptPath
     *            The toolDBVersionScriptPath to set.
     */
    public void setToolDBVersionScriptPath(String toolDBVersionScriptPath) {
	this.toolDBVersionScriptPath = toolDBVersionScriptPath;
    }

    public void setLanguageFiles(ArrayList<String> languageFiles) {
	this.languageFiles = languageFiles;
    }

    public String getToolApplicationContextPath() {
	return toolApplicationContextPath;
    }

    public void setToolApplicationContextPath(String applicationContextPath) {
	this.toolApplicationContextPath = applicationContextPath;
    }

    public String getMinServerVersionNumber() {
	return minServerVersionNumber;
    }

    public void setMinServerVersionNumber(String minServerVersionNumber) {
	this.minServerVersionNumber = minServerVersionNumber;
    }

    public String getToolJarFileName() {
	return toolJarFileName;
    }

    public void setToolJarFileName(String toolJarFileName) {
	this.toolJarFileName = toolJarFileName;
    }

    @Override
    public void convertForInstallers() {
	if (isGenerateForInstallers()) {
	    System.out.println("Stripping paths output path " + outputPath);
	    int lengthOfPath = outputPath.length();
	    toolInsertScriptPath = stripPath(toolInsertScriptPath, outputPath, lengthOfPath);
	    toolLibraryInsertScriptPath = stripPath(toolLibraryInsertScriptPath, outputPath, lengthOfPath);
	    toolActivityInsertScriptPath = stripPath(toolActivityInsertScriptPath, outputPath, lengthOfPath);
	    toolTablesScriptPath = stripPath(toolTablesScriptPath, outputPath, lengthOfPath);
	    if (toolDBVersionScriptPath != null && toolDBVersionScriptPath.trim().length() > 0) {
		toolDBVersionScriptPath = stripPath(toolDBVersionScriptPath, outputPath, lengthOfPath);
	    }

	    ArrayList<String> newLanguageFiles = new ArrayList<String>(languageFiles.size());
	    for (String file : languageFiles) {
		newLanguageFiles.add(stripPath(file, outputPath, lengthOfPath));
	    }
	    languageFiles = newLanguageFiles;

	    ArrayList<String> newDeployFiles = new ArrayList<String>(deployFiles.size());
	    for (String file : deployFiles) {
		newDeployFiles.add(stripPath(file, outputPath, lengthOfPath));
	    }
	    deployFiles = newDeployFiles;
	    ;

	    setDbDriverClass("@dbDriverClass@");
	    setDbDriverUrl("@dbDriverUrl@");
	    setDbUsername("@dbUsername@");
	    setDbPassword("@dbPassword@");
	    setLamsEarPath("@lamsear@");
	}
    }

    private String stripPath(String path, String outputPath, int lengthOfPath) {
	System.out.println("script path " + path + " index of " + path.indexOf(outputPath));
	if (path.indexOf(outputPath) == 0) {
	    return "@toolDeployPackageDir@" + path.substring(lengthOfPath);
	} else {
	    String convertedPath = path.replace('/', '\\');
	    if (convertedPath.indexOf(outputPath) == 0) {
		return "@toolDeployPackageDir@" + convertedPath.substring(lengthOfPath);
	    }
	    return path;
	}
    }
}
