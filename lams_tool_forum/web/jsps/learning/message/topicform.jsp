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
	<tr>
		<td class="field-name">
			<bean:message key="message.label.attachment" />
		</td>
		<td>
			<html:file tabindex="3" property="attachmentFile" />
			<html:errors property="message.attachment" />
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td align="left">
			<BR>
			<html:submit styleClass="buttonStyle" style="width:120px">
				<bean:message key="button.submit" />
			</html:submit>
			<c:set var="backToForum">
				<html:rewrite page="/learning/viewForum.do?toolSessionID=${sessionScope.toolSessionID}" />
			</c:set>
			<html:button property="goback" onclick="javascript:location.href='${backToForum}';" styleClass="buttonStyle" style="width:120px">
				<bean:message key="button.cancel" />
			</html:button>
		</td>
	</tr>
</table>
