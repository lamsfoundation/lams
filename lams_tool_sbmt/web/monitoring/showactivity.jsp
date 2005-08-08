<%@include file="../sharing/share.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<title>Submit Files</title>
	<html:base />
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
    <link href="<%=LAMS_WEB_ROOT%>/css/aqua.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
</head>

<body>
	<html:form action="monitoring" method="post" >
		<div id="basic">
		<h1><fmt:message key="label.authoring.heading.basic" /></h1>
		<h1><fmt:message key="label.authoring.heading.basic.desc" /></h1>
		<table class="forms">
			<!--hidden field contentID passed by flash-->
			<input type="hidden" name="toolContentID" value="<c:out value='${authoring.contentID}'/>"/>
			<input type="hidden" name="method" value="editActivity"/>
			<!-- Title Row -->
			<tr>
				<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
				<td class="formcontrol">
					<c:out value="${authoring.title}" escapeXml="false"/>
				</td>
			</tr>
			<!-- Instructions Row -->
			<tr>
				<td class="formlabel"><fmt:message key="label.authoring.basic.instruction" />:</td>
				<td class="formcontrol">
					<c:out value="${authoring.instruction}" escapeXml="false"/>
				</td>
			</tr>
			<tr><td colspan="2">
				<html:submit>
					<fmt:message key="label.monitoring.edit.activity.edit"/>
				</html:submit>
			</td></tr>
		</table>
		</div>
	</html:form>
</body>
</html:html>