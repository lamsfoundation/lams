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

/**
 * @hibernate.class table="lams_learning_design"
 *  
 */
public class LearningDesign implements Serializable {

	/** Represents a single LearningDesign object in the WDDXPacket */
	public static final String DESIGN_OBJECT = "LearningDesign";

	/** Represents a list of LearningDesign objects in the WDDXPacket */
	public static final String DESIGN_LIST_OBJECT = "LearningDesignList";
	
	public static final int COPY_TYPE_NONE =1;
	public static final int COPY_TYPE_LESSON=2;
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
	
	/** persistent field */
	private String licenseText;
	
	private License license;
	
	public License getLicense() {
		return license;
	}
	public void setLicense(License license) {
		this.license = license;
	}
	/** full constructor */
	public LearningDesign(
			Long learningDesignId,
			Integer id,
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
		this.learningDesignUIID = id;
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
	public static LearningDesign createLearningDesignCopy(LearningDesign design,boolean lesson){
		LearningDesign newDesign = newInstance(design);
		if(lesson){
			newDesign.setReadOnly(new Boolean(true));
			newDesign.setCopyTypeID(new Integer(COPY_TYPE_LESSON));			
		}else{
			newDesign.setReadOnly(new Boolean(false));
			newDesign.setCopyTypeID(new Integer(COPY_TYPE_NONE));			
		}
		return newDesign;
	}	
	private static LearningDesign newInstance(LearningDesign design) {
		
		LearningDesign newDesign = new LearningDesign();
		
		newDesign.setDescription(design.getDescription());
		newDesign.setTitle(design.getTitle());		
		newDesign.setMaxId(design.getMaxId());
		newDesign.setValidDesign(design.getValidDesign());		
		newDesign.setHelpText(design.getHelpText());
		newDesign.setVersion(design.getVersion());
		newDesign.setCreateDateTime(new Date());
			
		/**
		 * TODO Set the First Activity of the new Design
		 * TODO Set the ID of the new Design
		 */
		return newDesign;

	}	
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
	public Integer getLearningDesignUIID() {
		return this.learningDesignUIID;
	}

	public void setLearningDesignUIID(Integer id) {
		this.learningDesignUIID = id;
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

	public void setParentLearningDesign(
			org.lamsfoundation.lams.learningdesign.LearningDesign parentLearningDesign) {
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
	/**
	 * @return Returns the workspaceFolder.
	 */
	public WorkspaceFolder getWorkspaceFolder() {
		return workspaceFolder;
	}

	/**
	 * @param workspaceFolder
	 *            The workspaceFolder to set.
	 */
	public void setWorkspaceFolder(WorkspaceFolder workspaceFolder) {
		this.workspaceFolder = workspaceFolder;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getLicenseText() {
		return licenseText;
	}
	public void setLicenseText(String licenseText) {
		this.licenseText = licenseText;
	}
	public Integer getCopyTypeID() {
		return copyTypeID;
	}
	public void setCopyTypeID(Integer copyTypeID) {
		this.copyTypeID = copyTypeID;
	}
}