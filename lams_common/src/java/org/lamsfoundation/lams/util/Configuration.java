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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.config.ConfigurationItem;
import org.lamsfoundation.lams.config.Registration;
import org.lamsfoundation.lams.config.dao.hibernate.ConfigurationDAO;
import org.lamsfoundation.lams.config.dao.hibernate.RegistrationDAO;
import org.springframework.beans.factory.InitializingBean;

/**
 * Configuration Object
 * 
 * @author Fei Yang
 * @author Mitchell Seaton
 * @author Luke Foxton
 */
public class Configuration implements InitializingBean {

    protected Logger log = Logger.getLogger(Configuration.class);

    public static String CONFIGURATION_HELP_PAGE = "LAMS+Configuration";

    private static Map items = null;

    protected ConfigurationDAO configurationDAO;
    
    protected static RegistrationDAO registrationDAO;

    /**
     * @param configurationDAO
     *            The configurationDAO to set.
     */
    public void setConfigurationDAO(ConfigurationDAO configurationDAO) {
	this.configurationDAO = configurationDAO;
    }

    public void afterPropertiesSet() {
	if (items != null) {
	    return;
	}

	Map itemsmap = Collections.synchronizedMap(new LinkedHashMap());

	try {
	    List mapitems = getAllItems();

	    if (mapitems.size() > 0) {
		Iterator it = mapitems.iterator();
		while (it.hasNext()) {
		    ConfigurationItem item = (ConfigurationItem) it.next();

		    // init ssl truststore path and password
		    if (StringUtils.equals(item.getKey(), ConfigurationKeys.TRUSTSTORE_PATH)) {
			setSystemProperty(item.getKey(), item.getValue());
		    } else if (StringUtils.equals(item.getKey(), ConfigurationKeys.TRUSTSTORE_PASSWORD)) {
			setSystemProperty(item.getKey(), item.getValue());
		    }

		    itemsmap.put(item.getKey(), item);
		}
	    }

	    items = itemsmap;

	} catch (Exception e) {
	    log.error("Exception has occurred: ", e);
	}
    }

    public List getAllItems() {
	return configurationDAO.getAllItems();
    }

    public static Map getAll() {
	return items;
    }

    public ConfigurationItem getConfigItemByKey(String key) {
	if ((items != null) && (items.get(key) != null))
	    if (items.get(key) != null)
		return (ConfigurationItem) items.get(key);
	return null;
    }

    public static String getItemValue(Object obj) {
	ConfigurationItem item = (ConfigurationItem) obj;
	if (item.getValue() != null)
	    return item.getValue().trim();
	return null;
    }

    public static void setItemValue(Object obj, String value) {
	ConfigurationItem item = (ConfigurationItem) obj;
	item.setValue(value);
    }

    public static String get(String key) {
	if ((items != null) && (items.get(key) != null))
	    if (getItemValue(items.get(key)) != null)
		return getItemValue(items.get(key));
	return null;
    }

    public static int getAsInt(String key) {
	if ((items != null) && (items.get(key) != null))
	    // could throw NumberFormatException which is a RuntimeException
	    if (getItemValue(items.get(key)) != null)
		return new Integer(getItemValue(items.get(key))).intValue();
	return -1;
    }

    public static boolean getAsBoolean(String key) {
	if ((items != null) && (items.get(key) != null))
	    if (getItemValue(items.get(key)) != null)
		return new Boolean(getItemValue(items.get(key))).booleanValue();
	return false;
    }

    public static void updateItem(String key, String value) {
	if (value != null)
	    value = value.trim();
	if (items.containsKey(key))
	    setItemValue(items.get(key), value);
    }

    public void persistUpdate() {
	// update ssl truststore path and password
	setSystemProperty(ConfigurationKeys.TRUSTSTORE_PATH, get(ConfigurationKeys.TRUSTSTORE_PATH));
	setSystemProperty(ConfigurationKeys.TRUSTSTORE_PASSWORD, get(ConfigurationKeys.TRUSTSTORE_PASSWORD));
	configurationDAO.insertOrUpdateAll(items.values());
    }

    public String toString() {
	return "Configuration items:" + (items != null ? items.toString() : "none");
    }

    // update jvm system property
    private void setSystemProperty(String key, String value) {
	if (StringUtils.isBlank(key)) {
	    // use default
	    System.clearProperty(key);
	} else {
	    System.setProperty(key, value);
	}
    }
    
    public static void saveOrUpdateRegistration(Registration reg){
	registrationDAO.saveOrUpdate(reg);
    }
    
    public static Registration getRegistration(){
	return registrationDAO.get();
    }

    public RegistrationDAO getRegistrationDAO() {
        return registrationDAO;
    }

    public void setRegistrationDAO(RegistrationDAO registrationDAO) {
        this.registrationDAO = registrationDAO;
    }
}
