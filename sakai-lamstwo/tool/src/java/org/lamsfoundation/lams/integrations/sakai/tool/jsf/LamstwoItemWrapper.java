/******************************************************************************
 * LamstwoItemWrapper.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.tool.jsf;

import org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem;


/**
 * This is a wrapper class which is required to get the interactions from the user
 * without storing UI dependent information in the data POJO
 * @author Sakai App Builder -AZ
 */
public class LamstwoItemWrapper {

	private LamstwoItem item;
	private boolean canDelete; // can this item be deleted
	private boolean isSelected; // is this item selected by the user

	/**
	 * Constructor which accepts the object we are wrapping
	 * @param item the LamstwoItem we are wrapping
	 */
	public LamstwoItemWrapper(LamstwoItem item) {
		this.item = item;
	}


	/**
	 * Basic setters and getters
	 */
	public LamstwoItem getItem() {
		return item;
	}

	public void setItem(LamstwoItem item) {
		this.item = item;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
