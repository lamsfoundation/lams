<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<%-- includeBodyTag, this variable is used to stop the default behaviour of
	displaying body tags (for use with a frameset) --%>
	<tiles:useAttribute name="includeBodyTag" ignore="true" />

	<head>
		<title>LAMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="css/learner.css" rel="stylesheet" type="text/css">
	</head>

	<c:if test="${includeBodyTag}">
		<body>
	</c:if>

			<tiles:insert attribute="body" />

	<c:if test="${includeBodyTag}">
		</body>
	</c:if>
	
</html:html>