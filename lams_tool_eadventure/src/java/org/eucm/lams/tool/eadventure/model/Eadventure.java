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
/* $Id$ */
package org.eucm.lams.tool.eadventure.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * Eadventure
 * 
 * @author Dapeng Ni
 * @author ángel del Blanco
 *
 * @hibernate.class table="tl_eueadv10_eadventure"
 *
 */
public class Eadventure implements Cloneable {

    private static final Logger log = Logger.getLogger(Eadventure.class);

    //key 
    private Long uid;

    //tool contentID
    private Long contentId;

    private String title;

    private String instructions;

    //advance

    private boolean lockWhenFinished;

    private boolean defineLater;

    private boolean contentInUse;

    //general infomation
    private Date created;

    private Date updated;

    private EadventureUser createdBy;

    //eadventure Items
    //private Set eadventureItems;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    //Info from item
    private String imsSchema;

    private String organizationXml;

    //info for file
    private String initialItem;

    private Long fileUuid;

    private Long fileVersionId;

    private String fileName;

    private String fileType;

    //If is set, the eAdventure game will completed the activity, and the user can't complete it by clicking in finish
    private boolean defineComplete;

    private Set params;

    private Set conditions;

    // DTO fields:
    private boolean complete;

    /**
     * Default contruction method.
     *
     */
    public Eadventure() {
	params = new HashSet();
	conditions = new HashSet();

    }

    //  **********************************************************
    //		Function method for Eadventure
    //  **********************************************************
    public static Eadventure newInstance(Eadventure defaultContent, Long contentId) {
	Eadventure toContent = new Eadventure();
	toContent = (Eadventure) defaultContent.clone();
	toContent.setContentId(contentId);

	//reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setEadventure(toContent);
	}

	return toContent;
    }

    @Override
    public Object clone() {

	Eadventure eadventure = null;
	try {
	    eadventure = (Eadventure) super.clone();
	    eadventure.setUid(null);
	    //clone ReourceUser as well
	    if (createdBy != null) {
		eadventure.setCreatedBy((EadventureUser) createdBy.clone());
	    }
	    if (params != null) {
		Iterator iter = params.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    EadventureParam param = (EadventureParam) iter.next();
		    EadventureParam newParam = (EadventureParam) param.clone();

		    set.add(newParam);
		}
		eadventure.params = set;
	    }

	    if (conditions != null) {
		Iterator iter = conditions.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    EadventureCondition cond = (EadventureCondition) iter.next();
		    EadventureCondition newCond = (EadventureCondition) cond.clone();
		    //TODO ver como hago esto bien
		    Iterator it2 = newCond.getEadListExpression().iterator();
		    while (it2.hasNext()) {
			EadventureExpression eadExpr = (EadventureExpression) it2.next();
			Iterator it3 = eadventure.params.iterator();
			while (it3.hasNext()) {
			    EadventureParam eadPar = (EadventureParam) it3.next();
			    if (eadPar.getName().equals(eadExpr.getFirstOp().getName())) {
				eadExpr.setFirstOp(eadPar);
			    }
			    if (eadExpr.getVarIntroduced() != null
				    && eadPar.getName().equals(eadExpr.getVarIntroduced().getName())) {
				eadExpr.setVarIntroduced(eadPar);
			    }
			}
		    }
		    set.add(newCond);
		}

		eadventure.conditions = set;
	    }
	} catch (CloneNotSupportedException e) {
	    Eadventure.log.error("When clone " + Eadventure.class + " failed");
	}

	return eadventure;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Eadventure)) {
	    return false;
	}

	final Eadventure genericEntity = (Eadventure) o;

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

    //**********************************************************
    // get/set methods
    //**********************************************************
    /**
     * Returns the object's creation date
     *
     * @return date
     * @hibernate.property column="create_date"
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
     * @hibernate.property column="update_date"
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
     * @return Returns the userid of the user who created the Share eadventure.
     *
     * @hibernate.many-to-one
     * 		       cascade="save-update"
     *                        column="create_by"
     *
     */
    public EadventureUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Share eadventure.
     */
    public void setCreatedBy(EadventureUser createdBy) {
	this.createdBy = createdBy;
    }

    /**
     * @hibernate.id column="uid" generator-class="native"
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
     * @hibernate.property
     * 		    column="title"
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
     * @hibernate.property
     * 		    column="lock_on_finished"
     *
     */
    public boolean getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished
     *            Set to true to lock the eadventure for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the instructions set by the teacher.
     *
     * @hibernate.property
     * 		    column="instructions"
     *                     type="text"
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @hibernate.property column="content_in_use"
     * @return
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * @hibernate.property column="define_later"
     * @return
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @hibernate.property column="content_id" unique="true"
     * @return
     */
    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    /**
     * @hibernate.property column="reflect_instructions"
     * @return
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     * @hibernate.property column="reflect_on_activity"
     * @return
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     * @hibernate.property column="ims_schema" type="text"
     * @return
     */
    public String getImsSchema() {
	return imsSchema;
    }

    public void setImsSchema(String imsSchema) {
	this.imsSchema = imsSchema;
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
     * @hibernate.property column="file_name" type="text"
     * 
     * @return
     */
    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	this.fileName = name;
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

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    public boolean isComplete() {
	return complete;
    }

    /**
     * @hibernate.property column="file_type"
     * @return
     */
    public String getFileType() {
	return fileType;
    }

    public void setFileType(String fileType) {
	this.fileType = fileType;
    }

    /**
     * @hibernate.property column="define_complete"
     * @return
     */

    public boolean isDefineComplete() {
	return defineComplete;
    }

    public void setDefineComplete(boolean defineComplete) {
	this.defineComplete = defineComplete;
    }

    /**
     * 
     * @hibernate.set lazy="true" inverse="false" cascade="all"
     * @hibernate.collection-key column="eadventure_uid"
     * @hibernate.collection-one-to-many class="org.eucm.lams.tool.eadventure.model.EadventureParam"
     * 
     * @return
     */

    public Set getParams() {
	return params;
    }

    public void setParams(Set params) {
	this.params = params;
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
     * 
     * @hibernate.set lazy="true" inverse="false" cascade="all" order-by = "sequence_id"
     * @hibernate.collection-key column="eadventure_uid"
     * @hibernate.collection-one-to-many class="org.eucm.lams.tool.eadventure.model.EadventureCondition"
     * 
     * @return
     */
    public Set getConditions() {
	return conditions;
    }

    public void setConditions(Set conditions) {
	this.conditions = conditions;
    }

}
