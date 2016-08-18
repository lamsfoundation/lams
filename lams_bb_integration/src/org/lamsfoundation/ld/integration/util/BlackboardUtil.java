package org.lamsfoundation.ld.integration.util;

import java.util.ArrayList;
import java.util.List;

import blackboard.base.BbList;
import blackboard.data.content.Content;
import blackboard.data.course.CourseMembership;
import blackboard.data.navigation.CourseToc;
import blackboard.data.user.User;
import blackboard.persist.Id;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.navigation.CourseTocDbLoader;

/**
 * Set of utilities dealing with Blackboard data.
 *
 * @author Andrey Balan
 */
public class BlackboardUtil {

    /**
     * Returns one random teacher from the specified course.
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

    /**
     * Returns all LAMS lessons from the specified course.
     * 
     * @param courseId
     *            BB course id
     * @return list of LAMS lessons
     * @throws PersistenceException
     */
    public static List<Content> getLamsLessonsByCourse(PkId courseId) throws PersistenceException {

	ContentDbLoader contentLoader = ContentDbLoader.Default.getInstance();
	CourseTocDbLoader cTocDbLoader = CourseTocDbLoader.Default.getInstance();

	// get a CourseTOC (Table of Contents) loader. We will need this to iterate through all of the "areas"
	// within the course
	BbList<CourseToc> courseTocs = cTocDbLoader.loadByCourseId(courseId);

	// iterate through the course TOC items
	List<Content> lamsContents = new ArrayList<Content>();
	for (CourseToc courseToc : courseTocs) {

	    // determine if the TOC item is of type "CONTENT" rather than applicaton, or something else
	    if ((courseToc.getTargetType() == CourseToc.Target.CONTENT) && (courseToc.getContentId() != Id.UNSET_ID)) {
		// we have determined that the TOC item is content, next we need to load the content object and
		// iterate through it
		// load the content tree into an object "content" and iterate through it
		BbList<Content> contents = contentLoader.loadListById(courseToc.getContentId());
		// iterate through the content items in this content object
		for (Content content : contents) {
		    // only LAMS content
		    if ("resource/x-lams-lamscontent".equals(content.getContentHandler())
			    || content.getContentHandler().equals("resource/x-ntu-hdllams")) {
			lamsContents.add(content);
		    }
		}
	    }
	}
	
	return lamsContents;
    }

}
