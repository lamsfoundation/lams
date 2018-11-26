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

package org.lamsfoundation.lams.workspace;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;

/**
 * @author Manpreet Minhas
 */
@Entity
@Table(name = "lams_workspace_folder_content")
public class WorkspaceFolderContent implements Serializable {

    private static final long serialVersionUID = 3861351481415168595L;
    public static final Integer CONTENT_TYPE_FILE = 1;
    public static final Integer CONTENT_TYPE_PACKAGE = 2;

    @Id
    @Column(name = "folder_content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderContentID;

    /**
     * non-nullable persistent field
     * representing the type of content.
     * It can be either CONTENT_TYPE_FILE or
     * CONTENT_TYPE_PACKAGE
     */
    @Column(name = "content_type_id")
    private Integer contentTypeID;

    /**
     * non-nullable persistent field
     * representing the name of the File
     **/
    @Column
    private String name;

    /**
     * non-nullable persistent field
     * representing the description of the File
     **/
    @Column
    private String description;

    /**
     * non-nullable persistent field
     * representing the date the content
     * was created.
     **/
    @Column(name = "create_date_time")
    private Date createDate;

    /**
     * non-nullable persistent field
     * representing the date the content
     * was last modified.
     **/
    @Column(name = "last_modified_date")
    private Date lastModified;

    /**
     * nullable persistent field. Represents the two
     * part key - UUID and version, returned by the
     * ContentRepository once the content has been successfully updated.
     **/
    @Column
    private Long uuid;

    /**
     * nullable persistent field. Represents the two
     * part key - UUID and version, returned by the
     * ContentRepository once the content has been successfully updated.
     **/
    @Column(name = "version_id")
    private Long versionID;

    /**
     * non-nullable persistent field
     * indicating the type of the file
     **/
    @Column(name = "mime_type")
    private String mimeType;

    /**
     * non-nullable persistent field indicating the
     * <code>WorkspaceFolder</code> that contains
     * this content
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_folder_id")
    private WorkspaceFolder workspaceFolder;

    /** Default Constructor */
    public WorkspaceFolderContent() {

    }

    /** Full Constructor */
    public WorkspaceFolderContent(Long folderContentID, Integer contentTypeID, String name, String description,
	    Date createDate, Date lastModified, Long uuid, Long versionID, String mimeType,
	    WorkspaceFolder workspaceFolder) {
	super();
	this.folderContentID = folderContentID;
	this.contentTypeID = contentTypeID;
	this.name = name;
	this.description = description;
	this.createDate = createDate;
	this.lastModified = lastModified;
	this.uuid = uuid;
	this.versionID = versionID;
	this.mimeType = mimeType;
	this.workspaceFolder = workspaceFolder;
    }

    /** Minimal Constructor */
    public WorkspaceFolderContent(Integer contentTypeID, String name, String description, Date createDate,
	    Date lastModified, String mimeType, WorkspaceFolder workspaceFolder) {
	super();
	this.contentTypeID = contentTypeID;
	this.name = name;
	this.description = description;
	this.createDate = createDate;
	this.lastModified = lastModified;
	this.mimeType = mimeType;
	this.workspaceFolder = workspaceFolder;
    }

    /**
     * @return Returns the contentTypeID.
     */
    public Integer getContentTypeID() {
	return contentTypeID;
    }

    /**
     * @param contentTypeID
     *            The contentTypeID to set.
     */
    public void setContentTypeID(Integer contentTypeID) {
	this.contentTypeID = contentTypeID;
    }

    /**
     * @return Returns the createDate.
     */
    public Date getCreateDate() {
	return createDate;
    }

    /**
     * @param createDate
     *            The createDate to set.
     */
    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @return Returns the folderContentID.
     */
    public Long getFolderContentID() {
	return folderContentID;
    }

    /**
     * @param folderContentID
     *            The folderContentID to set.
     */
    public void setFolderContentID(Long folderContentID) {
	this.folderContentID = folderContentID;
    }

    /**
     * @return Returns the lastModified.
     */
    public Date getLastModified() {
	return lastModified;
    }

    /**
     * @param lastModified
     *            The lastModified to set.
     */
    public void setLastModified(Date lastModified) {
	this.lastModified = lastModified;
    }

    /**
     * @return Returns the mimeType.
     */
    public String getMimeType() {
	return mimeType;
    }

    /**
     * @param mimeType
     *            The mimeType to set.
     */
    public void setMimeType(String mimeType) {
	this.mimeType = mimeType;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return Returns the uuid.
     */
    public Long getUuid() {
	return uuid;
    }

    /**
     * @param uuid
     *            The uuid to set.
     */
    public void setUuid(Long uuid) {
	this.uuid = uuid;
    }

    /**
     * @return Returns the versionID.
     */
    public Long getVersionID() {
	return versionID;
    }

    /**
     * @param versionID
     *            The versionID to set.
     */
    public void setVersionID(Long versionID) {
	this.versionID = versionID;
    }

    /**
     * @return Returns the workspaceFolder.
     */
    public WorkspaceFolder getWorkspaceFolder() {
	return workspaceFolder;
    }

    /**
     * @param workspaceFolder
     *            The workspaceFolder to set.
     */
    public void setWorkspaceFolder(WorkspaceFolder workspaceFolder) {
	this.workspaceFolder = workspaceFolder;
    }
}
