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

package org.lamsfoundation.lams.tool.dokumaran.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * Dokumaran
 *
 * @author Andrey Balan
 */
public class Dokumaran implements Cloneable {

    private static final Logger log = Logger.getLogger(Dokumaran.class);

    // key
    private Long uid;

    // tool contentID
    private Long contentId;

    private String title;

    private String instructions;

    // advance

    private boolean useSelectLeaderToolOuput;
    
    private boolean allowMultipleLeaders;

    private boolean showChat;

    private boolean showLineNumbers;
    
    private String sharedPadId;

    private boolean lockWhenFinished;

    private boolean defineLater;

    private boolean contentInUse;

    // general infomation
    private Date created;

    private Date updated;

    private DokumaranUser createdBy;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    // **********************************************************
    // Function method for Dokumaran
    // **********************************************************
    public static Dokumaran newInstance(Dokumaran defaultContent, Long contentId) {
	Dokumaran toContent = new Dokumaran();
	toContent = (Dokumaran) defaultContent.clone();
	toContent.setContentId(contentId);

	// reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setDokumaran(toContent);
	}
	return toContent;
    }

    @Override
    public Object clone() {

	Dokumaran dokumaran = null;
	try {
	    dokumaran = (Dokumaran) super.clone();
	    dokumaran.setUid(null);
	    // clone ReourceUser as well
	    if (createdBy != null) {
		dokumaran.setCreatedBy((DokumaranUser) createdBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    Dokumaran.log.error("When clone " + Dokumaran.class + " failed");
	}

	return dokumaran;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Dokumaran)) {
	    return false;
	}

	final Dokumaran genericEntity = (Dokumaran) o;

	return new EqualsBuilder().append(uid, genericEntity.uid).append(title, genericEntity.title)
		.append(instructions, genericEntity.instructions).append(created, genericEntity.created)
		.append(updated, genericEntity.updated).append(createdBy, genericEntity.createdBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(created).append(updated)
		.append(createdBy).toHashCode();
    }

    /**
     * Updates the modification data for this entity.
     */
    public void updateModificationData() {

	long now = System.currentTimeMillis();
	if (created == null) {
	    this.setCreated(new Date(now));
	}
	this.setUpdated(new Date(now));
    }

    // **********************************************************
    // get/set methods
    // **********************************************************
    /**
     * Returns the object's creation date
     *
     * @return date
     *
     */
    public Date getCreated() {
	return created;
    }

    /**
     * Sets the object's creation date
     *
     * @param created
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * Returns the object's date of last update
     *
     * @return date updated
     *
     */
    public Date getUpdated() {
	return updated;
    }

    /**
     * Sets the object's date of last update
     *
     * @param updated
     */
    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    /**
     * @return Returns the userid of the user who created the Share dokumaran.
     *
     *
     *
     */
    public DokumaranUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Share dokumaran.
     */
    public void setCreatedBy(DokumaranUser createdBy) {
	this.createdBy = createdBy;
    }

    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the title.
     *
     *
     *
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return Returns the lockWhenFinish.
     *
     *
     *
     */
    public boolean getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished
     *            Set to true to lock the dokumaran for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the instructions set by the teacher.
     *
     *
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     *
     * @return
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     *
     * @return
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     *
     * @return
     */
    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    /**
     *
     * @return
     */
    public boolean isShowChat() {
	return showChat;
    }

    public void setShowChat(boolean showChat) {
	this.showChat = showChat;
    }

    /**
     *
     * @return
     */
    public boolean isShowLineNumbers() {
	return showLineNumbers;
    }

    public void setShowLineNumbers(boolean showLineNumbers) {
	this.showLineNumbers = showLineNumbers;
    }
    
    /**
    *
    * @return
    */
   public String getSharedPadId() {
	return sharedPadId;
   }

   public void setSharedPadId(String sharedPadId) {
	this.sharedPadId = sharedPadId;
   }
   
   public boolean isSharedPadEnabled() {
       return StringUtils.isNotEmpty(sharedPadId);
   }

    /**
     *
     * @return
     */
    public boolean isUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }
    
    /**
    *
    * @return
    */
   public boolean isAllowMultipleLeaders() {
	return allowMultipleLeaders;
   }

   public void setAllowMultipleLeaders(boolean allowMultipleLeaders) {
	this.allowMultipleLeaders = allowMultipleLeaders;
   }

    /**
     *
     * @return
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
     * @return
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }
}
