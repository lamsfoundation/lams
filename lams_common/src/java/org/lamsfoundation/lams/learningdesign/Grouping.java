package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 *        @hibernate.class
 *         table="lams_grouping"
 *
 */
public abstract class Grouping implements Serializable
{
    /** Grouping type id of random grouping */
    public static final int RANDOM_GROUPING_TYPE = 1;
    
    /** Grouping type id of chosen grouping */
    public static final int CHOSEN_GROUPING_TYPE = 2;
    
    /** Grouping type id for lesson class grouping */
    public static final int CLASS_GROUPING_TYPE = 3;
    
    /** identifier field */
    private Long groupingId;
    
    
    
    /** nullable persistent field */
    private Integer maxNumberOfGroups;
    
    /** persistent field */
    private Integer groupingTypeId;
    
    /** persistent field */
    private Set groups;
    
    /** persistent field */
    private Set activities;
    
    /** full constructor */
    public Grouping(Long groupingId, Integer groupingTypeId, Set groups, Set activities)
    {
        this.groupingId = groupingId;
        this.groupingTypeId = groupingTypeId;
        this.groups = groups;
        this.activities = activities;
    }
    
    /** default constructor */
    public Grouping()
    {
    }
    
    /** minimal constructor */
    public Grouping(Long groupingId, Integer groupingTypeId)
    {
        this.groupingId = groupingId;
        this.groupingTypeId = groupingTypeId;
    }
    
    /**
     *            
     *
     */
    public Long getGroupingId()
    {
        return this.groupingId;
    }
    
    public void setGroupingId(Long groupingId)
    {
        this.groupingId = groupingId;
    }
    
    
    
    /**
     *            
     *
     */
    public Integer getGroupingTypeId()
    {
        return this.groupingTypeId;
    }
    
    public void setGroupingTypeId(Integer groupingTypeId)
    {
        this.groupingTypeId = groupingTypeId;
    }
    
    /**
     *            
     *
     */
    public Set getGroups()
    {
        return this.groups;
    }
    
    public void setGroups(Set groups)
    {
        this.groups = groups;
    }
    
    /**
     *           
     *
     */
    public Set getActivities()
    {
        return this.activities;
    }
    
    public void setActivities(Set activities)
    {
        this.activities = activities;
    }
    
    public String toString()
    {
        return new ToStringBuilder(this)
        .append("groupingId", getGroupingId())
        .toString();
    }
    
    public boolean equals(Object other)
    {
        if ( (this == other ) ) return true;
        if ( !(other instanceof Grouping) ) return false;
        Grouping castOther = (Grouping) other;
        return new EqualsBuilder()
        .append(this.getGroupingId(), castOther.getGroupingId())
        .isEquals();
    }
    
    public int hashCode()
    {
        return new HashCodeBuilder()
        .append(getGroupingId())
        .toHashCode();
    }
    /**
     * 
     */
    public Integer getMaxNumberOfGroups()
    {
        return maxNumberOfGroups;
    }
    /**
     * @param maxNumberOfGroups The maxNumberOfGroups to set.
     */
    public void setMaxNumberOfGroups(Integer maxNumberOfGroups)
    {
        this.maxNumberOfGroups = maxNumberOfGroups;
    }
}
