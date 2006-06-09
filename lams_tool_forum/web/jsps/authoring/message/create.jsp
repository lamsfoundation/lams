<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<script type="text/javascript">
			function success(){
				var flag = "<c:out value="${SUCCESS_FLAG}"/>";
				if(flag == "CREATE_SUCCESS"){
					var d = new Date()
					var t = d.getTime()
					loadDoc("<html:rewrite page='/authoring/refreshTopic.do'/>"+"?reqID="+escape(t),window.parent.document.getElementById("messageListArea"));
					window.parent.hideMessage();
				}
			}
			success();
		</script>

	</head>
	<body class="tabpart">
		<script type="text/javascript">
			success();
		</script>
		<table class="forms">
			<!-- Basic Info Form-->
			<tr>
				<td>
					<div align="center">
						<%@ include file="/common/messages.jsp"%>
						<html:form action="/authoring/createTopic.do" focus="message.subject" enctype="multipart/form-data">
							<table width="100%" cellspacing="8" border="0" align="left">
								<tr>
									<td>
										<b><bean:message key="message.label.subject" /><b class="required">*</b></b>
									</td>
									<td align="left">
										<html:text size="30" tabindex="1" property="message.subject" />
										<BR>
										<html:errors property="message.subject" />
									</td>
								</tr>
								<tr>
									<td>
										<b><bean:message key="message.label.body" /></b><b class="required">*</b>
									</td>
									<td>
										<FCK:editor id="message.body" basePath="/lams/fckeditor/" height="200" width="100%">
										</FCK:editor>
										<BR>
										<html:errors property="message.body" />
									</td>
								</tr>
								<tr>
									<td>
										<b><bean:message key="message.label.attachment" /></b>
									</td>
									<td>
										<html:file tabindex="3" property="attachmentFile" />
										<html:errors property="message.attachment" />
									</td>
								</tr>
								<tr>
									<td></td>
									<td align="left" valign="bottom">
										<html:submit style="width:120px" styleClass="buttonStyle">
											<bean:message key="button.add" />
										</html:submit>
										<html:button property="cancel" onclick="javascript:window.parent.hideMessage()" styleClass="buttonStyle" style="width:120px">
											<bean:message key="button.cancel" />
										</html:button>
									</td>
								</tr>
							</table>

						</html:form>
					</div>
				</td>
			</tr>
		</table>

	</body>
</html>
