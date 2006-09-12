<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
	</head>
	<body>
		<html:form action="/learning/saveOrUpdateItem" method="post" styleId="surveyItemForm" enctype="multipart/form-data">
			<html:hidden property="itemType" styleId="itemType" value="2" />
			<html:hidden property="mode"/>
			<html:hidden property="sessionMapID"/>
			<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="2">
						<h2>
							<fmt:message key="label.learning.new.file" />
						</h2>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<%@ include file="/common/messages.jsp"%>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fmt:message key="label.authoring.basic.survey.title.input" />
						<BR>
						<html:text property="title" size="40" tabindex="1" />
						<BR>
						<BR>
						<fmt:message key="label.authoring.basic.survey.file.input" />
						<BR>
						<input type="file" name="file" size="25" />
					</td>
					<td valign="top">
						<fmt:message key="label.learning.comment.or.instruction" />
						<BR>
						<lams:STRUTS-textarea rows="5" cols="25" tabindex="2" property="description" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center" valign="bottom">
						<a href="#" onclick="document.getElementById('surveyItemForm').submit()" class="button"> <fmt:message key="button.upload" /> </a>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>
