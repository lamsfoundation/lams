 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<title>LAMS</title>
	</head>
	
		<script language="JavaScript" type="text/JavaScript"><!--
				function redirectPage() {
					setTimeout("doRedirect()", 2000);
				}
				function doRedirect() {
					//top.frames['LDContent'].location.href = "<c:out value='${activity.activityURLs[0].url}' />";
					var parentWindow = window.parent;
					parentWindow.location.href = "Activity.do?method=display&activityId=<c:out value='${activityId}' />";
				}
				//window.onload = redirectPage;
				window.onload = doRedirect;
				//-->
			//-->
		</script>
		
</html:html>
