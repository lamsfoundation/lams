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

package org.lamsfoundation.lams.tool.commonCartridge.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridge;

/**
 *
 * CommonCartridge Form.
 *
 * User: Dapeng.Ni
 */
public class CommonCartridgeForm {
    private static final long serialVersionUID = 3599879328307492312L;

    private static Logger logger = Logger.getLogger(CommonCartridgeForm.class.getName());

    // Forum fields
    private String sessionMapID;
    private String contentFolderID;
    private int currentTab;
    private String offlineFile;
    private String onlineFile;
    private String mode;

    private CommonCartridge commonCartridge;

    public CommonCartridgeForm() {
	commonCartridge = new CommonCartridge();
	commonCartridge.setTitle("Common Cartridge");
	currentTab = 1;
    }

    public void setCommonCartridge(CommonCartridge commonCartridge) {
	this.commonCartridge = commonCartridge;
	// set Form special varaible from given forum
	if (commonCartridge == null) {
	    logger.error("Initial Common Cartridge failed by null value of CommonCartridge.");
	}
    }

    public void reset(HttpServletRequest request, String param) {
	// if it is start page, all data read out from database or current session
	// so need not reset checkbox to refresh value!
	if (!StringUtils.equals(param, "start") && !StringUtils.equals(param, "initPage")) {
	    commonCartridge.setDefineLater(false);
	    commonCartridge.setRunAuto(false);
	}
    }

    public int getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(int currentTab) {
	this.currentTab = currentTab;
    }

    public String getOfflineFile() {
	return offlineFile;
    }

    public void setOfflineFile(String offlineFile) {
	this.offlineFile = offlineFile;
    }

    public String getOnlineFile() {
	return onlineFile;
    }

    public void setOnlineFile(String onlineFile) {
	this.onlineFile = onlineFile;
    }

    public CommonCartridge getCommonCartridge() {
	return commonCartridge;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    
    

}
