package org.lamsfoundation.lams.contentrepository;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_cr_node_version"
 *     
 * 		  @hibernate.cache usage = "transactional"
*/
public class CrNodeVersion implements Serializable {

    /** identifier field */
    private Long nvId;

    /** persistent field. Should never be null, but make it a Long
     * (rather than long), so that we don't have to keep converting
     * it to Long to match UUID. */
    private Long versionId;

    /** nullable persistent field */
    private Date createdDateTime;

    /** persistent field */
    private org.lamsfoundation.lams.contentrepository.CrNode node;

    /* child Nodes. persistent field 
    private Set childNodes;
    */

    /** persistent field */
    private Set crNodeVersionProperties;

    /** full constructor */
    public CrNodeVersion(Long versionId, Date createdDateTime, org.lamsfoundation.lams.contentrepository.CrNode node, Set crNodeVersionProperties) {
        this.versionId = versionId;
        this.createdDateTime = createdDateTime;
        this.node = node;
        this.crNodeVersionProperties = crNodeVersionProperties;
        //this.childNodes = childNodes;
    }

    /** default constructor */
    public CrNodeVersion() {
    }

    /** minimal constructor */
    public CrNodeVersion(Long versionId, org.lamsfoundation.lams.contentrepository.CrNode node,Set crNodeVersionProperties) {
        this.versionId = versionId;
        this.node = node;
        this.crNodeVersionProperties = crNodeVersionProperties;
        //this.childNodes = childNodes;
    }

    /** 
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="nv_id"
     *             unsaved-value="0"
     *         
     */
    public Long getNvId() {
        return this.nvId;
    }

    public void setNvId(Long nvId) {
        this.nvId = nvId;
    }

    /** 
     *            @hibernate.property
     *             column="version_id"
     *             type="java.lang.Long"
     *             length="20"
     *             not-null="true"
     *         
     */
    public Long getVersionId() {
        return this.versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    /** 
     *            @hibernate.property
     *             column="created_date_time"
     *             type="java.sql.Timestamp"
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
     * bi-directional many-to-one association to CrNode
     * 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="node_id"         
     *         
     */
    public org.lamsfoundation.lams.contentrepository.CrNode getNode() {
        return this.node;
    }

    public void setNode(org.lamsfoundation.lams.contentrepository.CrNode node) {
        this.node = node;
    }

    /** 
     * bi-directional one-to-many association to CrNodeVersionProperty
     * 
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="all-delete-orphan"
     *            @hibernate.collection-key
     *             column="nv_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.contentrepository.CrNodeVersionProperty"
     *         	  @hibernate.collection-cache	
     * 			   usage = "transactional"
     */
    public Set getCrNodeVersionProperties() {
        return this.crNodeVersionProperties;
    }

    public void setCrNodeVersionProperties(Set crNodeVersionProperties) {
        this.crNodeVersionProperties = crNodeVersionProperties;
    }

    /* 
     * Gets the set of child nodes. Do not use this method if you can use
     * addChildNode(), removeChildNode() or 
     * getChildNode() instead.
     * 
     * This is lazy loaded, so will need special care when you need these
     * details.
     * 
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="all-delete-orphan"
     *            @hibernate.collection-key
     *             column="parent_nv_id "
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.contentrepository.CrNodeVersion"
     *         
    public Set getChildNodes() {
        return this.childNodes;
    }
     */

    /*
    * Assigns a new set of child nodes. Do not use this method if 
    * you can use addChildNode(), removeChildNode() or 
    * getChildNode() instead.
    public void setChildNodes(Set childNodes) {
        this.childNodes = childNodes;
    }
    */

    /** Add a child node to the child collection. 
     * Use this method rather than calling getChildNodes()
     * and adding to the set. See also removeChildNode() 
     * and getChildNode();
     * 
     * @param childNode
    public void addChildNode(CrNode childNode) {
    	Set set = getChildNodes();
    	
    	if ( set == null ) {
    		set = new HashSet();
    		setChildNodes(set);
    	}
    	
    	getChildNodes().add(childNode);
    }
     */

    /** Remove a child node to the childNodes collection. 
     * Use this method rather than calling getChildNodes()
     * and iterating through the set. See also addChildNode() 
     * and getChildNode();
     * 
     * Uses equals method defined in this class.
     * 
     * Not tested!!!!
     * 
     * @param childNode
    public void removeChildNode(CrNode childNode) {
    	Set set = getChildNodes();
    	
    	if ( set != null ) {
    		Iterator iter = set.iterator();
    		boolean removed = false;
    		while ( iter.hasNext() && ! removed ) {
    			CrNode element = (CrNode) iter.next();
				if ( element.equals(childNode) ) {
					iter.remove();
				}
			}
    	}
    }
     */

    /** Get a child node from the childNodes collection. 
     * Use this method rather than calling getChildNodes()
     * and iterating through the set. See also addChildNode() 
     * and removeChildNode();
     * 
     * @param relPath of child
    public CrNode getChildNode(String relPath) {
    	 
    	if ( getChildNodes() == null )
    		return null;
    
    	CrNode childNode = null;
   		Iterator iter = getChildNodes().iterator();
		while (iter.hasNext() && childNode == null) {
			CrNode element = (CrNode) iter.next();
	    	String path = element.getPath();
	    	if ( ( relPath == null && path == null ) || 
				 ( relPath != null && relPath.equals(path)) ) {
	    		childNode = element;
			}
		}
		return childNode;
    }
     */

    public String toString() {
        return new ToStringBuilder(this)
            .append("nvId", getNvId())
            .append("versionId", getVersionId())
            .append("createdDateTime", getCreatedDateTime())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof CrNodeVersion) ) return false;
        CrNodeVersion castOther = (CrNodeVersion) other;
        return new EqualsBuilder()
            .append(this.getNvId(), castOther.getNvId())
            .append(this.getVersionId(), castOther.getVersionId())
            .append(this.getCreatedDateTime(), castOther.getCreatedDateTime())
            .append(this.getNode(), castOther.getNode())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getNvId())
            .append(getVersionId())
            .append(getCreatedDateTime())
            .append(getNode())
            .toHashCode();
    }
    
    /* ** Added code - not generated by hbm ****/
    /** Get a property value */
    public CrNodeVersionProperty getProperty(String name) {
    	
    	if ( name == null ) { 
			return null;
    	}
			
    	Set properties = this.getCrNodeVersionProperties();
    	if ( properties != null ) {
    		Iterator iter = properties.iterator();
    		while (iter.hasNext()) {
    			CrNodeVersionProperty element = (CrNodeVersionProperty) iter.next();
    			if ( name.equals(element.getName()) ) {
    				return element;
    			}
    		}
    	}
    	return null;
    }

    /** Remove a property.
     * 
     * Removes it from the collection, and as the collection
     * is cascade="all-delete-orphan", the property should be 
     * removed from the db automatically.
     */
    private void removeProperty(String name) {
    	
    	if ( name != null ) { 
	    	Set properties = this.getCrNodeVersionProperties();
	    	CrNodeVersionProperty prop;
	    	if ( properties != null ) {
	    		Iterator iter = properties.iterator();
	    		while (iter.hasNext()) {
	    			CrNodeVersionProperty element = (CrNodeVersionProperty) iter.next();
	    			if ( name.equals(element.getName()) ) {
	    				iter.remove();
	    				break;
	    			}
	    		}
	    	}
    	}
    }

	private CrNodeVersionProperty createProperty(String name, Object value, int valueType) {
		CrNodeVersionProperty property = new CrNodeVersionProperty();
		property.setCrNodeVersion(this);
		property.setName(name);
		property.setValue(value.toString());
		property.setType(valueType);
		return property;
	}

    /** Set a property value. Removes the property if the value is null
     * @throws RepositoryRuntimeException if name is null
     * @return CrNodeVersionProperty for inserted/updated value, null if removing value */
    public CrNodeVersionProperty setProperty(String name, Object value, int valueType) 
    	throws RepositoryRuntimeException{
    
    	if ( name == null )
    		throw new RepositoryRuntimeException("A name must be supplied - a property cannot have a null name.");

    	// if value is null then remove the property
    	if ( value == null ) {
    		removeProperty(name);
    		return null;
    	} 
    	
    	// otherwise update/insert the property.
		Set properties = getCrNodeVersionProperties();
    	CrNodeVersionProperty prop = null;
		if ( properties != null ) {
			Iterator iter = properties.iterator();
			while (iter.hasNext() && prop == null) {
				CrNodeVersionProperty element = (CrNodeVersionProperty) iter.next();
				if ( name.equals(element.getName()) ) {
					prop = element;
				}
			}
			if ( prop != null ) {
				prop.setValue(value.toString());
				prop.setType(valueType);
			} else {
				prop = createProperty(name, value, valueType);
				properties.add(prop);
			}
		} else {
			properties = new HashSet();
			prop = createProperty(name, value, valueType);
			properties.add(prop);
			setCrNodeVersionProperties(properties);
		}
		return prop; 
	}
    
    /** Get the version description. Stored as a property so
     * could be accessed via getProperty() */
    public String getVersionDescription() {
    	CrNodeVersionProperty prop =  getProperty(PropertyName.VERSIONDESC);
    	return prop != null ? prop.getString() : null;
    }
    
    /** Set the version description. Stored as a property so
     * could be accessed via setProperty */
    public void setVersionDescription(String description) {
    	setProperty(PropertyName.VERSIONDESC,description, PropertyType.STRING);
    }
    
    /**
     * Indicates whether a property exists for this name
     * Returns true if a property exists and false otherwise.
     *
     * @param name The name of a (possible) property.
     * @return true if a property exists at relPath;
     * false otherwise.
     */
    public boolean hasProperty(String name) {
    	return getProperty(name) != null;
    }

     /**
     * Indicates whether this node has properties.
     * Returns true if this node has one or more properties;
     * false otherwise.
     *
     * @return true if this node has one or more properties;
     * false otherwise.
     */
    public boolean hasProperties() {
    	Set properties = getCrNodeVersionProperties();
    	return properties != null && properties.size() > 0 ;
    }

    
}
