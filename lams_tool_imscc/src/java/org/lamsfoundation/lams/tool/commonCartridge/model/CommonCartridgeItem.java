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
package org.lamsfoundation.lams.tool.commonCartridge.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * CommonCartridge
 *
 * @author Andrey Balan
 *
 * @hibernate.class table="tl_laimsc11_commoncartridge_item"
 *
 */
public class CommonCartridgeItem implements Cloneable {
    private static final Logger log = Logger.getLogger(CommonCartridgeItem.class);

    private Long uid;
    // CommonCartridge Type:1=URL,2=File,3=Website,4=Learning Object
    private short type;

    private String title;

    private String description;

    private String url;

    private String imsSchema;

    private String initialItem;

    private String organizationXml;

    private String launchUrl;
    private String secureLaunchUrl;
    private String key;
    private String secret;
    private String customStr;
    private int frameHeight;
    private boolean openUrlNewWindow;
    private String buttonText;

    private Long fileUuid;

    private Long fileVersionId;

    private String fileName;

    private String fileType;

    private Set itemInstructions;

    private boolean isHide;
    private boolean isCreateByAuthor;

    private Date createDate;
    private CommonCartridgeUser createBy;

    // ***********************************************
    // DTO fields:
    private boolean complete;

    @Override
    public Object clone() {
	CommonCartridgeItem obj = null;
	try {
	    obj = (CommonCartridgeItem) super.clone();
	    // clone attachment
	    if (itemInstructions != null) {
		Iterator iter = itemInstructions.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    CommonCartridgeItemInstruction instruct = (CommonCartridgeItemInstruction) iter.next();
		    CommonCartridgeItemInstruction newInsruct = (CommonCartridgeItemInstruction) instruct.clone();
		    set.add(newInsruct);
		}
		obj.itemInstructions = set;
	    }
	    obj.setUid(null);
	    // clone ReourceUser as well
	    if (this.createBy != null) {
		obj.setCreateBy((CommonCartridgeUser) this.createBy.clone());
	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + CommonCartridgeItem.class + " failed");
	}

	return obj;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     * @hibernate.property column="file_uuid"
     *
     * @return
     */
    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long crUuid) {
	this.fileUuid = crUuid;
    }

    /**
     * @hibernate.property column="file_version_id"
     * @return
     */
    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long crVersionId) {
	this.fileVersionId = crVersionId;
    }

    /**
     * @hibernate.property column="description"
     * @return
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @hibernate.property column="ims_schema"
     * @return
     */
    public String getImsSchema() {
	return imsSchema;
    }

    public void setImsSchema(String imsSchema) {
	this.imsSchema = imsSchema;
    }

    /**
     * @hibernate.property column="init_item"
     * @return
     */
    public String getInitialItem() {
	return initialItem;
    }

    public void setInitialItem(String initialItem) {
	this.initialItem = initialItem;
    }

    /**
     * @hibernate.set lazy="false" cascade="all-delete-orphan" inverse="false" order-by="sequence_id asc"
     * @hibernate.collection-key column="item_uid"
     * @hibernate.collection-one-to-many
     * 				  class=
     *                                   "org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItemInstruction"
     * @return
     */
    public Set getItemInstructions() {
	return itemInstructions;
    }

    public void setItemInstructions(Set itemInstructions) {
	this.itemInstructions = itemInstructions;
    }

    /**
     * @hibernate.property column="organization_xml" length="65535"
     * @return
     */
    public String getOrganizationXml() {
	return organizationXml;
    }

    public void setOrganizationXml(String organizationXml) {
	this.organizationXml = organizationXml;
    }

    /**
     * @hibernate.property column="title" length="255"
     * @return
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.property column="url" length="65535"
     * @return
     */
    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    /**
     * @hibernate.many-to-one cascade="none" column="create_by"
     *
     * @return
     */
    public CommonCartridgeUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(CommonCartridgeUser createBy) {
	this.createBy = createBy;
    }

    /**
     * @hibernate.property column="create_date"
     * @return
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     * @hibernate.property column="create_by_author"
     * @return
     */
    public boolean isCreateByAuthor() {
	return isCreateByAuthor;
    }

    public void setCreateByAuthor(boolean isCreateByAuthor) {
	this.isCreateByAuthor = isCreateByAuthor;
    }

    /**
     * @hibernate.property column="is_hide"
     * @return
     */
    public boolean isHide() {
	return isHide;
    }

    public void setHide(boolean isHide) {
	this.isHide = isHide;
    }

    /**
     * @hibernate.property column="item_type"
     * @return
     */
    public short getType() {
	return type;
    }

    public void setType(short type) {
	this.type = type;
    }

    /**
     * @hibernate.property column="file_type"
     */
    public String getFileType() {
	return fileType;
    }

    public void setFileType(String type) {
	this.fileType = type;
    }

    /**
     * @hibernate.property column="file_name"
     */
    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	this.fileName = name;
    }

    /**
     * @hibernate.property column="open_url_new_window"
     * @return
     */
    public boolean isOpenUrlNewWindow() {
	return openUrlNewWindow;
    }

    public void setOpenUrlNewWindow(boolean openUrlNewWindow) {
	this.openUrlNewWindow = openUrlNewWindow;
    }

    /**
     * @hibernate.property column="launch_url" length="255"
     * @return
     */
    public String getLaunchUrl() {
	return launchUrl;
    }

    public void setLaunchUrl(String launchUrl) {
	this.launchUrl = launchUrl;
    }

    /**
     * @hibernate.property column="secure_launch_url" length="255"
     * @return
     */
    public String getSecureLaunchUrl() {
	return secureLaunchUrl;
    }

    public void setSecureLaunchUrl(String secureLaunchUrl) {
	this.secureLaunchUrl = secureLaunchUrl;
    }

    /**
     * @hibernate.property column="tool_key" length="255"
     * @return
     */
    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    /**
     * @hibernate.property column="tool_secret" length="255"
     * @return
     */
    public String getSecret() {
	return secret;
    }

    public void setSecret(String secret) {
	this.secret = secret;
    }

    /**
     * @hibernate.property column="custom_str" length="65535"
     * @return
     */
    public String getCustomStr() {
	return customStr;
    }

    public void setCustomStr(String customStr) {
	this.customStr = customStr;
    }

    /**
     * @hibernate.property column="button_text" length="255"
     * @return
     */
    public String getButtonText() {
	return buttonText;
    }

    public void setButtonText(String buttonText) {
	this.buttonText = buttonText;
    }

    /**
     * @hibernate.property column="frame_height"
     * @return
     */
    public int getFrameHeight() {
	return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
	this.frameHeight = frameHeight;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    public boolean isComplete() {
	return complete;
    }
}
