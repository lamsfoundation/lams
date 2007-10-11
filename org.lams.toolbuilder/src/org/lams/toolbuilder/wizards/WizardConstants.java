package org.lams.toolbuilder.wizards;

public class WizardConstants 
{
	// the default template to be used if the user clicks finish on the 
	// first wizard page
	public static final String DEFAULT_TOOL_TEMPLATE="lams_tool_forum";
	
	public static final String DEFAULT_SERVER_VERSION="2.0";
	
	public static final String DEFAULT_VENDOR="LAMS foundation";
	
	public static final String DEFAULT_TOOL_VERSION="20071100";
	
	/*
	 * Regular expression for the required form of the tool signature
	 * Takes the form [mmttttvv] where:
	 * mm is a two character name representing the maker
	 * tttt is a two character name representing the tool
	 * vv is a two character number representing the major and minor versions of the tool
	 * 
	 * for example with lasurv10 "la" represents "lams" "surv" represents "survey"
	 * and "10" represents version 1.0
	 */ 	
	public static final String TOOL_SIG_REGEX = "^[a-zA-Z]{3,6}\\d\\d$";
	
	/*
	 * Regex for the vendor string, must only contain alpha-numeric numbers
	 * and it must start with an alphabetic character
	 */
	public static final String VENDOR_REGEX = "^[a-zA-Z][$a-zA-z0-9\\s]*+$";
	
	/*
	 * Regex for the tool name
	 * Must start with alpha numeric
	 * Then can have optionally many letters, numbers, spaces or underscores
	 */
	public static final String TOOL_NAME_REGEX = "^[a-zA-Z][$a-zA-z0-9\\s_]*+$";
	
	/*
	 * Regex for the server version string,
	 * must take the form 2.10.3.2 or something simular
	 * Ie, starts with 1 or many numbers, followed by a .
	 * followed by 1 or mant numvers... etc
	 */
	public static final String SERVER_VERSION_REGEX = "^2(\\.\\d+)*+$";
	
	/*
	 * Regex for tool version
	 * should just be a string of numbers 8 characters long
	 * to denote the date
	 */
	public static final String TOOL_VERSION_REGEX = "^\\d{8}$";
	
	
	public static final String TOOL_NAME_ERROR = 
		"Tool name must only contain alpha-numeric characters\n" +
		"and must start with an alphabetic character.";
	
	public static final String TOOL_VERSION_ERROR = 
		"Tool version must be a string of numbers denoting the date\n " +
		"the tool is created in the form yyyymmdd.";
	
	public static final String SERVER_VERSION_ERROR = 
		"LAMS2 Server version must start with a 2, followed by any number\n" +
		"of major and minor versions. eg 2.0.4";
	
	public static final String VENDOR_ERROR =
		"Vendor name must start with a alphabetic character and only\n" +
		"contain alpha-numeric characters.";
	
	public static final String TOOL_SIG_ERROR =
		"Tool signature must follow the form mmttttvv where mm is the maker, tttt\n" +
		"is the abbreviated tool name and vv is the minor and major tool versions.";
	
	public static final String TOOL_SIG_EXISTS_ERROR = 
		"The tool signature given is the same as an existing default tool.\n" +
		"Please enter a new unique tool signature.";
	
	public static final String TOOL_SIG_TOOL_TIP = 
		"Enter tool signature here. It should follow the form mmttttvv where " +
		"mm = maker, ttttt = tool name, and vv = major and minor version numbers.";

	public static final String TOOL_NAME_TOOL_TIP = 
		"This name will be used to rename your chosen LAMS tool template.";
	
	public static final String TOOL_VENDOR_TOOL_TIP = 
		"Put your vendor name here.";
	
	public static final String SERVER_VERSION_TOOL_TIP = 
		"Put the minimum required server" +
		" version of LAMS here. 2.0 is the default.";
	
	public static final String TOOL_VERSION_TOOL_TIP = 
		"The tool version should be a string of numbers denoting the date" +
		" the tool is created in the form yyyymmdd.";
	
	
	public static final String PAGE_COMPLETE_MESSAGE = 
		"Click next to go to the templates page.";
	
	public static final String PLEASE_ENTER_DETAILS =
		"One or more required fields are missing\n" +
		"Please enter the details below for your new LAMS tool.";	
	
	public static final String PLEASE_CLICK_NEXT =
		"No template chosen.\nClick next to go to the templates page";
	
	public static final String SUCCESS_MESSAGE =
		"Tool created successfully.\n\n" +
		"To build and deploy the tool, first run a normal LAMS build by\n" +
		"running the following ant tasks in lams_build/build.xml.\n" +
		"* rebuild-db\n" +
		"* assemble-ear\n" +
		"* deploy-ear\n" +
		"* copyfiles\n" +
		"* deploy-tools\n\n" +
		"Once you have LAMS core deployed, you can run the tool's 'deploy'\n" +
		"task in its build.xml. Then run LAMS to check that the tool appears in author.";
}
