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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */ 
 
/* $Id$ */ 

package org.lams.toolbuilder.renameTool;


import java.util.List;
import org.lams.toolbuilder.util.Constants;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

/**
 * A helper class to create task lists for the rename tool
 * based on information gained from the wizard
 * 
 * Uses Eclipse lazy initialisation so that only the needed 
 * tasklists are initialised to preserve memory
 * 
 * @author Luke Foxton
 */
public class RenameToolTaskList 
{	
	private String toolTemplate;		// the tool that will be used as a template
	private String toolSig;				// the tool signature of the new tool
	private String toolDispName;		// the display name of the new tool
	private String vendor;				// the vendor who is creating the new tool
	private String classPrefix;			// the string to replce the tool's java class prefixes with eg JavaClass
	private String variablePrefix; 		// the string to replce the tool's java variable prefixes with eg javaVariable
	
	private List<String[]> tasklist; 	// the tasklist created for the rename tool
	
	// success tests came with lamsfoundation(([\\\\/\\.]lams[\\\\/\\.]tool[\\\\/\\.]forum))
	private static final String VENDOR_REGEX_SUFFIX = "(?=([\\\\/\\.]lams[\\\\/\\.]tool[\\\\/\\.]";
	/**
	 * Contructor for the RenameToolTaskList
	 * @param tooltemplate the template that will be used for the new project
	 * @param toolsig the new tool signature of the project
	 * @param tooldispname the new display name of the project
	 */
	public RenameToolTaskList(String tooltemplate, String toolsig, String tooldispname, String vendorin)
	{
		tasklist = new ArrayList<String []>();
		
		this.vendor = vendorin.replace(" ", "").toLowerCase();
		
		this.toolTemplate = tooltemplate.trim();
		this.toolSig = toolsig.trim(); 
		this.toolDispName = tooldispname.trim();
	
		init();
	}
	
	/**
	 * Main init
	 * Calls other inits to create different tasklists for each tool template
	 * using Eclipse lazy initialisation to make sure variables are only 
	 * initialised when they are needed 
	 * 
	 * LAMS DEFAULT TOOLS:
	 * chat
	 * forum
	 * multiple choice
	 * question and answer
	 * share resource
	 * noticeboard
	 * notebook
	 * submit
	 * scribe
	 * survey
	 * vote
	 */
	public void init()
	{
		classPrefix = toJavaClassString(toolDispName);
		variablePrefix = toJavaVariableString(toolDispName);
		
		if (toolTemplate.equals(Constants.CHAT_TOOL_DIR)) {initChat();}
		else if (toolTemplate.equals(Constants.FORUM_TOOL_DIR)) {initForum();}
		else if (toolTemplate.equals(Constants.MC_TOOL_DIR)) {initMC();}
		else if (toolTemplate.equals(Constants.QA_TOOL_DIR)) {initQa();}
		else if (toolTemplate.equals(Constants.SHARE_RESOURCE_TOOL_DIR)) {initShareResources();}
		else if (toolTemplate.equals(Constants.NOTICEBOARD_TOOL_DIR)) {initNoticeboard();}
		else if (toolTemplate.equals(Constants.NOTEBOOK_TOOL_DIR)) {initNotebook();}
		else if (toolTemplate.equals(Constants.SUBMIT_TOOL_DIR)) {initSubmit();}
		else if (toolTemplate.equals(Constants.SCRIBE_TOOL_DIR)) {initScribe();}
		else if (toolTemplate.equals(Constants.SURVEY_TOOL_DIR)) {initSurvey();}
		else if (toolTemplate.equals(Constants.VOTE_TOOL_DIR)) {initVote();}
	}
	
	/**
	 * Init for forum template
	 */
	public void initForum() 
	{
		tasklist.add(new String[] {"", VENDOR_REGEX_SUFFIX + "forum))", "lamsfoundation", vendor});
		tasklist.add(new String[] {"","","TestForum", "Test" + classPrefix});
		tasklist.add(new String[] {"","","Forum", classPrefix});
		tasklist.add(new String[] {"((?<!get\\.)(?<!view\\.)(?<!to\\.))","","forum", variablePrefix});
		tasklist.add(new String[] {"","",Constants.FORUM_TOOL_SIG, toolSig});

		tasklist.add(new String[] {"","","AttachmentDao", classPrefix+"AttachmentDao"});
		tasklist.add(new String[] {"","","attachmentDao", variablePrefix+"AttachmentDao"});
		tasklist.add(new String[] {"","","MessageDao", classPrefix+"MessageDao"});
		tasklist.add(new String[] {"","","messageDao", variablePrefix+"MessageDao"});
		tasklist.add(new String[] {"","","MessageSeqDao", classPrefix+"MessageSeqDao"});
		tasklist.add(new String[] {"","","messageSeqDao", variablePrefix+"MessageSeqDao"});
		tasklist.add(new String[] {"","","FK", "FK_NEW_" + Math.abs(new Random().nextInt()) + "_"});
	}
	
	/**
	 * Init for share resources template
	 */
	public void initShareResources() 
	{
		tasklist.add(new String[] {"", VENDOR_REGEX_SUFFIX + "rsrc))", "lamsfoundation", vendor});
		tasklist.add(new String[] {"((?<!mapping)(?<!taglib.bean\\.)(?<!Application))", "((s?+)(?!Bundle))", "Resource", classPrefix});
		tasklist.add(new String[] {"((?<!name.)(?<!no\\.)(?<!label\\.)(?<!error\\.)(?<!number\\.)(?<!basic\\.)(?<!init\\.)(?<!message-))", "(s?+)", "resource", variablePrefix});
		tasklist.add(new String[] {"(?<!la)", "(?!11)", "rsrc", variablePrefix});
		tasklist.add(new String[] {"","",Constants.SHARE_RESOURCES_TOOL_SIG, toolSig});
		tasklist.add(new String[] {"","","FK", "FK_NEW_" + Math.abs(new Random().nextInt()) + "_"});
		tasklist.add(new String[] {"", "", "Share " + classPrefix, classPrefix});
		tasklist.add(new String[] {"", "", "share " + variablePrefix, variablePrefix});
	}
	
	/**
	 * Init for noticeboard template
	 */
	public void initNoticeboard() 
	{
		tasklist.add(new String[] {"", VENDOR_REGEX_SUFFIX + "noticeboard))", "lamsfoundation", vendor});
		tasklist.add(new String[] {"(?<!exception\\.)", "", "Nb", classPrefix});
		tasklist.add(new String[] {"", "", "Noticeboard", classPrefix});
		tasklist.add(new String[] {"((?<!heading\\.)(?<!label\\.))", "", "noticeboard", variablePrefix});
		tasklist.add(new String[] {"(?<!la)", "(?!11)", "nb", variablePrefix});
		tasklist.add(new String[] {"","",Constants.NOTICEBOARD_TOOL_SIG, toolSig});
		tasklist.add(new String[] {"","","FK", "FK_NEW_" + Math.abs(new Random().nextInt()) + "_"});
	}
	
	/**
	 * Init for notebook template
	 */
	public void initNotebook() 
	{
		tasklist.add(new String[] {"", VENDOR_REGEX_SUFFIX + "notebook))", "lamsfoundation", vendor});
		tasklist.add(new String[] {"((?<!Core)(?<!core))", "(?!Entry)", "Notebook", classPrefix});
		tasklist.add(new String[] {"((?<!label\\.)(?<!lams.)(?<!heading\\.))", "", "notebook", variablePrefix});
		tasklist.add(new String[] {"","",Constants.NOTEBOOK_TOOL_SIG, toolSig});
		tasklist.add(new String[] {"","","FK", "FK_NEW_" + Math.abs(new Random().nextInt()) + "_"});
	}
	
	// not implemented becuase the tool is too complicated to be a template
	public void initChat() {}
	
	// not implemented because the tool is badly strucured
	public void initMC() {}
	
	// not implemented because the tool is badly strucured
	public void initQa() {}
	
	// not implemented because of problem associated with the reserved word "submit"
	public void initSubmit() 
	{
		/*
		tasklist.add(new String[] {"lasbmt11", toolSig});
		tasklist.add(new String[] {"TestSubmitFiles", "Test" + classPrefix});
		tasklist.add(new String[] {"SubmitFiles", classPrefix});
		tasklist.add(new String[] {"submitFiles", variablePrefix});
		tasklist.add(new String[] {"Submit Files", toolDispName});
		tasklist.add(new String[] {"Submit",classPrefix});
		tasklist.add(new String[] {"submit",variablePrefix});		
		tasklist.add(new String[] {"sbmt",variablePrefix});
		tasklist.add(new String[] {"Sbmt",classPrefix});
		tasklist.add(new String[] {"Submission",classPrefix});
		tasklist.add(new String[] {"submission",variablePrefix});
		*/
	}
	
	// not implemented because it is the tool is not useful as a template
	public void initScribe() {}
	
	// not implemented because the tool is too complicated to use for a template
	public void initSurvey() {}
	
	// not implemented because the tool is too complicated to use for a template
	public void initVote() {}

	/**
	 * @param str the string to be altered
	 * @return string in the conventional java class form eg: JavaClass
	 */
	public String toJavaClassString(String str)
	{
		String retStr = "";
		str = str.trim();
		String[] strs = str.split(" ");
		
		for (int i =0; i<strs.length; i++)
		{
			retStr += strs[i].substring(0, 1).toUpperCase() + strs[i].substring(1);
		}
		return retStr;
	}
	
	/**
	 * 
	 * @param str the string to be altered
	 * @return string in the conventional java variable form eg: javaVariable
	 */
	public String toJavaVariableString(String str)
	{
		str = str.trim();
		String firstWord;
		
		if (str.contains(" "))
		{
			firstWord = str.substring(0, str.indexOf(" "));
			firstWord = firstWord.substring(0, 1).toLowerCase() + firstWord.substring(1);
			return firstWord + toJavaClassString(str.substring(str.indexOf(" ")));
		}
		else
		{
			return str.substring(0, 1).toLowerCase() + str.substring(1);
		}
	}
	
	public String getToolTemplate() {return toolTemplate;}
	public String getToolSig() {return toolSig;}
	public String getToolDispName() {return toolDispName;}
	public List<String[]> getTasklist() {return tasklist;}
	
	
	
}
