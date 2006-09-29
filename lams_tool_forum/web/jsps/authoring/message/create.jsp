<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
	</head>
	<body>
		<div class="content">
		
		<table  width="100%">
			<!-- Basic Info Form-->
			<%@ include file="/common/messages.jsp"%>
			<html:form action="/authoring/createTopic.do" focus="message.subject" enctype="multipart/form-data" styleId="topicFormId">
				<html:hidden property="sessionMapID"/>
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
						<lams:FCKEditor id="message.body" value="" toolbarSet="Default-Learner"></lams:FCKEditor>
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
					<td align="center" valign="bottom"><p>
						<a href="#" onclick="getElementById('topicFormId').submit();" class="button-add-item">
							<bean:message key="button.add" />
						</a>
						&nbsp; &nbsp;
						<a href="#" onclick="javascript:window.parent.hideMessage()" class="button">
							<bean:message key="button.cancel" />
						</a>
						</p>
					</td>
				</tr>
			</html:form>
		</table>

		</div>
	</body>
</html>
