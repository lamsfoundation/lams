package org.lamsfoundation.lams.contentrepository;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/** 
 *        @hibernate.class
 *         table="lams_cr_node"
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
    
    /** transitory field. 
	 * don't create the versionHistory till requested, but
	 * keep it just in case the caller asks for it again.
	 */
	private SortedSet versionHistory = null;

    /** full constructor */
    public CrNode(String path, String type, Date createdDateTime, Long nextVersionId, org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace, org.lamsfoundation.lams.contentrepository.CrNodeVersion parentNodeVersion, Set crNodeVersions) {
        this.path = path;
        this.type = type;
        this.createdDateTime = createdDateTime;
        this.nextVersionId = nextVersionId;
        this.crWorkspace = crWorkspace;
        this.parentNodeVersion = parentNodeVersion;
        this.crNodeVersions = crNodeVersions;
    }

    /** default constructor */
    public CrNode() {
    }

    /** minimal constructor */
    public CrNode(String type, Long nextVersionId, org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace,  org.lamsfoundation.lams.contentrepository.CrNodeVersion parentNodeVersion, Set crNodeVersions) {
        this.type = type;
        this.nextVersionId = nextVersionId;
        this.crWorkspace = crWorkspace;
        this.parentNodeVersion = parentNodeVersion;
        this.crNodeVersions = crNodeVersions;
    }

    /** 
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="node_id"
     *             unsaved-value="0"
     *         
     */
    public Long getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    /** 
     *            @hibernate.property
     *             column="path"
     *             length="255"
     *         
     */
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /** 
     *            @hibernate.property
     *             column="type"
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /** 
     *            @hibernate.property
     *             column="created_date_time"
     *             length="14"
     *         
     */
    public Date getCreatedDateTime() {
        return this.createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /** 
     *            @hibernate.property
     *             type="java.lang.Long"
     *             column="next_version_id"
     *         
     */
    public Long getNextVersionId() {
        return this.nextVersionId;
    }

    public void setNextVersionId(Long nextVersionId) {
        this.nextVersionId = nextVersionId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="workspace_id"         
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
     *  
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="parent_nv_id"         
     *         
     */
    public org.lamsfoundation.lams.contentrepository.CrNodeVersion getParentNodeVersion() {
        return this.parentNodeVersion;
    }

    public void setParentNodeVersion(org.lamsfoundation.lams.contentrepository.CrNodeVersion parentNodeVersion) {
        this.parentNodeVersion = parentNodeVersion;
    }


    /** 
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="all-delete-orphan"
     *            @hibernate.collection-key
     *             column="node_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.contentrepository.CrNodeVersion"
     *         
     */
    public Set getCrNodeVersions() {
        return this.crNodeVersions;
    }

    public void setCrNodeVersions(Set crNodeVersions) {
        this.crNodeVersions = crNodeVersions;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("nodeId", getNodeId())
            .append("path", getPath())
            .append("type", getType())
            .append("createdDateTime", getCreatedDateTime())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof CrNode) ) return false;
        CrNode castOther = (CrNode) other;
        return new EqualsBuilder()
            .append(this.getNodeId(), castOther.getNodeId())
            .append(this.getPath(), castOther.getPath())
            .append(this.getType(), castOther.getType())
            .append(this.getCreatedDateTime(), castOther.getCreatedDateTime())
            .append(this.getCrWorkspace(), castOther.getCrWorkspace())
            .append(this.getParentNodeVersion(), castOther.getParentNodeVersion())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getNodeId())
            .append(getPath())
            .append(getType())
            .append(getCreatedDateTime())
            .append(getCrWorkspace())
            .append(getParentNodeVersion())			
            .toHashCode();
    }

    /* ********* Manually added methods ****/
    /** Get the next version id for this node and then 
     * increment it. All other ids come out of the database
     * but for the version we need the next id within a 
     * uuid, and the db can't help us there.
     */
    public synchronized Long incrementNextVersionId() {
    	Long retValue = nextVersionId;
    	nextVersionId = new Long(nextVersionId.longValue() + 1);
    	return retValue;
    }
    
	/** 
	 * Get the history for this node. Quite intensive operation the
	 * first time it is run on a node as it has to build all the 
	 * data structures.
	 * @return SortedSet of IVersionDetail objects, ordered by version
	 */
	public SortedSet getVersionHistory() {
		
		if ( versionHistory == null ) {
			
			SortedSet history = new TreeSet();
			
			Set versions = getCrNodeVersions();
			if ( versions != null ) {
				Iterator iter = versions.iterator();
				while (iter.hasNext()) {
					CrNodeVersion element = (CrNodeVersion) iter.next();
					String desc = element.getVersionDescription();
					history.add(	new SimpleVersionDetail(
										element.getVersionId(),
										element.getCreatedDateTime(), 
										desc ));
				}
			}
			versionHistory = history;
		}
		
		return versionHistory;
	}

    /**
     * Indicates whether this node is of the specified node type.
     * @param nodeTypeName the name of a node type.
     * @return true if this node is of the specified node type
     * or a subtype of the specified node type; returns false otherwise.
     */
	public boolean isNodeType(String nodeTypeName) {
		return nodeTypeName != null && nodeTypeName.equals(getType());		
	}

	/** Get a particular version of this node 
	 */
	public CrNodeVersion getNodeVersion(Long versionId) 
		{

		long start = System.currentTimeMillis();
		String key = "getNodeVersion "+versionId;

		CrNodeVersion nodeVersion = null;
		Set nodeVersionSet = getCrNodeVersions();
		log.error(key+" gotSet "+(System.currentTimeMillis()-start));
		
		if ( nodeVersionSet != null ) {
			Iterator iter = nodeVersionSet.iterator();
			if ( versionId != null ) {
				nodeVersion = findParticularVersion(iter, versionId);
			} else {
				nodeVersion = findLatestVersion(iter);
			}
		} 
		log.error(key+" gotVersion "+(System.currentTimeMillis()-start));
		return nodeVersion;
	}
	
	/* Assumes that verionId will never be null - otherwise findLatestVersion
	 * would have been called.
	 */
	private CrNodeVersion findParticularVersion(Iterator nodeVersionSetIterator, Long versionId ) {
		// find version, throw exception if not found.
		CrNodeVersion found = null;
		while (nodeVersionSetIterator.hasNext()) {
			CrNodeVersion element = (CrNodeVersion) nodeVersionSetIterator.next();
			if ( versionId.equals(element.getVersionId()) ) {
				found = element;
				break;
			}
		}
		return found;
	}

	/* Find the latest version of a node. Tries to find the node
	 * with the highest version number. If it finds a node with
	 * no version number, that will only be selected if there 
	 * are no other versions.
	 */
	private CrNodeVersion findLatestVersion(Iterator nodeVersionSetIterator ) {
		// find lastest version, throw exception if not found.
		CrNodeVersion found = null;
		while (nodeVersionSetIterator.hasNext()) {
			CrNodeVersion element = (CrNodeVersion) nodeVersionSetIterator.next();
			if ( found == null || ( found.getVersionId() != null &&
					found.getVersionId().compareTo(element.getVersionId()) < 0 ))
				found = element;
		}
		return found;
	}

	/** Get a list of all the versions - just the Long value. */
	public Long[] getVersionIds() {
		Set allCrNodeVersions = getCrNodeVersions();
		Long[] versions = new Long[allCrNodeVersions!=null?allCrNodeVersions.size():0];
		Iterator iter = allCrNodeVersions.iterator();
		int i=0;
		while (iter.hasNext()) {
			CrNodeVersion element = (CrNodeVersion) iter.next();
			versions[i] = element.getVersionId();
		}
		return versions;
	}

}
