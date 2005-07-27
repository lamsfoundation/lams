
<%@ page language="java"%>

<%@ taglib uri="fck-editor" prefix="FCK"%>
<%@ taglib uri="tags-html-el" prefix="html"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
<html:base />
<title>Submit Files</title>
<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
<link href="../css/aqua.css" rel="stylesheet" type="text/css">

</head>

<body>
<html:form action="authoring?method=updateContent" method="post"
	focus="login">
	<div id="basic">
	<h1><fmt:message key="label.authoring.heading.basic" /></h1>
	<h1><fmt:message key="label.authoring.heading.basic.desc" /></h1>
	<table class="forms">
		<!--hidden field contentID passed by flash-->
		<html:hidden property="toolContentID" value="${toolContentID}" />
		<!-- Title Row -->
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
			<td class="formcontrol"><html:text property="title" /></td>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.basic.instruction" />:</td>
			<td class="formcontrol"><FCK:editor id="instructions"
				basePath="/lams/fckEditor/" height="150" width="85%">
			</FCK:editor></td>
		</tr>
	</table>
	</div>
	<div id="instruction">
	<h1><fmt:message key="label.authoring.heading.instructions" /></h1>
	<h2><fmt:message key="label.authoring.heading.instructions.desc" /></h2>
	<table class="forms">
		<!--hidden field contentID passed by flash-->
		<html:hidden property="toolContentID" value="${toolContentID}" />
		<!-- Title Row -->
		<tr>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.online.instruction" />:</td>
			<td class="formcontrol"><FCK:editor id="online_instruction"
				basePath="/lams/fckEditor/" height="150" width="85%">
			</FCK:editor></td>
		</tr>
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.offline.instruction" />:</td>
			<td class="formcontrol"><FCK:editor id="offline_instruction"
				basePath="/lams/fckEditor/" height="150" width="85%">
			</FCK:editor></td>
		</tr>
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.uploadfile" />:</td>
			<td class="formcontrol">
				<html:file property="title" />
				<input name="filename" type="file" value="choose file">
				<input name="upload" type="button" value="upload">
			</td>
		</tr>			
		</tr>
	</table>
	</div>
	<table class="forms">
		<!-- Button Row -->
		<tr>
			<td class="formcontrol"><html:button property="cancel"
				onclick="window.close()">
				<fmt:message key="label.authoring.cancel.button" />
			</html:button></td>
			<td class="formcontrol"><html:submit property="save">
				<fmt:message key="label.authoring.save.button" />
			</html:submit></td>
	</table>
</html:form>
</body>
</html:html>
