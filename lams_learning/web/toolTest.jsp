 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean-el" prefix="bean-el"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="org.lamsfoundation.lams.lesson.LearnerProgress"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<title>LAMS</title>
	</head>
	<body>
	
		<bean:parameter id="activityId" name="activityId" />
		<b><c:out value="${activityId}" /></b><br />
		<bean:parameter id="progressState" name="progressState" value="3" />
		<c:if test="${progressState == 1}">
			this activity has already been completed<br />
		</c:if>
		
		<!--a href="Activity.do?method=complete&activityId=<c:out value="${activityId}"/>">Finish</a-->
		<a href="toolTest.do?method=finish&activityId=<c:out value="${activityId}"/>">Finish</a>

	</body>
</html:html>
