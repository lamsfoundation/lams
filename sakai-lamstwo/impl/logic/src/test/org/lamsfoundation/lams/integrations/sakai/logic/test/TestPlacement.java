/******************************************************************************
 * TestPlacement.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.logic.test;

import java.util.Properties;

import org.sakaiproject.tool.api.Placement;
import org.sakaiproject.tool.api.Tool;

/**
 * Test class for the Sakai Placement object<br/>
 * This has to be here since I cannot create a Placement object in Sakai for some 
 * reason... sure would be nice if I could though -AZ
 * @author Sakai App Builder -AZ
 */
public class TestPlacement implements Placement {

	private String id = "FAKE12345";
	private String context; // a.k.a. siteId
	private String title;
	private Tool tool;
	private String toolId;
	
	/**
	 * Construct a test Placement object with a context (siteId) set
	 * @param context a String representing a site context (siteId)
	 */
	public TestPlacement(String context) {
		this.context = context;
	}


	public Properties getConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getContext() {
		return context;
	}

	public String getId() {
		return id;
	}

	public Properties getPlacementConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		return this.title;
	}

	public Tool getTool() {
		return tool;
	}

	public String getToolId() {
		return toolId;
	}

	public void save() {
		// TODO Auto-generated method stub
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTool(String toolId, Tool tool) {
		this.tool = tool;
		this.toolId = toolId;
	}
	
}