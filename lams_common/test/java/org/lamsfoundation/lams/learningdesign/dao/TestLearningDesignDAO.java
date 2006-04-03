/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
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
package org.lamsfoundation.lams.learningdesign.dao;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.TransitionDAO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.test.AbstractCommonTestCase;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.WorkspaceFolderDAO;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;

/**
 * @author MMINHAS
 */
public class TestLearningDesignDAO extends AbstractCommonTestCase {
	
	protected ActivityDAO activityDAO;
	private LearningDesignDAO learningDesignDAO;
	protected TransitionDAO transitionDAO;
	protected WorkspaceFolderDAO workspaceFolderDAO;
	private UserDAO userDAO;
	private User user;
	private LearningDesign learningDesign;
	
	public TestLearningDesignDAO(String name) {
		super(name);
	}	
	public void setUp() throws Exception{
		super.setUp();
		learningDesignDAO =(LearningDesignDAO)context.getBean("learningDesignDAO");		
		transitionDAO =(TransitionDAO) context.getBean("transitionDAO");
		activityDAO =(ActivityDAO) context.getBean("activityDAO");
		userDAO = (UserDAO)context.getBean("userDAO");
	}
	public void testCalculateFirstActivity(){
		learningDesign = learningDesignDAO.getLearningDesignById(new Long(1));
		Activity activity = learningDesign.calculateFirstActivity();
		assertNotNull(activity.getActivityId());
		long x = 20;
		assertEquals(activity.getActivityId().longValue(),x);
	}
	public void testGetAllValidLearningDesignsInFolder(){
		List list = learningDesignDAO.getAllValidLearningDesignsInFolder(new Integer(1));
		System.out.println("SIZE:"+list.size());
	}
	public void testGetLearningDesignDTO() throws Exception{
		learningDesign = learningDesignDAO.getLearningDesignById(new Long(1));
		LearningDesignDTO learningDesignDTO = learningDesign.getLearningDesignDTO();		
		String str = WDDXProcessor.serialize(learningDesignDTO);
		System.out.println(str);		
	}	
}
