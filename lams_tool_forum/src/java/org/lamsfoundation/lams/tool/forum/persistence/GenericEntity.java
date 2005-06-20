package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Date;

/**
 * GenericEntity
 * @author conradb
 *
 *
 * @hibernate.class table="GENERICENTITY"
 *
 * @hibernate.query name="all" query="from GenericEntity genericEntity"
 */
public class GenericEntity {
	protected Long id;
	protected Date created;
	protected Date updated;
  	protected Long createdBy;
    protected Long modifiedBy;

	public GenericEntity() {

	}

	/**
	 * Gets the GenericEntity's id
	 *
	 * @return the id
	 * @hibernate.id column="ID" generator-class="native"
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the project id
	 *
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns the object's creation date
	 *
	 * @return date
	 * @hibernate.property column="CREATED"
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
	 * @hibernate.property column="UPDATED"
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
     * @return Returns the userid of the user who created the Forum.
     *
     * @hibernate.property
     * 		column="CREATEDBY"
     *
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy The userid of the user who created this Forum.
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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

    /**
     * @return Returns the userid of the user who modified the posting.
     *
     *
     * @hibernate.property
     * 		column="MODIFIEDBY"
     *
     */
    public Long getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy The userid of the user who modified the posting.
     */
    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GenericEntity))
			return false;

		final GenericEntity genericEntity = (GenericEntity) o;

      	if (this.id != null ? !this.id.equals(genericEntity.id)
				: genericEntity.id != null)
			return false;

      	if (this.createdBy != null ? !this.createdBy.equals(genericEntity.createdBy)
				: genericEntity.createdBy != null)
			return false;

      	if (this.modifiedBy != null ? !this.modifiedBy.equals(genericEntity.modifiedBy)
				: genericEntity.modifiedBy != null)
			return false;

		if (this.created != null ? !new Date(this.created.getTime()/1000).equals(new Date(genericEntity.created.getTime()/1000))
				: genericEntity.created != null)
			return false;

		if (this.updated != null ? !new Date(this.updated.getTime()/1000).equals(new Date(genericEntity.updated.getTime()/1000))
				: genericEntity.updated != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
        result = 29 * result + (this.createdBy != null ? this.createdBy.hashCode() : 0);
        result = 29 * result + (this.modifiedBy != null ? this.modifiedBy.hashCode() : 0);
        result = 29 * result + (this.created != null ? new Date(this.created.getTime()/1000).hashCode() : 0);
		result = 29 * result + (this.updated != null ? new Date(this.updated.getTime()/1000).hashCode() : 0);
		return result;
	}

}
