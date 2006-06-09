<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
	</head>
	<body class="tabpart">
		<table class="forms">
			<!-- Basic Info Form-->
			<tr>
				<td>
					<div align="center">
						<%@ include file="/common/messages.jsp"%>
						<html:form action="/authoring/updateTopic.do" focus="message.subject" enctype="multipart/form-data">
							<fieldset>
								<input type="hidden" name="topicIndex" value="<c:out value="${topicIndex}"/>">
								<%@ include file="/jsps/message/topiceditform.jsp"%>
							</fieldset>
						</html:form>
					</div>
				</td>
			</tr>
		</table>

	</body>
</html>
