
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
	focus="login"  enctype="multipart/form-data">
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
		<tr>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.online.instruction" />:</td>
			<td class="formcontrol"><FCK:editor id="onlineInstruction"
				basePath="/lams/fckEditor/" height="150" width="85%">
			</FCK:editor></td>
		</tr>
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.online.file" />:
			</td>
			<td class="formcontrol">
				<html:file property="filename">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<html:submit property="submit">
					<fmt:message key="label.authoring.upload.online.button" />
				</html:submit>
			</td>
		</tr>	
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.offline.instruction" />:</td>
			<td class="formcontrol"><FCK:editor id="offlineInstruction"
				basePath="/lams/fckEditor/" height="150" width="85%">
			</FCK:editor></td>
		</tr>
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.offline.file" />:
			</td>
			<td class="formcontrol">
				<html:file property="filename">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<html:submit property="submit">
					<fmt:message key="label.authoring.upload.offline.button" />
				</html:submit>
			</td>
		</tr>			
		</tr>
	</table>
	</div>
	<div id="instruction">
	<h1><fmt:message key="label.authoring.heading.advance" /></h1>
	<h2><fmt:message key="label.authoring.heading.advance.desc" /></h2>
	<table class="forms">
		<!--hidden field contentID passed by flash-->
		<html:hidden property="toolContentID" value="${toolContentID}" />
		<tr>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td class="formcontrol">
				<html:radio property="lockOnFinished" value="1">
					<fmt:message key="label.authoring.advance.lock.on.finished" />
				</html:radio>
			</td>
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
			<td class="formcontrol">
			<html:submit property="submit">
				<fmt:message key="label.authoring.save.button" />
			</html:submit></td>
	</table>
</html:form>
</body>
</html:html>
