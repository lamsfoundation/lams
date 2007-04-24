/******************************************************************************
 * LamstwoLogic.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.logic;

import java.util.List;

import org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem;

/**
 * This is the interface for the app Logic, 
 * @author Sakai App Builder -AZ
 */
public interface LamstwoLogic {

	/**
	 * This returns an item based on an id
	 * @param id the id of the item to fetch
	 * @return a LamstwoItem or null if none found
	 */
	public LamstwoItem getItemById(Long id);

	/**
	 * Check if the current user can write this item in the current site
	 * @param item to be modifed or removed
	 * @return true if item can be modified, false otherwise
	 */
	public boolean canWriteItem(LamstwoItem item);

	/**
	 * Check if a specified user can write this item in a specified site
	 * @param item to be modified or removed
	 * @param siteId the Sakai id of the site
	 * @param userId the Sakai id of the user
	 * @return true if item can be modified, false otherwise
	 */
	public boolean canWriteItem(LamstwoItem item, String siteId, String userId);

	/**
	 * This returns a List of items for the current site that are
	 * visible to the current user
	 * @return a List of LamstwoItem objects
	 */
	public List getAllVisibleItems();

	/**
	 * This returns a List of items for a specified site that are
	 * visible to the specified user
	 * @param siteId siteId of the site
	 * @param userId the Sakai id of the user
	 * @return a List of LamstwoItem objects
	 */
	public List getAllVisibleItems(String siteId, String userId);

	/**
	 * Save (Create or Update) an item (uses the current site)
	 * @param item the LamstwoItem to create or update
	 */
	public void saveItem(LamstwoItem item);

	/**
	 * Remove an item
	 * @param item the LamstwoItem to remove
	 */
	public void removeItem(LamstwoItem item);

	/**
	 * Get the display name for the current user
	 * @return display name (probably firstname lastname)
	 */
	public String getCurrentUserDisplayName();
	
	/**
	 * Get all available learning designs from LAMS
	 * @param username
	 * @param courseID
	 * @param mode
	 * @param countryCode
	 * @param langCode
	 * @return LAMS learning designs
	 */
	public String getLearningDesigns(Integer mode);
	/**
	 * Start a lesson
	 * @param username
	 * @param ldId
	 * @param courseId
	 * @param title
	 * @param desc
	 * @param countryCode
	 * @param langCode
	 * @return LAMS ??
	 */
	public Long startLesson(long ldId,
			String title, String desc);

	/**
	 * Schedule a lesson
	 * @param username
	 * @param ldId
	 * @param courseId
	 * @param title
	 * @param desc
	 * @param startDate
	 * @param countryCode
	 * @param langCode
	 * @return LAMS ??
	 */
	public Long scheduleLesson(long ldId,
			String title, String desc, String startDate);
	
	/**
	 * Delete a Lesson
	 * @param username
	 * @param lsId
	 * @return ??
	 */
	public boolean deleteLesson(long lsId);
	
	
	/**
	 * Get the LAMS Server ID from sakai.properties
	 * @return LAMS Server ID
	 */
	public String getServerID();
	
	/**
	 * Get the LAMS Server Key from sakai.properties 
	 * @return LAMS Server Key
	 */
	public String getServerKey();
	
	/**
	 * Get the LAMS Server Address from sakai.properties
	 * @return LAMS Server Address
	 */
	public String getServerAddress();
	
	/**
	 * Get the Request Source from sakai.properties
	 * @return Request Source
	 */
	public String getRequestSource();
	
	/**
	 * Generates a hash of the the strings in element in the order they are presented
	 * and compares this to hash
	 * 
	 * @param elements An ordered collection of strings used to generate the hash
	 * @param hash The hash to compare against
	 * @return Whether the two hashes match
	 */
	public boolean isHashValid(String[] elements, String hash);
	
	/**
	 * 
	 * @param username A sakai user
	 * @return A comma separated list of values containing the users information
	 */
	public String getUserInfo(String username);
	
	/**
	 * Connstructs URL to open the LAMS authoring environment
	 * @return LAMS authoring URL
	 */
	public String getAuthorURL();
	
	/**
	 * Connstructs URL to open the LAMS Learning environment
	 * @return LAMS learning URL
	 */
	public String getLearnerURL();
	
	/**
	 * Connstructs URL to open the LAMS monitoring environment
	 * @return LAMS monitoring URL
	 */
	public String getMonitorURL();

	/**
	 * Checks whether user can create a new item.
	 * @return True if user has a maintainer role, otherwise false.
	 */
	public Boolean canCreateItem();
}
