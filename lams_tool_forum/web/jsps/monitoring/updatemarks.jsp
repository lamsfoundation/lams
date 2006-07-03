<%@ include file="/includes/taglibs.jsp"%>

<html:form action="/monitoring/updateMark" method="post">
	<c:set var="formBean" value="<%= session.getAttribute("markForm") %>" />

	<table cellpadding="0">
		<tr>
			<td colspan="5">
				<fmt:message key="message.assign.mark" />
				<c:out value="${formBean.user.loginName}" />
				,
				<c:out value="${formBean.user.firstName}" />
				<c:out value="${formBean.user.lastName}" />
			</td>
		</tr>

		<tr>
			<td>
				<c:set var="viewtopic">
					<html:rewrite page="/monitoring/viewTopic.do?messageID=${formBean.messageDto.message.uid}&create=${formBean.messageDto.message.created.time}" />
				</c:set>
				<html:link href="javascript:launchPopup('${viewtopic}','viewtopic')">
					<c:out value="${formBean.messageDto.message.subject}" />
				</html:link>
			</td>
			<td>
				<c:if test="${formBean.messageDto.hasAttachment}">
					<img src="<html:rewrite page="/images/paperclip.gif"/>">
				</c:if>
			</td>
			<td>
				<c:out value="${formBean.messageDto.author}" />
			</td>
			<td>
				<c:out value="${formBean.messageDto.message.replyNumber}" />
			</td>
			<td>
				<fmt:formatDate value="${formBean.messageDto.message.updated}" type="time" timeStyle="short" />
				<fmt:formatDate value="${formBean.messageDto.message.updated}" type="date" dateStyle="full" />
			</td>
		</tr>
	</table>
	<table>
		<input type="hidden" name="toolSessionID" value="<c:out value='${formBean.sessionId}'/>" />
		<input type="hidden" name="messageID" value="<c:out value='${formBean.messageDto.message.uid}'/>" />
		<input type="hidden" name="userID" value="<c:out value='${formBean.user.uid}'/>" />
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="lable.topic.title.mark" />
				* :
			</td>
			<td>
				<input type="text" name="mark" value="<c:out value='${formBean.mark}'/>" />
			</td>
			<td>
				<html:errors property="report.mark" />
			</td>
		</tr>

		<tr>
			<td colspan="3">
				<lams:SetEditor id="comment" small="true" text="${formBean.comment}" key="lable.topic.title.comment" />
			</td>
		</tr>

		<tr>
			<td colspan="3">
				<html:errors property="report.comment" />
			</td>
		</tr>

		<tr>
			<td class="field-name" colspan="3">
				<html:errors property="report.globel" />
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<input type="submit" value="<fmt:message key="lable.update.mark"/>" class="button" />
			</td>
		</tr>
	</table>
</html:form>

<lams:HTMLEditor />
