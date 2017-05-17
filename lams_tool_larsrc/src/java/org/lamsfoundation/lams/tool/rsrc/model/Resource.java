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

package org.lamsfoundation.lams.tool.rsrc.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;

/**
 * Resource
 *
 * @author Dapeng Ni
 *
 *
 *
 */
public class Resource implements Cloneable {

    private static final Logger log = Logger.getLogger(Resource.class);

    // key
    private Long uid;

    // tool contentID
    private Long contentId;

    private String title;

    private String instructions;

    // advance

    private boolean runAuto;

    private int miniViewResourceNumber;

    private boolean allowAddFiles;

    private boolean allowAddUrls;

    private boolean lockWhenFinished;

    private boolean defineLater;

    private boolean contentInUse;

    private boolean notifyTeachersOnAssigmentSumbit;

    private boolean notifyTeachersOnFileUpload;

    // general infomation
    private Date created;

    private Date updated;

    private ResourceUser createdBy;

    // resource Items
    private Set resourceItems;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private Set<LearnerItemRatingCriteria> ratingCriterias;

    // *************** NON Persist Fields ********************

    private String miniViewNumberStr;

    /**
     * Default contruction method.
     *
     */
    public Resource() {
	resourceItems = new HashSet();
    }

    // **********************************************************
    // Function method for Resource
    // **********************************************************
    public static Resource newInstance(Resource defaultContent, Long contentId) {
	Resource toContent = new Resource();
	toContent = (Resource) defaultContent.clone();
	toContent.setContentId(contentId);

	// reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setResource(toContent);
	    Set<ResourceItem> items = toContent.getResourceItems();
	    for (ResourceItem item : items) {
		item.setCreateBy(toContent.getCreatedBy());
	    }
	}
	
	// reset contentId
	if (toContent.getRatingCriterias() != null) {
	    Set<LearnerItemRatingCriteria> criterias = toContent.getRatingCriterias();
	    for (LearnerItemRatingCriteria criteria : criterias) {
		criteria.setToolContentId(contentId);
	    }
	}

	return toContent;
    }

    @Override
    public Object clone() {

	Resource resource = null;
	try {
	    resource = (Resource) super.clone();
	    resource.setUid(null);
	    if (resourceItems != null) {
		Iterator iter = resourceItems.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    ResourceItem item = (ResourceItem) iter.next();
		    ResourceItem newItem = (ResourceItem) item.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newItem);
		}
		resource.resourceItems = set;
	    }
	    // clone ReourceUser as well
	    if (createdBy != null) {
		resource.setCreatedBy((ResourceUser) createdBy.clone());
	    }
	    
	    // clone ratingCriterias as well
	    if (ratingCriterias != null) {
		Set<LearnerItemRatingCriteria> newCriterias = new HashSet<LearnerItemRatingCriteria>();
		for (LearnerItemRatingCriteria criteria : ratingCriterias) {
		    LearnerItemRatingCriteria newCriteria = (LearnerItemRatingCriteria) criteria.clone();
		    // just clone old file without duplicate it in repository
		    newCriterias.add(newCriteria);
		}
		resource.ratingCriterias = newCriterias;
	    }

	    
	} catch (CloneNotSupportedException e) {
	    Resource.log.error("When clone " + Resource.class + " failed");
	}

	return resource;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Resource)) {
	    return false;
	}

	final Resource genericEntity = (Resource) o;

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
     * @return Returns the userid of the user who created the Share resources.
     *
     *
     *
     */
    public ResourceUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Share resources.
     */
    public void setCreatedBy(ResourceUser createdBy) {
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
     *            Set to true to lock the resource for finished users.
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
     *
     *
     *
     *
     *
     * @return
     */
    public Set getResourceItems() {
	return resourceItems;
    }

    public void setResourceItems(Set resourceItems) {
	this.resourceItems = resourceItems;
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
    public boolean isAllowAddFiles() {
	return allowAddFiles;
    }

    public void setAllowAddFiles(boolean allowAddFiles) {
	this.allowAddFiles = allowAddFiles;
    }

    /**
     *
     * @return
     */
    public boolean isAllowAddUrls() {
	return allowAddUrls;
    }

    public void setAllowAddUrls(boolean allowAddUrls) {
	this.allowAddUrls = allowAddUrls;
    }

    /**
     *
     * @return
     */
    public int getMiniViewResourceNumber() {
	return miniViewResourceNumber;
    }

    public void setMiniViewResourceNumber(int minViewResourceNumber) {
	miniViewResourceNumber = minViewResourceNumber;
    }

    /**
     *
     * @return
     */
    public boolean isRunAuto() {
	return runAuto;
    }

    public void setRunAuto(boolean runAuto) {
	this.runAuto = runAuto;
    }

    /**
     * For display use
     *
     * @return
     */
    public String getMiniViewNumberStr() {
	return miniViewNumberStr;
    }

    public void setMiniViewNumberStr(String minViewNumber) {
	miniViewNumberStr = minViewNumber;
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

    /**
     *
     */
    public boolean isNotifyTeachersOnAssigmentSumbit() {
	return notifyTeachersOnAssigmentSumbit;
    }

    public void setNotifyTeachersOnAssigmentSumbit(boolean notifyTeachersOnAssigmentSumbit) {
	this.notifyTeachersOnAssigmentSumbit = notifyTeachersOnAssigmentSumbit;
    }

    /**
     *
     */
    public boolean isNotifyTeachersOnFileUpload() {
	return notifyTeachersOnFileUpload;
    }

    public void setNotifyTeachersOnFileUpload(boolean notifyTeachersOnFileUpload) {
	this.notifyTeachersOnFileUpload = notifyTeachersOnFileUpload;
    }
    
    /**
    *
    * @return
    */
   public Set<LearnerItemRatingCriteria> getRatingCriterias() {
	return ratingCriterias;
   }

   public void setRatingCriterias(Set<LearnerItemRatingCriteria> ratingCriterias) {
	this.ratingCriterias = ratingCriterias;
   }

}