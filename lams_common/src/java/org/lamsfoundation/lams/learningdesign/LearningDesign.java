package org.lamsfoundation.lams.learningdesign;

import org.lamsfoundation.lams.usermanagement.User;
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
 *         table="lams_learning_design"
 *     
*/
public class LearningDesign implements Serializable {

	/** Represents a single LearningDesign object in the WDDXPacket */
	public static final String DESIGN_OBJECT ="LearningDesign";
	
	/** Represents a list of LearningDesign objects in the WDDXPacket */
	public static final String DESIGN_LIST_OBJECT="LearningDesignList";
	
	   /** identifier field */
    private Long learningDesignId;

    /** nullable persistent field */
    private Integer id;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private Activity firstActivity;

    /** nullable persistent field */
    private Integer maxId;

    /** persistent field */
    private Boolean validDesign;

    /** persistent field */
    private Boolean readOnly;

    /** nullable persistent field */
    private Date dateReadOnly;

    /** nullable persistent field */
    private String helpText;

    /** persistent field */
    private Boolean lessonCopy;

    /** persistent field */
    private Date createDateTime;

    /** persistent field */
    private String version;

    /** nullable persistent field */
    private Date openDateTime;

    /** nullable persistent field */
    private Date closeDateTime;

    /** persistent field */
    private User user;

    /** persistent field */
    private LearningDesign parentLearningDesign;

    /** persistent field */
    private Set childLearningDesigns;

    /** persistent field */
    private Set lessons;

    /** persistent field */
    private Set transitions;

    /** persistent field */
    private Set activities;
    
    /** non-persistent field containing a list
     * of optional activities in the design*/
    private Set optionalActivities;
    
    private Set paralleActivities;

    private Set sequenceActivities;
    
    /** full constructor */
    public LearningDesign(Long learningDesignId, Integer id, String description, String title, Activity firstActivity, Integer maxId, Boolean validDesign, Boolean readOnly, Date dateReadOnly,String helpText, Boolean lessonCopy, Date createDateTime, String version, Date openDateTime, Date closeDateTime, User user, org.lamsfoundation.lams.learningdesign.LearningDesign parentLearningDesign, Set childLearningDesigns, Set lessons, Set transitions, Set activities) {
        this.learningDesignId = learningDesignId;
        this.id = id;
        this.description = description;
        this.title = title;
        this.firstActivity = firstActivity;
        this.maxId = maxId;
        this.validDesign = validDesign;
        this.readOnly = readOnly;
        this.dateReadOnly = dateReadOnly;
        this.helpText = helpText;
        this.lessonCopy = lessonCopy;
        this.createDateTime = createDateTime;
        this.version = version;
        this.openDateTime = openDateTime;
        this.closeDateTime = closeDateTime;
        this.user = user;
        this.parentLearningDesign = parentLearningDesign;
        this.childLearningDesigns = childLearningDesigns;
        this.lessons = lessons;
        this.transitions = transitions;
        this.activities = activities;
    }

    /** default constructor */
    public LearningDesign() {
    }

    /** minimal constructor */
    public LearningDesign(Long learningDesignId, Boolean validDesign, Boolean readOnly, Boolean lessonCopy, Date createDateTime, String version, User user, org.lamsfoundation.lams.learningdesign.LearningDesign parentLearningDesign, Set childLearningDesigns, Set lessons, Set transitions, Set activities) {
        this.learningDesignId = learningDesignId;
        this.validDesign = validDesign;
        this.readOnly = readOnly;
        this.lessonCopy = lessonCopy;
        this.createDateTime = createDateTime;
        this.version = version;
        this.user = user;
        this.parentLearningDesign = parentLearningDesign;
        this.childLearningDesigns = childLearningDesigns;
        this.lessons = lessons;
        this.transitions = transitions;
        this.activities = activities;
    }

    /** 
     *         
     *         
     */
    public Long getLearningDesignId() {
        return this.learningDesignId;
    }

    public void setLearningDesignId(Long learningDesignId) {
        this.learningDesignId = learningDesignId;
    }

    /** 
     *           
     *         
     */
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /** 
     *           
     *         
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     *           
     *         
     */
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /** 
     *          
     *         
     */
    public Activity getFirstActivity() {
        return this.firstActivity;
    }

    public void setFirstActivity(Activity firstActivity) {
        this.firstActivity = firstActivity;
    }

    /** 
     *            
     *         
     */
    public Integer getMaxId() {
        return this.maxId;
    }

    public void setMaxId(Integer maxId) {
        this.maxId = maxId;
    }

    /** 
     *           
     *         
     */
    public Boolean getValidDesign() {
        return this.validDesign;
    }

    public void setValidDesign(Boolean validDesign) {
        this.validDesign = validDesign;
    }

    /** 
     *            
     *         
     */
    public Boolean getReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /** 
     *         
     */
    public Date getDateReadOnly() {
        return this.dateReadOnly;
    }

    public void setDateReadOnly(Date dateReadOnly) {
        this.dateReadOnly = dateReadOnly;
    }
    /** 
     *           
     */
    public String getHelpText() {
        return this.helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    /** 
     *            
     *         
     */
    public Boolean getLessonCopy() {
        return this.lessonCopy;
    }

    public void setLessonCopy(Boolean lessonCopy) {
        this.lessonCopy = lessonCopy;
    }

    /** 
     *           
     *         
     */
    public Date getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    /** 
     *            
     *         
     */
    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /** 
     *            
     *         
     */
    public Date getOpenDateTime() {
        return this.openDateTime;
    }

    public void setOpenDateTime(Date openDateTime) {
        this.openDateTime = openDateTime;
    }

    /** 
     *           
     */
    public Date getCloseDateTime() {
        return this.closeDateTime;
    }

    public void setCloseDateTime(Date closeDateTime) {
        this.closeDateTime = closeDateTime;
    }

    /** 
     *                  
     *         
     */
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /** 
     *                 
     *         
     */
    public org.lamsfoundation.lams.learningdesign.LearningDesign getParentLearningDesign() {
        return this.parentLearningDesign;
    }

    public void setParentLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign parentLearningDesign) {
        this.parentLearningDesign = parentLearningDesign;
    }

    /** 
     *            
     *         
     */
    public Set getChildLearningDesigns() {
        return this.childLearningDesigns;
    }

    public void setChildLearningDesigns(Set childLearningDesigns) {
        this.childLearningDesigns = childLearningDesigns;
    }

    /** 
     *            
     *         
     */
    public Set getLessons() {
        return this.lessons;
    }

    public void setLessons(Set lessons) {
        this.lessons = lessons;
    }

    /** 
     *            
     *         
     */
    public Set getTransitions() {
        return this.transitions;
    }

    public void setTransitions(Set transitions) {
        this.transitions = transitions;
    }

    /** 
     *           
     *         
     */
    public Set getActivities() {
        return this.activities;
    }

    public void setActivities(Set activities) {
        this.activities = activities;
       /* HashSet designActivities =(HashSet)this.activities;
		Iterator iter = designActivities.iterator();
		Activity activity = null;
		while(iter.hasNext()){
			activity =(Activity) iter.next();
			Integer activityTypeID = activity.getActivityTypeId();
			populateDesignActivitySets(activity,activityTypeID);
		}*/
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("learningDesignId", getLearningDesignId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof LearningDesign) ) return false;
        LearningDesign castOther = (LearningDesign) other;
        return new EqualsBuilder()
            .append(this.getReadOnly(), castOther.getReadOnly())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReadOnly())
            .toHashCode();
    }
    /**
	 * @return Returns the optionalActivities.
	 */
	public Set getOptionalActivities() {
		return optionalActivities;
	}
	/**
	 * @param optionalActivities The optionalActivities to set.
	 */
	public void setOptionalActivities(Set optionalActivities) {
		this.optionalActivities = optionalActivities;
	}    
    /**
	 * Calculates the "first activity" of this learning design.
	 * The first activity is the activity which is not a part of 
	 * a Optional Activity and doesn't appears in any of the "to_activity_id"
	 * fields in the lams_learning_transition table.
	 * <p> Note : Returns the activity_id and not the id </p>
	 * 
	 * @return Returns the firstActivityId in the design
	 */
	public Long calculateFirstActivityID(){
		Long firstID = null;
		HashSet allActivities = new HashSet();
		
		if(this.getActivities()!=null)
			allActivities.addAll(this.getActivities());
		if(this.getOptionalActivities()!=null)
			allActivities.addAll(this.getOptionalActivities());
		
		Set nonFirstActivities = getAllSubsequentActivityIds();
		Set activitiesFromOptionalActivities = getAllActivityIdsFromOptionalActivities();
		if(activitiesFromOptionalActivities!=null)
			nonFirstActivities.addAll(activitiesFromOptionalActivities);
		
		Iterator iter = allActivities.iterator();
		while(iter.hasNext()&& firstID==null){
			Activity activity = (Activity)iter.next();
			Long activityID = activity.getActivityId();
			if(!nonFirstActivities.contains(activityID))
				firstID = activityID;
			
		}
		return firstID;
	}
	
	/**
	 * @return Returns a list of all the activity_id 's following another
	 * activity, within this learning design. 
	 */
	private Set getAllSubsequentActivityIds(){
		Set transitions = this.getTransitions();
		Iterator iter = transitions.iterator();
		HashSet set = new HashSet();
		while(iter.hasNext()){
			Transition trans = (Transition)iter.next();
			Activity toActivity = trans.getActivityByToActivityId();
			if(toActivity!=null)			
				set.add(toActivity.getActivityId());
		}
		return set;
	}	
	
	/**
	 * @return Returns a list of all activity_id 's of the activities 
	 * which are in the Optional Activities set in the Learning Design
	 * 
	 */
	private Set getAllActivityIdsFromOptionalActivities(){
		Set optionalActivities = this.getOptionalActivities();
		HashSet activityIds =null;
		if(optionalActivities!=null){
			activityIds = new HashSet();
			Iterator iter = optionalActivities.iterator();
			while(iter.hasNext()){
				OptionsActivity optActivity =(OptionsActivity)iter.next();
				Set activities = optActivity.getActivities();
				if(activities!=null){
					Iterator optIterator = activities.iterator();
					while(optIterator.hasNext()){
						Activity act = (Activity)optIterator.next();
						activityIds.add(act.getActivityId());
					}
			}
		}
			
		}
		return activityIds;
	}
	private void populateDesignActivitySets(Activity activity, Integer activityTypeID){
		int typeID = activityTypeID.intValue();
		if(typeID==7){
			if(paralleActivities==null)
				paralleActivities = new HashSet();
			paralleActivities.add(activity);
		}else if(typeID==8){
			if(optionalActivities==null)
				optionalActivities = new HashSet();
			optionalActivities.add(activity);
		}else if(typeID==9){
			if(sequenceActivities==null)
				sequenceActivities = new HashSet();
			sequenceActivities.add(activity);
		}
		
	}

}
