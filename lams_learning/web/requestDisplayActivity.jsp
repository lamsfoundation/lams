 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<title>LAMS</title>
	</head>
	
		<c:if test="${empty url}">
			<bean:parameter id="url" name="url" />
		</c:if>
	
		<script language="JavaScript" type="text/JavaScript"><!--
				function doRedirect() {
					var parentWindow = window.parent;
					//parentWindow.location.href = "Activity.do?method=display&activityId=<c:out value='${activityId}' />";
					parentWindow.location.href = "<c:out value='${url}' escapeXml='false' />";
				}
				window.onload = doRedirect;
				//-->
			//-->
		</script>
		
</html:html>
