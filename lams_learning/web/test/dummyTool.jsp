 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean-el" prefix="bean-el"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<title>LAMS</title>
	</head>
	<body>
	
		<bean:parameter id="toolSessionId" name="toolSessionId" />
		<b><c:out value="${toolSessionId}" /></b><br />
		
		<a href="DummyTool.do?method=finish&toolSessionId=<c:out value="${toolSessionId}"/>">Finish</a>

	</body>
</html:html>
