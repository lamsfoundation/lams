package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * 
 * @hibernate.class table="lams_grouping"
 * 
 * @author Jacky Fang
 */
public abstract class Grouping implements Serializable
{
    
    /** Grouping type id of random grouping */
    public static final Integer RANDOM_GROUPING_TYPE = new Integer(1);
    
    /** Grouping type id of chosen grouping */
    public static final Integer CHOSEN_GROUPING_TYPE = new Integer(2);
    
    /** Grouping type id for lesson class grouping */
    public static final Integer CLASS_GROUPING_TYPE = new Integer(3);
    
    /** identifier field */
    private Long groupingId;
    
    /** nullable persistent field 
     *  TODO It make sense only if we want to setup some limits for the number 
     * 		 of groups the author can setup in the authoring GUI. It might need 
     * 		 to be deleted if the end user doesn't like this limits.
     */
    private Integer maxNumberOfGroups;
    
    /** nullable persistent field */
    private Integer groupingUIID;
    
   
    /** persistent field */
    private Set groups;
    
    /** persistent field */
    private Set activities;
    
    /** non-persistent field */
    protected Set learners;

    protected Grouper grouper;
    /**
     * static final variables indicating the grouping_support of activities
     *******************************************************************/
    public static final int GROUPING_SUPPORT_NONE = 1;
    
    public static final int GROUPING_SUPPORT_OPTIONAL = 2;

    public static final int GROUPING_SUPPORT_REQUIRED = 3;
    /******************************************************************/
    
    /** full constructor */
    public Grouping(Long groupingId, Set groups, Set activities,Grouper grouper)
    {
        this.groupingId = groupingId;
        this.groups = groups;
        this.activities = activities;
        this.grouper = grouper;
    }
    
    /** default constructor */
    public Grouping()
    {
    }
    
    /** minimal constructor */
    public Grouping(Long groupingId)
    {
        this.groupingId = groupingId;
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
        if(this instanceof LessonClass)
            return CLASS_GROUPING_TYPE;
        else if(this instanceof ChosenGrouping)
            return CHOSEN_GROUPING_TYPE;
        else
            return RANDOM_GROUPING_TYPE;
    }

    /**
     *            
     *
     */
    public Set getGroups()
    {
        if(this.groups==null)
            setGroups(new TreeSet());
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
        if(this.activities==null)
            setActivities(new TreeSet(new ActivityOrderComparator()));
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
	public Integer getGroupingUIID() {
		return groupingUIID;
	}
	public void setGroupingUIID(Integer groupingUIID) {
		this.groupingUIID = groupingUIID;
	}
    //---------------------------------------------------------------------
    // Service methods
    //---------------------------------------------------------------------
    /**
     * Return the next group order id.
     * @return the next order id.
     */
    public synchronized int getNextGroupOrderId()
    {
        int order =0;
        if(this.groups!=null)
        {
            order = groups.size();
            return ++order;
        }
        else return ++order;
    }
    
    /**
     * Return all the learners who participate this grouping.
     * @return the learners set.
     */
    public Set getLearners()
    {
        
        learners = new HashSet();
        for(Iterator i = getGroups().iterator();i.hasNext();)
        {
            Group group = (Group)i.next();
            if(isLearnerGroup(group))
                learners.addAll(group.getUsers());
        }
        return learners;
    }

    /**
     * Returns the group that current learner is in.
     * @param learner the user in the group
     * @return the group that has the learner
     */
    public Group getGroupBy(User learner)
    {
        for(Iterator i = getGroups().iterator();i.hasNext();)
        {
            Group group = (Group)i.next();
            if(isLearnerGroup(group)&&group.hasLearner(learner))
                return group;
        }
        return new NullGroup();
    }
    
    /**
     * Iterate through all the groups in this grouping and figure out the group
     * with the least members in it. 
     * @return the group with the least member.
     */
    public Group getGroupWithLeastMember()
    {
        List groups = new ArrayList(this.getGroups());
        
		Group minGroup = (Group) groups.get(0);
		
		for (int i = 1; i < groups.size(); i++) 
		{
			Group tempGroup = (Group) groups.get(i);
			if (tempGroup.getUsers().size() < minGroup.getUsers().size()) 
				minGroup = tempGroup;
		}
		return minGroup;
    }
    
    /**
     * Is this group a learner group. It is also possible that the group is a
     * staff group.
     * @return	whether the group is learner group or not.
     */
    public abstract boolean isLearnerGroup(Group group);
	
    /**
     * Create or update groups for this grouping with a list of new learners.
     * @param learners the list of learners need to be grouped.
     */
	public void doGrouping(List learners)
	{
	    this.grouper.doGrouping(this,learners);
	}
	
	/**
	 * Create or updates groups for this grouping with a single new learner.
	 * @param learner the new learner needs to be grouped.
	 */
	public void doGrouping(User learner)
	{
	    this.grouper.doGrouping(this,learner);
	}
	
	/**
	 * Return whether a learner is a existing learner for this grouping or not.
	 * @param learner the current leaner
	 * @return the boolean result
	 */
	public boolean doesLearnerExist(User learner)
	{
	    return !getGroupBy(learner).isNull();
	}
}
