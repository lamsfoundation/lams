<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>

	</head>
	<body>
	<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
	<tr><td>
		<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
			<tr>
				<th colspan="2">
					<h2>
						<fmt:message key="label.learning.new.file" />
					</h2>	
				</th>
			</tr>
		</table>
	</td></tr>
	<tr><td>
		<table width="400px" border="0" align="left" cellpadding="5" cellspacing="5">

			<!-- Basic Info Form-->
			<tr>
				<td>
					<%@ include file="/common/messages.jsp"%>
					<html:form action="/learning/saveOrUpdateItem" method="post" styleId="resourceItemForm" enctype="multipart/form-data">
						<input type="hidden" name="itemType" id="itemType" value="2"/>
						<table class="innerforms">
							<tr>
								<td valign="top">
									<fmt:message key="label.authoring.basic.resource.title.input" /><BR>
									<html:text property="title" size="40" tabindex="1" />
								</td>
								<td valign="top">
									<fmt:message key="label.learning.comment.or.instruction" /><BR>
									<lams:STRUTS-textarea rows="5" cols="30" tabindex="2" property="description" />
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="label.authoring.basic.resource.file.input" />
									<input type="file" name="file" size="25"  /> 
								</td>
								<td>
									<input type="submit" name="add" value="<fmt:message key="button.upload"/>" class="buttonStyle">
								</td>
							</tr>
						</table>
					</html:form>
				</td>
			</tr>
		</table>
	</td></tr>
	</table>
	</body>
</html>
