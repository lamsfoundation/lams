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

/* $Id$ */
package org.eucm.lams.tool.eadventure.dto;

import java.util.List;

import org.eucm.lams.tool.eadventure.model.Eadventure;
import org.eucm.lams.tool.eadventure.model.EadventureUser;

/**
 * List contains following element: <br>
 *
 * <li>session_id</li>
 * <li>session_name</li>
 * <li>EadventureItem.uid</li>
 * <li>
 * EadventureItem.item_type</li>
 * <li>EadventureItem.create_by_author</li>
 * <li>
 * EadventureItem.is_hide</li>
 * <li>EadventureItem.title</li>
 * <li>User.login_name</li>
 * <li>count(eadventure_item_uid)</li>
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class Summary {

    private Long sessionId;
    private String sessionName;
    private Long eadventureUid;
    private boolean[] existList;
    private String[] reportList;
    private List<EadventureUser> users;
    private int numberOfLearners;
    private int numberOfFinishedLearners;
    //private short itemType;
    //private boolean itemCreateByAuthor;
    //TODO ver que pasa con itemHide
    private boolean itemHide;
    private String itemTitle;
    //TODO ver que pasa con item instructions
    //  private List<String> itemInstructions = new ArrayList<String>();
    private String username;
    //TODO Cuidado, = viewNumber y numberOfLearners....
    private int viewNumber;

    // following is used for export portfolio programs:
    private String url;
    private Long fileUuid;
    private Long fileVersionId;
    private String fileName;
    private String attachmentLocalUrl;

    // true: initial group item, false, belong to some group.
    private boolean isInitGroup;

    public Summary() {
    }

    /**
     * Contruction method for monitoring summary function.
     *
     * <B>Don't not set isInitGroup and viewNumber fields</B>
     *
     * @param sessionName
     * @param item
     * @param isInitGroup
     */
    public Summary(Long sessionId, String sessionName, Eadventure ead) {
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	if (ead != null) {
	    this.eadventureUid = ead.getUid();
	    //this.itemType = ead.getType();
	    // this.itemCreateByAuthor = ead.isCreatedByAuthor();

	    //TODO ver que pasa con hide
	    // this.itemHide = ead.isHide();
	    this.itemTitle = ead.getTitle();
	    this.username = ead.getCreatedBy() == null ? "" : ead.getCreatedBy().getLoginName();
	    //  this.url = ead.protocol(ead.getUrl());
	    this.fileName = ead.getFileName();
	    this.fileUuid = ead.getFileUuid();
	    this.fileVersionId = ead.getFileVersionId();
	} else {
	    this.eadventureUid = new Long(-1);
	}
    }

    /**
     * Contruction method for export profolio function.
     *
     * <B>Don't not set sessionId and viewNumber fields</B>
     *
     * @param sessionName
     * @param item
     * @param isInitGroup
     */
    public Summary(Long sessionId, String sessionName, Eadventure ead, boolean isInitGroup) {
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	if (ead != null) {
	    this.eadventureUid = ead.getUid();
	    //TODO ver que pasa con HIDE
	    //this.itemHide = ead.isHide();
	    this.itemTitle = ead.getTitle();
	    this.username = ead.getCreatedBy() == null ? "" : ead.getCreatedBy().getLoginName();
	    // this.url = EadventureWebUtils.protocol(item.getUrl());
	    this.fileName = ead.getFileName();
	    this.fileUuid = ead.getFileUuid();
	    this.fileVersionId = ead.getFileVersionId();

	} else {
	    this.eadventureUid = new Long(-1);
	    //this.isInitGroup = isInitGroup;
	}
    }

    public boolean isItemHide() {
	return itemHide;
    }

    public void setItemHide(boolean itemHide) {
	this.itemHide = itemHide;
    }

    public String getItemTitle() {
	return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
	this.itemTitle = itemTitle;
    }

    public Long getItemUid() {
	return eadventureUid;
    }

    public void setItemUid(Long itemUid) {
	this.eadventureUid = itemUid;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
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

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public boolean isInitGroup() {
	return isInitGroup;
    }

    public void setInitGroup(boolean isInitGroup) {
	this.isInitGroup = isInitGroup;
    }

    public String getAttachmentLocalUrl() {
	return attachmentLocalUrl;
    }

    public void setAttachmentLocalUrl(String attachmentLocalUrl) {
	this.attachmentLocalUrl = attachmentLocalUrl;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public int getViewNumber() {
	return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
	this.viewNumber = viewNumber;
    }

    public boolean[] getExistList() {
	return existList;
    }

    public void setExistList(boolean[] existList) {
	this.existList = existList;
    }

    public List<EadventureUser> getUsers() {
	return users;
    }

    public void setUsers(List<EadventureUser> users) {
	this.users = users;
    }

    public int getNumberOfLearners() {
	return numberOfLearners;
    }

    public void setNumberOfLearners(int numberOfLearners) {
	this.numberOfLearners = numberOfLearners;
    }

    public int getNumberOfFinishedLearners() {
	return numberOfFinishedLearners;
    }

    public void setNumberOfFinishedLearners(int numberOfFinishedLearners) {
	this.numberOfFinishedLearners = numberOfFinishedLearners;
    }

    public String[] getReportList() {
	return reportList;
    }

    public void setReportList(String[] reportList) {
	this.reportList = reportList;
    }

}
