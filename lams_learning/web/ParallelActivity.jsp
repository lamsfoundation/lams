 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<title>LAMS</title>
	</head>

	<script language="JavaScript" type="text/JavaScript"><!--
		// The sub-activities will need to call back to this on finish to 
		// redirect the big frame.
	//-->
	</script>	

	<%-- TODO: use type --%>
	<frameset cols="50%,*" border="" id="lamsDynamicFrameSet">
		<c:forEach items="${activity.activityURLs}" var="activityURL" varStatus="loop">
			<%--c:out value="${activityURL}" /--%>
			<frame src="<c:out value="${activityURL.url}" />" 
				name="TaskFrame<c:out value="${loop.index}" />"
				frameborder="" bordercolor="#E0E7EB"
				id="lamsDynamicFrame<c:out value="${loop.index}" />">
		</c:forEach>
	</frameset>

	<noframes>
		<body>
			Your browser does not handle frames!
		</body>
	</noframes>
	
</html:html>
