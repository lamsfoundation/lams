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

package org.lamsfoundation.lams.tool.imageGallery.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * ImageGallery Form.
 *
 *
 *
 * User: Andrey Balan
 */
public class ImageGalleryForm {
    private static final long serialVersionUID = 3599879328307492312L;

    private static Logger logger = Logger.getLogger(ImageGalleryForm.class.getName());

    // Forum fields
    private String sessionMapID;
    private String contentFolderID;
    private int currentTab;
    private MultipartFile offlineFile;
    private MultipartFile onlineFile;

    private ImageGallery imageGallery;
    private boolean allowRatingsOrVote;

    public ImageGalleryForm() {
	imageGallery = new ImageGallery();
	imageGallery.setTitle("Shared ImageGallery");
	currentTab = 1;
    }

    public void setImageGallery(ImageGallery imageGallery) {
	this.imageGallery = imageGallery;
	// set Form special varaible from given forum
	if (imageGallery == null) {
	    logger.error("Initial ImageGalleryForum failed by null value of ImageGallery.");
	}
    }

    public void reset(HttpServletRequest request) {
	String param = (String) request.getAttribute("action");
	// if it is start page, all data read out from database or current session
	// so need not reset checkbox to refresh value!
	if (!StringUtils.equals(param, "start.do") && !StringUtils.equals(param, "initPage.do")) {
	    imageGallery.setAllowShareImages(false);
	    imageGallery.setLockWhenFinished(false);
	    imageGallery.setDefineLater(false);
	    imageGallery.setAllowVote(false);
	}
    }

    public int getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(int currentTab) {
	this.currentTab = currentTab;
    }

    public MultipartFile getOfflineFile() {
	return offlineFile;
    }

    public void setOfflineFile(MultipartFile offlineFile) {
	this.offlineFile = offlineFile;
    }

    public MultipartFile getOnlineFile() {
	return onlineFile;
    }

    public void setOnlineFile(MultipartFile onlineFile) {
	this.onlineFile = onlineFile;
    }

    public ImageGallery getImageGallery() {
	return imageGallery;
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

    public boolean isAllowRatingsOrVote() {
	return allowRatingsOrVote;
    }

    public void setAllowRatingsOrVote(boolean allowRatingsOrVote) {
	this.allowRatingsOrVote = allowRatingsOrVote;
    }

}
