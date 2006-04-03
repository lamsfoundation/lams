/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
package org.lamsfoundation.lams.admin.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Fei Yang
 * 
 * @struts:form name="UsersRemoveActionForm"
 * 		include-pk="true"
 * 		include-all="true"
 */
public class UsersRemoveActionForm extends ActionForm {

	private static Logger log = Logger.getLogger(UsersRemoveActionForm.class);
	public static final String formName = "UsersRemoveActionForm"; // must match name in @struts:action section above
	
	private Integer orgId; 	// don't update - for display only
	private String name; 	// don't update - for display only
	private String description; 	// don't update - for display only
	private List users; // these are the current users
	
	private Integer[] toRemove; 
	
	
	public UsersRemoveActionForm()
	{
		orgId = new Integer(-1);
		setUsers(new ArrayList());
		toRemove = new Integer[0]; 	
	}
	
	/**
	 * Returns the name
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description
	 * @return String
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = StringUtils.trimToEmpty(name);
	}

	/**
	 * Sets the description.
	 * @param description The description to set
	 */
	public void setDescription(String description) {
		this.description = StringUtils.trimToEmpty(description);
	}


	/**
	 * Sets the orgId.
	 * @param orgId The orgId to set
	 */
	public void setOrgId(Integer orgId) {
		log.debug("Setting orgId "+orgId);
		this.orgId = orgId;
	}

	/**
	 * Returns the orgId.
	 * @return Integer
	 */
	public Integer getOrgId() {
		return orgId;
	}

    /**
     * Reset all properties to their default values. Don't reset the ones
     * that are display only.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
		toRemove = new Integer[0]; 	
    }

	/**
	 * Returns the users. 
	 * @return List of users
	 */
	public List getUsers() {
		return users;
	}

	/**
	 * Sets the users. 
	 * @param users The users to set
	 */
	public void setUsers(List users) {
		this.users = users;
	}

	/**
	 * Returns the Ids of the users to remove
	 * @return Integer[]
	 */
	public Integer[] getToRemove() {
		return toRemove;
	}

	/**
	 * Set the Ids of the users to remove
	 */
	public void setToRemove(Integer[] values) {
		toRemove = values;
	}

	/** Validate method - doesn't actually check anything yet! */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) 
    {
       ActionErrors errors = super.validate(mapping, request);
       if (errors==null || errors.isEmpty()) 
       		return null;
       else 
       		return errors;
    }

}
