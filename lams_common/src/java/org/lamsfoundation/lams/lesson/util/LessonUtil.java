package org.lamsfoundation.lams.lesson.util;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.CompletedActivityProgressArchive;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.LearnerProgressArchive;

public class LessonUtil {

    public static Long getActivityDuration(Object learnerProgress, Activity activity) {
	if (learnerProgress != null) {
	    // this construct looks bad but see LDEV-4609 commit for explanation
	    if (learnerProgress instanceof LearnerProgressArchive) {
		CompletedActivityProgressArchive compProg = ((LearnerProgressArchive) learnerProgress)
			.getCompletedActivities().get(activity);
		if (compProg != null) {
		    Date startTime = compProg.getStartDate();
		    Date endTime = compProg.getFinishDate();
		    if ((startTime != null) && (endTime != null)) {
			return endTime.getTime() - startTime.getTime();
		    }
		}
	    } else {
		CompletedActivityProgress compProg = ((LearnerProgress) learnerProgress).getCompletedActivities()
			.get(activity);
		if (compProg != null) {
		    Date startTime = compProg.getStartDate();
		    Date endTime = compProg.getFinishDate();
		    if ((startTime != null) && (endTime != null)) {
			return endTime.getTime() - startTime.getTime();
		    }
		}
	    }

	}
	return null;
    }
}