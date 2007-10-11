package org.lams.toolbuilder.util;

import java.util.List;
import java.util.ArrayList;

public class Constants {

	// Constants used for build.properties file generation
	public static final String PROP_TOOL_NAME = "tool.name";
	public static final String PROP_SIGNATURE = "signature";
	public static final String PROP_TOOL_VERSION = "tool.version";
	public static final String PROP_PACKAGE = "package";
	public static final String PROP_PACKAGE_PATH = "package.path";
	public static final String PROP_SERVER_VERSION = "min.server.version.number";
	public static final String PROP_LANG_PACKAGE = "language.files.package";
	public static final String PROP_HIDE_TOOL = "hideTool";
	
	
	// Tool folder names for all default LAMS tools
	public static final String CHAT_TOOL_DIR = "lams_tool_chat";
	public static final String FORUM_TOOL_DIR = "lams_tool_forum";
	public static final String MC_TOOL_DIR = "lams_tool_lamc";
	public static final String QA_TOOL_DIR = "lams_tool_laqa";
	public static final String SHARE_RESOURCE_TOOL_DIR = "lams_tool_larsrc";
	public static final String NOTICEBOARD_TOOL_DIR = "lams_tool_nb";
	public static final String NOTEBOOK_TOOL_DIR = "lams_tool_notebook";
	public static final String SUBMIT_TOOL_DIR = "lams_tool_sbmt";
	public static final String SCRIBE_TOOL_DIR = "lams_tool_scribe";
	public static final String SURVEY_TOOL_DIR = "lams_tool_survey";
	public static final String VOTE_TOOL_DIR = "lams_tool_vote";
	
	// A list of LAMS default tools
	public static String[] defaultTools;
	
	/**
	 * Instantiates the defaultTools variable
	 * We are only making forum, share resources, noticeboard and notebook 
	 * available becuase the others are either too complicated or too badly 
	 * structured for re-use
	 */
	public static void initToolProjects()
	{
		defaultTools = new String[]
		{
			//CHAT_TOOL_DIR,
			FORUM_TOOL_DIR,
			//MC_TOOL_DIR,
			//QA_TOOL_DIR,
			SHARE_RESOURCE_TOOL_DIR,
			NOTICEBOARD_TOOL_DIR,
			NOTEBOOK_TOOL_DIR//,
			//SUBMIT_TOOL_DIR,
			//SCRIBE_TOOL_DIR,
			//SURVEY_TOOL_DIR,
			//VOTE_TOOL_DIR
		};
	}
	
	
	//	 Tool signatures for all default LAMS tools
	public static final String CHAT_TOOL_SIG = "lachat11";
	public static final String FORUM_TOOL_SIG = "lafrum11";
	public static final String MC_TOOL_SIG = "lamc11";
	public static final String QA_TOOL_SIG = "laqa11";
	public static final String SHARE_RESOURCES_TOOL_SIG = "larsrc11";
	public static final String NOTICEBOARD_TOOL_SIG = "lanb11";
	public static final String NOTEBOOK_TOOL_SIG = "lantbk11";
	public static final String SUBMIT_TOOL_SIG = "lasbmt11";
	public static final String SCRIBE_TOOL_SIG = "lascrb11";
	public static final String SURVEY_TOOL_SIG = "lasurv11";
	public static final String VOTE_TOOL_SIG = "lavote11";
	

	
	public static String[] getToolSignatures()
	{
		String[] defaultToolSignatures = new String[]
		{
			CHAT_TOOL_SIG,
			FORUM_TOOL_SIG,
			MC_TOOL_SIG,
			QA_TOOL_SIG,
			SHARE_RESOURCES_TOOL_SIG,
			NOTICEBOARD_TOOL_SIG,
			NOTEBOOK_TOOL_SIG,
			SUBMIT_TOOL_SIG,
			SCRIBE_TOOL_SIG,
			SURVEY_TOOL_SIG,
			VOTE_TOOL_SIG
		};
		return defaultToolSignatures;
		
	}
	
	// All the LAMS core tools
	public static final String LAMS_ADMIN_DIR = "lams_admin";
	public static final String LAMS_CENTRAL_DIR = "lams_central";
	public static final String LAMS_COMMON_DIR = "lams_common";
	public static final String LAMS_LEARNING_DIR = "lams_learning";
	public static final String LAMS_MONITORING_DIR = "lams_monitoring";
	public static final String LAMS_CONTENT_REPOSITORY = "lams_contentrepository";
	public static final String LAMS_BUILD_DIR = "lams_build";
	public static final String LAMS_TOOL_DEPLOY = "lams_tool_deploy";
	public static final String LAMS_WWW_DIR = "lams_www";
	
	// A list of LAMS core projects
	public static List<String> coreProjects;
	
	/*
	 * Instantiates the coreProjects list
	 * added forum to the core projects as it will be the default project
	 * used for the lams tool builder
	 */
	public static void initCorePrjects()
	{
		coreProjects = new ArrayList<String>();
		
		coreProjects.add(LAMS_ADMIN_DIR);
		coreProjects.add(LAMS_CENTRAL_DIR);
		coreProjects.add(LAMS_COMMON_DIR);
		coreProjects.add(LAMS_LEARNING_DIR);
		coreProjects.add(LAMS_MONITORING_DIR);
		coreProjects.add(LAMS_CONTENT_REPOSITORY);
		coreProjects.add(LAMS_BUILD_DIR);
		coreProjects.add(LAMS_TOOL_DEPLOY);
		coreProjects.add(LAMS_WWW_DIR);
		coreProjects.add(FORUM_TOOL_DIR);
	}
	
	
	
	
}
