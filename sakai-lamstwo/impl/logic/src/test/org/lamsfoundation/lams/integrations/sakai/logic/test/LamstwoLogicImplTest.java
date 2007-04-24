/******************************************************************************
 * LamstwoLogicImplTest.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.logic.test;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.MockControl;
import org.lamsfoundation.lams.integrations.sakai.dao.LamstwoDao;
import org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl;
import org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.springframework.test.AbstractTransactionalSpringContextTests;


/**
 * Testing the Logic implementation methods
 * @author Sakai App Builder -AZ
 */
public class LamstwoLogicImplTest extends
		AbstractTransactionalSpringContextTests {

	private static Log log = LogFactory.getLog(LamstwoLogicImplTest.class);

	protected LamstwoLogicImpl logicImpl;

	private ToolManager toolManager;
	private MockControl toolManagerControl;
	private UserDirectoryService userDirectoryService;
	private MockControl userDirectoryServiceControl;
	private SecurityService securityService;
	private MockControl securityServiceControl;
	private SiteService siteService;
	private MockControl siteServiceControl;

	private LamstwoItem item1;
	private LamstwoItem item2;
	private LamstwoItem adminitem;
	private LamstwoItem maintitem;

	private final static String USER_NAME = "aaronz";
	private final static String USER_DISPLAY = "Aaron Zeckoski";
	private final static String USER_ID = "user-11111111";
	private final static String OTHER_USER_ID = "user-999999999";
	private final static String ADMIN_USER_ID = "admin";
	private final static String MAINT_USER_ID = "maintainer";
	private final static String SITE_ID = "site-1111111";
	private final static String SITE_REF = "siteref-1111111";
	private final static String SITE2_ID = "site-22222222";
	private final static String SITE2_REF = "siteref-22222222";
	private final static String ITEM_TITLE = "New Title";
	private final static String ITEM_INTRODUCTION = "New Introduction";
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
		item1 = new LamstwoItem(ITEM_TITLE, ITEM_INTRODUCTION, USER_ID, SITE_ID, ITEM_HIDDEN, new Date());
		item2 = new LamstwoItem(ITEM_TITLE, ITEM_INTRODUCTION, USER_ID, SITE2_ID, ITEM_HIDDEN, new Date());
		maintitem = new LamstwoItem("New maint item title", ITEM_INTRODUCTION, MAINT_USER_ID, SITE_ID, ITEM_HIDDEN, new Date());
		adminitem = new LamstwoItem("New admin item title", ITEM_INTRODUCTION, ADMIN_USER_ID, SITE2_ID, ITEM_HIDDEN, new Date());
	}

	// run this before each test starts and as part of the transaction
	protected void onSetUpInTransaction() {
		// load the spring created dao class bean from the Spring Application Context
		LamstwoDao dao = (LamstwoDao) applicationContext.
			getBean("org.lamsfoundation.lams.integrations.sakai.dao.LamstwoDao");
		if (dao == null) {
			log.error("onSetUpInTransaction: DAO could not be retrieved from spring context");
		}

		// init the class if needed

		// setup the mock objects
		toolManagerControl = MockControl.createControl(ToolManager.class);
		toolManager = (ToolManager) toolManagerControl.getMock();

		userDirectoryServiceControl = MockControl.createControl(UserDirectoryService.class);
		userDirectoryService = (UserDirectoryService) userDirectoryServiceControl.getMock();

		securityServiceControl = MockControl.createControl(SecurityService.class);
		securityService = (SecurityService) securityServiceControl.getMock();

		siteServiceControl = MockControl.createControl(SiteService.class);
		siteService = (SiteService) siteServiceControl.getMock();

		// create and setup the object to be tested
		logicImpl = new LamstwoLogicImpl();
		logicImpl.setDao(dao);
		logicImpl.setToolManager(toolManager);
		logicImpl.setUserDirectoryService(userDirectoryService);
		logicImpl.setSecurityService(securityService);
		logicImpl.setSiteService(siteService);

		// can set up the default mock object returns here if desired
		// Note: Still need to activate them in the test methods though
		userDirectoryService.getCurrentUser(); // expect this to be called
		userDirectoryServiceControl.setDefaultReturnValue(new TestUser(USER_ID));

		toolManager.getCurrentPlacement(); // expect this to be called
		toolManagerControl.setDefaultReturnValue(new TestPlacement(SITE_ID));

		siteService.siteReference(SITE_ID); // expect this to be called
		siteServiceControl.setReturnValue(SITE_REF, MockControl.ZERO_OR_MORE); // return for above param
		siteService.siteReference(SITE2_ID); // expect this to be called
		siteServiceControl.setReturnValue(SITE2_REF, MockControl.ZERO_OR_MORE); // return for above param

		securityService.isSuperUser(USER_ID); // normal user
		securityServiceControl.setReturnValue(false, MockControl.ZERO_OR_MORE); // return for above param
		securityService.isSuperUser(MAINT_USER_ID); // maintain user
		securityServiceControl.setReturnValue(false, MockControl.ZERO_OR_MORE); // return for above param
		securityService.isSuperUser(ADMIN_USER_ID); // admin user
		securityServiceControl.setReturnValue(true, MockControl.ZERO_OR_MORE); // return for above param

		// preload the DB for testing
		dao.save(item1);
		dao.save(item2);
		dao.save(adminitem);
		dao.save(maintitem);
	}
	
	/**
	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl#getItemById(java.lang.Long)}.
	 */
	public void testGetItemById() {
		LamstwoItem item = logicImpl.getItemById(item1.getId());
		Assert.assertNotNull(item);
		Assert.assertEquals(item, item1);

		LamstwoItem baditem = logicImpl.getItemById( new Long(9999999) );
		Assert.assertNull(baditem);

		try {
			logicImpl.getItemById(null);
			Assert.fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			Assert.assertNotNull(e.getMessage());
			log.info("Could not get null item (this is correct)");
		}
	}

	/**
	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl#canWriteItem(org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem)}.
	 */
	public void testCanWriteItemLamstwoItem() {
		// minimal testing for convenience methods

		// activate the mock objects
		userDirectoryServiceControl.replay();
		toolManagerControl.replay();
		siteServiceControl.replay();

		// mock objects needed here
		Assert.assertTrue( logicImpl.canWriteItem(item1) );
		Assert.assertTrue( logicImpl.canWriteItem(item2) );

		// verify the mock objects were used
		userDirectoryServiceControl.verify();
		toolManagerControl.verify();
		siteServiceControl.verify();
	}

	/**
	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl#canWriteItem(org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem, java.lang.String, java.lang.String)}.
	 */
	public void testCanWriteItemLamstwoItemStringString() {
		// set up mock objects with return values
		securityService.unlock(USER_ID, LamstwoLogicImpl.ITEM_WRITE_ANY, SITE_REF);
		securityServiceControl.setReturnValue(false, MockControl.ZERO_OR_MORE);
		securityService.unlock(MAINT_USER_ID, LamstwoLogicImpl.ITEM_WRITE_ANY, SITE_REF);
		securityServiceControl.setReturnValue(true, MockControl.ZERO_OR_MORE);

		// activate the mock objects
		securityServiceControl.replay();
		siteServiceControl.replay();

		// mock objects needed here
		// testing perms as a normal user
		Assert.assertFalse( logicImpl.canWriteItem(adminitem, SITE_ID, USER_ID) );
		Assert.assertFalse( logicImpl.canWriteItem(maintitem, SITE_ID, USER_ID) );
		Assert.assertTrue( logicImpl.canWriteItem(item1, SITE_ID, USER_ID) );

		// testing perms as user with special perms
		Assert.assertFalse( logicImpl.canWriteItem(adminitem, SITE_ID, MAINT_USER_ID) );
		Assert.assertTrue( logicImpl.canWriteItem(maintitem, SITE_ID, MAINT_USER_ID) );
		Assert.assertTrue( logicImpl.canWriteItem(item1, SITE_ID, MAINT_USER_ID) );

		// testing perms as admin user
		Assert.assertTrue( logicImpl.canWriteItem(adminitem, SITE_ID, ADMIN_USER_ID) );
		Assert.assertTrue( logicImpl.canWriteItem(maintitem, SITE_ID, ADMIN_USER_ID) );
		Assert.assertTrue( logicImpl.canWriteItem(item1, SITE_ID, ADMIN_USER_ID) );

		// verify the mock objects were used
		securityServiceControl.verify();
		siteServiceControl.verify();
	}

	/**
	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl#getAllVisibleItems()}.
	 */
	public void testGetAllVisibleItems() {
		// minimal testing for convenience methods
		
		// set up mock objects with return values
		securityService.unlock(USER_ID,	LamstwoLogicImpl.ITEM_READ_HIDDEN, SITE_REF);
		securityServiceControl.setReturnValue(false, MockControl.ZERO_OR_MORE);

		// activate the mock objects
		userDirectoryServiceControl.replay();
		securityServiceControl.replay();
		toolManagerControl.replay();
		siteServiceControl.replay();

		// mock objects needed below here
		List l = logicImpl.getAllVisibleItems(SITE_ID, USER_ID); // test normal user

		Assert.assertNotNull(l);
		Assert.assertTrue(l.size() > 0);
		Assert.assertTrue(l.contains(item1));
		Assert.assertTrue(! l.contains(item2));

		// verify the mock objects were used
		userDirectoryServiceControl.verify();
		securityServiceControl.verify();
		toolManagerControl.verify();
		siteServiceControl.verify();
	}

	/**
	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl#getAllVisibleItems(java.lang.String, java.lang.String)}.
	 */
	public void testGetAllVisibleItemsStringString() {
		// always match the mock call regardless of corrent param values
		//securityServiceControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);

		// set up mock objects with return values
		securityService.unlock(USER_ID,	LamstwoLogicImpl.ITEM_READ_HIDDEN, SITE_REF);
		securityServiceControl.setReturnValue(false, MockControl.ZERO_OR_MORE);
		securityService.unlock(MAINT_USER_ID, LamstwoLogicImpl.ITEM_READ_HIDDEN, SITE_REF);
		securityServiceControl.setReturnValue(true, MockControl.ZERO_OR_MORE);

		// add 2 items to test if we can see the visible one and not the hidden one
		LamstwoItem itemHidden = new LamstwoItem("New item title", ITEM_INTRODUCTION, 
				OTHER_USER_ID, SITE_ID, Boolean.TRUE, new Date());
		logicImpl.saveItem(itemHidden);
		LamstwoItem itemVisible = new LamstwoItem("New item title", ITEM_INTRODUCTION,
				OTHER_USER_ID, SITE_ID, Boolean.FALSE, new Date());
		logicImpl.saveItem(itemVisible);

		// activate the mock objects
		userDirectoryServiceControl.replay();
		securityServiceControl.replay();
		toolManagerControl.replay();
		siteServiceControl.replay();

		// mock objects needed below here
		List l = logicImpl.getAllVisibleItems(SITE_ID, USER_ID); // test normal user

		Assert.assertNotNull(l);
		Assert.assertTrue(l.size() > 0);
		Assert.assertTrue(l.contains(item1));
		Assert.assertTrue(! l.contains(item2));
		Assert.assertTrue(l.contains(itemVisible));
		Assert.assertTrue(! l.contains(itemHidden));

		List lmaintain = logicImpl.getAllVisibleItems(SITE_ID, MAINT_USER_ID); // test maintainer

		Assert.assertNotNull(lmaintain);
		Assert.assertTrue(lmaintain.size() > 0);
		Assert.assertTrue(lmaintain.contains(item1));
		Assert.assertTrue(! lmaintain.contains(item2));
		Assert.assertTrue(lmaintain.contains(itemVisible));
		Assert.assertTrue(lmaintain.contains(itemHidden));

		List ladmin = logicImpl.getAllVisibleItems(SITE_ID, ADMIN_USER_ID); // test admin

		Assert.assertNotNull(ladmin);
		Assert.assertTrue(ladmin.size() > 0);
		Assert.assertTrue(ladmin.contains(item1));
		Assert.assertTrue(! ladmin.contains(item2));
		Assert.assertTrue(ladmin.contains(itemVisible));
		Assert.assertTrue(ladmin.contains(itemHidden));

		// verify the mock objects were used
		userDirectoryServiceControl.verify();
		securityServiceControl.verify();
		toolManagerControl.verify();
		siteServiceControl.verify();
	}

	/**
	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl#removeItem(org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem)}.
	 */
	public void testRemoveItem() {
		// set up mock objects with return values
		userDirectoryService.getCurrentUser(); // expect this to be called
		userDirectoryServiceControl.setReturnValue(new TestUser(USER_ID));
		userDirectoryServiceControl.setReturnValue(new TestUser(MAINT_USER_ID));
		userDirectoryServiceControl.setReturnValue(new TestUser(ADMIN_USER_ID));
		userDirectoryServiceControl.setReturnValue(new TestUser(MAINT_USER_ID));
		userDirectoryServiceControl.setReturnValue(new TestUser(USER_ID));

		securityService.unlock(new TestUser(USER_ID), LamstwoLogicImpl.ITEM_WRITE_ANY, SITE_REF);
		securityServiceControl.setReturnValue(false, MockControl.ZERO_OR_MORE);
		securityService.unlock(new TestUser(MAINT_USER_ID), LamstwoLogicImpl.ITEM_WRITE_ANY, SITE_REF);
		securityServiceControl.setReturnValue(true, MockControl.ZERO_OR_MORE);

		// activate the mock objects
		userDirectoryServiceControl.replay();
		securityServiceControl.replay();
		toolManagerControl.replay();
		siteServiceControl.replay();

		// mock object is needed here
		try {
			logicImpl.removeItem(adminitem); // user cannot delete this
			Assert.fail("Should have thrown SecurityException");
		} catch (SecurityException e) {
			Assert.assertNotNull(e.getMessage());
			log.info("Could not remove item (this is correct)");
		}

		try {
			logicImpl.removeItem(adminitem); // permed user cannot delete this
			Assert.fail("Should have thrown SecurityException");
		} catch (SecurityException e) {
			Assert.assertNotNull(e.getMessage());
			log.info("Could not remove item (this is correct)");
		}

		LamstwoItem item;

		logicImpl.removeItem(adminitem); // admin can delete this
		item = logicImpl.getItemById(adminitem.getId());
		Assert.assertNull(item);

		logicImpl.removeItem(maintitem); // permed user can delete this
		item = logicImpl.getItemById(maintitem.getId());
		Assert.assertNull(item);

		logicImpl.removeItem(item1); // user can delete this
		item = logicImpl.getItemById(item1.getId());
		Assert.assertNull(item);

		// verify the mock objects were used
		userDirectoryServiceControl.verify();
		securityServiceControl.verify();
		toolManagerControl.verify();
		siteServiceControl.verify();
	}

	/**
	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl#saveItem(org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem)}.
	 */
	public void testSaveItem() {
		// set up the mock objects
		userDirectoryService.getCurrentUser(); // expect this to be called
		userDirectoryServiceControl.setReturnValue(new TestUser(USER_ID)); // return this

		toolManager.getCurrentPlacement(); // expect this to be called
		toolManagerControl.setReturnValue(new TestPlacement(SITE_ID)); // return this

		LamstwoItem item = new LamstwoItem("New item title", ITEM_INTRODUCTION, USER_ID, SITE_ID, ITEM_HIDDEN, new Date());
		logicImpl.saveItem(item);
		Long itemId = item.getId();
		Assert.assertNotNull(itemId);

		// test saving an incomplete item
		LamstwoItem incompleteItem = new LamstwoItem();
		incompleteItem.setTitle("New incomplete item");
		incompleteItem.setHidden(ITEM_HIDDEN);

		// activate the mock objects
		userDirectoryServiceControl.replay();
		toolManagerControl.replay();

		// mock object is needed here
		logicImpl.saveItem(incompleteItem);

		Long incItemId = item.getId();
		Assert.assertNotNull(incItemId);

		item = logicImpl.getItemById(incItemId);
		Assert.assertNotNull(item);		
		Assert.assertEquals(item.getOwnerId(), USER_ID);
		Assert.assertEquals(item.getSiteId(), SITE_ID);

		// test saving a null value for failure
		try {
			logicImpl.saveItem(null);
			Assert.fail("Should have thrown NullPointerException");
		} catch (NullPointerException e) {
			Assert.assertNotNull(e.getStackTrace());
			log.info("Could not save null item (this is correct)");
		}

		// verify the mock object was used
		userDirectoryServiceControl.verify();
		toolManagerControl.verify();
	}

//	/**
//	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl#getCurrentSiteId()}.
//	 */
//	public void testGetCurrentSiteId() {
//		// set up this mock object
//		toolManager.getCurrentPlacement(); // expect this to be called
//		toolManagerControl.setReturnValue(new TestPlacement(SITE_ID)); // return this
//
//		// activate the mock object
//		toolManagerControl.replay();
//
//		// mock object is needed here
//		String siteId = logicImpl.getCurrentContext();
//
//		// verify the mock object was used
//		toolManagerControl.verify();
//
//		Assert.assertNotNull(siteId);
//		Assert.assertEquals(siteId, SITE_ID);
//	}
//
//	/**
//	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl#getCurrentUserDisplayName()}.
//	 */
//	public void testGetCurrentUserDisplayName() {
//		userDirectoryService.getCurrentUser(); // expect this to be called
//		userDirectoryServiceControl.setReturnValue(new TestUser(USER_ID, USER_NAME, USER_DISPLAY)); // return this
//
//		// activate the mock object
//		userDirectoryServiceControl.replay();
//
//		// mock object is needed here
//		String userDisplayName = logicImpl.getCurrentUserDisplayName();
//
//		// verify the mock object was used
//		userDirectoryServiceControl.verify();
//
//		Assert.assertNotNull(userDisplayName);
//		Assert.assertEquals(userDisplayName, USER_DISPLAY);
//	}
//
//	/**
//	 * Test method for {@link org.lamsfoundation.lams.integrations.sakai.logic.impl.LamstwoLogicImpl#getCurrentUserId()}.
//	 */
//	public void testGetCurrentUserId() {
//		// set up this mock object
//		userDirectoryService.getCurrentUser(); // expect this to be called
//		userDirectoryServiceControl.setReturnValue(new TestUser(USER_ID)); // return this
//
//		// activate the mock object
//		userDirectoryServiceControl.replay();
//
//		// mock object is needed here
//		String userId = logicImpl.getCurrentUserId();
//
//		// verify the mock object was used
//		userDirectoryServiceControl.verify();
//		
//		Assert.assertNotNull(userId);
//		Assert.assertEquals(userId, USER_ID);
//	}

}
