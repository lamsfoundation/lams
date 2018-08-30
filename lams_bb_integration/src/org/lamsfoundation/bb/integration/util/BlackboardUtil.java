package org.lamsfoundation.bb.integration.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import blackboard.base.FormattedText;
import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.content.CourseDocument;
import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.data.navigation.CourseToc;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.content.ContentDbPersister;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.navigation.CourseTocDbLoader;
import blackboard.persist.user.UserDbLoader;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;

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
	List<CourseMembership> monitorCourseMemberships = courseMemLoader.loadByCourseIdAndRole(courseId,
		CourseMembership.Role.INSTRUCTOR, null, true);
	if (monitorCourseMemberships.isEmpty()) {
	    List<CourseMembership> teachingAssistantCourseMemberships = courseMemLoader
		    .loadByCourseIdAndRole(courseId, CourseMembership.Role.TEACHING_ASSISTANT, null, true);
	    monitorCourseMemberships.addAll(teachingAssistantCourseMemberships);
	    if (monitorCourseMemberships.isEmpty()) {
		List<CourseMembership> courseBuilderCourseMemberships = courseMemLoader
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
    public static List<Content> getLamsLessonsByCourse(PkId courseId, boolean includeLessonsCreatedByOldNtuPlugin) throws PersistenceException {

	ContentDbLoader contentLoader = ContentDbLoader.Default.getInstance();
	CourseTocDbLoader cTocDbLoader = CourseTocDbLoader.Default.getInstance();

	// get a CourseTOC (Table of Contents) loader. We will need this to iterate through all of the "areas"
	// within the course
	List<CourseToc> courseTocs = cTocDbLoader.loadByCourseId(courseId);

	// iterate through the course TOC items
	List<Content> lamsContents = new ArrayList<Content>();
	for (CourseToc courseToc : courseTocs) {

	    // determine if the TOC item is of type "CONTENT" rather than applicaton, or something else
	    if ((courseToc.getTargetType() == CourseToc.Target.CONTENT) && (courseToc.getContentId() != Id.UNSET_ID)) {
		// we have determined that the TOC item is content, next we need to load the content object and
		// iterate through it
		// load the content tree into an object "content" and iterate through it
		List<Content> contents = contentLoader.loadListById(courseToc.getContentId());
		// iterate through the content items in this content object
		for (Content content : contents) {
		    // only LAMS content
		    if ("resource/x-lams-lamscontent".equals(content.getContentHandler())
			    || includeLessonsCreatedByOldNtuPlugin && content.getContentHandler().equals("resource/x-ntu-hdllams")) {
			lamsContents.add(content);
		    }
		}
	    }
	}

	return lamsContents;
    }

    /**
     * @throws ParseException
     * @throws IOException
     * @throws ValidationException
     * @throws SAXException 
     * @throws ParserConfigurationException 
     */
    public static String storeBlackboardContent(HttpServletRequest request, HttpServletResponse response, User user)
	    throws PersistenceException, ParseException, IOException, ValidationException, ParserConfigurationException, SAXException {

	ContentDbLoader contentLoader = ContentDbLoader.Default.getInstance();
	
	// Set the new LAMS Lesson Content Object
	CourseDocument bbContent = new blackboard.data.content.CourseDocument();
	// Retrieve the Db persistence manager from the persistence service
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();

	String _course_id = request.getParameter("course_id");
	String _content_id = request.getParameter("content_id");
	String strTitle = getTrimmedString(request, "title");
	String strSequenceID = getTrimmedString(request, "sequence_id");
	// TODO: Use bb text area instead
	String strDescription = getTrimmedString(request, "descriptiontext");
	String strIsAvailable = request.getParameter("isAvailable");
	String strIsGradecenter = request.getParameter("isGradecenter");
	String strIsTracked = request.getParameter("isTracked");
	String strIsAllowLearnerRestart = request.getParameter("isAllowLearnerRestart");
	String isDisplayDesignImage = request.getParameter("isDisplayDesignImage");

	// Internal Blackboard IDs for the course and parent content item
	Id courseId = bbPm.generateId(Course.DATA_TYPE, _course_id);	
	Id folderId = bbPm.generateId(CourseDocument.DATA_TYPE, _content_id);

	FormattedText description = new FormattedText(strDescription, FormattedText.Type.HTML);
	long ldId = Long.parseLong(strSequenceID);

	boolean isAvailable = (strIsAvailable == null || strIsAvailable.equals("true")) ? true : false; // default true
	boolean isGradecenter = (strIsGradecenter != null && strIsGradecenter.equals("true")) ? true : false; // default false
	boolean isTracked = (strIsTracked != null && strIsTracked.equals("true")) ? true : false; // default false
	boolean enforceAllowLearnerRestart = (strIsAllowLearnerRestart != null && strIsAllowLearnerRestart.equals("true")); // default false
	
	// Set Availability Dates
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// Start Date
	String strStartDate = request.getParameter("lessonAvailability_start_datetime");
	if (strStartDate != null) {
	    Calendar startDate = Calendar.getInstance();
	    startDate.setTime(formatter.parse(strStartDate));
	    String strStartDateCheckbox = request.getParameter("lessonAvailability_start_checkbox");
	    if (strStartDateCheckbox != null) {
		if (strStartDateCheckbox.equals("1")) {
		    bbContent.setStartDate(startDate);
		}
	    }
	}

	// End Date
	String strEndDate = request.getParameter("lessonAvailability_end_datetime");
	if (strEndDate != null) {
	    Calendar endDate = Calendar.getInstance();
	    endDate.setTime(formatter.parse(strEndDate));
	    String strEndDateCheckbox = request.getParameter("lessonAvailability_end_checkbox");
	    if (strEndDateCheckbox != null) {
		if (strEndDateCheckbox.equals("1")) {
		    bbContent.setEndDate(endDate);
		}
	    }
	}

	// Set the New LAMS Lesson content data (in Blackboard)
	bbContent.setTitle(strTitle);
	bbContent.setIsAvailable(isAvailable);
	bbContent.setIsDescribed(isGradecenter);// isDescribed field is used for storing isGradecenter parameter
	bbContent.setIsTracked(isTracked);
	//bbContent.setIsFromCartridge(isAllowLearnerRestart);// isFromCartridge field is used for storing isAllowLearnerRestart parameter
	bbContent.setAllowGuests(false);
	bbContent.setContentHandler(LamsPluginUtil.CONTENT_HANDLE);

	bbContent.setCourseId(courseId);
	bbContent.setParentId(folderId);

	bbContent.setRenderType(Content.RenderType.URL);
	bbContent.setBody(description);

	// assign LAMS lesson the last position number so it appears at the bottom of the list
	List<Content> contents = contentLoader.loadListById(folderId);
	int countContentsInsideFolder = contents.size();
	bbContent.setPosition(countContentsInsideFolder);
	
	//get course's courseId string that we need to provide LAMS with 
	CourseDbLoader courseLoader = CourseDbLoader.Default.getInstance();
	Course course = courseLoader.loadById(courseId);
	String courseIdStr = course.getCourseId();

	// Start the Lesson in LAMS (via Webservices) and capture the lesson ID
	final long lamsLessonIdLong = LamsSecurityUtil.startLesson(user, courseIdStr, ldId, strTitle, strDescription,
		enforceAllowLearnerRestart, false);
	// error checking
	if (lamsLessonIdLong == -1) {
	    response.sendRedirect("lamsServerDown.jsp");
	    System.exit(1);
	}
	String lamsLessonId = Long.toString(lamsLessonIdLong);
	bbContent.setLinkRef(lamsLessonId);

	// Persist the New Lesson Object in Blackboard
	ContentDbPersister persister = (ContentDbPersister) bbPm.getPersister(ContentDbPersister.TYPE);
	persister.persist(bbContent);
	//same as "_" + bbContent.getId().getPk1() + "_" + bbContent.getId().getPk2()
	String bbContentId = bbContent.getId().toExternalString();

	// Build and set the content URL. Include new lesson id parameter
	int bbport = request.getServerPort();// Add port to the url if the port is in the blackboard url.
	String bbportstr = bbport != 0 ? ":" + bbport : "";
	String contentUrl = request.getScheme() + "://" +
		request.getServerName() + bbportstr + request.getContextPath() + "/modules/learnermonitor.jsp" +
		"?lsid=" + lamsLessonId +
		"&course_id=" + request.getParameter("course_id") +
		"&content_id=" + bbContentId +
		"&ldid=" + ldId + 
		"&isDisplayDesignImage=" + isDisplayDesignImage;
	bbContent.setUrl(contentUrl);
	persister.persist(bbContent);

	// store internalContentId -> externalContentId. It's used for GradebookServlet
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
	ExtraInfo ei = pei.getExtraInfo();
	ei.setValue(bbContentId, lamsLessonId);
	PortalUtil.savePortalExtraInfo(pei);

	// Create new Gradebook column for current lesson
	if (isGradecenter) {
	    String userName = user.getUserName();
	    LineitemUtil.createLineitem(bbContent, userName);
	}

	// create a new thread to pre-add students and monitors to a lesson (in order to do this task in parallel not to
	// slow down later work)
	final User userFinal = user;
	final Course courseFinal = course;
	Thread preaddLearnersMonitorsThread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		LamsSecurityUtil.preaddLearnersMonitorsToLesson(userFinal, courseFinal, lamsLessonIdLong);
	    }
	}, "LAMS_preaddLearnersMonitors_thread");
	preaddLearnersMonitorsThread.start();

	return bbContentId;
    }

    public static String getTrimmedString(HttpServletRequest request, String paramName) {
	String value = request.getParameter(paramName);
	return value != null ? value.trim() : "";
    }

    public static User loadUserFromDB(String username) {
	User user = null;
	try {
	    final UserDbLoader userDbLoader = UserDbLoader.Default.getInstance();
	    user = userDbLoader.loadByUserName(username);
	} catch (KeyNotFoundException e) {
	    throw new RuntimeException("No user details found in context or via username parameter. Unable access LAMS. "+e.getMessage()+" Username "+username,e);
	} catch (PersistenceException e) {
	    throw new RuntimeException("No user details found in context or via username parameter. Unable access LAMS. "+e.getMessage()+" Username "+username,e);
	}
	if ( user == null ) {
	    throw new RuntimeException("No user details found in context or via username parameter. Unable access LAMS. Username "+username);
	}
	return user;
    }

}
