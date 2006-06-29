<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

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
		</script>
		<style type="text/css">
		<!--
		td { 
			padding:4px; 
			font-size:12px;
		}
		-->
		</style>
	</head>
	<body>
		<script type="text/javascript">
			success();
		</script>
		<table cellpadding="3">
			<!-- Basic Info Form-->
			<%@ include file="/common/messages.jsp"%>
			<html:form action="/authoring/createTopic.do" focus="message.subject" enctype="multipart/form-data" styleId="topicFormId">
				<tr>
					<td>
						<b><bean:message key="message.label.subject" />*</b><BR>
						<html:text size="30" tabindex="1" property="message.subject" /><BR>
						<html:errors property="message.subject" />
					</td>
				</tr>
				<tr>
					<td>
						<b><bean:message key="message.label.body" />*</b><BR>
						<FCK:editor id="message.body" basePath="/lams/fckeditor/" height="150" width="600">
						</FCK:editor>
						<html:errors property="message.body" />
					</td>
				</tr>
				<tr>
					<td>
						<b><bean:message key="message.label.attachment" /></b>
						<html:file tabindex="3" property="attachmentFile" />
					</td>
				</tr>
				<tr>
					<td align="center" valign="bottom">
						<a href="#" onclick="getElementById('topicFormId').submit();" class="button-add-item">
							<bean:message key="button.add" />
						</a>
						&nbsp; &nbsp;
						<a href="#" onclick="javascript:window.parent.hideMessage()" class="button">
							<bean:message key="button.cancel" />
						</a>
						<BR><BR>
					</td>
				</tr>
			</html:form>
		</table>

	</body>
</html>
