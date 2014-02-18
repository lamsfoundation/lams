<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
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
<%@ page import="blackboard.platform.plugin.*"%>
<%@ page import="blackboard.data.gradebook.*"%> 
<%@ page import="org.lamsfoundation.ld.util.*"%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:genericPage title="Modify A LAMS Lesson" ctxId="ctx">
  
    <%
        // Authorise current user for Course Control Panel (automatic redirect)
        try{
            if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
                return;
        } catch(PlugInException e) {
            throw new RuntimeException(e);
        }
    
	    String courseIdStr = request.getParameter("course_id");
	    String contentIdStr = request.getParameter("content_id");

        // Retrieve the Db persistence manager from the persistence service
        BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
        Container bbContainer = bbPm.getContainer();

        // Internal Blackboard IDs for the course and parent content item
        Id courseId = bbPm.generateId(Course.DATA_TYPE, courseIdStr);
        Id contentId = new PkId( bbContainer, CourseDocument.DATA_TYPE, contentIdStr);

        // Load the content item
        ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );
        Content bbContent = (Content)courseDocumentLoader.loadById( contentId );

        // Get the form parameters and convert into correct data types
        // TODO: Use bb text area instead
        String strTitle = request.getParameter("title").trim();
        String strDescription = request.getParameter("description").trim();
        FormattedText description = new FormattedText(strDescription, FormattedText.Type.HTML);
        
        String strIsAvailable = request.getParameter("isAvailable");
        String strIsGradecenter = request.getParameter("isGradecenter");
        String strIsTracked = request.getParameter("isTracked");
        boolean isAvailable = strIsAvailable.equals("true")?true:false;
        boolean isGradecenter = strIsGradecenter.equals("true")?true:false;
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
        
        //if teacher turned Gradecenter option ON (and it was OFF previously) - create lineitem
        if (!bbContent.getIsDescribed() && isGradecenter) {
            LineitemUtil.createLineitem(ctx, bbContent);
            
        //if teacher turned Gradecenter option OFF (and it was ON previously) - remove lineitem
        } else if (bbContent.getIsDescribed() && !isGradecenter) {
            LineitemUtil.removeLineitem(contentIdStr, courseIdStr);
            
        //change existing lineitem's name if lesson name has been changed
        } else if (isGradecenter && !strTitle.equals(bbContent.getTitle())) {
            LineitemUtil.changeLineitemName(contentIdStr, courseIdStr, strTitle);
        }
    
        // Set LAMS content data in Blackboard
        bbContent.setTitle(strTitle);
        bbContent.setIsAvailable(isAvailable);
        bbContent.setIsDescribed(isGradecenter);//isDescribed field is used for storing isGradecenter parameter
        bbContent.setIsTracked(isTracked);
        bbContent.setBody(description);
    
        // Set Availability Dates
        // Clear the date (set to null) if the checkbox is unchecked
        // Start Date
        if (strStartDateCheckbox != null){
            if (strStartDateCheckbox.equals("1")){
                bbContent.setStartDate(startDate);
            } else {
                bbContent.setStartDate(null);
            }
        } else {
            bbContent.setStartDate(null);
        }
        // End Date
        if (strEndDateCheckbox != null){
            if (strEndDateCheckbox.equals("1")){
                bbContent.setEndDate(endDate);
            } else {
                bbContent.setEndDate(null);
            }
        } else {
            bbContent.setEndDate(null);
        }

        //Persist the Modified Lesson Object in Blackboard    
        ContentDbPersister persister= (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE );
        persister.persist( bbContent );
    
        String strReturnUrl = PlugInUtil.getEditableContentReturnURL(bbContent.getParentId(), courseId);
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