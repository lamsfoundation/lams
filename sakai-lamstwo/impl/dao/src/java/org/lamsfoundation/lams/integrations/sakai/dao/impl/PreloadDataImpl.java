/******************************************************************************
 * PreloadDataImpl.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.dao.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.lamsfoundation.lams.integrations.sakai.dao.LamstwoDao;
import org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem;

/**
 * This checks and preloads any data that is needed for this app
 * @author Sakai App Builder -AZ
 */
public class PreloadDataImpl {

	private static Log log = LogFactory.getLog(PreloadDataImpl.class);

	private LamstwoDao dao;
	public void setDao(LamstwoDao dao) {
		this.dao = dao;
	}

	public void init() {
		preloadItems();
	}

	/**
	 * Preload some items into the database
	 */
	public void preloadItems() {

		// check if there are any items present, load some if not
		if(dao.findAll(LamstwoItem.class).isEmpty()){

			// use the dao to preload some data here
			dao.save( new LamstwoItem("Preload Title", "Preloader Introduction", 0L,
					0L, "Preload Owner", "Preload Site", Boolean.TRUE, Boolean.FALSE, new Date(), new Date()) );

			log.info("Preloaded " + dao.countAll(LamstwoItem.class) + " items");
		}
	}
}
