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
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsServiceUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsPluginUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.Constants"%>
            
<%@ page errorPage="/error.jsp"%><%@ page import="org.lamsfoundation.ld.integration.Constants" %>      

<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>

<%
	String courseId = request.getParameter("course_id");
	String contentId = request.getParameter("content_id");
	String designId = request.getParameter("itemSequenceId");
	String sequenceID = request.getParameter("sequence_id");
%>

<bbData:context id="ctx">
	<bbUI:docTemplate title = "Start a LAMS lesson." >

	<form name="lesson_form" id="lesson_form" action="start_lesson.jsp" method="post">
		<bbUI:step title="Name and describe the lesson">
			<bbUI:dataElement label="Name">
	            <input id="title" type="text" name="title" value="">
	        </bbUI:dataElement>
	    
	        <bbUI:dataElement label="Description">
	            <bbUI:textbox name="description" rows="12" cols="55"></bbUI:textbox>
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
	    <bbUI:stepSubmit title="Start lesson"/>
	   </form>
	</bbUI:docTemplate>
</bbData:context>