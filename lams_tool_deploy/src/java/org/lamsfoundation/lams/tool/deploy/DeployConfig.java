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

/*
 * Created on 24/11/2005
 */
package org.lamsfoundation.lams.tool.deploy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.lamsfoundation.lams.tool.deploy.libraryActivity.DeployLibraryConfig;
import org.lamsfoundation.lams.tool.deploy.libraryActivity.LearningLibrary;
import org.lamsfoundation.lams.tool.deploy.libraryActivity.ToolActivity;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author mtruong
 *
 *         Parent of the config related classes.
 *
 *         Contains the shared constant definitions that is used by child classes
 *         DeployToolConfig and DeployLibraryConfig.
 *         Instantiates an XStream object which is used by its subclasses to specify
 *         aliases for class names to XML elements. See DeployToolConfig or
 *         DeployLibraryConfig for more details.
 *
 *         All subclasses must implement the methods updateConfigurationProperties and validateProperties.
 *
 *         Some variables are declared as transient so it won't be serialized into XML
 *
 */
public abstract class DeployConfig {

    /* Common tags in the XML file */
    public static final String ROOT_ELEMENT = "Deploy";
    public static final String DB_USERNAME = "dbUsername";
    public static final String DB_PASSWORD = "dbPassword";
    public static final String DB_DRIVER_CLASS = "dbDriverClass";
    public static final String DB_DRIVER_URL = "dbDriverUrl";
    public static final String TOOL_SIGNATURE = "toolSignature";
    public static final String TOOL_VERSION = "toolVersion";
    public static final String HIDE_TOOL = "hideTool";
    public static final String TOOL_ACTIVITY_INSERT_SCRIPT_PATH = "toolActivityInsertScriptPath";
    public static final String LAMS_EAR_PATH = "lamsEarPath";
    public static final String I8N_LANGUAGE_FILES_PACKAGE = "languageFilesPackage";

    protected transient String validationError = "";
    protected transient XStream xstream;

    protected transient boolean generateForInstallers = false;
    protected transient String outputPath = null;

    protected DeployConfig() {

    }

    /**
     * Holds value of property toolVersion.
     */
    protected String toolVersion;

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
    /**
     * Holds value of property lamsEarPath.
     */
    private String lamsEarPath;

    /**
     * Holds the value of the package where I8N files are store
     * Export format org.lamsfoundation.lams.tool.web
     */
    private String languageFilesPackage;

    public DeployConfig(String outputPath) {
	this.validationError = "";
	xstream = new XStream(new DomDriver());
	Class<?>[] classes = new Class[] { DeployToolConfig.class, DeployLibraryConfig.class, LearningLibrary.class,
		ToolActivity.class };
	xstream.allowTypes(classes);

	this.outputPath = outputPath != null ? outputPath : "";
    }

    /**
     * In general, this method should read in the xml configuration file,
     * deserialise the xml string into an object, and copy the properties
     * to the calling object.
     *
     * @param configFilePath
     *            The file location of the configuration file deploy.xml.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public abstract void updateConfigurationProperties(String configFilePath)
	    throws ParserConfigurationException, IOException, SAXException;

    /**
     * Check that all the properties have a value, will throw an exception if a property does not have a value
     *
     * @throws DeployException
     */
    public abstract void validateProperties() throws DeployException;

    /**
     * Validates the value of a property. If the property does not exist, or
     * the property doesn't have a value, it will return false.
     * Otherwise return true.
     *
     * @param property
     * @param key
     * @return
     */
    protected boolean validateStringProperty(String property, String key) {
	if (((property == null) || (property.length() < 1))) {
	    validationError = validationError + "Property " + key + " is missing or has no value.";
	    return false;
	}
	return true;
    }

    /**
     * Will return false if the list property is empty or null. *
     *
     * @param property
     * @param key
     * @return
     */
    protected boolean validateListProperty(List property, String key) {
	if (((property == null) || (property.isEmpty()))) {
	    validationError = validationError + "Property " + key + " is missing or has no value.";
	    return false;
	}
	return true;
    }

    /**
     * Reads in the deploy configuration xml file, and will return the xml
     * as a string.
     *
     * @param configFilePath
     * @return String. The xml contents as a String
     */
    protected String readFile(String configFilePath) {
	BufferedReader in;
	StringBuffer tempStrBuf;
	String tempStr;
	try {
	    in = new BufferedReader(new FileReader(configFilePath));
	    tempStrBuf = new StringBuffer();

	    tempStr = in.readLine();
	    while (tempStr != null) {
		tempStrBuf.append(tempStr);
		tempStr = in.readLine();
	    }
	} catch (IOException e) {
	    throw new DeployException("An error has occurred while trying to read file " + configFilePath);
	}

	return (tempStrBuf.toString());
    }

    /**
     * Takes in the xml string and deserialises it to an object
     *
     * @param xml
     *            The xml to deserialise
     * @return the object constructed from the xml
     */
    protected Object deserialiseXML(String xml) {
//	System.out.println(xml);
	return xstream.fromXML(xml);
    }

    /**
     * Set an arbitrary property. Note: these will probably have come through
     * ant, and that removes the case of the key, so ignore case.
     *
     * @param key
     * @param value
     * @throws DeployException
     */
    protected void setProperty(String key, String value) throws DeployException {
	if (key == null) {
	    throw new DeployException("Invalid parameter: Key is null. ");
	    //System.out.println(key + " is: " + value);
	}

	if (key.equalsIgnoreCase(DB_USERNAME)) {
	    setDbUsername(value);
	}

	if (key.equalsIgnoreCase(DB_PASSWORD)) {
	    setDbPassword(value);
	}

	if (key.equalsIgnoreCase(DB_DRIVER_CLASS)) {
	    setDbDriverClass(value);
	}

	if (key.equalsIgnoreCase(DB_DRIVER_URL)) {
	    setDbDriverUrl(value);
	}

	if (key.equalsIgnoreCase(I8N_LANGUAGE_FILES_PACKAGE)) {
	    setLanguageFilesPackage(value);
	}

	if (key.equalsIgnoreCase(LAMS_EAR_PATH)) {
	    setLamsEarPath(value);
	}

    }

    /**
     * Pass through some filename lists to be assigned to a parameter
     */
    protected void setFilenames(String key, ArrayList<String> filenames) throws DeployException {
	if (key == null) {
	    throw new DeployException("Invalid parameter: Key is null. ");
	}

	// no filelists are known to the basic configuration.
    }

    /**
     * Serialize an object to the given Writer as pretty-printed XML.
     *
     * @param writer
     */
    protected void writePropertiesToFile(Writer writer) {
	xstream.toXML(this, writer);
    }

    /**
     * @return Returns the dbDriverClass.
     */
    public String getDbDriverClass() {
	return dbDriverClass;
    }

    /**
     * @param dbDriverClass
     *            The dbDriverClass to set.
     */
    public void setDbDriverClass(String dbDriverClass) {
	this.dbDriverClass = dbDriverClass;
    }

    /**
     * @return Returns the dbDriverUrl.
     */
    public String getDbDriverUrl() {
	return dbDriverUrl;
    }

    /**
     * @param dbDriverUrl
     *            The dbDriverUrl to set.
     */
    public void setDbDriverUrl(String dbDriverUrl) {
	this.dbDriverUrl = dbDriverUrl;
    }

    /**
     * @return Returns the dbPassword.
     */
    public String getDbPassword() {
	return dbPassword;
    }

    /**
     * @param dbPassword
     *            The dbPassword to set.
     */
    public void setDbPassword(String dbPassword) {
	this.dbPassword = dbPassword;
    }

    /**
     * @return Returns the dbUsername.
     */
    public String getDbUsername() {
	return dbUsername;
    }

    /**
     * @param dbUsername
     *            The dbUsername to set.
     */
    public void setDbUsername(String dbUsername) {
	this.dbUsername = dbUsername;
    }

    public String getLanguageFilesPackage() {
	return languageFilesPackage;
    }

    public void setLanguageFilesPackage(String languageFilesPackage) {
	this.languageFilesPackage = languageFilesPackage;
    }

    /**
     * @return Returns the lamsEarPath.
     */
    public String getLamsEarPath() {
	return lamsEarPath;
    }

    /**
     * @param lamsEarPath
     *            The lamsEarPath to set.
     */
    public void setLamsEarPath(String lamsEarPath) {
	this.lamsEarPath = lamsEarPath;
    }

    /**
     * @return Returns the toolVersion.
     */
    public String getToolVersion() {
	return toolVersion;
    }

    /** Used for testing purposes only */
    public void printObjectProperties() {
	System.out.println("========Object Properties=======");
	System.out.println("DbUsername: " + getDbUsername());
	System.out.println("DbPassword: " + getDbPassword());
	System.out.println("DbDriverClass: " + getDbDriverClass());
	System.out.println("DbDriverUrl: " + getDbDriverUrl());
	System.out.println("LanguageFilesPackage: " + getLanguageFilesPackage());
	System.out.println("LamsEarPath: " + getLamsEarPath());
    }

    /**
     * Upon deserialisation of the xml string, a new object will be created.
     * The properties of this object will be copied to the calling object.
     * Only copy properties if the properties are not null
     *
     * @param config
     */
    protected void copyProperties(DeployConfig config) {
	if (config.getDbUsername() != null) {
	    this.setDbUsername(config.getDbUsername());
	}
	if (config.getDbPassword() != null) {
	    this.setDbPassword(config.getDbPassword());
	}
	if (config.getDbDriverUrl() != null) {
	    this.setDbDriverUrl(config.getDbDriverUrl());
	}
	if (config.getDbDriverClass() != null) {
	    this.setDbDriverClass(config.getDbDriverClass());
	}
	if (config.getLanguageFilesPackage() != null) {
	    this.setLanguageFilesPackage(config.getLanguageFilesPackage());
	}
	if (config.getLamsEarPath() != null) {
	    this.setLamsEarPath(config.getLamsEarPath());
	}
    }

    public boolean isGenerateForInstallers() {
	return generateForInstallers;
    }

    public void setGenerateForInstallers(boolean generateForInstallers) {
	this.generateForInstallers = generateForInstallers;
    }

    public String getOutputPath() {
	return outputPath;
    }

    public void setOutputPath(String outputPath) {
	this.outputPath = outputPath;
    }

    public abstract void convertForInstallers();

}
