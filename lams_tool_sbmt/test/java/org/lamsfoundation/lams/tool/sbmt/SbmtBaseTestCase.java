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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt;

import javax.sql.DataSource;

import org.lamsfoundation.lams.test.AbstractLamsTestCase;

/**
 * Base test case containing all the needed application context
 * files for submission tool testing 
 */
public class SbmtBaseTestCase extends AbstractLamsTestCase {
	
	/* Content ids and session ids from the test data (see insert_test_data.sql) */
	public static final Long TEST_CONTENT_ID=new Long(2);
	public static final String TEST_CONTENT_TITLE="Test Submission";
	public static final String TEST_CONTENT_INSTRUCTIONS="Submit your a file";
	public static final Long TEST_SESSION_ID=new Long(3);
	public static final Long TEST_REPORT_ID=new Long(1);
	public static final String TEST_REPORT_COMMENT="Not much effort";
	public static final Long TEST_SUBMISSION_ID=new Long(1);
	public static final String TEST_FILE_NAME="myfile.txt";

	public static final String CONTENT_TABLE = "tl_lasbmt11_content";
	public static final String SESSION_TABLE = "tl_lasbmt11_session";
	
	public SbmtBaseTestCase(String name){
		super(name);
	}

	/**
	 *(non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/localApplicationContext.xml",
							 "org/lamsfoundation/lams/tool/sbmt/submitFilesApplicationContext.xml",
							 "org/lamsfoundation/lams/contentrepository/applicationContext.xml",
							 "org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
							 "org/lamsfoundation/lams/learning/learningApplicationContext.xml",	
							 "org/lamsfoundation/lams/toolApplicationContext.xml"};
	}


	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
	 */
	protected String getHibernateSessionFactoryName() {
		return "sbmtSessionFactory";
	}
	
	public void setUp()throws Exception{
		super.setUp();
	}
	
	/** Gets the datasource for the tool database */
	protected DataSource getDataSource() {
		DataSource toolDataSource = (DataSource) this.context.getBean("toolDataSource");;
		assertNotNull(toolDataSource);
		return toolDataSource;
	}

	protected long getMaxContentId() {
			return getMaxId(CONTENT_TABLE,"content_id",getDataSource());
	}

	protected long getMaxSessionId() {
		return getMaxId(SESSION_TABLE,"session_id",getDataSource());
}
}
 