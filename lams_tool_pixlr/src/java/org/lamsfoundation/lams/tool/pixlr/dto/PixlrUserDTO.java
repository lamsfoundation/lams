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



package org.lamsfoundation.lams.tool.pixlr.dto;

import org.lamsfoundation.lams.tool.pixlr.model.PixlrUser;

public class PixlrUserDTO implements Comparable<Object> {

    public Long uid;

    public Long userId;

    public String loginName;

    public String firstName;

    public String lastName;

    public boolean finishedActivity;

    public Long entryUID;

    public String imageFileName;

    private Long imageHeight;

    private Long imageWidth;

    private long imageThumbnailWidth;

    private long imageThumbnailHeight;

    private boolean imageHidden;

    public PixlrUserDTO(PixlrUser user) {
	this.uid = user.getUid();
	this.loginName = user.getLoginName();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.finishedActivity = user.isFinishedActivity();
	this.entryUID = user.getEntryUID();
	this.imageFileName = user.getImageFileName();
	this.userId = user.getUserId();
	this.imageHeight = user.getImageHeight();
	this.imageWidth = user.getImageWidth();
	this.imageHidden = user.isImageHidden();
	setThumbnailDimensions();
    }

    @Override
    public int compareTo(Object o) {
	int returnValue;
	PixlrUserDTO toUser = (PixlrUserDTO) o;
	returnValue = this.lastName.compareTo(toUser.lastName);
	if (returnValue == 0) {
	    returnValue = this.uid.compareTo(toUser.uid);
	}
	return returnValue;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getLoginName() {
	return loginName;
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getEntryUID() {
	return entryUID;
    }

    public void setEntryUID(Long entryUID) {
	this.entryUID = entryUID;
    }

    public boolean isFinishedActivity() {
	return finishedActivity;
    }

    public void setFinishedActivity(boolean finishedActivity) {
	this.finishedActivity = finishedActivity;
    }

    public String getImageFileName() {
	return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
	this.imageFileName = imageFileName;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getImageHeight() {
	return imageHeight;
    }

    public void setImageHeight(Long imageHeight) {
	this.imageHeight = imageHeight;
    }

    public Long getImageWidth() {
	return imageWidth;
    }

    public void setImageWidth(Long imageWidth) {
	this.imageWidth = imageWidth;
    }

    public long getImageThumbnailWidth() {
	return imageThumbnailWidth;
    }

    public void setImageThumbnailWidth(long imageThumbnailWidth) {
	this.imageThumbnailWidth = imageThumbnailWidth;
    }

    public long getImageThumbnailHeight() {
	return imageThumbnailHeight;
    }

    public void setImageThumbnailHeight(long imageThumbnailHeight) {
	this.imageThumbnailHeight = imageThumbnailHeight;
    }

    public boolean isImageHidden() {
	return imageHidden;
    }

    public void setImageHidden(boolean imageHidden) {
	this.imageHidden = imageHidden;
    }

    // set thumbnail dimensions with a maximum of 100 width or height
    public void setThumbnailDimensions() {
	if (imageWidth != null && imageHeight != null) {
	    if (imageWidth >= imageHeight) {
		this.imageThumbnailWidth = 100;
		double temp = 100.0 / imageWidth.longValue();
		this.imageThumbnailHeight = (new Double(imageHeight.longValue() * temp)).longValue();

	    } else {
		this.imageThumbnailHeight = 100;
		double temp = 100.0 / imageHeight.longValue();
		this.imageThumbnailWidth = (new Double(imageWidth.longValue() * temp)).longValue();
	    }
	}

    }
}
