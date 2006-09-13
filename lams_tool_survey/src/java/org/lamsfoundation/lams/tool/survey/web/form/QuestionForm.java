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
package org.lamsfoundation.lams.tool.survey.web.form;

import org.apache.struts.action.ActionForm;
/**
 *  Survey Item  Form.
 *	@struts.form name="surveyItemForm"
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class QuestionForm extends ActionForm {
	private String itemIndex;
	private String sessionMapID;
	
	//tool access mode;
	private String mode;
	
	private short itemType;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getItemIndex() {
		return itemIndex;
	}
	public void setItemIndex(String itemIndex) {
		this.itemIndex = itemIndex;
	}
	public short getItemType() {
		return itemType;
	}
	public void setItemType(short type) {
		this.itemType = type;
	}
	public String getSessionMapID() {
		return sessionMapID;
	}
	public void setSessionMapID(String sessionMapID) {
		this.sessionMapID = sessionMapID;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
}
