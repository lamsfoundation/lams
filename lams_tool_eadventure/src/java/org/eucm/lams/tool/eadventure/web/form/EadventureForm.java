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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.eucm.lams.tool.eadventure.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.eucm.lams.tool.eadventure.model.Eadventure;

/**
 *
 * Eadventure Form.
 * 
 * @struts.form name="eadventureForm"
 *
 *              User: Dapeng.Ni
 */
public class EadventureForm extends ActionForm {
    private static final long serialVersionUID = 3599879328307492312L;

    private static Logger logger = Logger.getLogger(EadventureForm.class.getName());

    //Forum fields
    private String sessionMapID;
    private String contentFolderID;
    private int currentTab;

    //flag of this item has attachment or not
    private boolean hasFile;

    private Eadventure eadventure;

    public EadventureForm() {
	eadventure = new Eadventure();
	eadventure.setTitle("eAdventure");
	currentTab = 1;
    }

    public void resetFileInfo() {
	hasFile = false;
    }

    public void setEadventure(Eadventure eadventure) {
	this.eadventure = eadventure;
	if (eadventure == null) {
	    logger.error("Initial EadventureForum failed by null value of Eadventure.");
	}
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	String param = mapping.getParameter();
	//if it is start page, all data read out from database or current session
	//so need not reset checkbox to refresh value!
	if (!StringUtils.equals(param, "start") && !StringUtils.equals(param, "initPage")) {
	    eadventure.setDefineComplete(false);
	    eadventure.setLockWhenFinished(false);
	    eadventure.setDefineLater(false);
	    eadventure.setReflectOnActivity(false);
	}
    }

    public int getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(int currentTab) {
	this.currentTab = currentTab;
    }

    public Eadventure getEadventure() {
	return eadventure;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    public boolean isHasFile() {
	return hasFile;
    }

    public void setHasFile(boolean hasFile) {
	this.hasFile = hasFile;
    }
}
