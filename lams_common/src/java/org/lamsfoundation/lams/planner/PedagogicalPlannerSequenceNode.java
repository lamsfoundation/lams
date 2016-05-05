package org.lamsfoundation.lams.planner;

import java.util.LinkedHashSet;
import java.util.Set;

import org.lamsfoundation.lams.usermanagement.User;

/**
 *
 * @author Marcin Cieslak
 *
 */
public class PedagogicalPlannerSequenceNode {
    // user bit encoding to compact permissions into a single integer
    public static final int PERMISSION_EDITOR_VIEW = 1;
    public static final int PERMISSION_EDITOR_MODIFY = 1 << 1;
    public static final int PERMISSION_EDITOR_REPLACE = 1 << 2;
    public static final int PERMISSION_EDITOR_REMOVE = 1 << 3;
    public static final int PERMISSION_TEACHER_VIEW = 1 << 4;
    public static final int PERMISSION_TEACHER_COPY = 1 << 5;
    public static final int PERMISSION_TEACHER_PREVIEW = 1 << 6;
    public static final int PERMISSION_TEACHER_VIEW_IN_FULL_AUTHOR = 1 << 7;
    public static final int PERMISSION_TEACHER_EXPORT = 1 << 8;
    public static final int PERMISSION_TEACHER_SAVE = 1 << 9;

    public static final int PERMISSION_DEFAULT_SETTING = PERMISSION_EDITOR_VIEW + PERMISSION_EDITOR_MODIFY
	    + PERMISSION_TEACHER_VIEW + PERMISSION_TEACHER_COPY + PERMISSION_TEACHER_PREVIEW
	    + PERMISSION_TEACHER_VIEW_IN_FULL_AUTHOR + PERMISSION_TEACHER_EXPORT + PERMISSION_TEACHER_SAVE;
    // --------- persistent fields -------------
    private Long uid;
    private PedagogicalPlannerSequenceNode parent;
    private Set<PedagogicalPlannerSequenceNode> subnodes = new LinkedHashSet<PedagogicalPlannerSequenceNode>();
    private Integer order;
    private String contentFolderId;
    private String title;
    private String briefDescription;
    private String fullDescription;
    private Long learningDesignId;
    private String learningDesignTitle;
    private Boolean locked = false;
    private User user;
    private Integer permissions = PERMISSION_DEFAULT_SETTING;

    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    /**
     *
     * @return
     */
    public PedagogicalPlannerSequenceNode getParent() {
	return parent;
    }

    public void setParent(PedagogicalPlannerSequenceNode parent) {
	this.parent = parent;
    }

    /**
     *
     * @return
     */
    public Integer getOrder() {
	return order;
    }

    public void setOrder(Integer subdir) {
	order = subdir;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     * @return
     */
    public String getBriefDescription() {
	return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
	this.briefDescription = briefDescription;
    }

    /**
     *
     * @return
     */
    public String getFullDescription() {
	return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
	this.fullDescription = fullDescription;
    }

    /**
     *
     * @return
     */
    public String getLearningDesignTitle() {
	return learningDesignTitle;
    }

    public void setLearningDesignTitle(String learningDesignTitle) {
	this.learningDesignTitle = learningDesignTitle;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     *
     * @return
     */
    public Set<PedagogicalPlannerSequenceNode> getSubnodes() {
	return subnodes;
    }

    public void setSubnodes(Set<PedagogicalPlannerSequenceNode> subnodes) {
	this.subnodes = subnodes;
    }

    /**
     *
     */
    public Boolean getLocked() {
	return locked;
    }

    public void setLocked(Boolean locked) {
	this.locked = locked;
    }

    /**
     *
     */
    public Long getLearningDesignId() {
	return learningDesignId;
    }

    public void setLearningDesignId(Long id) {
	this.learningDesignId = id;
    }

    /**
     *
     */
    public String getContentFolderId() {
	return contentFolderId;
    }

    public void setContentFolderId(String contentFolderId) {
	this.contentFolderId = contentFolderId;
    }

    /**
     *
     */
    public Integer getPermissions() {
	return permissions;
    }

    public void setPermissions(Integer permissions) {
	this.permissions = permissions;
    }

    /**
     *
     */
    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }
}