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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.planner.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.planner.PedagogicalPlannerSequenceNode;
import org.lamsfoundation.lams.planner.dao.PedagogicalPlannerDAO;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class PedagogicalPlannerSequenceNodeDTO {
    private Long uid;
    private List<String[]> titlePath;
    private String title;
    private String briefDescription;
    private String fullDescription;
    private String learningDesignTitle;
    private Boolean locked;
    private List<PedagogicalPlannerSequenceNodeDTO> subnodes;
    private Long parentUid;
    // which existing LD to open
    private Long learningDesignId;
    // access control
    private Boolean isOwner = false;
    private Boolean isEditor = false;

    private Boolean permitViewTemplate;
    private Boolean permitPreview;
    private Boolean permitEditCopy;
    private Boolean permitEditOriginal;
    private Boolean permitReplaceTemplate;
    private Boolean permitRemoveNode;

    // Not node-bound variables, but simply attributes used in JSP page
    private Boolean edit = false;
    private Boolean createSubnode = false;
    private Boolean importNode = false;

    // for the list on the main screen
    private List<PedagogicalPlannerSequenceNodeDTO> recentlyModifiedNodes;
    private Boolean displayAddRemoveEditorsLink = true;

    private static PedagogicalPlannerDAO pedagogicalPlannerDAO;

    private static final String FULL_DESCRIPTION_NOT_EMPTY = "FULL";

    public PedagogicalPlannerSequenceNodeDTO() {
    }

    public PedagogicalPlannerSequenceNodeDTO(PedagogicalPlannerSequenceNode node,
	    Set<PedagogicalPlannerSequenceNode> subnodes, boolean isSysAdmin, PedagogicalPlannerDAO dao) {
	if (pedagogicalPlannerDAO == null) {
	    pedagogicalPlannerDAO = dao;
	}
	uid = node.getUid();
	title = node.getTitle();
	briefDescription = node.getBriefDescription();
	fullDescription = node.getFullDescription();
	learningDesignTitle = node.getLearningDesignTitle();
	locked = node.getLocked();
	if (node.getParent() != null) {
	    parentUid = node.getParent().getUid();
	}

	HttpSession s = SessionManager.getSession();
	UserDTO user = (UserDTO) s.getAttribute(AttributeNames.USER);
	PedagogicalPlannerSequenceNodeDTO.setPermissions(this, isSysAdmin, user, node);

	this.subnodes = new LinkedList<PedagogicalPlannerSequenceNodeDTO>();
	if (subnodes != null) {
	    for (PedagogicalPlannerSequenceNode subnode : subnodes) {
		PedagogicalPlannerSequenceNodeDTO subnodeDTO = new PedagogicalPlannerSequenceNodeDTO();
		subnodeDTO.setTitle(subnode.getTitle());
		subnodeDTO.setBriefDescription(subnode.getBriefDescription());
		if (!StringUtils.isEmpty(subnode.getFullDescription())) {
		    subnodeDTO.setFullDescription(PedagogicalPlannerSequenceNodeDTO.FULL_DESCRIPTION_NOT_EMPTY);
		}
		subnodeDTO.setLocked(subnode.getLocked());
		subnodeDTO.setLearningDesignTitle(subnode.getLearningDesignTitle());
		subnodeDTO.setUid(subnode.getUid());
		// viewing for everyone is the default setting
		PedagogicalPlannerSequenceNodeDTO.setPermissions(subnodeDTO, isSysAdmin, user, subnode);
		if (user != null) {
		    subnodeDTO.setDisplayAddRemoveEditorsLink(subnodeDTO.isEditor);
		}
		this.subnodes.add(subnodeDTO);
	    }
	}
    }

    private static void setPermissions(PedagogicalPlannerSequenceNodeDTO dto, boolean isSysAdmin, UserDTO user,
	    PedagogicalPlannerSequenceNode node) {
	// set permissions the view is based on
	dto.isOwner = isSysAdmin
		|| (user != null && node.getUser() != null && user.getUserID().equals(node.getUser().getUserId()));
	dto.isEditor = dto.isOwner || (user != null
		&& pedagogicalPlannerDAO.isEditor(user.getUserID(), node.getUid(), Role.ROLE_SYSADMIN));
	Integer nodePermissions = node.getPermissions();
	dto.permitViewTemplate = dto.isOwner || nodePermissions == null
		|| (dto.isEditor ? (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_VIEW) != 0
			: (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_VIEW) != 0);
	dto.permitPreview = dto.isOwner || nodePermissions == null || dto.isEditor
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_PREVIEW) != 0;
	dto.permitEditCopy = dto.isOwner || nodePermissions == null || dto.isEditor
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_COPY) != 0;
	dto.permitEditOriginal = dto.isOwner || (dto.isEditor && (nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_MODIFY) != 0));
	dto.permitReplaceTemplate = dto.isOwner || (dto.isEditor && (nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_REPLACE) != 0));
	dto.permitRemoveNode = dto.isOwner || (dto.isEditor && (nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_REMOVE) != 0));
    }

    public List<String[]> getTitlePath() {
	return titlePath;
    }

    public void setTitlePath(List<String[]> titlePath) {
	this.titlePath = titlePath;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getBriefDescription() {
	return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
	this.briefDescription = briefDescription;
    }

    public String getFullDescription() {
	return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
	this.fullDescription = fullDescription;
    }

    public String getLearningDesignTitle() {
	return learningDesignTitle;
    }

    public void setLearningDesignTitle(String fileName) {
	this.learningDesignTitle = fileName;
    }

    public List<PedagogicalPlannerSequenceNodeDTO> getSubnodes() {
	return subnodes;
    }

    public void setSubnodes(List<PedagogicalPlannerSequenceNodeDTO> subnodes) {
	this.subnodes = subnodes;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Boolean getLocked() {
	return locked;
    }

    public void setLocked(Boolean locked) {
	this.locked = locked;
    }

    public Long getParentUid() {
	return parentUid;
    }

    public void setParentUid(Long parentUid) {
	this.parentUid = parentUid;
    }

    public Boolean getEdit() {
	return edit;
    }

    public void setEdit(Boolean edit) {
	this.edit = edit;
    }

    public Boolean getCreateSubnode() {
	return createSubnode;
    }

    public void setCreateSubnode(Boolean createSubnode) {
	this.createSubnode = createSubnode;
    }

    public Boolean getIsEditor() {
	return isEditor;
    }

    public void setIsEditor(Boolean hasRole) {
	this.isEditor = hasRole;
    }

    public Boolean getImportNode() {
	return importNode;
    }

    public void setImportNode(Boolean importNode) {
	this.importNode = importNode;
    }

    public Long getLearningDesignId() {
	return learningDesignId;
    }

    public void setLearningDesignId(Long learningDesignId) {
	this.learningDesignId = learningDesignId;
    }

    public List<PedagogicalPlannerSequenceNodeDTO> getRecentlyModifiedNodes() {
	return recentlyModifiedNodes;
    }

    public void setRecentlyModifiedNodes(List<PedagogicalPlannerSequenceNodeDTO> recentlyModifiedNodes) {
	this.recentlyModifiedNodes = recentlyModifiedNodes;
    }

    public Boolean getDisplayAddRemoveEditorsLink() {
	return displayAddRemoveEditorsLink;
    }

    public void setDisplayAddRemoveEditorsLink(Boolean displayAddRemoveEditorsLink) {
	this.displayAddRemoveEditorsLink = displayAddRemoveEditorsLink;
    }

    public Boolean getPermitEditCopy() {
	return permitEditCopy;
    }

    public void setPermitEditCopy(Boolean editCopyPermitted) {
	this.permitEditCopy = editCopyPermitted;
    }

    public Boolean getPermitEditOriginal() {
	return permitEditOriginal;
    }

    public void setPermitEditOriginal(Boolean editOriginalPermitted) {
	this.permitEditOriginal = editOriginalPermitted;
    }

    public Boolean getIsOwner() {
	return isOwner;
    }

    public void setIsOwner(Boolean isOwner) {
	this.isOwner = isOwner;
    }

    public Boolean getPermitViewTemplate() {
	return permitViewTemplate;
    }

    public void setPermitViewTemplate(Boolean permitViewTemplate) {
	this.permitViewTemplate = permitViewTemplate;
    }

    public Boolean getPermitPreview() {
	return permitPreview;
    }

    public void setPermitPreview(Boolean permitPreview) {
	this.permitPreview = permitPreview;
    }

    public Boolean getPermitReplaceTemplate() {
	return permitReplaceTemplate;
    }

    public void setPermitReplaceTemplate(Boolean permitChangeTemplate) {
	this.permitReplaceTemplate = permitChangeTemplate;
    }

    public Boolean getPermitRemoveNode() {
	return permitRemoveNode;
    }

    public void setPermitRemoveNode(Boolean permitRemoveTemplate) {
	this.permitRemoveNode = permitRemoveTemplate;
    }
}