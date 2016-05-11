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

package org.lamsfoundation.lams.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.config.ConfigurationItem;
import org.lamsfoundation.lams.config.Registration;
import org.lamsfoundation.lams.config.dao.IConfigurationDAO;
import org.lamsfoundation.lams.config.dao.IRegistrationDAO;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.InitializingBean;

/**
 * Configuration Object
 *
 * @author Fei Yang
 * @author Mitchell Seaton
 * @author Luke Foxton
 */
public class Configuration implements InitializingBean {

    protected static Logger log = Logger.getLogger(Configuration.class);

    public static String CONFIGURATION_HELP_PAGE = "LAMS+Configuration";
    public static final int ITEMS_ALL = 1;
    public static final int ITEMS_NON_LDAP = 2;
    public static final int ITEMS_ONLY_LDAP = 3;

    private static Map<String, ConfigurationItem> items = null;

    protected static IConfigurationDAO configurationDAO;
    protected static IRegistrationDAO registrationDAO;
    protected static MessageService messageService;
    protected static Scheduler scheduler;

    public static String get(String key) {
	if ((Configuration.items != null) && (Configuration.items.get(key) != null)) {
	    if (Configuration.getItemValue(Configuration.items.get(key)) != null) {
		return Configuration.getItemValue(Configuration.items.get(key));
	    }
	}
	return null;
    }

    public static Map<String, ConfigurationItem> getAll() {
	return Configuration.items;
    }

    public static boolean getAsBoolean(String key) {
	if ((Configuration.items != null) && (Configuration.items.get(key) != null)) {
	    if (Configuration.getItemValue(Configuration.items.get(key)) != null) {
		return new Boolean(Configuration.getItemValue(Configuration.items.get(key))).booleanValue();
	    }
	}
	return false;
    }

    public static int getAsInt(String key) {
	if ((Configuration.items != null) && (Configuration.items.get(key) != null)) {
	    // could throw NumberFormatException which is a RuntimeException
	    if (Configuration.getItemValue(Configuration.items.get(key)) != null) {
		return new Integer(Configuration.getItemValue(Configuration.items.get(key))).intValue();
	    }
	}
	return -1;
    }

    public static String getItemValue(Object obj) {
	ConfigurationItem item = (ConfigurationItem) obj;
	if (item.getValue() != null) {
	    return item.getValue().trim();
	}
	return null;
    }

    public static Registration getRegistration() {
	return Configuration.registrationDAO.get();
    }

    public static void refreshCache() {
	Map<String, ConfigurationItem> itemMap = Collections
		.synchronizedMap(new LinkedHashMap<String, ConfigurationItem>());

	try {
	    List<ConfigurationItem> itemList = Configuration.getAllItems();

	    if (itemList.size() > 0) {
		Iterator<ConfigurationItem> itemIterator = itemList.iterator();
		while (itemIterator.hasNext()) {
		    ConfigurationItem item = itemIterator.next();
		    itemMap.put(item.getKey(), item);
		}
	    }

	    Configuration.items = itemMap;
	    if (Configuration.log.isDebugEnabled()) {
		Configuration.log.debug("Configuration cache refreshed");
	    }

	} catch (Exception e) {
	    Configuration.log.error("Exception while refreshing Configuration cache", e);
	}
    }

    public static void saveOrUpdateRegistration(Registration reg) {
	Configuration.registrationDAO.saveOrUpdate(reg);
    }

    public static void setItemValue(Object obj, String value) {
	ConfigurationItem item = (ConfigurationItem) obj;
	item.setValue(value);
    }

    public static void updateItem(String key, String value) {
	if (value != null) {
	    value = value.trim();
	}
	if (Configuration.items.containsKey(key)) {
	    Configuration.setItemValue(Configuration.items.get(key), value);
	}
    }

    private static List<ConfigurationItem> getAllItems() {
	return Configuration.configurationDAO.getAllItems();
    }

    // update jvm system property
    private static void setSystemProperty(String key, String value) {
	if (StringUtils.isBlank(key)) {
	    // use default
	    System.clearProperty(key);
	} else {
	    System.setProperty(key, value);
	}
    }

    @Override
    public void afterPropertiesSet() {
	if (Configuration.items != null) {
	    return;
	}
	Configuration.refreshCache();
	if (Configuration.items == null) {
	    return;
	}

	String refreshCacheIntervalString = Configuration.get(ConfigurationKeys.CONFIGURATION_CACHE_REFRESH_INTERVAL);
	Integer refreshCacheInterval = StringUtils.isBlank(refreshCacheIntervalString) ? null
		: Integer.valueOf(refreshCacheIntervalString);
	if ((refreshCacheInterval != null) && (refreshCacheInterval > 0)) {
	    JobDetail jobDetail = JobBuilder.newJob(ConfigurationRefreshCacheJob.class)
		    .withIdentity("configurationCacheRefresh").build();
	    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("configurationCacheRefreshTrigger")
		    .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(refreshCacheInterval)).build();

	    try {
		Configuration.scheduler.scheduleJob(jobDetail, trigger);
	    } catch (SchedulerException e) {
		Configuration.log.error(
			"Error while scheduling Configuration cache refresh. The cache will NOT be periodically updated.",
			e);
	    }
	}
    }

    /**
     * Get contents of lams_configuration and group them using header names as key. Includes ldap items.
     *
     * @return
     */
    public HashMap<String, ArrayList<ConfigurationItem>> arrangeItems() {
	return arrangeItems(Configuration.ITEMS_ALL);
    }

    /**
     * Get contents of lams_configuration and group them using header names as key.
     *
     * @param filter
     *            ITEMS_ALL: include all items; ITEMS_NON_LDAP: include non-ldap items only; ITEMS_ONLY_LDAP: include
     *            ldap-only items.
     * @return
     */
    public HashMap<String, ArrayList<ConfigurationItem>> arrangeItems(int filter) {
	List<ConfigurationItem> originalList = Configuration.getAllItems();
	HashMap<String, ArrayList<ConfigurationItem>> groupedList = new HashMap<String, ArrayList<ConfigurationItem>>();

	for (int i = 0; i < originalList.size(); i++) {
	    ConfigurationItem item = originalList.get(i);
	    String header = item.getHeaderName();

	    switch (filter) {
		case ITEMS_ALL:
		    // all items included
		    break;
		case ITEMS_NON_LDAP:
		    // non-ldap items only
		    if (StringUtils.contains(header, "config.header.ldap")) {
			continue;
		    }
		    break;
		case ITEMS_ONLY_LDAP:
		    // ldap-only items
		    if (!StringUtils.contains(header, "config.header.ldap")) {
			continue;
		    }
		    break;
		default:
		    break;
	    }

	    if (!groupedList.containsKey(header)) {
		groupedList.put(header, new ArrayList<ConfigurationItem>());
	    }
	    ArrayList<ConfigurationItem> currentList = groupedList.get(header);
	    currentList.add(item);
	    groupedList.put(header, currentList);
	}

	return groupedList;
    }

    public ConfigurationItem getConfigItemByKey(String key) {
	if ((Configuration.items != null) && (Configuration.items.get(key) != null)) {
	    if (Configuration.items.get(key) != null) {
		return Configuration.items.get(key);
	    }
	}
	return null;
    }

    public MessageService getMessageService() {
	return Configuration.messageService;
    }

    public IRegistrationDAO getRegistrationDAO() {
	return Configuration.registrationDAO;
    }

    public void persistUpdate() {
	updatePublicFolderName();
	Configuration.configurationDAO.insertOrUpdateAll(Configuration.items.values());
    }

    /**
     * @param configurationDAO
     *            The configurationDAO to set.
     */
    public void setConfigurationDAO(IConfigurationDAO configurationDAO) {
	Configuration.configurationDAO = configurationDAO;
    }

    public void setMessageService(MessageService messageService) {
	Configuration.messageService = messageService;
    }

    public void setRegistrationDAO(IRegistrationDAO registrationDAO) {
	Configuration.registrationDAO = registrationDAO;
    }

    public void setScheduler(Scheduler scheduler) {
	Configuration.scheduler = scheduler;
    }

    @Override
    public String toString() {
	return "Configuration items:" + (Configuration.items != null ? Configuration.items.toString() : "none");
    }

    @SuppressWarnings("unchecked")
    private void updatePublicFolderName() {
	// LDEV-2430 update public folder name according to default server locale
	WorkspaceFolder publicFolder = null;
	List<WorkspaceFolder> list = Configuration.configurationDAO.findByProperty(WorkspaceFolder.class,
		"workspaceFolderType", WorkspaceFolder.PUBLIC_SEQUENCES);

	if ((list != null) && (list.size() > 0)) {
	    publicFolder = list.get(0);
	    String[] langCountry = LanguageUtil.getDefaultLangCountry();
	    Locale locale = new Locale(langCountry[0], langCountry[1]);
	    publicFolder.setName(
		    Configuration.messageService.getMessageSource().getMessage("public.folder.name", null, locale));
	    Configuration.configurationDAO.update(publicFolder);
	}
    }
}