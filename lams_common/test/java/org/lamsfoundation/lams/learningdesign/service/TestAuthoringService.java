/*
 * Created on Dec 7, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.service;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.BaseTestCase;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;


/**
 * @author MMINHAS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestAuthoringService extends BaseTestCase {
		
	private AuthoringService authService;
	private LearningDesign learningDesign;	
	private LearningLibraryDAO learningLibraryDAO;
	private LearningDesignDAO learningDesignDAO;
	
	protected void setUp()throws Exception{
		super.setUp();
		learningLibraryDAO =(LearningLibraryDAO) context.getBean("learningLibraryDAO");
		learningDesignDAO =(LearningDesignDAO)context.getBean("learningDesignDAO");
		
		authService =(AuthoringService)context.getBean("authoringServiceTarget");
		authService.setLearningLibraryDAO(learningLibraryDAO);
		authService.setLearningDesignDAO(learningDesignDAO);
	}
	
	/*public void testGetLearningDesignByID(){
		
		learningDesign = authService.getLearningDesign(new Long(20));
		assertNotNull(learningDesign.getTitle());
	}*/
	public void testRequestLearningLibraryWDDX(){		
		//String wddxpacket = authService.requestLearningLibraryWDDX();
		String wddxpacket = authService.requestLearningDesignWDDX(new Long(1));
		System.out.println("WDDXPACKET:" + wddxpacket);
		//ClientStatusMessage statusMessage = new ClientStatusMessage(ClientStatusMessage.ERROR,
			//	"Some Error Message",
				//"");
		//String str = authService.serializeStatusMessages(statusMessage);
		//System.out.println(str);
	}
}
