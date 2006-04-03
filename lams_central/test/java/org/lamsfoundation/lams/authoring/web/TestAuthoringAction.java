/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.authoring.web;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.XmlWebApplicationContext;

import servletunit.struts.MockStrutsTestCase;

/**
 * @author Manpreet Minhas
 */
public class TestAuthoringAction extends MockStrutsTestCase {

	public TestAuthoringAction(String name){
		super(name);
	}
	public void setUp() throws Exception{
		super.setUp();
		ContextLoader ctxLoader = new ContextLoader();
        context.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM,        		
        							XmlWebApplicationContext.class.getName());

        context.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                                "/org/lamsfoundation/lams/localApplicationContext.xml,/org/lamsfoundation/lams/authoring/authoringApplicationContext.xml");        
        ctxLoader.initWebApplicationContext(context);
	}
	public void testGetLearningDesign(){
		setConfigFile("/WEB-INF/struts/struts-config.xml");
		setRequestPathInfo("/authoring/author");
		addRequestParameter("method","getLearningDesignDetails");
		addRequestParameter("learningDesignID","1");
		addRequestParameter("jspoutput","true");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}
}
