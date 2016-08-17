package org.lamsfoundation.ld.integration.util;

import blackboard.base.BbList;
import blackboard.data.course.CourseMembership;
import blackboard.data.user.User;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.course.CourseMembershipDbLoader;

/**
 * Set of utilities dealing with Blackboard data.
 *
 * @author Andrey Balan
 */
public class BlackboardUtil {
    
    /**
     * Returns some random teacher from the specified course.
     * 
     * @param courseId
     *            BB course id
     * @return teacher
     * @throws PersistenceException 
     */
    public static User getCourseTeacher(PkId courseId) throws PersistenceException {
	// find the main teacher
	CourseMembershipDbLoader courseMemLoader = CourseMembershipDbLoader.Default.getInstance();
	BbList<CourseMembership> monitorCourseMemberships = courseMemLoader.loadByCourseIdAndRole(courseId,
		CourseMembership.Role.INSTRUCTOR, null, true);
	if (monitorCourseMemberships.isEmpty()) {
	    BbList<CourseMembership> teachingAssistantCourseMemberships = courseMemLoader
		    .loadByCourseIdAndRole(courseId, CourseMembership.Role.TEACHING_ASSISTANT, null, true);
	    monitorCourseMemberships.addAll(teachingAssistantCourseMemberships);
	    if (monitorCourseMemberships.isEmpty()) {
		BbList<CourseMembership> courseBuilderCourseMemberships = courseMemLoader
			.loadByCourseIdAndRole(courseId, CourseMembership.Role.COURSE_BUILDER, null, true);
		monitorCourseMemberships.addAll(courseBuilderCourseMemberships);
	    }
	}
	// validate teacher existence
	if (monitorCourseMemberships.isEmpty()) {
	    throw new RuntimeException("There are no monitors in the course courseId=" + courseId);
	}
	User teacher = monitorCourseMemberships.get(0).getUser();

	return teacher;
    }

}
