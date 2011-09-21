<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Step 2 For Creating a New LAMS Lesson
    Set the various attributes for the LAMS lesson in Blackboard

    Step 1 - create.jsp
    Step 2 - start_lesson.jsp
    Step 3 - start_lesson_proc.jsp
--%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="blackboard.base.FormattedText"%>
<%@ page import="blackboard.data.course.Course"%>
<%@ page import="blackboard.data.content.Content"%>
<%@ page import="blackboard.data.content.ContentFolder"%>
<%@ page import="blackboard.data.content.CourseDocument"%>
<%@ page import="blackboard.persist.Id"%>
<%@ page import="blackboard.persist.BbPersistenceManager"%>
<%@ page import="blackboard.persist.content.ContentDbPersister"%>
<%@ page import="blackboard.persist.content.ContentDbLoader"%>
<%@ page import="blackboard.platform.session.BbSessionManagerService"%>
<%@ page import="blackboard.platform.session.BbSession"%>
<%@ page import="blackboard.platform.*"%>
<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="blackboard.platform.plugin.PlugInException"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsPluginUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.Constants"%>         
<%@ page errorPage="/error.jsp"%>      
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%
    // SECURITY!
    // Authorise current user for Course Control Panel (automatic redirect)
    try{
        if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
            return;
    } catch(PlugInException e) {
        throw new RuntimeException(e);
    }
    
    // Get the sequence ID from the form request parameter
    String sequenceID = request.getParameter("sequence_id");
%>

<bbNG:genericPage title="Start A LAMS Lesson" ctxId="ctx">

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="COURSE" isContent="true">
        <bbNG:breadcrumb title="Start A LAMS Lesson" />
    </bbNG:breadcrumbBar>
    
    <%-- Page Header --%>
    <bbNG:pageHeader>    	
        <bbNG:pageTitleBar title="Start A LAMS Lesson"/>
    </bbNG:pageHeader>

    <%-- Form to Collect LAMS Lesson Attributes --%>
    <form name="lesson_form" id="lesson_form" action="start_lesson_proc.jsp" method="post" onSubmit="return confirmSubmit();">    	
        <input type="hidden" name="sequence_id" id="sequence_id" value="<%=sequenceID%>">
        <input type="hidden" name="content_id" value="<%=request.getParameter("content_id")%>">
    	<input type="hidden" name="course_id" value="<%=request.getParameter("course_id")%>">
   		
        <bbNG:dataCollection>
		
            <bbNG:step title="Name and describe the lesson">
                <bbNG:dataElement label="Name" isRequired="true" labelFor="title">
                    <input id="title" type="text" name="title" value="">
                </bbNG:dataElement>
                
                <bbNG:dataElement label="Description" labelFor="description">
                    <textarea name="description" rows="12" cols="35"></textarea>
                </bbNG:dataElement>
            </bbNG:step> 
                
            <bbNG:step title="Lesson options">
                <bbNG:dataElement label="Do you want to make this LAMS lesson visible?" labelFor="isAvailable">
                    <input type="Radio" name="isAvailable" value="true" checked>Yes 
                    <input type="Radio" name="isAvailable" value="false">No
                </bbNG:dataElement>
                <bbNG:dataElement label="Track number of views" labelFor="isTracked">
                    <input type="radio" name="isTracked" value="true">Yes
                    <input type="radio" name="isTracked" value="false" checked>No
                </bbNG:dataElement>
                <bbNG:dataElement label="Choose date restrictions">
                    <bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true"/>
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
            function confirmSubmit() {
                var title = rettrim(document.lesson_form.title.value);
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