/******************************************************************************
 * LamstwoDaoImplTest.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.dao.test;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.lamsfoundation.lams.integrations.sakai.dao.LamstwoDao;
import org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem;

import org.springframework.test.AbstractTransactionalSpringContextTests;

import junit.framework.Assert;

/**
 * Testing for the specialized DAO methods (do not test the Generic Dao methods)
 * @author Sakai App Builder -AZ
 */
public class LamstwoDaoImplTest extends AbstractTransactionalSpringContextTests {

	private static Log log = LogFactory.getLog(LamstwoDaoImplTest.class);

	protected LamstwoDao dao;

	private LamstwoItem item;

	private final static String ITEM_TITLE = "New Title";
	private final static String ITEM_INTRODUCTION = "New Introduction";
	private final static String ITEM_OWNER = "11111111";
	private final static String ITEM_SITE = "22222222";
	private final static Boolean ITEM_HIDDEN = Boolean.FALSE;


	protected String[] getConfigLocations() {
		// point to the needed spring config files, must be on the classpath
		// (add component/src/webapp/WEB-INF to the build path in Eclipse),
		// they also need to be referenced in the project.xml file
		return new String[] {"hibernate-test.xml", "spring-hibernate.xml"};
	}

	// run this before each test starts
	protected void onSetUpBeforeTransaction() throws Exception {
		// create test objects
		item = new LamstwoItem(ITEM_TITLE, ITEM_INTRODUCTION,ITEM_OWNER, ITEM_SITE, ITEM_HIDDEN, new Date());
	}

	// run this before each test starts and as part of the transaction
	protected void onSetUpInTransaction() {
		// load the spring created dao class bean from the Spring Application Context
		dao = (LamstwoDao) applicationContext.
			getBean("org.lamsfoundation.lams.integrations.sakai.dao.LamstwoDao");
		if (dao == null) {
			log.error("onSetUpInTransaction: DAO could not be retrieved from spring context");
		}

		// init the class if needed

		// check the preloaded data
		Assert.assertTrue("Error preloading data", dao.countAll(LamstwoItem.class) > 0);

		// preload data if desired
		dao.save(item);
	}


	/**
	 * ADD unit tests below here, use testMethod as the name of the unit test,
	 * Note that if a method is overloaded you should include the arguments in the
	 * test name like so: testMethodClassInt (for method(Class, int);
	 */

	
	// THESE ARE SAMPLE UNIT TESTS WHICH SHOULD BE REMOVED LATER -AZ
	/**
	 * TODO - Remove this sample unit test
	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.dao.impl.GenericHibernateDao#save(java.lang.Object)}.
	 */
	public void testSave() {
		LamstwoItem item1 = new LamstwoItem("New item1", ITEM_INTRODUCTION, ITEM_OWNER, ITEM_SITE, ITEM_HIDDEN, new Date());
		dao.save(item1);
		Long itemId = item1.getId();
		Assert.assertNotNull(itemId);
		Assert.assertEquals(dao.countAll(LamstwoItem.class), 3);
	}

	/**
	 * TODO - Remove this sample unit test
	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.dao.impl.GenericHibernateDao#delete(java.lang.Object)}.
	 */
	public void testDelete() {
		Assert.assertEquals(dao.countAll(LamstwoItem.class), 2);
		dao.delete(item);
		Assert.assertEquals(dao.countAll(LamstwoItem.class), 1);
	}

	/**
	 * TODO - Remove this sample unit test
	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.dao.impl.GenericHibernateDao#findById(java.lang.Class, java.io.Serializable)}.
	 */
	public void testFindById() {
		Long id = item.getId();
		Assert.assertNotNull(id);
		LamstwoItem item1 = (LamstwoItem) dao.findById(LamstwoItem.class, id);
		Assert.assertNotNull(item1);
		Assert.assertEquals(item, item1);
	}

	/**
	 * Add anything that supports the unit tests below here
	 */
}
