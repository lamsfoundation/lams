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

import org.springframework.web.multipart.MultipartFile;

/**
 * CommonCartridge Item Form.
 *
 *
 * @author Steve.Ni
 */
public class CommonCartridgeItemForm {
    private String itemIndex;
    private String sessionMapID;

    // tool access mode;
    private String mode;

    private String title;
    private short itemType;
    private String description;
    private String url;
    private boolean openUrlNewWindow;
    // flag of this item has attachment or not
    private boolean hasFile;
    private Long fileUuid;
    private Long fileVersionId;
    private String fileName;
    private MultipartFile file;

    private String launchUrl;
    private String secureLaunchUrl;
    private String key;
    private String secret;
    private String customStr;
    private int frameHeight;
    private String buttonText;

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public MultipartFile getFile() {
	return file;
    }

    public void setFile(MultipartFile file) {
	this.file = file;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
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

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long fileUuid) {
	this.fileUuid = fileUuid;
    }

    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long fileVersionId) {
	this.fileVersionId = fileVersionId;
    }

    public boolean isHasFile() {
	return hasFile;
    }

    public void setHasFile(boolean hasFile) {
	this.hasFile = hasFile;
    }

    public boolean isOpenUrlNewWindow() {
	return openUrlNewWindow;
    }

    public void setOpenUrlNewWindow(boolean openUrlNewWindow) {
	this.openUrlNewWindow = openUrlNewWindow;
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

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getSecret() {
	return secret;
    }

    public void setSecret(String secret) {
	this.secret = secret;
    }

    public String getCustomStr() {
	return customStr;
    }

    public void setCustomStr(String customStr) {
	this.customStr = customStr;
    }

    public String getButtonText() {
	return buttonText;
    }

    public void setButtonText(String buttonText) {
	this.buttonText = buttonText;
    }

    public int getFrameHeight() {
	return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
	this.frameHeight = frameHeight;
    }

}
