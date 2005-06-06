/*
 * Created on May 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.web;

import java.io.File;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.XmlWebApplicationContext;

import servletunit.struts.MockStrutsTestCase;

/**
 * @author Manpreet Minhas
 */
public class TestLearnerAction extends MockStrutsTestCase {
	
	public TestLearnerAction(String name){
		super(name);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		ContextLoader ctxLoader = new ContextLoader();
        context.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM,
                                 XmlWebApplicationContext.class.getName());
        context.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                                 "/org/lamsfoundation/lams/tool/sbmt/submitFilesApplicationContext.xml");
        ctxLoader.initWebApplicationContext(context);
	}
	
	public void testUploadFile(){		
		setConfigFile("/WEB-INF/struts/struts-config.xml");
		setRequestPathInfo("/tool/sbmt/learner");
		addRequestParameter("method","uploadFile");
		
		addRequestParameter("contentID","1");		
		String filePath = "c:" + File.separator + "mminhas.txt";
		addRequestParameter("filePath",filePath);		
		addRequestParameter("fileDescription","Mock file description ");
		addRequestParameter("userID","1");
		
		actionPerform();
		verifyForward("upload");
		verifyNoActionErrors();
	}

}
