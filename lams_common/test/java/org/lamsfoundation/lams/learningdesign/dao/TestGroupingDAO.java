/*
 * Created on Dec 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao;
import org.lamsfoundation.lams.learningdesign.BaseTestCase;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
/**
 * @author MMINHAS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestGroupingDAO extends BaseTestCase {
	
	protected GroupingDAO groupingDAO;
	protected Grouping grouping;
	
	public void setUp()throws Exception {
		super.setUp();
		groupingDAO = (GroupingDAO)context.getBean("groupingDAO");
		
	}
	public void testGetGroupingByID(){
		//grouping =(Grouping) groupingDAO.find(Grouping.class,new Long(1));
		grouping = groupingDAO.getGroupingById(new Long(1));
		System.out.println("grouping:" + grouping);
		//assertNotNull(grouping.getMaxNumberOfGroups());
	}

}
