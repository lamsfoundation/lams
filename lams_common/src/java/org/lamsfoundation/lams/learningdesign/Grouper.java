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
package org.lamsfoundation.lams.learningdesign;

import java.util.List;

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;




/**
 * This is interface that defines the contract for performing grouping 
 * algorithm.
 * 
 * It would be nicer to get the message service directly from within the classes, rather than havea setter.
 * But can't think of a way to directly access it when the grouper object doesn't 
 * have any link to the Spring context. (Fiona Malikoff)
 * 
 * @author Jacky Fang
 * @since  2005-3-24
 * @version 1.1
 * 
 */
public abstract class Grouper
{
	String DEFAULT_GROUP_NAME_PREFIX_I18N = "group.name.prefix";
	private MessageService commonMessageService;

	/** Set the message service, needed for the I18N of the default group name prefix.
	 * Should call this method before calling doGrouping() or getPrefix() */
	public void setCommonMessageService(MessageService commonMessageService) {
		this.commonMessageService = commonMessageService;
	}
 
	protected MessageService getCommonMessageService() {
		return commonMessageService;
	}

	/**
     * Do the grouping for a list of learners that the teacher requested. 
     * @param grouping the grouping that is used to perform groups creation.
     * @param name for this group
     * @param learners the list of learners that the teacher requested.
     */
	public abstract void doGrouping(Grouping grouping, String groupName, List learners);
	
	/**
	 * Do the grouping for a single learner. Should call setCommonMessageService() before calling this method.
	 * @param grouping the grouping that is used to perform groups creation.
	 * @param name for this group
	 * @param learner the learner teacher want to add.
     * @param commonMessageService the message service from lams.jar. Needed for prefix for the group names.
	 */
	public abstract void doGrouping(Grouping grouping, String groupName, User learnere);

	/**
	 * Get the default group name prefix
	 * @return default group name prefix
	 */
	public String getPrefix() {
		String prefix = commonMessageService != null ? commonMessageService.getMessage(DEFAULT_GROUP_NAME_PREFIX_I18N) : "";
		prefix = prefix.trim();
		return prefix.length()>0 ? prefix : "Group";
	}

}
