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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $Id$ */
package org.lamsfoundation.lams.tool.survey.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.survey.util.SurveyToolContentHandler;

/**
 * Survey
 * @author Dapeng Ni
 *
 * @hibernate.class  table="tl_lasurv11_survey"
 *
 */
public class Survey implements Cloneable{
	
	private static final Logger log = Logger.getLogger(Survey.class);
	
	//key 
	private Long uid;
	//tool contentID
	private Long contentId;
	private String title;
	private String instructions;
	//advance
	
	private boolean lockWhenFinished;
	private boolean defineLater;
	private boolean runOffline;
	private boolean contentInUse;
	//instructions
	private String onlineInstructions;
	private String offlineInstructions;
	private Set attachments;
	
	//general infomation
	private Date created;
	private Date updated;
	private SurveyUser createdBy;
	
	//survey Items
	private Set surveyItems;
	
	private boolean reflectOnActivity;
	private String reflectInstructions;
	
	//*************** NON Persist Fields ********************
	private IToolContentHandler toolContentHandler;

	private List<SurveyAttachment> onlineFileList;
	private List<SurveyAttachment> offlineFileList;
	/**
	 * Default contruction method. 
	 *
	 */
  	public Survey(){
  		attachments = new HashSet();
  		surveyItems = new HashSet();
  	}
//  **********************************************************
  	//		Function method for Survey
//  **********************************************************
	public static Survey newInstance(Survey defaultContent, Long contentId, SurveyToolContentHandler surveyToolContentHandler) {
		Survey toContent = new Survey();
		defaultContent.toolContentHandler = surveyToolContentHandler;
		toContent = (Survey) defaultContent.clone();
		toContent.setContentId(contentId);
		
		//reset user info as well
		if(toContent.getCreatedBy() != null){
			toContent.getCreatedBy().setSurvey(toContent);
			Set<SurveyQuestion> items = toContent.getSurveyItems();
			for(SurveyQuestion item:items){
				item.setCreateBy(toContent.getCreatedBy());
			}
		}
		return toContent;
	}
  	public Object clone(){
  		
  		Survey survey = null;
  		try{
  			survey = (Survey) super.clone();
  			survey.setUid(null);
  			if(surveyItems != null){
  				Iterator iter = surveyItems.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					SurveyQuestion item = (SurveyQuestion)iter.next(); 
  					SurveyQuestion newItem = (SurveyQuestion) item.clone();
					set.add(newItem);
  				}
  				survey.surveyItems = set;
  			}
  			//clone attachment
  			if(attachments != null){
  				Iterator iter = attachments.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					SurveyAttachment file = (SurveyAttachment)iter.next(); 
  					SurveyAttachment newFile = (SurveyAttachment) file.clone();
//  					if toolContentHandle is null, just clone old file without duplicate it in repository
  					if(toolContentHandler != null){
						//duplicate file node in repository
						NodeKey keys = toolContentHandler.copyFile(file.getFileUuid());
						newFile.setFileUuid(keys.getUuid());
						newFile.setFileVersionId(keys.getVersion());
  					}
					set.add(newFile);
  				}
  				survey.attachments = set;
  			}
  			//clone ReourceUser as well
  			if(this.createdBy != null){
  				survey.setCreatedBy((SurveyUser) this.createdBy.clone());
  			}
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + Survey.class + " failed");
		} catch (ItemNotFoundException e) {
			log.error("When clone " + Survey.class + " failed");
		} catch (RepositoryCheckedException e) {
			log.error("When clone " + Survey.class + " failed");
		}
  		
  		return survey;
  	}
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Survey))
			return false;

		final Survey genericEntity = (Survey) o;

      	return new EqualsBuilder()
      	.append(this.uid,genericEntity.uid)
      	.append(this.title,genericEntity.title)
      	.append(this.instructions,genericEntity.instructions)
      	.append(this.onlineInstructions,genericEntity.onlineInstructions)
      	.append(this.offlineInstructions,genericEntity.offlineInstructions)
      	.append(this.created,genericEntity.created)
      	.append(this.updated,genericEntity.updated)
      	.append(this.createdBy,genericEntity.createdBy)
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uid).append(title)
		.append(instructions).append(onlineInstructions)
		.append(offlineInstructions).append(created)
		.append(updated).append(createdBy)
		.toHashCode();
	}
	
	/**
	 * Updates the modification data for this entity.
	 */
	public void updateModificationData() {
	
		long now = System.currentTimeMillis();
		if (created == null) {
			this.setCreated (new Date(now));
		}
		this.setUpdated(new Date(now));
	}

	public void toDTO(){
		onlineFileList = new ArrayList<SurveyAttachment>();
		offlineFileList = new ArrayList<SurveyAttachment>();
		Set<SurveyAttachment> fileSet = this.getAttachments();
		if(fileSet != null){
			for(SurveyAttachment file:fileSet){
				if(StringUtils.equalsIgnoreCase(file.getFileType(),IToolContentHandler.TYPE_OFFLINE))
					offlineFileList.add(file);
				else
					onlineFileList.add(file);
			}
		}
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
     * @return Returns the userid of the user who created the Share surveys.
     *
     * @hibernate.many-to-one
     *      cascade="save-update"
     * 		column="create_by"
     *
     */
    public SurveyUser getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy The userid of the user who created this Share surveys.
     */
    public void setCreatedBy(SurveyUser createdBy) {
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
	 * 		column="title"
	 *
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	

	/**
	 * @return Returns the runOffline.
	 *
	 * @hibernate.property 
	 * 		column="run_offline"
	 *
	 */
	public boolean getRunOffline() {
		return runOffline;
	}
    
	/**
	 * @param runOffline The forceOffLine to set.
	 *
	 *
	 */
	public void setRunOffline(boolean forceOffline) {
		this.runOffline = forceOffline;
	}

    /**
     * @return Returns the lockWhenFinish.
     *
     * @hibernate.property
     * 		column="lock_on_finished"
     *
     */
    public boolean getLockWhenFinished() {
        return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished Set to true to lock the survey for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
        this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the instructions set by the teacher.
     *
     * @hibernate.property
     * 		column="instructions"
     *      type="text"
     */
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * @return Returns the onlineInstructions set by the teacher.
     *
     * @hibernate.property
     * 		column="online_instructions"
     *      type="text"
     */
    public String getOnlineInstructions() {
        return onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
        this.onlineInstructions = onlineInstructions;
    }

    /**
     * @return Returns the onlineInstructions set by the teacher.
     *
     * @hibernate.property
     * 		column="offline_instructions"
     *      type="text"
     */
    public String getOfflineInstructions() {
        return offlineInstructions;
    }

    public void setOfflineInstructions(String offlineInstructions) {
        this.offlineInstructions = offlineInstructions;
    }

	/**
     *
     * @hibernate.set   lazy="true"
     * 					cascade="all"
     * 					inverse="false"
     * 					order-by="create_date desc"
     * @hibernate.collection-key column="survey_uid"
     * @hibernate.collection-one-to-many
     * 			class="org.lamsfoundation.lams.tool.survey.model.SurveyAttachment"
     *
     * @return a set of Attachments to this Message.
     */
	public Set getAttachments() {
		return attachments;
	}

    /*
	 * @param attachments The attachments to set.
     */
    public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}

	/**
	 * 
	 * 
	 * @hibernate.set lazy="true"
	 *                inverse="false"
	 *                cascade="all"
	 *                order-by="create_date desc"
	 * @hibernate.collection-key column="survey_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.survey.model.SurveyItem"
	 * 
	 * @return
	 */
	public Set getSurveyItems() {
		return surveyItems;
	}
	public void setSurveyItems(Set surveyItems) {
		this.surveyItems= surveyItems;
	}


	/**
	 * @hibernate.property  column="content_in_use"
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
	public List<SurveyAttachment> getOfflineFileList() {
		return offlineFileList;
	}
	public void setOfflineFileList(List<SurveyAttachment> offlineFileList) {
		this.offlineFileList = offlineFileList;
	}
	public List<SurveyAttachment> getOnlineFileList() {
		return onlineFileList;
	}
	public void setOnlineFileList(List<SurveyAttachment> onlineFileList) {
		this.onlineFileList = onlineFileList;
	}
	public void setToolContentHandler(IToolContentHandler toolContentHandler) {
		this.toolContentHandler = toolContentHandler;
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
}
