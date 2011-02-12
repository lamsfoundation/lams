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

/* $Id$ */
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

    // Not node-bound variables, but simply attributes used in JSP page
    private Boolean edit = false;
    private Boolean createSubnode = false;
    private Boolean hasRole = true;
    private Boolean importNode = false;
    // for the list on the main screen
    private List<PedagogicalPlannerSequenceNodeDTO> recentlyModifiedNodes;
    private Boolean displayAddRemoveEditorsLink = true;

    private static final String FULL_DESCRIPTION_NOT_EMPTY = "FULL";

    public PedagogicalPlannerSequenceNodeDTO() {
    }
    
    public PedagogicalPlannerSequenceNodeDTO(PedagogicalPlannerSequenceNode node,
	    Set<PedagogicalPlannerSequenceNode> subnodes, Boolean isSysadmin, PedagogicalPlannerDAO dao) {
	uid = node.getUid();
	title = node.getTitle();
	briefDescription = node.getBriefDescription();
	fullDescription = node.getFullDescription();
	learningDesignTitle = node.getLearningDesignTitle();
	locked = node.getLocked();
	if (node.getParent() != null) {
	    parentUid = node.getParent().getUid();
	}
	this.subnodes = new LinkedList<PedagogicalPlannerSequenceNodeDTO>();
	if (subnodes != null) {
	    HttpSession s = SessionManager.getSession();
	    UserDTO u = (UserDTO) s.getAttribute(AttributeNames.USER);
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
		if (u != null) {
		    subnodeDTO.setDisplayAddRemoveEditorsLink(isSysadmin || dao.isEditor(u.getUserID(), subnode.getUid(), Role.ROLE_AUTHOR_ADMIN));
		}
		this.subnodes.add(subnodeDTO);
	    }
	}
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

    public Boolean getHasRole() {
	return hasRole;
    }

    public void setHasRole(Boolean hasRole) {
	this.hasRole = hasRole;
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
}