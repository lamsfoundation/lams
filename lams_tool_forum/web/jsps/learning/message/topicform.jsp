<c_rt:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<table cellpadding="0">
	<tr>
		<td class="field-name">
			<bean:message key="message.label.subject" />
			*
		</td>
		<td>
			<html:text size="30" tabindex="1" property="message.subject" />
			<br>
			<html:errors property="message.subject" />
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<bean:message key="message.label.body" />
			*
		</td>
		<td>
			<%@include file="bodyarea.jsp"%>
		</td>
	</tr>
	<c:if test="${allowUpload}">
		<tr>
			<td class="field-name">
				<bean:message key="message.label.attachment" />
			</td>
			<td>
				<html:file tabindex="3" property="attachmentFile" />
				<html:errors property="message.attachment" />
			</td>
		</tr>
	</c:if>
</table>

<div class="right-buttons">
	<html:submit styleClass="button">
		<bean:message key="button.submit" />
	</html:submit>
	<c:set var="backToForum">
		<html:rewrite page="/learning/viewForum.do?toolSessionID=${sessionScope.toolSessionID}" />
	</c:set>
	<html:button property="goback" onclick="javascript:location.href='${backToForum}';" styleClass="button">
		<bean:message key="button.cancel" />
	</html:button>
</div>

<div class="space-bottom"></div>
