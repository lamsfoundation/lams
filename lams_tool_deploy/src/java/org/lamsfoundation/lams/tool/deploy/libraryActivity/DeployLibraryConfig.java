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


package org.lamsfoundation.lams.tool.deploy.libraryActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.lamsfoundation.lams.tool.deploy.DeployConfig;
import org.lamsfoundation.lams.tool.deploy.DeployException;
import org.xml.sax.SAXException;

/**
 * @author mtruong
 *
 *         Encapsulates configuration data for the Library deployer
 */
public class DeployLibraryConfig extends DeployConfig {

    // private static Log log = LogFactory.getLog(DeployLibraryConfig.class);

    private static final String LEARNING_LIBRARY = "learningLibrary";
    private static final String LIBRARY_INSERT_SCRIPT = "libraryInsertScriptPath";
    private static final String TEMPLATE_ACTIVITY_INSERT_SCRIPT = "templateActivityInsertScriptPath";
    private static final String TOOL_ACTIVITY = "toolActivity";
    private static final String LEARNING_LIBRARY_LIST = "learningLibraryList";
    private static final String TOOL_ACTIVITY_LIST = "toolActivityList";

    private ArrayList learningLibraryList;

    public DeployLibraryConfig() {
	super();
    }

    public DeployLibraryConfig(String outputPath) {
	super(outputPath);
	xstream.alias(ROOT_ELEMENT, DeployLibraryConfig.class);
	xstream.alias(LEARNING_LIBRARY, LearningLibrary.class);
	xstream.alias(TOOL_ACTIVITY, ToolActivity.class);
    }

    public DeployLibraryConfig(String dbUsername, String dbPassword, String dbDriverClass, String dbDriverUrl,
	    ArrayList learningLibraries, String outputPath) {
	super(outputPath);
	setDbUsername(dbUsername);
	setDbPassword(dbPassword);
	setDbDriverClass(dbDriverClass);
	setDbDriverUrl(dbDriverUrl);
	this.learningLibraryList = learningLibraries;

	xstream.alias(ROOT_ELEMENT, DeployLibraryConfig.class);
	xstream.alias(LEARNING_LIBRARY, LearningLibrary.class);
	xstream.alias(TOOL_ACTIVITY, ToolActivity.class);
    }

    public DeployLibraryConfig(String configurationFilePath, String outputPath)
	    throws ParserConfigurationException, IOException, SAXException {
	super(outputPath);
	xstream.alias(ROOT_ELEMENT, DeployLibraryConfig.class);
	xstream.alias(LEARNING_LIBRARY, LearningLibrary.class);
	xstream.alias(TOOL_ACTIVITY, ToolActivity.class);
	updateConfigurationProperties(configurationFilePath);
    }

    /**
     * Takes in the file path location of the XML configuration file.
     * Parses the configuration file and will create
     * LearningLibrary object(s) and ToolActivity object(s).
     *
     * @param configFilePath
     *            the file path for the XML configuration file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    @Override
    public void updateConfigurationProperties(String configFilePath)
	    throws ParserConfigurationException, IOException, SAXException {
	String xml = readFile(configFilePath);
	DeployLibraryConfig config = (DeployLibraryConfig) deserialiseXML(xml);
	copyProperties(config);
    }

    /**
     * Upon deserialisation of the xml string, a new object will be created.
     * The properties of this object will be copied to the calling object.
     * Only copy properties if the properties are not null
     * 
     * @param config
     */
    protected void copyProperties(DeployLibraryConfig config) {
	super.copyProperties(config);
	if (config.getLearningLibraryList() != null) {
	    this.setLearningLibraryList(config.getLearningLibraryList());
	}

    }

    @Override
    public void printObjectProperties() {
	super.printObjectProperties();
	ArrayList learningLibraries = getLearningLibraryList();
	for (int j = 0; j < learningLibraries.size(); j++) {
	    LearningLibrary libraryActivity = (LearningLibrary) learningLibraries.get(j);
	    System.out.println("\t Learning Library " + j + "-> libraryInsertScriptPath: "
		    + libraryActivity.getLibraryInsertScriptPath());
	    System.out.println("\t Learning Library " + j + "-> templateActivityInsertScriptPath: "
		    + libraryActivity.getTemplateActivityInsertScriptPath());

	    ArrayList list = libraryActivity.getToolActivityList();
	    for (int i = 0; i < list.size(); i++) {
		ToolActivity a = (ToolActivity) list.get(i);
		System.out.println("\t\tToolActivity " + i + "-> ToolSignature: " + a.getToolSignature());
		System.out.println(
			"\t\tToolActivity " + i + "-> ToolActivityScriptPath: " + a.getToolActivityInsertScriptPath());
	    }
	}
    }

    @Override
    public void validateProperties() throws DeployException {
	boolean valid;
	validationError = ""; // object attribute - will be updated by validateProperty() if something is missing.

	valid = validateStringProperty(getDbUsername(), DB_USERNAME);
	valid = valid && validateStringProperty(getDbPassword(), DB_PASSWORD);
	valid = valid && validateStringProperty(getDbDriverClass(), DB_PASSWORD);
	valid = valid && validateStringProperty(getDbDriverUrl(), DB_DRIVER_URL);
	valid = valid && validateStringProperty(getLamsEarPath(), LAMS_EAR_PATH);

	//iterate through learning libraries
	ArrayList learningLibraries = getLearningLibraryList();
	if (learningLibraries != null) {
	    Iterator libraryIterator = learningLibraries.iterator();
	    while (libraryIterator.hasNext()) {
		LearningLibrary learningLibrary = (LearningLibrary) libraryIterator.next();
		valid = valid
			&& validateStringProperty(learningLibrary.getLibraryInsertScriptPath(), LIBRARY_INSERT_SCRIPT);
		valid = valid && validateStringProperty(learningLibrary.getTemplateActivityInsertScriptPath(),
			TEMPLATE_ACTIVITY_INSERT_SCRIPT);
		ArrayList toolActivities = learningLibrary.getToolActivityList();

		valid = valid && validateListProperty(toolActivities, TOOL_ACTIVITY_LIST);
		if (toolActivities != null) {
		    Iterator toolActivityIterator = toolActivities.iterator();
		    while (toolActivityIterator.hasNext()) {
			ToolActivity toolActivity = (ToolActivity) toolActivityIterator.next();
			valid = valid && validateStringProperty(toolActivity.getToolActivityInsertScriptPath(),
				TOOL_ACTIVITY_INSERT_SCRIPT_PATH);
			valid = valid && validateStringProperty(toolActivity.getToolSignature(), TOOL_SIGNATURE);
		    }
		}

	    }
	}

	if (!valid) {
	    throw new DeployException("Invalid deployment properties: " + validationError);
	}
    }

    /*
     * public void writePropertiesToFile(Writer writer)
     * {
     * xstream.toXML(this, writer);
     * }
     */

    /*
     * protected void setProperty(String key, String value) throws DeployException {
     * if ( key == null )
     * throw new DeployException("Invalid parameter: Key is null. ");
     * 
     * //super.setProperty(key, value);
     * 
     * System.out.println("LibraryConfig " + key + " is: " + value);
     * 
     * if ( key.equalsIgnoreCase(DB_USERNAME) ) {
     * setDbUsername(value);
     * }
     * 
     * if ( key.equalsIgnoreCase(DB_PASSWORD) ) {
     * setDbPassword(value);
     * }
     * 
     * if ( key.equalsIgnoreCase(DB_DRIVER_CLASS) ) {
     * setDbDriverClass(value);
     * }
     * 
     * if ( key.equalsIgnoreCase(DB_DRIVER_URL) ) {
     * setDbDriverUrl(value);
     * }
     * }
     */

    /**
     * @return Returns the learningLibraryList.
     */
    public ArrayList getLearningLibraryList() {
	return learningLibraryList;
    }

    /**
     * @param learningLibraryList
     *            The learningLibraryList to set.
     */
    public void setLearningLibraryList(ArrayList learningLibraryList) {
	this.learningLibraryList = learningLibraryList;
    }

    @Override
    public void convertForInstallers() {
	// No conversion being done at this stage as the updater doesn't use them.
    }
}
