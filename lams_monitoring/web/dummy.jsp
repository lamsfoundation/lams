<%@ page language="java" import="java.util.*" %>
<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/jstl/x.tld" prefix="x" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>Lesson Management</title>
  </head>
  
  <body>
    <H1>Lesson Management</H1>
    
    <p>This is a dummy page for Lesson Management. It is to be used until the 
    Flash interface is ready. It pull the urls from the database so if the 
    urls are invalid, you need to check the tool deployment scripts.</p>

	<H2>Start Lesson</H2>
	
	<P>Before you can start a lesson, you will need to create a learning design.
	At the moment, the only way to do this is to go into the authoring client
	and save a learning design containing your tool. Then get the tool_content_id
	from the learning design activity record and run your authoring screen 
	to create the tool content. Once that is done, you can start a lesson
	using the learning design id.
	
	<html:form action="/dummy" target="_self" method="POST">
	
	<table width="100%" border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">
		<tr><td class="formlabel">learningDesignId</td>
		<td class="formcontrol"><html:text property="learningDesignId" maxlength="4"  size="4"/></td></tr>
		<tr><td class="formlabel">title</td>
		<td class="formcontrol"><html:text property="title" maxlength="80"  size="80"/></td></tr>
		<tr><td class="formlabel">desc</td>
		<td class="formcontrol"><html:text property="desc" maxlength="80"  size="80"/></td></tr>
		<tr><td class="formlabel" colspan="2">
			<html:submit property="method" value="startLesson" styleClass="button" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')" >
				Start Lesson 
			</html:submit>
		</td></tr>
	</table>
	
	</html:form>
	
	<H2>Monitor Lessons</H2>
	<TABLE border="1">
		<TR>
			<TD>lessonId</TD>
			<TD>lessonName</TD>
			<TD>lessonDescription</TD>
			<TD>createDateTime</TD>
			<TD>startDateTime</TD>
			<TD>endDateTime</TD>
			<TD>lessonStateId</TD>
		</TR>
	<c:forEach var="lesson" items="${lessons}">
		<TR>
			<TD><c:out value="${lesson.lessonId}"/></TD>
			<TD><c:out value="${lesson.lessonName}"/></TD>
			<TD><c:out value="${lesson.lessonDescription}"/></TD>
			<TD><c:out value="${lesson.createDateTime}"/></TD>
			<TD><c:out value="${lesson.startDateTime}"/></TD>
			<TD><c:out value="${lesson.endDateTime}"/></TD>
			<TD><c:out value="${lesson.lessonStateId}"/></TD>
			<TD><A HREF='/lams/learning/learner.do?method=joinLesson&userId=<lams:user property="userID"/>&lessonId=<c:out value="${lesson.lessonId}"/>'>Join Lesson as <lams:user property="firstName"/></a>
		</TR>
	</c:forEach>
	</TABLE>
	</body>
</html>
