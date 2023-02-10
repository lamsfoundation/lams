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

package org.lamsfoundation.lams.tool.commonCartridge.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * CommonCartridge Item
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laimsc11_commoncartridge_item")
public class CommonCartridgeItem implements Cloneable {
    private static final Logger log = Logger.getLogger(CommonCartridgeItem.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    // CommonCartridge Type:1=URL,2=File,3=Website,4=Learning Object
    @Column(name = "item_type")
    private short type;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String url;

    @Column(name = "ims_schema")
    private String imsSchema;

    @Column(name = "init_item")
    private String initialItem;

    @Column(name = "organization_xml")
    private String organizationXml;

    @Column(name = "launch_url")
    private String launchUrl;

    @Column(name = "secure_launch_url")
    private String secureLaunchUrl;

    @Column(name = "tool_key")
    private String key;

    @Column(name = "tool_secret")
    private String secret;

    @Column(name = "custom_str")
    private String customStr;

    @Column(name = "frame_height")
    private int frameHeight;

    @Column(name = "open_url_new_window")
    private boolean openUrlNewWindow;

    @Column(name = "button_text")
    private String buttonText;

    @Column(name = "file_uuid")
    private Long fileUuid;

    @Column(name = "file_version_id")
    private Long fileVersionId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "item_uid")
    @OrderBy("sequence_id ASC")
    private Set<CommonCartridgeItemInstruction> itemInstructions = new HashSet<>();

    @Column(name = "is_hide")
    private boolean isHide;

    @Column(name = "create_by_author")
    private boolean isCreateByAuthor;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private CommonCartridgeUser createBy;

    // *********************************************** DTO fields
    @Transient
    private boolean complete;

    @Transient
    private String fileDisplayUuid;

    @Override
    public Object clone() {
	CommonCartridgeItem obj = null;
	try {
	    obj = (CommonCartridgeItem) super.clone();
	    // clone attachment
	    if (itemInstructions != null) {
		Iterator<CommonCartridgeItemInstruction> iter = itemInstructions.iterator();
		Set<CommonCartridgeItemInstruction> set = new HashSet<>();
		while (iter.hasNext()) {
		    CommonCartridgeItemInstruction instruct = iter.next();
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

    @Override
    public String toString() {
	return new ToStringBuilder(this).append(" uid", uid).append(" type", type).append(" title", title).toString();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    public Long getUid() {
	return uid;
    }

    public void setUid(Long userID) {
	this.uid = userID;
    }

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long crUuid) {
	this.fileUuid = crUuid;
    }

    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long crVersionId) {
	this.fileVersionId = crVersionId;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getImsSchema() {
	return imsSchema;
    }

    public void setImsSchema(String imsSchema) {
	this.imsSchema = imsSchema;
    }

    public String getInitialItem() {
	return initialItem;
    }

    public void setInitialItem(String initialItem) {
	this.initialItem = initialItem;
    }

    public Set<CommonCartridgeItemInstruction> getItemInstructions() {
	return itemInstructions;
    }

    public void setItemInstructions(Set<CommonCartridgeItemInstruction> itemInstructions) {
	this.itemInstructions = itemInstructions;
    }

    public String getOrganizationXml() {
	return organizationXml;
    }

    public void setOrganizationXml(String organizationXml) {
	this.organizationXml = organizationXml;
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

    public CommonCartridgeUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(CommonCartridgeUser createBy) {
	this.createBy = createBy;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public boolean isCreateByAuthor() {
	return isCreateByAuthor;
    }

    public void setCreateByAuthor(boolean isCreateByAuthor) {
	this.isCreateByAuthor = isCreateByAuthor;
    }

    public boolean isHide() {
	return isHide;
    }

    public void setHide(boolean isHide) {
	this.isHide = isHide;
    }

    public short getType() {
	return type;
    }

    public void setType(short type) {
	this.type = type;
    }

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String type) {
	this.fileType = type;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	this.fileName = name;
    }

    public boolean isOpenUrlNewWindow() {
	return openUrlNewWindow;
    }

    public void setOpenUrlNewWindow(boolean openUrlNewWindow) {
	this.openUrlNewWindow = openUrlNewWindow;
    }

    public String getLaunchUrl() {
	return launchUrl;
    }

    public void setLaunchUrl(String launchUrl) {
	this.launchUrl = launchUrl;
    }

    public String getSecureLaunchUrl() {
	return secureLaunchUrl;
    }

    public void setSecureLaunchUrl(String secureLaunchUrl) {
	this.secureLaunchUrl = secureLaunchUrl;
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

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    public boolean isComplete() {
	return complete;
    }

    public String getFileDisplayUuid() {
	return fileDisplayUuid;
    }

    public void setFileDisplayUuid(String fileDisplayUuid) {
	this.fileDisplayUuid = fileDisplayUuid;
    }
}