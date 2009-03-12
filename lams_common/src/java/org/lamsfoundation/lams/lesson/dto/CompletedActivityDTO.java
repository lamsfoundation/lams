package org.lamsfoundation.lams.lesson.dto;

import java.util.Date;
import org.lamsfoundation.lams.learningdesign.Activity;

public class CompletedActivityDTO {

	public Long completedActivityId;
	public String completedActivityTitle;
	public Long completedDateTime;
	
	public CompletedActivityDTO(Activity completedActivity, Long completedDateTime) {
        this.completedActivityId = completedActivity.getActivityId();
        this.completedActivityTitle = completedActivity.getTitle();
        this.completedDateTime = completedDateTime;
	}
	
	/**
     * @return Returns the completedActivityId.
     */
    public Long getCompletedActivityId()
    {
        return completedActivityId;
    }
    
    /**
     * @return Returns the completedActivityTitle.
     */
    public String getCompletedActivityTitle()
    {
        return completedActivityTitle;
    }
    
    /**
     * @return Returns the completedDateTime.
     */
    public Long getCompletedDateTime()
    {
        return completedDateTime;
    }
}
