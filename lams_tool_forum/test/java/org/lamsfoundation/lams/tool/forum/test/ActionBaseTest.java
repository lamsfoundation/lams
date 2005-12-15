/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.forum.test;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.XmlWebApplicationContext;

import servletunit.struts.MockStrutsTestCase;

public class ActionBaseTest extends MockStrutsTestCase {
	
	public void setUp()throws Exception{
		super.setUp();
		ContextLoader ctxLoader = new ContextLoader();
        context.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM,
                                 XmlWebApplicationContext.class.getName());
        context.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
        		"org/lamsfoundation/lams/localApplicationContext.xml," +
				 "org/lamsfoundation/lams/contentrepository/applicationContext.xml," +
				 "org/lamsfoundation/lams/tool/toolApplicationContext.xml," +
				 "org/lamsfoundation/lams/lesson/lessonApplicationContext.xml," +
				 "org/lamsfoundation/lams/learning/learningApplicationContext.xml,"+	
				 "org/lamsfoundation/lams/tool/forum/forumApplicationContext.xml,");
        ctxLoader.initWebApplicationContext(context);
	}

}
