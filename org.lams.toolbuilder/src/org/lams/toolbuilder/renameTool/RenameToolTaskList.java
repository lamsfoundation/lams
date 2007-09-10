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

public class RenameToolTaskList 
{	
	private String toolTemplate;		// the tool that will be used as a template
	private String toolSig;				// the tool signature of the new tool
	//private String toolName;			// the name of the new tool
	private String toolDispName;		// the display name of the new tool
	
	private List<String[]> tasklist; 	// the tasklist created for the rename tool
	
	public RenameToolTaskList(String tooltemplate, String toolsig, String tooldispname)
	{
		tasklist = new ArrayList();
		this.toolTemplate = tooltemplate.trim();
		this.toolSig = toolsig.trim(); 
		//this.toolName = toolname.trim();
		this.toolDispName = tooldispname.trim();
	
		init();
	}
	
	
	/* Main init
	 * Calls other inits to creat different tasklists for each tool template
	 * Using Eclipse lazy initialisation to make sure variables are only 
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
	
	public void initChat() {}
	
	public void initForum() {}
	
	public void initMC() {}
	
	public void initQa() {}
	
	public void initShareResources() {}
	
	public void initNoticeboard() {}
	
	public void initNotebook() {}
	
	public void initSubmit() 
	{
		String submitClassPrefix = toJavaClassString(toolDispName);
		String submitVariablePrefix = toJavaVariableString(toolDispName);
		System.out.println("Java Class Prefix: " + submitClassPrefix);
		System.out.println("Java Variable Prefix: " + submitVariablePrefix);
		
		
		tasklist.add(new String[] {"lasbmt11", toolSig});
		tasklist.add(new String[] {"TestSubmitFiles", "Test" + submitClassPrefix});
		tasklist.add(new String[] {"SubmitFiles", submitClassPrefix});
		tasklist.add(new String[] {"submitFiles", submitVariablePrefix});
		tasklist.add(new String[] {"Submit Files", toolDispName});
		tasklist.add(new String[] {"Submit",submitClassPrefix});
		tasklist.add(new String[] {"submit",submitVariablePrefix});		
		tasklist.add(new String[] {"sbmt",submitVariablePrefix});
		tasklist.add(new String[] {"Sbmt",submitClassPrefix});
		tasklist.add(new String[] {"Submission",submitClassPrefix});
		tasklist.add(new String[] {"submission",submitVariablePrefix});
	}
	
	
	public void initScribe() {}
	
	public void initSurvey() {}
	
	public void initVote() {}

	/**
	 * @param str
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
	 * @param str
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
	//public String getToolName() {return toolName;}
	public String getToolDispName() {return toolDispName;}
	public List<String[]> getTasklist() {return tasklist;}
	
	
	
}
