package org.lamsfoundation.lams.learningdesign;

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @hibernate.class table="lams_learning_design"
 *  
 */
public class LearningDesign implements Serializable {

	/** Represents a single LearningDesign object in the WDDXPacket */
	public static final String DESIGN_OBJECT = "LearningDesign";

	/** Represents a list of LearningDesign objects in the WDDXPacket */
	public static final String DESIGN_LIST_OBJECT = "LearningDesignList";
	
	/** Represents a copy of LearningDesign for authoring enviornment */
	public static final int COPY_TYPE_NONE =1;
	
	/** Represents a copy of LearningDesign for monitoring enviornment */
	public static final int COPY_TYPE_LESSON=2;
	
	/** Represents a copy of LearningDesign for preview purposes */
	public static final int COPY_TYPE_PREVIEW=3;

	/** identifier field */
	private Long learningDesignId;

	/** nullable persistent field */
	private Integer learningDesignUIID;

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
	private Integer copyTypeID;

	/** persistent field */
	private Date createDateTime;

	/** persistent field */
	private String version;

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

	/** persistent field */
	private WorkspaceFolder workspaceFolder;
	
	/** persistent field */
	private Long duration;
	
	/** nullable persistent field */
	private String licenseText;
	
	/** nullable persistent field */
	private License license;
	
	/** nullable persistent field*/
	private Long lessonOrgID;
	
	/** nullable persistent field*/
	private String lessonOrgName;
	
	/** nullable persistent field*/
	private Long lessonID;
	
	/** nullable persistent field*/
	private String lessonName;
	
	/** nullable persistent field*/
	private Date lessonStartDateTime;
	
	/** persistent field*/
	private Date lastModifiedDateTime;
	
	/** full constructor */
	public LearningDesign(
			Long learningDesignId,
			Integer ui_id,
			String description,
			String title,
			Activity firstActivity,
			Integer maxId,
			Boolean validDesign,
			Boolean readOnly,
			Date dateReadOnly,
			String helpText,
			Integer copyTypeID,
			Date createDateTime,
			String version,
			User user,
			LearningDesign parentLearningDesign,
			Set childLearningDesigns, Set lessons, Set transitions,
			Set activities,
			Long duration,
			String licenseText,
			License license) {
		this.learningDesignId = learningDesignId;
		this.learningDesignUIID = ui_id;
		this.description = description;
		this.title = title;
		this.firstActivity = firstActivity;
		this.maxId = maxId;
		this.validDesign = validDesign;
		this.readOnly = readOnly;
		this.dateReadOnly = dateReadOnly;
		this.helpText = helpText;
		this.copyTypeID = copyTypeID;
		this.createDateTime = createDateTime;
		this.version = version;
		this.user = user;
		this.parentLearningDesign = parentLearningDesign;
		this.childLearningDesigns = childLearningDesigns;
		this.lessons = lessons;
		this.transitions = transitions;
		this.activities = activities;
		this.duration = duration;
		this.licenseText = licenseText;
		this.license = license;
	}

	/** default constructor */
	public LearningDesign() {
	}

	/** minimal constructor */
	public LearningDesign(
			Long learningDesignId,
			Boolean validDesign,
			Boolean readOnly,
			Integer copyTypeID,
			Date createDateTime,
			String version,
			User user,
			org.lamsfoundation.lams.learningdesign.LearningDesign parentLearningDesign,
			Set childLearningDesigns, Set lessons, Set transitions,
			Set activities) {
		this.learningDesignId = learningDesignId;
		this.validDesign = validDesign;
		this.readOnly = readOnly;
		this.copyTypeID = copyTypeID;
		this.createDateTime = createDateTime;
		this.version = version;
		this.user = user;
		this.parentLearningDesign = parentLearningDesign;
		this.childLearningDesigns = childLearningDesigns;
		this.lessons = lessons;
		this.transitions = transitions;
		this.activities = activities;
	}
	public static LearningDesign createLearningDesignCopy(LearningDesign design, Integer designCopyType){
		LearningDesign newDesign = newInstance(design);
				
		if(designCopyType.intValue()!=COPY_TYPE_NONE)
			newDesign.setReadOnly(new Boolean(true));
		else
			newDesign.setReadOnly(new Boolean(false));
		
		newDesign.setCopyTypeID(designCopyType);		
		return newDesign;
	}	
	private static LearningDesign newInstance(LearningDesign design) {		
		LearningDesign newDesign = new LearningDesign();		
		newDesign.setDescription(design.getDescription());
		newDesign.setTitle(design.getTitle());		
		newDesign.setMaxId(design.getMaxId());
		newDesign.setValidDesign(design.getValidDesign());		
		newDesign.setDateReadOnly(design.getDateReadOnly());
		newDesign.setHelpText(design.getHelpText());
		newDesign.setVersion(design.getVersion());
		newDesign.setParentLearningDesign(design);
		newDesign.setCreateDateTime(new Date());
		newDesign.setDuration(design.getDuration());
		newDesign.setLicense(design.getLicense());
		newDesign.setLicenseText(design.getLicenseText());
		return newDesign;
	}	
	public Long getLearningDesignId() {
		return this.learningDesignId;
	}

	public void setLearningDesignId(Long learningDesignId) {
		this.learningDesignId = learningDesignId;
	}
	public Integer getLearningDesignUIID() {
		return this.learningDesignUIID!=null?this.learningDesignUIID : WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;		
	}
	public void setLearningDesignUIID(Integer id) {
		this.learningDesignUIID = id;
	}
	public String getDescription() {
		return this.description!=null?this.description:WDDXTAGS.STRING_NULL_VALUE;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return this.title!=null?this.title:WDDXTAGS.STRING_NULL_VALUE;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Activity getFirstActivity() {
		return this.firstActivity;
	}
	public void setFirstActivity(Activity firstActivity) {
		this.firstActivity = firstActivity;
	}
	public Integer getMaxId() {
		return maxId!=null?maxId:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	public void setMaxId(Integer maxId) {
		this.maxId = maxId;
	}
	public Boolean getValidDesign() {
		return validDesign!=null?validDesign:WDDXTAGS.BOOLEAN_NULL_VALUE;
	}
	public void setValidDesign(Boolean validDesign) {
		this.validDesign = validDesign;
	}
	public Boolean getReadOnly() {
		return readOnly!=null?readOnly:WDDXTAGS.BOOLEAN_NULL_VALUE;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	public Date getDateReadOnly() {
		return dateReadOnly!=null?dateReadOnly:WDDXTAGS.DATE_NULL_VALUE;
	}
	public void setDateReadOnly(Date dateReadOnly) {
		this.dateReadOnly = dateReadOnly;
	}
	public String getHelpText() {
		return helpText!=null?helpText:WDDXTAGS.STRING_NULL_VALUE;
	}
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
	public Date getCreateDateTime() {
		return createDateTime!=null?createDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}
	public String getVersion() {
		return version!=null?version:WDDXTAGS.STRING_NULL_VALUE;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public User getUser() {
		return this.user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public org.lamsfoundation.lams.learningdesign.LearningDesign getParentLearningDesign() {
		return this.parentLearningDesign;
	}
	public void setParentLearningDesign(
			org.lamsfoundation.lams.learningdesign.LearningDesign parentLearningDesign) {
		this.parentLearningDesign = parentLearningDesign;
	}
	public Set getChildLearningDesigns() {
		return this.childLearningDesigns;
	}
	public void setChildLearningDesigns(Set childLearningDesigns) {
		this.childLearningDesigns = childLearningDesigns;
	}
	public Set getLessons() {
		return this.lessons;
	}
	public void setLessons(Set lessons) {
		this.lessons = lessons;
	}
	public Set getTransitions() {
		return this.transitions;
	}
	public void setTransitions(Set transitions) {
		this.transitions = transitions;
	}
	public Set getActivities() {
		return this.activities;
	}
	public void setActivities(Set activities) {
		this.activities = activities;
	}
	public String toString() {
		return new ToStringBuilder(this).append("learningDesignId",
				getLearningDesignId()).toString();
	}
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LearningDesign))
			return false;
		LearningDesign castOther = (LearningDesign) other;
		return new EqualsBuilder().append(this.getReadOnly(),
				castOther.getReadOnly()).isEquals();
	}
	public int hashCode() {
		return new HashCodeBuilder().append(getReadOnly()).toHashCode();
	}	
	public HashSet getParentActivities(){
		HashSet parentActivities = new HashSet();
		Iterator iterator = this.getActivities().iterator();
		while(iterator.hasNext()){
			Activity activity = (Activity)iterator.next();
			if(activity.getParentActivity()==null)				
				parentActivities.add(activity);
		}
		return parentActivities;
	}	
	public Activity calculateFirstActivity(){
		Activity firstActivity = null;
		HashSet parentActivities = this.getParentActivities();
		Iterator parentIterator = parentActivities.iterator();
		while(parentIterator.hasNext()){
			Activity activity = (Activity)parentIterator.next();
			if(activity.getTransitionTo()==null){
				firstActivity = activity;
				break;
			}
		}
		return firstActivity;
	}
	public WorkspaceFolder getWorkspaceFolder() {
		return workspaceFolder;
	}
	public void setWorkspaceFolder(WorkspaceFolder workspaceFolder) {
		this.workspaceFolder = workspaceFolder;
	}
	public Long getDuration() {
		return duration!=null?duration:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getLicenseText() {
		return licenseText!=null?licenseText:WDDXTAGS.STRING_NULL_VALUE;
	}
	public void setLicenseText(String licenseText) {
		this.licenseText = licenseText;
	}
	public Integer getCopyTypeID() {
		return copyTypeID!=null?copyTypeID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	public void setCopyTypeID(Integer copyTypeID) {
		this.copyTypeID = copyTypeID;
	}
	public License getLicense() {
		return license;
	}
	public void setLicense(License license) {
		this.license = license;
	}
	public Long getLessonID() {
		return lessonID!=null?lessonID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	public void setLessonID(Long lessonID) {
		this.lessonID = lessonID;
	}
	public String getLessonName() {
		return lessonName!=null?lessonName:WDDXTAGS.STRING_NULL_VALUE;
	}
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
	public Long getLessonOrgID() {
		return lessonOrgID!=null?lessonOrgID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	public void setLessonOrgID(Long lessonOrgID) {
		this.lessonOrgID = lessonOrgID;
	}
	public String getLessonOrgName() {
		return lessonOrgName!=null?lessonOrgName:WDDXTAGS.STRING_NULL_VALUE;
	}
	public void setLessonOrgName(String lessonOrgName) {
		this.lessonOrgName = lessonOrgName;
	}
	public Date getLessonStartDateTime() {
		return lessonStartDateTime!=null?lessonStartDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	public void setLessonStartDateTime(Date lessonStartDateTime) {
		this.lessonStartDateTime = lessonStartDateTime;
	}
	public Date getLastModifiedDateTime() {
		return lastModifiedDateTime;
	}
	public void setLastModifiedDateTime(Date lastModifiedDateTime) {
		this.lastModifiedDateTime = lastModifiedDateTime;
	}
}