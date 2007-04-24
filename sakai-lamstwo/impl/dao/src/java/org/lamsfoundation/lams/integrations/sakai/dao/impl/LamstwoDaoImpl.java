/******************************************************************************
 * LamstwoDaoImpl.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.lamsfoundation.lams.integrations.sakai.dao.LamstwoDao;
import org.sakaiproject.genericdao.hibernate.HibernateCompleteGenericDao;


/**
 * Implementations of any specialized DAO methods from the specialized DAO 
 * that allows the developer to extend the functionality of the generic dao package
 * @author Sakai App Builder -AZ
 */
public class LamstwoDaoImpl 
	extends HibernateCompleteGenericDao 
		implements LamstwoDao {

	private static Log log = LogFactory.getLog(LamstwoDaoImpl.class);

	public void init() {
		log.debug("init");
		super.init();
	}

}
