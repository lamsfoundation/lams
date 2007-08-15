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
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsPluginUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.Constants"%>
            
<%@ page errorPage="/error.jsp"%><%@ page import="org.lamsfoundation.ld.integration.Constants" %>      

<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>

<%
	String sequenceID = request.getParameter("sequence_id");
%>

<bbData:context id="ctx">
	<bbUI:docTemplate title = "Start a LAMS lesson." >

	<form name="lesson_form" id="lesson_form" action="start_lesson_proc.jsp" method="post">
		<input type="hidden" name="sequence_id" id="sequence_id" value="<%=sequenceID%>">
		<input type="hidden" name="content_id" value="<%=request.getParameter("content_id")%>">
    	<input type="hidden" name="course_id" value="<%=request.getParameter("course_id")%>">
   		
		
		<bbUI:step title="Name and describe the lesson">
			<bbUI:dataElement label="Name">
	            <input id="title" type="text" name="title" value="">
	        </bbUI:dataElement>
	    
	        <bbUI:dataElement label="Description">
				<textarea name="description" rows="12" cols="35"></textarea>
	        </bbUI:dataElement>
	    </bbUI:step> 
	        
	   	<bbUI:step title="Lesson options">
	        <bbUI:dataElement label="Do you want to make LAMS visible?">
	            <input type="Radio" name="isAvailable" value="true" checked>Yes 
	            <input type="Radio" name="isAvailable" value="false">No
	        </bbUI:dataElement>
	        <bbUI:dataElement label="Track number of views">
	            <input type="radio" name="isTracked" value="true">Yes
	            <input type="radio" name="isTracked" value="false" checked>No
	        </bbUI:dataElement>
	        <bbUI:dataElement label="Choose date restrictions">
	            <bbUI:dateAvailability formName="lesson_form" startDateField="startDate" endDateField="endDate"/>
	        </bbUI:dataElement>
	    </bbUI:step> 
	    <bbUI:step title="Start lesson">
	    	<br>
	    	<bbUI:dataElement> 
	    		<input type="submit" name="start" onClick="validateStartLesson();" value="Start Lesson">
	    		<input type="button" name="cancel" onClick="back();" value="Cancel">
	    	</bbUI:dataElement> 
	    </bbUI:step>
	   </form>
	   
	   <script language="JavaScript" type="text/javascript">
		<!--
			function back()
			{
				history.go(-1);
			}
			
			function validateStartLesson()
			{
				var title = trim(document.getElementById("title").value);
				
				// valdation
				if (title==null||title=="")
				{
					alert("Lesson must have a name.");
					return false;
				}

			}
			
			
			
			
		//-->
		</script>
	</bbUI:docTemplate>
</bbData:context>