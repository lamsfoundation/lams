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


package org.lamsfoundation.lams.tool.pixlr.model;

import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 *
 * Caches the user details. This allows the tool to be more efficient at
 * displaying user names but means that when people's names change, they won't
 * change in the "old" tool data.
 *
 * @hibernate.class table="tl_lapixl10_user"
 */

public class PixlrUser implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3701664859818409197L;

    // Fields
    private Long uid;

    private Long userId;

    private String lastName;

    private String firstName;

    private String loginName;

    private PixlrSession pixlrSession;

    private boolean finishedActivity;

    private Long entryUID;

    private String imageFileName;

    private Long imageHeight;

    private Long imageWidth;

    private boolean imageHidden;

    // Constructors

    /** default constructor */
    public PixlrUser() {
    }

    public PixlrUser(UserDTO user, PixlrSession pixlrSession) {
	this.userId = new Long(user.getUserID().intValue());
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.pixlrSession = pixlrSession;
	this.finishedActivity = false;
    }

    /** full constructor */
    public PixlrUser(Long userId, String lastName, String firstName, PixlrSession pixlrSession) {
	this.userId = userId;
	this.lastName = lastName;
	this.firstName = firstName;
	this.pixlrSession = pixlrSession;
    }

    // Property accessors
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     */
    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="user_id" length="20"
     *
     */
    public Long getUserId() {
	return this.userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    /**
     * @hibernate.property column="last_name" length="255"
     *
     */
    public String getLastName() {
	return this.lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * @hibernate.property column="login_name" length="255"
     *
     */
    public String getLoginName() {
	return loginName;
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    /**
     * @hibernate.property column="first_name" length="255"
     *
     */
    public String getFirstName() {
	return this.firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * @hibernate.property column="finishedActivity"
     */
    public boolean isFinishedActivity() {
	return finishedActivity;
    }

    public void setFinishedActivity(boolean finishedActivity) {
	this.finishedActivity = finishedActivity;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="pixlr_session_uid"
     *
     */
    public PixlrSession getPixlrSession() {
	return this.pixlrSession;
    }

    public void setPixlrSession(PixlrSession pixlrSession) {
	this.pixlrSession = pixlrSession;
    }

    /**
     * @hibernate.property column="entry_uid"
     */
    public Long getEntryUID() {
	return entryUID;
    }

    public void setEntryUID(Long entryUID) {
	this.entryUID = entryUID;
    }

    /**
     * @hibernate.property column="image_file_name" length="511"
     *
     */

    public String getImageFileName() {
	return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
	this.imageFileName = imageFileName;
    }

    /**
     * @hibernate.property column="image_height
     *
     */
    public Long getImageHeight() {
	return imageHeight;
    }

    public void setImageHeight(Long imageHeight) {
	this.imageHeight = imageHeight;
    }

    /**
     * @hibernate.property column="image_width
     *
     */
    public Long getImageWidth() {
	return imageWidth;
    }

    public void setImageWidth(Long imageWidth) {
	this.imageWidth = imageWidth;
    }

    /**
     * @hibernate.property column="image_hidden
     *
     */
    public boolean isImageHidden() {
	return imageHidden;
    }

    public void setImageHidden(boolean imageHidden) {
	this.imageHidden = imageHidden;
    }

    /**
     * toString
     *
     * @return String
     */
    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("userId").append("='").append(getUserId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if ((other == null)) {
	    return false;
	}
	if (!(other instanceof PixlrUser)) {
	    return false;
	}
	PixlrUser castOther = (PixlrUser) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }
}
