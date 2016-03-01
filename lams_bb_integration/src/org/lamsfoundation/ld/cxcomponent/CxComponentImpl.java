package org.lamsfoundation.ld.cxcomponent;

import org.apache.log4j.Logger;
import org.lamsfoundation.ld.integration.blackboard.GradebookServlet;
import org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil;

import blackboard.base.BbList;
import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.course.CourseMembership;
import blackboard.data.navigation.CourseToc;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Id;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.content.ContentDbPersister;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.navigation.CourseTocDbLoader;
import blackboard.platform.BbServiceManager;
import blackboard.platform.cx.component.CopyControl;
import blackboard.platform.cx.component.CxComponent;
import blackboard.platform.cx.component.ExportControl;
import blackboard.platform.cx.component.GenericPackageEntry;
import blackboard.platform.cx.component.ImportControl;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;

public class CxComponentImpl implements CxComponent {
    
    private static Logger logger = Logger.getLogger(CxComponentImpl.class);

    @Override
    public void doCopy(CopyControl copyControl) {

	Id destinationCourseId = copyControl.getDestinationCourseId();
	Id sourceCourseId = copyControl.getSourceCourseId();

	try {

	    BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
	    ContentDbLoader contentLoader = ContentDbLoader.Default.getInstance();
	    CourseDbLoader cLoader = CourseDbLoader.Default.getInstance();
	    CourseMembershipDbLoader courseMemLoader = CourseMembershipDbLoader.Default.getInstance();

	    // find the main teacher
	    BbList<CourseMembership> monitorCourseMemberships = courseMemLoader.loadByCourseIdAndRole(sourceCourseId,
		    CourseMembership.Role.INSTRUCTOR, null, true);
	    if (monitorCourseMemberships.isEmpty()) {
		BbList<CourseMembership> teachingAssistantCourseMemberships = courseMemLoader
			.loadByCourseIdAndRole(sourceCourseId, CourseMembership.Role.TEACHING_ASSISTANT, null, true);
		monitorCourseMemberships.addAll(teachingAssistantCourseMemberships);
		if (monitorCourseMemberships.isEmpty()) {
		    BbList<CourseMembership> courseBuilderCourseMemberships = courseMemLoader
			    .loadByCourseIdAndRole(sourceCourseId, CourseMembership.Role.COURSE_BUILDER, null, true);
		    monitorCourseMemberships.addAll(courseBuilderCourseMemberships);
		}
	    }
	    // validate method parameter
	    if (monitorCourseMemberships.isEmpty()) {
		throw new RuntimeException("There are no monitors in the course courseId=" + sourceCourseId);
	    }
	    User teacher = monitorCourseMemberships.get(0).getUser();

	    // get a CourseTOC (Table of Contents) loader. We will need this to iterate through all of the "areas"
	    // within the course
	    CourseTocDbLoader cTocDbLoader = (CourseTocDbLoader) bbPm.getLoader(CourseTocDbLoader.TYPE);
	    BbList<CourseToc> courseTocs = cTocDbLoader.loadByCourseId(destinationCourseId);
	    
	    System.out.println("Starting clonning course (courseId="+sourceCourseId + ").");

	    // iterate through the course TOC items
	    for (CourseToc courseToc : courseTocs) {

		// determine if the TOC item is of type "CONTENT" rather than applicaton, or something else
		if ((courseToc.getTargetType() == CourseToc.Target.CONTENT)
			&& (courseToc.getContentId() != Id.UNSET_ID)) {
		    // we have determined that the TOC item is content, next we need to load the content object and
		    // iterate through it
		    // load the content tree into an object "content" and iterate through it
		    BbList<Content> contents = contentLoader.loadListById(courseToc.getContentId());
		    // iterate through the content items in this content object
		    for (Content content : contents) {
			// only LAMS content
			if ("resource/x-lams-lamscontent".equals(content.getContentHandler())) {
			    String lsId = content.getLinkRef();

			    String courseIdReadable = cLoader.loadById(destinationCourseId).getCourseId();

			    // update lesson id
			    final Long newLessonId = LamsSecurityUtil.cloneLesson(teacher, courseIdReadable, lsId);
			    content.setLinkRef(Long.toString(newLessonId));

			    // update URL
			    String contentUrl = content.getUrl();
			    contentUrl = replaceParameterValue(contentUrl, "lsid", Long.toString(newLessonId));

			    PkId coursePkId = (PkId) destinationCourseId;
			    String course_id = "_" + coursePkId.getPk1() + "_" + coursePkId.getPk2();
			    contentUrl = replaceParameterValue(contentUrl, "course_id", course_id);

			    PkId contentPkId = (PkId) content.getId();
			    String content_id = "_" + contentPkId.getPk1() + "_" + contentPkId.getPk2();
			    contentUrl = replaceParameterValue(contentUrl, "content_id", content_id);
			    content.setUrl(contentUrl);

			    // persist updated content
			    ContentDbPersister persister = (ContentDbPersister) bbPm
				    .getPersister(ContentDbPersister.TYPE);
			    persister.persist(content);

			    // store internalContentId -> externalContentId. It's used for lineitem removal (delete.jsp)
			    PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
			    ExtraInfo ei = pei.getExtraInfo();
			    ei.setValue(content_id, Long.toString(newLessonId));
			    PortalUtil.savePortalExtraInfo(pei);

			    // Gradebook column will be copied automatically if appropriate option is selected on
			    // cloning lesson page

			    System.out.println("Lesson (lessonId="+lsId + ") was successfully cloned to the one (lessonId="+newLessonId+").");
			}

		    }
		}
	    }

	} catch (PersistenceException e) {
	    throw new RuntimeException(e);
	} catch (ValidationException e) {
	    throw new RuntimeException(e);
	}
	
	System.out.println("Course (courseId="+sourceCourseId + ") was successfully cloned to the new one (courseId="+destinationCourseId+").");

    }

    @Override
    public void doExport(ExportControl exportControl) {
    }

    @Override
    public void doImport(GenericPackageEntry entry, ImportControl importControl) {
    }

    public String getName() {
	return "Sample Content";
    }

    public String getComponentHandle() {
	return "lams-sample-content";
    }

    public Usage getUsage() {
	return Usage.CONFIGURABLE;
    }

    public String getApplicationHandle() {
	return "lams-copy-archive";
    }

    @Override
    public void afterImportContent(Content arg0, ImportControl arg1) {
    }

    private static String replaceParameterValue(String url, String paramName, String newParamValue) {
	String oldParamValue = "";

	int quotationMarkIndex = url.indexOf("?");
	String queryPart = quotationMarkIndex > -1 ? url.substring(quotationMarkIndex + 1) : url;
	String[] paramEntries = queryPart.split("&");
	for (String paramEntry : paramEntries) {
	    String[] paramEntrySplitted = paramEntry.split("=");
	    if ((paramEntrySplitted.length > 1) && paramName.equalsIgnoreCase(paramEntrySplitted[0])) {
		oldParamValue = paramEntrySplitted[1];

		return url.replaceFirst(paramName + "=" + oldParamValue, paramName + "=" + newParamValue);
	    }
	}

	return url;
    }

}
