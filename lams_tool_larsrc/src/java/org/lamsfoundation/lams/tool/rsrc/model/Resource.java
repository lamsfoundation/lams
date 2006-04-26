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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.rsrc.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceToolContentHandler;

/**
 * Resource
 * @author Dapeng Ni
 *
 * @hibernate.class  table="tl_larsrc11_resource"
 *
 */
public class Resource implements Cloneable{
	
	private static final Logger log = Logger.getLogger(Resource.class);
	
	//key 
	private Long uid;
	//tool contentID
	private Long contentId;
	private String title;
	private String instructions;
	//advance
	private boolean runOffline;
	private boolean runAuto;
	private int minViewResourceNumber;
	private boolean allowAddFiles;
	private boolean allowAddUrls;
	
	private boolean lockWhenFinished;
	private boolean defineLater;
	private boolean contentInUse;
	//instructions
	private String onlineInstructions;
	private String offlineInstructions;
	private Set attachments;
	
	//general infomation
	private Date created;
	private Date updated;
	private ResourceUser createdBy;
	
	//resource Items
	private Set resourceItems;
	
	//*************** NON Persist Fields ********************
	private IToolContentHandler toolContentHandler;

	private String minViewNumber;
	/**
	 * Default contruction method. 
	 *
	 */
  	public Resource(){
  		attachments = new HashSet();
  		resourceItems = new HashSet();
  	}
//  **********************************************************
  	//		Function method for Resource
//  **********************************************************
	public static Resource newInstance(Resource defaultContent, Long contentId, ResourceToolContentHandler resourceToolContentHandler) {
		Resource toContent = new Resource();
		defaultContent.toolContentHandler = resourceToolContentHandler;
		toContent = (Resource) defaultContent.clone();
		toContent.setContentId(contentId);
		return toContent;
	}
  	public Object clone(){
  		
  		Resource resource = null;
  		try{
  			resource = (Resource) super.clone();
  			resource.setUid(null);
  			if(resourceItems != null){
  				Iterator iter = resourceItems.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					ResourceItem item = (ResourceItem)iter.next(); 
  					ResourceItem newItem = (ResourceItem) item.clone();
//  					if toolContentHandle is null, just clone old file without duplicate it in repository
  					if(toolContentHandler != null && item.getFileUuid() != null){
						//duplicate file node in repository
						NodeKey keys = toolContentHandler.copyFile(item.getFileUuid());
						newItem.setFileUuid(keys.getUuid());
						newItem.setFileVersionId(keys.getVersion());
  					}
					set.add(newItem);
  				}
  				resource.resourceItems = set;
  			}
  			//clone attachment
  			if(attachments != null){
  				Iterator iter = attachments.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					ResourceAttachment file = (ResourceAttachment)iter.next(); 
  					ResourceAttachment newFile = (ResourceAttachment) file.clone();
//  					if toolContentHandle is null, just clone old file without duplicate it in repository
  					if(toolContentHandler != null){
						//duplicate file node in repository
						NodeKey keys = toolContentHandler.copyFile(file.getFileUuid());
						newFile.setFileUuid(keys.getUuid());
						newFile.setFileVersionId(keys.getVersion());
  					}
					set.add(newFile);
  				}
  				resource.attachments = set;
  			}
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + Resource.class + " failed");
		} catch (ItemNotFoundException e) {
			log.error("When clone " + Resource.class + " failed");
		} catch (RepositoryCheckedException e) {
			log.error("When clone " + Resource.class + " failed");
		}
  		
  		return resource;
  	}
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Resource))
			return false;

		final Resource genericEntity = (Resource) o;

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
     * @return Returns the userid of the user who created the Share resources.
     *
     * @hibernate.many-to-one
     *      cascade="none"
     * 		column="create_by"
     *
     */
    public ResourceUser getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy The userid of the user who created this Share resources.
     */
    public void setCreatedBy(ResourceUser createdBy) {
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
     * @param lockWhenFinished Set to true to lock the resource for finished users.
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
     * @hibernate.collection-key column="resource_uid"
     * @hibernate.collection-one-to-many
     * 			class="org.lamsfoundation.lams.tool.rsrc.model.ResourceAttachment"
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
	 * @hibernate.collection-key column="resource_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.rsrc.model.ResourceItem"
	 * 
	 * @return
	 */
	public Set getResourceItems() {
		return resourceItems;
	}
	public void setResourceItems(Set resourceItems) {
		this.resourceItems= resourceItems;
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
	/**
	 * @hibernate.property column="allow_add_files"
	 * @return
	 */
	public boolean isAllowAddFiles() {
		return allowAddFiles;
	}
	public void setAllowAddFiles(boolean allowAddFiles) {
		this.allowAddFiles = allowAddFiles;
	}
	/**
	 * @hibernate.property column="allow_add_urls"
	 * @return
	 */
	public boolean isAllowAddUrls() {
		return allowAddUrls;
	}
	public void setAllowAddUrls(boolean allowAddUrls) {
		this.allowAddUrls = allowAddUrls;
	}
	/**
	 *  @hibernate.property column="min_view_resource_number"
	 * @return
	 */
	public int getMinViewResourceNumber() {
		return minViewResourceNumber;
	}
	public void setMinViewResourceNumber(int minViewResourceNumber) {
		this.minViewResourceNumber = minViewResourceNumber;
	}
	/**
	 * @hibernate.property column="allow_auto_run"
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
	 * @return
	 */
	public String getMinViewNumber() {
		return minViewNumber;
	}
	public void setMinViewNumber(String minViewNumber) {
		this.minViewNumber = minViewNumber;
	}


}
