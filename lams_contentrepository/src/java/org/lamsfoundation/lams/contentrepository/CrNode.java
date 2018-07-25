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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.contentrepository;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 *
 *
 *
 *
*/
public class CrNode implements Serializable {

    protected Logger log = Logger.getLogger(CrNode.class);

    /** identifier field */
    private Long nodeId;

    /** nullable persistent field */
    private String path;

    /** persistent field */
    private String type;

    /** nullable persistent field */
    private Date createdDateTime;

    /** persistent field */
    private Long nextVersionId;

    /** persistent field */
    private org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace;

    /** persistent field */
    private org.lamsfoundation.lams.contentrepository.CrNodeVersion parentNodeVersion;

    /** versions of this node. persistent field */
    private Set crNodeVersions;

    /** default constructor - used by Hibernate */
    public CrNode() {
    }

    /**
     * Create the CrNode and its initial CrNodeVersion.
     */
    public CrNode(String relPath, String nodeTypeName, Date createdDateTime, Integer userId, CrWorkspace workspace,
	    CrNodeVersion parentNodeVersion, String versionDescription) {

	this.path = relPath;
	this.type = nodeTypeName;
	this.createdDateTime = createdDateTime;

	// start the next version id at 1, which is used straight away by incrementNextVersionId()
	this.nextVersionId = new Long(1);

	this.crWorkspace = workspace;
	this.parentNodeVersion = parentNodeVersion;
	this.addCrNodeVersion(
		new CrNodeVersion(this, createdDateTime, incrementNextVersionId(), versionDescription, userId));
    }

    /** full constructor */
/*
 * public CrNode(String path, String type, Date createdDateTime, Long nextVersionId,
 * org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace,
 * org.lamsfoundation.lams.contentrepository.CrNodeVersion parentNodeVersion, Set crNodeVersions) {
 * }
 */

    /** minimal constructor */
/*
 * public CrNode(String type, Long nextVersionId, org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace,
 * org.lamsfoundation.lams.contentrepository.CrNodeVersion parentNodeVersion, Set crNodeVersions) {
 * this.type = type;
 * this.nextVersionId = nextVersionId;
 * this.crWorkspace = crWorkspace;
 * this.parentNodeVersion = parentNodeVersion;
 * this.crNodeVersions = crNodeVersions;
 * }
 */
    /**
     *
     *
     *
     *
     *
     *
     */
    public Long getNodeId() {
	return this.nodeId;
    }

    public void setNodeId(Long nodeId) {
	this.nodeId = nodeId;
    }

    /**
     *
     *
     *
     *
     */
    public String getPath() {
	return this.path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    /**
     *
     *
     *
     *
     *
     */
    public String getType() {
	return this.type;
    }

    public void setType(String type) {
	this.type = type;
    }

    /**
     *
     *
     *
     *
     */
    public Date getCreatedDateTime() {
	return this.createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
	this.createdDateTime = createdDateTime;
    }

    /**
     *
     *
     *
     *
     */
    public Long getNextVersionId() {
	return this.nextVersionId;
    }

    public void setNextVersionId(Long nextVersionId) {
	this.nextVersionId = nextVersionId;
    }

    /**
     * bi-directional many-to-one association to CrWorkspace
     * 
     *
     *
     *
     * 
     */
    public org.lamsfoundation.lams.contentrepository.CrWorkspace getCrWorkspace() {
	return this.crWorkspace;
    }

    public void setCrWorkspace(org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace) {
	this.crWorkspace = crWorkspace;
    }

    /**
     * Get the parent node/version to this node.
     * bi-directional many-to-one association to CrNodeVersion.
     * 
     *
     *
     *
     *
     *
     * 
     */
    public org.lamsfoundation.lams.contentrepository.CrNodeVersion getParentNodeVersion() {
	return this.parentNodeVersion;
    }

    public void setParentNodeVersion(org.lamsfoundation.lams.contentrepository.CrNodeVersion parentNodeVersion) {
	this.parentNodeVersion = parentNodeVersion;
    }

    /**
     * bi-directional one-to-many association to CrNodeVersion
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     * usage = "transactional"
     * 
     */
    public Set getCrNodeVersions() {
	return this.crNodeVersions;
    }

    private void setCrNodeVersions(Set crNodeVersions) {
	this.crNodeVersions = crNodeVersions;
    }

    /** Add a version to this node */
    public void addCrNodeVersion(CrNodeVersion version) {
	if (getCrNodeVersions() == null) {
	    Set set = new HashSet();
	    set.add(version);
	    setCrNodeVersions(set);
	} else {
	    getCrNodeVersions().add(version);
	}
	version.setNode(this);
    }

    /**
     * Remove a version to this node. Returns true if the version was found and deleted.
     * For some reason, this seems to be more reliable than getCrNodeVersions().remove(version)
     * in junit tests.
     */
    public boolean removeCrNodeVersion(CrNodeVersion version) {
	if (getCrNodeVersions() != null) {
	    Iterator iter = getCrNodeVersions().iterator();
	    while (iter.hasNext()) {
		CrNodeVersion element = (CrNodeVersion) iter.next();
		if (element == version) {
		    iter.remove();
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("nodeId", getNodeId()).append("path", getPath())
		.append("type", getType()).append("createdDateTime", getCreatedDateTime()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof CrNode)) {
	    return false;
	}
	CrNode castOther = (CrNode) other;
	return new EqualsBuilder().append(this.getNodeId(), castOther.getNodeId())
		.append(this.getPath(), castOther.getPath()).append(this.getType(), castOther.getType())
		.append(this.getCreatedDateTime(), castOther.getCreatedDateTime())
		.append(this.getCrWorkspace(), castOther.getCrWorkspace())
		.append(this.getParentNodeVersion(), castOther.getParentNodeVersion()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getNodeId()).append(getPath()).append(getType())
		.append(getCreatedDateTime()).append(getCrWorkspace()).append(getParentNodeVersion()).toHashCode();
    }

    /* ********* Manually added methods ****/

    /**
     * Get the next version id for this node and then
     * increment it. All other ids come out of the database
     * but for the version we need the next id within a
     * uuid, and the db can't help us there.
     */
    public synchronized Long incrementNextVersionId() {
	return nextVersionId++;
    }

    /**
     * Get the history for this node. Quite intensive operation
     * as it has to build all the data structures. Can't cache
     * it as can't tell easily when the versions are changed.
     * 
     * @return SortedSet of IVersionDetail objects, ordered by version
     */
    public SortedSet getVersionHistory() {

	SortedSet history = new TreeSet();

	Set versions = getCrNodeVersions();
	if (versions != null) {
	    Iterator iter = versions.iterator();
	    while (iter.hasNext()) {
		CrNodeVersion element = (CrNodeVersion) iter.next();
		String desc = element.getVersionDescription();
		history.add(new SimpleVersionDetail(element.getVersionId(), element.getCreatedDateTime(), desc));
	    }
	}
	return history;
    }

    /**
     * Indicates whether this node is of the specified node type.
     * 
     * @param nodeTypeName
     *            the name of a node type.
     * @return true if this node is of the specified node type
     *         or a subtype of the specified node type; returns false otherwise.
     */
    public boolean isNodeType(String nodeTypeName) {
	return nodeTypeName != null && nodeTypeName.equals(getType());
    }

    /**
     * Get a particular version of this node. If versionId is null, then
     * gets the latest version.
     */
    public CrNodeVersion getNodeVersion(Long versionId) {

	CrNodeVersion nodeVersion = null;
	Set nodeVersionSet = getCrNodeVersions();

	if (nodeVersionSet != null) {
	    Iterator iter = nodeVersionSet.iterator();
	    if (versionId != null) {
		nodeVersion = findParticularVersion(iter, versionId);
	    } else {
		nodeVersion = findLatestVersion(iter);
	    }
	}
	return nodeVersion;
    }

    /*
     * Assumes that verionId will never be null - otherwise findLatestVersion
     * would have been called.
     */
    private CrNodeVersion findParticularVersion(Iterator nodeVersionSetIterator, Long versionId) {
	// find version, throw exception if not found.
	CrNodeVersion found = null;
	while (nodeVersionSetIterator.hasNext()) {
	    CrNodeVersion element = (CrNodeVersion) nodeVersionSetIterator.next();
	    if (versionId.equals(element.getVersionId())) {
		found = element;
		break;
	    }
	}
	return found;
    }

    /*
     * Find the latest version of a node. Tries to find the node
     * with the highest version number. If it finds a node with
     * no version number, that will only be selected if there
     * are no other versions.
     */
    private CrNodeVersion findLatestVersion(Iterator nodeVersionSetIterator) {
	// find lastest version, throw exception if not found.
	CrNodeVersion found = null;
	while (nodeVersionSetIterator.hasNext()) {
	    CrNodeVersion element = (CrNodeVersion) nodeVersionSetIterator.next();
	    if (found == null
		    || (found.getVersionId() != null && found.getVersionId().compareTo(element.getVersionId()) < 0)) {
		found = element;
	    }
	}
	return found;
    }

    /** Get a list of all the versions - just the Long value. */
    public Long[] getVersionIds() {
	Set allCrNodeVersions = getCrNodeVersions();
	Long[] versions = new Long[allCrNodeVersions != null ? allCrNodeVersions.size() : 0];
	Iterator iter = allCrNodeVersions.iterator();
	int i = 0;
	while (iter.hasNext()) {
	    CrNodeVersion element = (CrNodeVersion) iter.next();
	    versions[i] = element.getVersionId();
	}
	return versions;
    }

}
