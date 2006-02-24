<%@ page language="java" import="java.util.*" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
	  <lams:css/>
	<title>Start Lesson</title>
	</head>
  <body>
	<H2>Start Lesson</H2>
	
	<P>Before you can start a lesson, you will need to create a learning design 
	using the authoring client. When you save the learning design, note down the id of 
	design ( or look it up in the lams_learning_design table in the database). 
	Once that is done, you can start a lesson using the learning design id.</p>

	<P>Authoring does not support the entry of a title or description for a design yet, so all we are able to display is the list
	of learning design ids.</p>
	
	<P>Please select the organisation to which the lesson should be created. The lesson
	will be started with all the users that are members of that organisation.</P>

	<html:form action="/dummy" target="_self" method="POST">
	
	<table width="100%" border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">

		<tr><td class="formlabel">Learning Design</td>
		<td class="formcontrol">
			<select name="learningDesignId">
				<c:forEach items="${designs}" var="design">
					<option value="<c:out value="${design.learningDesignId}"/>"><c:out value="${design.title}"/>: <c:out value="${design.learningDesignId}"/>
					<c:if test="${design.title}">
						: <c:out value="${design.title}"/> (<c:out value="${design.description}"/>)</option>
					</c:if>
				</c:forEach>
			</select>
		</td></tr>

		<tr><td class="formlabel">Title</td>
		<td class="formcontrol"><html:text property="title" maxlength="80"  size="50"/></td></tr>

		<tr><td class="formlabel">Description</td>
		<td class="formcontrol"><html:text property="desc" maxlength="80"  size="50"/></td></tr>

		<tr><td class="formlabel">Organisation</td>
		<td class="formcontrol">
			<select name="organisationId">
				<c:forEach items="${organisations}" var="org">
					<option value="<c:out value="${org.organisationId}"/>"><c:out value="${org.name}"/>
						(<c:forEach items="${org.users}" var="user"  varStatus="status"><c:out value="${user.login}"/><c:if test="${not status.last}">,</c:if></c:forEach>)
					</option>
				</c:forEach>
			</select>
		</td></tr>

		<tr><td class="formlabel" colspan="2">
			<html:submit property="method" value="startLesson" styleClass="button" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')" >
				Start Lesson 
			</html:submit>
		</td></tr>
	</table>
	
	</html:form>
	
	</body>
</html>
