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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;

import org.lamsfoundation.lams.config.ConfigurationItem;
import org.lamsfoundation.lams.config.dao.IConfigurationDAO;
import org.lamsfoundation.lams.config.dao.hibernate.ConfigurationDAO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Configuration Object
 * 
 * @author Fei Yang
 * @author Mitchell Seaton
 */
public class Configuration implements InitializingBean {

	protected Logger log = Logger.getLogger(Configuration.class);	
	
	private static Map items = null;

	protected ConfigurationDAO configurationDAO;
	
	/**
	 * @param configurationDAO The configurationDAO to set.
	 */
	public void setConfigurationDAO(ConfigurationDAO configurationDAO) {
		this.configurationDAO = configurationDAO;
	}
	
	public void afterPropertiesSet() {
		Map itemsmap = Collections.synchronizedMap(new HashMap());
		
		try {
			
			List mapitems = configurationDAO.getAllItems();
			
			if(mapitems.size() > 0) {
				Iterator it = mapitems.iterator();
				while(it.hasNext()) {
					ConfigurationItem item = (ConfigurationItem) it.next();
					itemsmap.put(item.getKey(), item.getValue());
				}
				
			}
			
		} catch (Exception e) {
			 log.error("Exception has occurred: ",e);
		}
		
		items = itemsmap;
		
	}
	
	public static String get(String key)
	{
		if ((items != null)&&(items.get(key)!=null))
			return (String) items.get(key);
		return null;			
	}

	public static int getAsInt(String key)
	{
		if ((items != null)&&(items.get(key)!=null))
			//could throw NumberFormatException which is a RuntimeException
			return new Integer((String)items.get(key)).intValue();
		return -1;			
	}

	public static boolean getAsBoolean(String key)
	{
		if((items != null)&&(items.get(key)!=null))
		{
			return new Boolean((String)items.get(key)).booleanValue();
		}
		return false;
	}
	
	public String toString()
	{
		return "Configuration items:"
			+ ( items!=null ? items.toString() : "none" ) ;
	}
	
	/**
	 * Retrieves the date in which the specified dictionary was created/updated.
	 * If the dictionary isnt found, null is returned.
	 * @return
	 * @deprecated
	 */
	public static String getDictionaryDateForLanguage()
	{
		if ((items != null)&&(items.get(ConfigurationKeys.DICTIONARY_DATE_CREATED)!=null))
			return (String)items.get(ConfigurationKeys.DICTIONARY_DATE_CREATED);
		else
			return null;
	}

}
