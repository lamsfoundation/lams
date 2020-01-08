<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="showErrorStack"><lams:Configuration key='<%= ConfigurationKeys.ERROR_STACK_TRACE %>'/></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<%-- Catch JSP Servlet Exception --%>
<%-- The javascript method checkForErrorScreen in error.js is coded to match this page exactly.
---- If you change this page, please change the javascript. --%>
<%
if ( Configuration.getAsBoolean(ConfigurationKeys.ERROR_STACK_TRACE)  ) {
if (exception != null ) {
%>
<c:set var="errorMessage">
	<%=exception.getMessage()%>
</c:set>
<c:set var="errorName">
	<%=exception.getClass().getName()%>
</c:set>
<%
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		java.io.PrintStream os = new java.io.PrintStream(bos);
		exception.printStackTrace(os);
		String stack = new String(bos.toByteArray());
%>
<c:set var="errorStack">
	<%=stack%>
</c:set>
<%
} else if ((Exception) request.getAttribute("javax.servlet.error.exception") != null) {
%>

<c:set var="errorMessage">
	<%=((Exception) request.getAttribute("javax.servlet.error.exception")).getMessage()%>
</c:set>
<c:set var="errorName">
	<%=((Exception) request.getAttribute("javax.servlet.error.exception")).getMessage()
									.getClass().getName()%>
</c:set>
<%
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		java.io.PrintStream os = new java.io.PrintStream(bos);
		((Exception) request.getAttribute("javax.servlet.error.exception")).printStackTrace(os);
		String stack = new String(bos.toByteArray());
%>
<c:set var="errorStack">
	<%=stack%>
</c:set>
<%
}
}
%>
<body>
<form action="${lams}errorpages/error.jsp" method="post" id="errorForm">
	<c:if test="${showErrorStack}">
	<input type="hidden" name="errorName" value="<c:out value='${errorName}' />"/>
	<input type="hidden" name="errorMessage" value="<c:out value='${errorMessage}' />"/>
	<input type="hidden" name="errorStack" value="<c:out value='${errorStack}' />"/>
	</c:if>
</form>

<script type="text/javascript">
if(window.top != null)  {
	document.getElementById("errorForm").target = "_parent";
}
document.getElementById("errorForm").submit();

</script>
</body>
</lams:html>
