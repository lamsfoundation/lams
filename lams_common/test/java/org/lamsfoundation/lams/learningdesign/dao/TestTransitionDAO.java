/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/
package org.lamsfoundation.lams.learningdesign.dao;

import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.TransitionDAO;
import org.lamsfoundation.lams.test.AbstractCommonTestCase;

/**
 * @author Minhas
 */
public class TestTransitionDAO extends AbstractCommonTestCase{
	
	protected TransitionDAO transitionDAO;
	protected Transition transition;
	
	
	public TestTransitionDAO(String name) {
		super(name);	
	}
	
	public void setUp() throws Exception {
		super.setUp();
		transitionDAO =(TransitionDAO) context.getBean("transitionDAO");		
	}
	public void testGetTransitionByToActivityID(){
		transition = transitionDAO.getTransitionByToActivityID(new Long(13));		
		System.out.println(transition.getTitle());
		
	}
}
