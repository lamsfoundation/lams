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
<%@ page import="blackboard.data.gradebook.Lineitem" %>
<%@ page import="blackboard.persist.gradebook.LineitemDbPersister" %>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsPluginUtil"%>    
<%@ page import="org.lamsfoundation.ld.integration.Constants" %> 
<%@ page import="blackboard.portal.data.*" %>
<%@ page import="blackboard.portal.servlet.PortalUtil" %>
<%@ page import="blackboard.persist.PersistenceException" %>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<bbNG:genericPage title="LAMS Learning Activity Management System" ctxId="ctx">

       <%-- Set the new LAMS Lesson Content Object --%>
    <jsp:useBean id="newLesson" scope="page" class="blackboard.data.content.CourseDocument"/>
    <jsp:setProperty name="newLesson" property="title"/>
    <jsp:setProperty name="newLesson" property="isAvailable"/>
    <jsp:setProperty name="newLesson" property="isTracked"/>
	
    <%
        // SECURITY!
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
        String strDescription = request.getParameter("description").trim();
        FormattedText description = new FormattedText(strDescription, FormattedText.Type.HTML);
        
        String strSequenceID = request.getParameter("sequence_id").trim();
        long ldId = Long.parseLong(strSequenceID);               
        
        String strIsAvailable = request.getParameter("isAvailable");
        String strIsTracked = request.getParameter("isTracked");
        boolean isAvailable = strIsAvailable.equals("true")?true:false;
        boolean isTracked = strIsTracked.equals("true")?true:false;
        
        String isDisplayDesignImage = request.getParameter("isDisplayDesignImage");
        
        String strStartDate = request.getParameter("lessonAvailability_start_datetime");
        String strEndDate = request.getParameter("lessonAvailability_end_datetime");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
        startDate.setTime(formatter.parse(strStartDate));
        endDate.setTime(formatter.parse(strEndDate));
        
        String strStartDateCheckbox = request.getParameter("lessonAvailability_start_checkbox");
        String strEndDateCheckbox = request.getParameter("lessonAvailability_end_checkbox");
	    
        // Set the New LAMS Lesson content data (in Blackboard)
        newLesson.setTitle(strTitle);
		newLesson.setIsAvailable(isAvailable);
        newLesson.setIsTracked(isTracked);
        newLesson.setAllowGuests(false);
		
        newLesson.setContentHandler(LamsPluginUtil.CONTENT_HANDLE);
        newLesson.setCourseId(courseId);
        newLesson.setParentId(folderId);
	    
        newLesson.setRenderType(Content.RenderType.URL);
        newLesson.setBody(description);
        
        // Start the Lesson in LAMS (via Webservices)
        // Capture the lesson ID
        String lessonIdStr = null;
        try{  	
            long lessonId = LamsSecurityUtil.startLesson(ctx, ldId, strTitle, strDescription, false);
            //error checking
            if (lessonId == -1) {
                response.sendRedirect("lamsServerDown.jsp");
                System.exit(1);
            }
	    	
            lessonIdStr = Long.toString(lessonId);
        } catch (Exception e){
            throw new ServletException(e.getMessage(), e);
        }
        
        //Create new Gradebook column for current lesson
        Lineitem lineitem = new Lineitem();
	    lineitem.setCourseId(courseId);
	    lineitem.setName(strTitle);
	    lineitem.setPointsPossible(Constants.GRADEBOOK_POINTS_POSSIBLE);
	    lineitem.setType(Constants.GRADEBOOK_LINEITEM_TYPE);
	    lineitem.setIsAvailable(true);
	    lineitem.setDateAdded();
	    lineitem.setAssessmentLocation( Lineitem.AssessmentLocation.EXTERNAL );
	    lineitem.setAssessmentId(lessonIdStr, Lineitem.AssessmentLocation.EXTERNAL);
		lineitem.validate();
	    LineitemDbPersister linePersister= (LineitemDbPersister) bbPm.getPersister( LineitemDbPersister.TYPE );
	    linePersister.persist(lineitem);
	    
	    OutcomeDefinitionDbPersister ocdPersister = (OutcomeDefinitionDbPersister)bbPm.getPersister(OutcomeDefinitionDbPersister.TYPE);
	    OutcomeDefinition ocd = lineitem.getOutcomeDefinition();
	    ocd.setCourseId(courseId);
	    ocd.setPosition(1);

		
	    OutcomeDefinitionScaleDbLoader ods2Loader = (OutcomeDefinitionScaleDbLoader)bbPm.getLoader(OutcomeDefinitionScaleDbLoader.TYPE);
	    OutcomeDefinitionScaleDbPersister uutcomeDefinitionScaleDbPersister = (OutcomeDefinitionScaleDbPersister)bbPm.getPersister(OutcomeDefinitionScaleDbPersister.TYPE);
	    
	    boolean hasLessonScoreOutputs = LamsSecurityUtil.hasLessonScoreOutputs(ctx);
	    OutcomeDefinitionScale ods;
	    if (hasLessonScoreOutputs) {
			ods = ods2Loader.loadByCourseIdAndTitle(courseId,OutcomeDefinitionScale.SCORE);
			ods.setNumericScale(true);
			ods.setPercentageScale(true);
			ocd.setScorable(true);
	    } else {
			ods = ods2Loader.loadByCourseIdAndTitle(courseId,OutcomeDefinitionScale.COMPLETE_INCOMPLETE);
			ods.setNumericScale(false);
			ocd.setScorable(false);
	    }

	    uutcomeDefinitionScaleDbPersister.persist(ods);
	    ocd.setScale(ods);
	    ocdPersister.persist(ocd);
	    
	    
	    
	    
	    // Add port to the url if the port is in the blackboard url.
	    int bbport = request.getServerPort();
	    String bbportstr = bbport != 0 ? ":" + bbport : "";
		
	    //Build and set the content URL
	    String contentUrl = request.getScheme()
	        + "://" +
	        request.getServerName() + 
	        bbportstr +
	        request.getContextPath() + 
	        "/modules/learnermonitor.jsp?lsid=" + lessonIdStr + 
	        "&course_id=" + request.getParameter("course_id") + "&lineitemid=" + lineitem.getId() + 
	        "&ldid=" + ldId + "&isDisplayDesignImage=" + isDisplayDesignImage + "&title=" + URLEncoder.encode(strTitle) + "&description=" + URLEncoder.encode(strDescription);
		
	    newLesson.setUrl(contentUrl);
	    newLesson.setLinkRef(lessonIdStr);

	    // Set Availability Dates	    
	    // Start Date
	    if (strStartDateCheckbox != null){
	        if (strStartDateCheckbox.equals("1")){
	            newLesson.setStartDate(startDate);
	        }
	    }
	    // End Date
	    if (strEndDateCheckbox != null){
	        if (strEndDateCheckbox.equals("1")){
	            newLesson.setEndDate(endDate);
	        }
	    }

	    //Persist the New Lesson Object in Blackboard
	    ContentDbPersister persister= (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE );
	    persister.persist( newLesson );
	    
	    //store internalContentId -> externalContentId. This is required for lesson and according lineitem removal (see delete.jsp)
		PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
		ExtraInfo ei = pei.getExtraInfo();
		PkId newLessonPkId = (PkId) newLesson.getId();
	    String newLessonKeyId = "_" + newLessonPkId.getPk1() + "_" + newLessonPkId.getPk2();
		ei.setValue(newLessonKeyId, lessonIdStr);
		PortalUtil.savePortalExtraInfo(pei);
	    
        String strReturnUrl = PlugInUtil.getEditableContentReturnURL(newLesson.getParentId(), courseId);
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
            Content successfully added.
    </bbNG:receipt>
        
</bbNG:genericPage>