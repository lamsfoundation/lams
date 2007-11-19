package org.lamsfoundation.integration.webct;


import java.sql.Date;


/**
 * Class for mapping LAMS lessons to the database
 * @author luke foxton
 *
 */
public class LamsLesson {

	private long id;
	private long ptId;				// powerlink instance id
	private long lessonId; 
	private long learningContextId;	// webct learning context id
	private long sequenceId;
	private String title;
	private String description;
	private String ownerId; 		// webct userId
	private String ownerFirstName;	// webct user first name
	private String ownerLastName;	// webct user second name
	private boolean hidden; 		// only visible to owner if true
	private boolean schedule;
	private Date startDate;
	private Date endDate;
	
	
	
	/**
	 * Default constructor
	 */
	public LamsLesson() {}
	
	/**
	 * Minimal constructor
	 */
	public LamsLesson(long lessonId, long learningContextId, String title) {
		this.lessonId = lessonId;
		this.learningContextId = learningContextId;
		this.title = title;
	}
	
	/**
	 * Full constructor
	 */
	public LamsLesson(
					long lessonId, 
					long ptId,
					long learningContextId, 
					long sequenceId, 
					String title,
					String description, 
					String ownerId, 
					String ownerFirstName,
					String ownerLastName, 
					boolean hidden, 
					boolean schedule,
					Date startDate, 
					Date endDate)
	{
		this.lessonId = lessonId;
		this.ptId = ptId;
		this.learningContextId = learningContextId;
		this.sequenceId = sequenceId;
		this.title = title;
		this.description = description;
		this.ownerId = ownerId;
		this.ownerFirstName = ownerFirstName;
		this.ownerLastName = ownerLastName;
		this.hidden = hidden;
		this.schedule = schedule;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	
	
	public long getLessonId() {
		return lessonId;
	}
	public void setLessonId(long lessonId) {
		this.lessonId = lessonId;
	}
	public long getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(long sequenceId) {
		this.sequenceId = sequenceId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerFirstName() {
		return ownerFirstName;
	}
	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}
	public String getOwnerLastName() {
		return ownerLastName;
	}
	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}
	public boolean getHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean getSchedule() {
		return schedule;
	}
	public void setSchedule(boolean schedule) {
		this.schedule = schedule;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getLearningContextId() {
		return learningContextId;
	}

	public void setLearningContextId(long learningContextId) {
		this.learningContextId = learningContextId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "LamsLesson ( "
	        + "lessonId = " + this.lessonId + TAB
	        + "ptId = " + this.ptId + TAB
	        + "learningContextId = " + this.learningContextId + TAB
	        + "sequenceId = " + this.sequenceId + TAB
	        + "title = " + this.title + TAB
	        + "description = " + this.description + TAB
	        + "ownerId = " + this.ownerId + TAB
	        + "ownerFirstName = " + this.ownerFirstName + TAB
	        + "ownerLastName = " + this.ownerLastName + TAB
	        + "hidden = " + this.hidden + TAB
	        + "schedule = " + this.schedule + TAB
	        + "startDate = " + this.startDate + TAB
	        + "endDate = " + this.endDate + TAB
	        + " )";
	
	    return retValue;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPtId() {
		return ptId;
	}

	public void setPtId(long ptId) {
		this.ptId = ptId;
	}
	
}
