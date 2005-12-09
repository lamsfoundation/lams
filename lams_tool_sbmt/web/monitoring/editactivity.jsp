<%@include file="../sharing/share.jsp" %>
<%@ taglib uri="fck-editor" prefix="FCK"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
<html:base />
<title>Submit Files</title>
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
    <link href="<%=LAMS_WEB_ROOT%>/css/aqua.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=LAMS_WEB_ROOT%>/includes/javascript/common.js"></script>

	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
</head>

<body>
    <div>
    <%@ include file="tabmenu.jsp"%>
    </div>
	<html:form action="monitoring" method="post" >
		<html:errors/>
		<div id="basic">
		<h1><fmt:message key="label.authoring.heading.basic" /></h1>
		<h1><fmt:message key="label.authoring.heading.basic.desc" /></h1>
		<table class="forms">
			<!--hidden field contentID passed by flash-->
			<input type="hidden" name="toolContentID" value="<c:out value='${authoring.contentID}'/>"/>
			<input type="hidden" name="method" value="updateActivity"/>
			<!-- Title Row -->
			<tr>
				<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
				<td class="formcontrol">
					<input type="text" name="title" value="<c:out value='${authoring.title}' escapeXml='false'/>" />
				</td>
			</tr>
			<!-- Instructions Row -->
			<tr>
				<td class="formlabel"><fmt:message key="label.authoring.basic.instruction" />:</td>
				<td class="formcontrol">
					<FCK:editor id="instructions"
						basePath="/lams/fckeditor/" height="150" width="85%">
						<c:out value="${authoring.instruction}" escapeXml="false"/>
					</FCK:editor>
				</td>
			</tr>
			<tr><td colspan="2">
				<html:cancel>
					<fmt:message key="label.monitoring.edit.activity.cancel"/>
				</html:cancel>
				<html:submit>
					<fmt:message key="label.monitoring.edit.activity.update"/>
				</html:submit>
			</td></tr>
		</table>
		</div>
	</html:form>
</body>
</html:html>