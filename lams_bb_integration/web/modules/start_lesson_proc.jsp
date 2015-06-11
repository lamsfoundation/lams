<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Step 2 For Creating a New LAMS Lesson
    Process the Lesson Information and add it to the Blackboard site

    Step 1 - create.jsp
    Step 2 - start_lesson_proc.jsp
--%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="blackboard.base.FormattedText"%>
<%@ page import="blackboard.data.course.Course"%>
<%@ page import="blackboard.data.content.Content"%>
<%@ page import="blackboard.data.content.ContentFile"%>
<%@ page import="blackboard.data.content.ContentFolder"%>
<%@ page import="blackboard.data.content.CourseDocument"%>
<%@ page import="blackboard.persist.*"%>
<%@ page import="blackboard.persist.content.*"%>
<%@ page import="blackboard.data.gradebook.impl.*"%>
<%@ page import="blackboard.platform.session.*"%>
<%@ page import="blackboard.persist.gradebook.ext.*"%>
<%@ page import="blackboard.persist.gradebook.impl.*"%>
<%@ page import="blackboard.platform.*"%>
<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="blackboard.platform.plugin.PlugInException"%>
<%@ page import="blackboard.platform.context.Context"%>
<%@ page import="blackboard.data.gradebook.Lineitem" %>
<%@ page import="blackboard.persist.gradebook.LineitemDbPersister" %>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsPluginUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.Constants" %>
<%@ page import="org.lamsfoundation.ld.util.*"%>
<%@ page import="blackboard.portal.data.*" %>
<%@ page import="blackboard.data.content.ExternalLink" %>
<%@ page import="blackboard.portal.servlet.PortalUtil" %>
<%@ page import="blackboard.persist.PersistenceException" %>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<bbNG:genericPage title="LAMS Learning Activity Management System" ctxId="ctx">

    <%
    	//Set the new LAMS Lesson Content Object
    	ExternalLink bbContent = new blackboard.data.content.ExternalLink();
    
        // Authorise current user for Course Control Panel (automatic redirect)
        try{
            if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
                return;
        } catch(PlugInException e) {
            throw new RuntimeException(e);
        }

		// Retrieve the Db persistence manager from the persistence service
        BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
        ContentDbPersister contentPersister = (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE );

        // Internal Blackboard IDs for the course and parent content item
        Id courseId = bbPm.generateId(Course.DATA_TYPE,request.getParameter("course_id"));
        Id folderId = bbPm.generateId(CourseDocument.DATA_TYPE,request.getParameter("content_id"));

		// Load parent content item
        ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );
		ContentFolder courseDocParent = (ContentFolder)courseDocumentLoader.loadById( folderId );

        // Get the session object to obtain the user and course object
        BbSessionManagerService sessionService = BbServiceManager.getSessionManagerService();
        BbSession bbSession = sessionService.getSession( request );

        // Get the form parameters and convert into correct data types
        // TODO: Use bb text area instead
        String strTitle = request.getParameter("title").trim();
        String strDescription = request.getParameter("descriptiontext").trim();
        FormattedText description = new FormattedText(strDescription, FormattedText.Type.HTML);

        String strSequenceID = request.getParameter("sequence_id").trim();
        long ldId = Long.parseLong(strSequenceID);

        String strIsAvailable = request.getParameter("isAvailable");
        String strIsGradecenter = request.getParameter("isGradecenter");
        String strIsTracked = request.getParameter("isTracked");
        boolean isAvailable = (strIsAvailable==null || strIsAvailable.equals("true"))?true:false; // default true
        boolean isGradecenter = (strIsGradecenter!=null && strIsGradecenter.equals("true"))?true:false; // default false
        boolean isTracked = (strIsTracked!=null && strIsTracked.equals("true"))?true:false; // default false

        String isDisplayDesignImage = request.getParameter("isDisplayDesignImage");

	    // Set Availability Dates
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	    // Start Date
        String strStartDate = request.getParameter("lessonAvailability_start_datetime");
        if ( strStartDate != null ) {
	        Calendar startDate = Calendar.getInstance();
	        startDate.setTime(formatter.parse(strStartDate));
	        String strStartDateCheckbox = request.getParameter("lessonAvailability_start_checkbox");
		    if (strStartDateCheckbox != null){
		        if (strStartDateCheckbox.equals("1")){
		            bbContent.setStartDate(startDate);
		        }
		    }
        }
	        
	    // End Date
        String strEndDate = request.getParameter("lessonAvailability_end_datetime");
	    if ( strEndDate != null ) {
	        Calendar endDate = Calendar.getInstance();
    	    endDate.setTime(formatter.parse(strEndDate));
	        String strEndDateCheckbox = request.getParameter("lessonAvailability_end_checkbox");
		    if (strEndDateCheckbox != null){
		        if (strEndDateCheckbox.equals("1")){
	    	        bbContent.setEndDate(endDate);
	        	}
	    	}
	    }

        // Set the New LAMS Lesson content data (in Blackboard)
        bbContent.setTitle(strTitle);
		bbContent.setIsAvailable(isAvailable);
		bbContent.setIsDescribed(isGradecenter);//isDescribed field is used for storing isGradecenter parameter
        bbContent.setIsTracked(isTracked);
        bbContent.setAllowGuests(false);
        bbContent.setContentHandler(LamsPluginUtil.CONTENT_HANDLE);

        bbContent.setCourseId(courseId);
        bbContent.setParentId(folderId);

        bbContent.setRenderType(Content.RenderType.URL);
        bbContent.setBody(description);

        // Start the Lesson in LAMS (via Webservices) and capture the lesson ID
        final long LamsLessonIdLong = LamsSecurityUtil.startLesson(ctx, ldId, strTitle, strDescription, false);
        //error checking
        if (LamsLessonIdLong == -1) {
        	response.sendRedirect("lamsServerDown.jsp");
        	System.exit(1);
        }
        String lamsLessonId = Long.toString(LamsLessonIdLong);
        bbContent.setLinkRef(lamsLessonId);

	    //Persist the New Lesson Object in Blackboard
	    ContentDbPersister persister= (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE );
	    persister.persist( bbContent );
		PkId bbContentPkId = (PkId) bbContent.getId();
		String bbContentId = "_" + bbContentPkId.getPk1() + "_" + bbContentPkId.getPk2();
	    
		//Build and set the content URL. Include new lesson id parameter
		int bbport = request.getServerPort();// Add port to the url if the port is in the blackboard url.
		String bbportstr = bbport != 0 ? ":" + bbport : "";
		String contentUrl = request.getScheme()
			+ "://" +
			request.getServerName() +
			bbportstr +
			request.getContextPath() +
			"/modules/learnermonitor.jsp?lsid=" + lamsLessonId +
			"&course_id=" + request.getParameter("course_id") +
			"&content_id=" + bbContentId +
			"&ldid=" + ldId + 
			"&isDisplayDesignImage=" + isDisplayDesignImage;
		bbContent.setUrl(contentUrl);
		persister.persist(bbContent);

		//store internalContentId -> externalContentId. It's used for lineitem removal (delete.jsp)
		PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
		ExtraInfo ei = pei.getExtraInfo();
		ei.setValue(bbContentId, lamsLessonId);
		PortalUtil.savePortalExtraInfo(pei);
		
		//Create new Gradebook column for current lesson
		if (isGradecenter) {
		    LineitemUtil.createLineitem(ctx, bbContent);
		}
		
        String strReturnUrl = PlugInUtil.getEditableContentReturnURL(bbContent.getParentId(), courseId);
        
		// create a new thread to pre-add students and monitors to a lesson (in order to do this task in parallel not to slow down later work)
		final Context ctxFinal = ctx;
		Thread preaddLearnersMonitorsThread = new Thread(new Runnable() {
		    @Override
		    public void run() {
				LamsSecurityUtil.preaddLearnersMonitorsToLesson(ctxFinal, LamsLessonIdLong);
		    }
		}, "LAMS_preaddLearnersMonitors_thread");
		preaddLearnersMonitorsThread.start();
        
	%>

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="COURSE" isContent="true">
        <bbNG:breadcrumb title="Start A LAMS Lesson" />
    </bbNG:breadcrumbBar>

    <%-- Page Header --%>
    <bbNG:pageHeader>
        <bbNG:pageTitleBar title="Start A LAMS Lesson"/>
    </bbNG:pageHeader>

    <%-- Receipt --%>
    <bbNG:receipt type="SUCCESS"
        iconUrl="/images/ci/icons/receiptsuccess_u.gif"
        title="Start A LAMS Lesson"
        recallUrl="<%=strReturnUrl%>">
        	<!-- LESSON START SUCCESS (DO NOT REMOVE: String is used to check for completion) --> 
            Content successfully added.
    </bbNG:receipt>

</bbNG:genericPage>
