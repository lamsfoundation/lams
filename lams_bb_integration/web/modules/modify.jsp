<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Step 1 For Modifing an existing LAMS Lesson
    Set the various attributes for the LAMS lesson in Blackboard

    Step 1 - modify.jsp
    Step 2 - modify_proc.jsp
--%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="blackboard.data.*"%>
<%@ page import="blackboard.persist.*"%>
<%@ page import="blackboard.data.course.*"%>
<%@ page import="blackboard.data.user.*"%>
<%@ page import="blackboard.persist.course.*"%>
<%@ page import="blackboard.data.content.*"%>
<%@ page import="blackboard.persist.content.*"%>
<%@ page import="blackboard.db.*"%>
<%@ page import="blackboard.base.*"%>
<%@ page import="blackboard.platform.*"%>
<%@ page import="blackboard.platform.plugin.*"%>
<%@ page errorPage="/error.jsp"%>  
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:genericPage title="Modify A LAMS Lesson" ctxId="ctx">

<%
    String NOT_AVAILABLE = "<i>Item is not available.</i><br>";

    // Authorise current user for Course Control Panel (automatic redirect)
    try{
        if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
            return;
    } catch(PlugInException e) {
        throw new RuntimeException(e);
    }

 	// Get the Course Document (Lams Lesson)
    BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
    Container bbContainer = bbPm.getContainer();
    Id contentId = new PkId( bbContainer, CourseDocument.DATA_TYPE, request.getParameter("content_id") );
    ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );
    Content bbContent = (Content)courseDocumentLoader.loadById( contentId );

    // Get the Item Attributes
    Calendar startDate = bbContent.getStartDate();
    Calendar endDate = bbContent.getEndDate();	
    FormattedText desc = bbContent.getBody();
    String description = desc.getText().replaceFirst(NOT_AVAILABLE, ""); //remove the NOT_AVAILABLE substring

%>

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="CTRL_PANEL" isContent="true">
        <bbNG:breadcrumb title="Modify A LAMS Lesson" />
    </bbNG:breadcrumbBar>
    
    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="Modify A LAMS Lesson"/>
    </bbNG:pageHeader>
    
    <%-- Form for the LAMS Lesson Attributes --%>
    <form name="lams_form" id="lams_form" action="modify_proc.jsp" method="post" onSubmit="return validateModify();">
        <input type="hidden" name="content_id" value="<%=request.getParameter("content_id")%>">
        <input type="hidden" name="course_id" value="<%=request.getParameter("course_id")%>">
  		
        <bbNG:dataCollection>
	
            <bbNG:step title="Name and describe the lesson">
            
                <bbNG:dataElement label="Name" isRequired="true" labelFor="title">
                    <input id="title" type="text" name="title" value="<%=bbContent.getTitle()%>">
                </bbNG:dataElement>
        
                <bbNG:dataElement label="Description" labelFor="description">
                    <textarea name="description" rows="12" cols="35"><%=description%></textarea>
                </bbNG:dataElement>
                
            </bbNG:step> 
            
            <bbNG:step title="Lesson options">
            
                <bbNG:dataElement label="Do you want to make LAMS visible?">
                    <input type="Radio" name="isAvailable" value="true" <%=(bbContent.getIsAvailable())?"checked":""%>>Yes
                    <input type="Radio" name="isAvailable" value="false" <%=(bbContent.getIsAvailable())?"":"checked"%>>No
                </bbNG:dataElement>
                
                <bbNG:dataElement label="Do you want to add a mark/completion column in Gradecenter?" labelFor="isGradecenter">
                    <input type="Radio" name="isGradecenter" value="true" <%=(bbContent.getIsDescribed())?"checked":""%>>Yes
                    <input type="Radio" name="isGradecenter" value="false" <%=(bbContent.getIsDescribed())?"":"checked"%>>No
                </bbNG:dataElement>                
                
                <bbNG:dataElement label="Track number of views">
                    <input type="radio" name="isTracked" value="true" <%=(bbContent.getIsTracked())?"checked":""%>>Yes
                    <input type="radio" name="isTracked" value="false" <%=(bbContent.getIsTracked())?"":"checked"%>>No
                </bbNG:dataElement>
                
                <bbNG:dataElement label="Choose date restrictions">
                    <%--
                        Show start and end dates if they have been set
                        If non ehave been set, leave the tags out so that Blackboard puts the default dates in
                    --%>
                    <% if(startDate==null && endDate==null) { %>
                    	<bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true" />
                    <% } else if(endDate==null) { %>
                    	<bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true" startDateTime="<%=startDate%>" />
                    <% } else if(startDate==null) { %>
                    	<bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true" endDateTime="<%=endDate%>" />
                    <% } else { %>
                    	<bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true" startDateTime="<%=startDate%>" endDateTime="<%=endDate%>" />
                    <% } %>
                </bbNG:dataElement>   
                
            </bbNG:step> 
            
            <bbNG:stepSubmit title="Start Lesson" cancelOnClick="back();" />

        </bbNG:dataCollection>
    </form>


    <bbNG:jsBlock>
        <script language="JavaScript" type="text/javascript">
        <!--
        
            // Go back one page if the user clicks the Cancel Button
            function back() {
                history.go(-1);
            }
            
            // Do form vaildation
            // Check that a title has been supplied 
            function validateModify() {
                var title = rettrim(document.lams_form.title.value);
                if ((title == "")||(title == null)) {
                    alert("The title is empty. Please enter a title for the LAMS sequence.");
                    return false;
                }        
            }
            
            // Utility function to trim
            function rettrim(stringToTrim) {
                return stringToTrim.replace(/^\s+|\s+$/g,"");
            }

        //-->
        </script>
    </bbNG:jsBlock>
    
</bbNG:genericPage>

