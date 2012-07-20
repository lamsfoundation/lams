<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Step 2 For Modifing an existing LAMS Lesson
    Process the data and persist to Blackboard

    Step 1 - modify.jsp
    Step 2 - modify_proc.jsp
--%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.io.*"%>
<%@ page import="blackboard.data.*"%>
<%@ page import="blackboard.persist.*"%>
<%@ page import="blackboard.data.course.*"%>
<%@ page import="blackboard.data.user.*"%>
<%@ page import="blackboard.persist.course.*"%>
<%@ page import="blackboard.data.content.*"%>
<%@ page import="blackboard.persist.content.*"%>
<%@ page import="blackboard.base.*"%>
<%@ page import="blackboard.platform.*"%>
<%@ page import="blackboard.platform.persistence.*"%>
<%@ page import="blackboard.platform.plugin.*"%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:genericPage title="Modify A LAMS Lesson" ctxId="ctx">
  
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
        Container bbContainer = bbPm.getContainer();

        // Internal Blackboard IDs for the course and parent content item
        Id courseId = bbPm.generateId(Course.DATA_TYPE,request.getParameter("course_id"));
        Id contentId = new PkId( bbContainer, CourseDocument.DATA_TYPE, request.getParameter("content_id") );

        // Load the content item
        ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );
        Content modifiedLesson = (Content)courseDocumentLoader.loadById( contentId );

        // Get the form parameters and convert into correct data types
        // TODO: Use bb text area instead
        String strTitle = request.getParameter("title").trim();
        String strDescription = request.getParameter("description").trim();
        FormattedText description = new FormattedText(strDescription, FormattedText.Type.HTML);
        
        String strIsAvailable = request.getParameter("isAvailable");
        String strIsTracked = request.getParameter("isTracked");
        boolean isAvailable = strIsAvailable.equals("true")?true:false;
        boolean isTracked = strIsTracked.equals("true")?true:false;
        
        String strStartDate = request.getParameter("lessonAvailability_start_datetime");
        String strEndDate = request.getParameter("lessonAvailability_end_datetime");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
        startDate.setTime(formatter.parse(strStartDate));
        endDate.setTime(formatter.parse(strEndDate));
        
        String strStartDateCheckbox = request.getParameter("lessonAvailability_start_checkbox");
        String strEndDateCheckbox = request.getParameter("lessonAvailability_end_checkbox");
    
        // Set LAMS content data in Blackboard
        modifiedLesson.setTitle(strTitle);
        modifiedLesson.setIsAvailable(isAvailable);
        modifiedLesson.setIsTracked(isTracked);
        modifiedLesson.setBody(description);
    
        // Set Availability Dates
        // Clear the date (set to null) if the checkbox is unchecked
        // Start Date
        if (strStartDateCheckbox != null){
            if (strStartDateCheckbox.equals("1")){
                modifiedLesson.setStartDate(startDate);
            } else {
                modifiedLesson.setStartDate(null);
            }
        } else {
            modifiedLesson.setStartDate(null);
        }
        // End Date
        if (strEndDateCheckbox != null){
            if (strEndDateCheckbox.equals("1")){
                modifiedLesson.setEndDate(endDate);
            } else {
                modifiedLesson.setEndDate(null);
            }
        } else {
            modifiedLesson.setEndDate(null);
        }

        //Persist the Modified Lesson Object in Blackboard    
        ContentDbPersister persister= (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE );
        persister.persist( modifiedLesson );
    
        String strReturnUrl = PlugInUtil.getEditableContentReturnURL(modifiedLesson.getParentId(), courseId);
    %>

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="CTRL_PANEL" isContent="true">
        <bbNG:breadcrumb title="Modify A LAMS Lesson" />
    </bbNG:breadcrumbBar>
    
    <%-- Page Header --%>    
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="Modify A LAMS Lesson"/>
    </bbNG:pageHeader>

    <%-- Receipt --%>
    <bbNG:receipt type="SUCCESS" 
        iconUrl="/images/ci/icons/receiptsuccess_u.gif" 
        title="Content Modified" 
        recallUrl="<%=strReturnUrl%>">
            Content successfully modified.
    </bbNG:receipt>

</bbNG:genericPage>