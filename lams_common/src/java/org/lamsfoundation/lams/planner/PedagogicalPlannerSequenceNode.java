package org.lamsfoundation.lams.planner;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @hibernate.class table="lams_planner_nodes"
 * @author Marcin Cieslak
 * 
 */
public class PedagogicalPlannerSequenceNode {

    // --------- persistent fields -------------
    private Long uid;
    private PedagogicalPlannerSequenceNode parent;
    private Set<PedagogicalPlannerSequenceNode> subnodes = new LinkedHashSet<PedagogicalPlannerSequenceNode>();
    private Integer order;
    private String contentFolderId;
    private String title;
    private String briefDescription;
    private String fullDescription;
    private Long fileUuid;
    private String fileName;
    private Boolean locked = false;

    /**
     * @hibernate.id column="uid" generator-class="native" type="java.lang.Long"
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @hibernate.many-to-one column="parent_uid" cascade="save-update" foreign-key="FK_lams_planner_node_parent"
     * @return
     */
    public PedagogicalPlannerSequenceNode getParent() {
	return parent;
    }

    public void setParent(PedagogicalPlannerSequenceNode parent) {
	this.parent = parent;
    }

    /**
     * @hibernate.property column="order_id"
     * @return
     */
    public Integer getOrder() {
	return order;
    }

    public void setOrder(Integer subdir) {
	order = subdir;
    }

    /**
     * @hibernate.property column="title"
     * @return
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.property column="brief_desc"
     * @return
     */
    public String getBriefDescription() {
	return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
	this.briefDescription = briefDescription;
    }

    /**
     * @hibernate.property column="full_desc"
     * @return
     */
    public String getFullDescription() {
	return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
	this.fullDescription = fullDescription;
    }

    /**
     * @hibernate.property column="file_name"
     * @return
     */
    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    private void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.set cascade="all" order-by="order_id asc"
     * @hibernate.collection-key column="parent_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.planner.PedagogicalPlannerSequenceNode"
     * @return
     */
    public Set<PedagogicalPlannerSequenceNode> getSubnodes() {
	return subnodes;
    }

    public void setSubnodes(Set<PedagogicalPlannerSequenceNode> subnodes) {
	this.subnodes = subnodes;
    }

    /**
     * @hibernate.property column="locked"
     * @return
     */
    public Boolean getLocked() {
	return locked;
    }

    public void setLocked(Boolean locked) {
	this.locked = locked;
    }

    /**
     * @hibernate.property column="file_uuid"
     * @return
     */
    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long fileUuid) {
	this.fileUuid = fileUuid;
    }

    /**
     * @hibernate.property column="content_folder_id"
     * @return
     */
    public String getContentFolderId() {
	return contentFolderId;
    }

    public void setContentFolderId(String contentFolderId) {
	this.contentFolderId = contentFolderId;
    }

}