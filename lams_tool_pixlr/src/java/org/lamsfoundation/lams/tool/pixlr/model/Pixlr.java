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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;

@Entity
@Table(name = "tl_lapixl10_pixlr")
public class Pixlr implements java.io.Serializable, Cloneable {
    private static final long serialVersionUID = 579733009969321015L;

    private static Logger log = Logger.getLogger(Pixlr.class.getName());

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "create_by")
    private Long createBy;

    @Column
    private String title;

    @Column
    private String instructions;

    @Column(name = "lock_on_finished")
    private boolean lockOnFinished;

    @Column(name = "allow_view_others_images")
    private boolean allowViewOthersImages;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "tool_content_id")
    private Long toolContentId;

    @Column(name = "image_file_name")
    private String imageFileName; // Image uploaded for pixlr

    @Column(name = "image_width")
    private Long imageWidth;

    @Column(name = "image_height")
    private Long imageHeight;

    @OneToMany(mappedBy = "pixlr")
    private Set<PixlrSession> pixlrSessions = new HashSet<PixlrSession>();

    public Pixlr() {
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public Long getCreateBy() {
	return createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public boolean isAllowViewOthersImages() {
	return allowViewOthersImages;
    }

    public void setAllowViewOthersImages(boolean allowViewOthersImages) {
	this.allowViewOthersImages = allowViewOthersImages;
    }

    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    public Set<PixlrSession> getPixlrSessions() {
	return pixlrSessions;
    }

    public void setPixlrSessions(Set<PixlrSession> pixlrSessions) {
	this.pixlrSessions = pixlrSessions;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("title").append("='").append(getTitle()).append("' ");
	buffer.append("instructions").append("='").append(getInstructions()).append("' ");
	buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (other == null) {
	    return false;
	}
	if (!(other instanceof Pixlr)) {
	    return false;
	}
	Pixlr castOther = (Pixlr) other;

	return this.getUid() == castOther.getUid()
		|| this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid());
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Pixlr newInstance(Pixlr fromContent, Long toContentId) {
	Pixlr toContent = new Pixlr();
	toContent = (Pixlr) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    @Override
    protected Object clone() {

	Pixlr pixlr = null;
	try {
	    pixlr = (Pixlr) super.clone();
	    pixlr.setUid(null);

	    // create an empty set for the pixlrSession
	    pixlr.pixlrSessions = new HashSet<PixlrSession>();

	} catch (CloneNotSupportedException cnse) {
	    Pixlr.log.error("Error cloning " + Pixlr.class);
	}
	return pixlr;
    }

    public String getImageFileName() {
	return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
	this.imageFileName = imageFileName;
    }

    public Long getImageWidth() {
	return imageWidth;
    }

    public void setImageWidth(Long imageWidth) {
	this.imageWidth = imageWidth;
    }

    public Long getImageHeight() {
	return imageHeight;
    }

    public void setImageHeight(Long imageHeight) {
	this.imageHeight = imageHeight;
    }

}