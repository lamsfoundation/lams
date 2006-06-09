<%@ include file="/common/taglibs.jsp"%>

<b> <c:out value="${user.loginName}" /> , <c:out value="${user.firstName}" /> <c:out value="${user.lastName}" /> <fmt:message key="monitoring.user.post.topic" /> </b>
</p>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<c:if test="${empty topicList}">
		<tr>
			<td>
				<div align="center">
					<b><fmt:message key="message.not.avaliable" /></b>
				</div>
			</td>
		</tr>
	</c:if>
	<c:forEach items="${topicList}" var="topic">
		<tr>
			<td valign="MIDDLE" width="48%">
				<c:set var="viewtopic">
					<html:rewrite page="/monitoring/viewTopic.do?messageID=${topic.message.uid}&create=${topic.message.created.time}" />
				</c:set>
				<html:link href="javascript:launchPopup('${viewtopic}','viewtopic');">
					<c:out value="${topic.message.subject}" />
				</html:link>
			</td>
			<td width="2%">
				<c:if test="${topic.hasAttachment}">
					<img src="<html:rewrite page="/images/paperclip.gif"/>">
				</c:if>
			</td>
			<td>
				<c:out value="${topic.author}" />
			</td>
			<td>
				<c:out value="${topic.message.replyNumber}" />
			</td>
			<td>
				<fmt:formatDate value="${topic.message.updated}" type="time" timeStyle="short" />
				<fmt:formatDate value="${topic.message.updated}" type="date" dateStyle="full" />
			</td>
		</tr>
		<tr>
		<tr>
			<td>
				<fmt:message key="lable.topic.title.mark" />
				:
			</td>
			<td colspan="3">
				<c:choose>
					<c:when test="${empty topic.message.report.mark}">
						<c:out value="Not Available" />
					</c:when>
					<c:otherwise>
						<c:out value="${topic.message.report.mark}" escapeXml="false" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="lable.topic.title.comment" />
				:
			</td>
			<td colspan="3">
				<c:choose>
					<c:when test="${empty topic.message.report.comment}">
						<fmt:message key="message.not.avaliable" />
					</c:when>
					<c:otherwise>
						<c:out value="${topic.message.report.comment}" escapeXml="false" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br>
				<html:form action="/monitoring/editMark" method="post">
					<input type="hidden" name="messageID" value=<c:out value='${topic.message.uid}' />>
					<input type="hidden" name="toolSessionID" value=<c:out value='${toolSessionID}' />>
					<input type="hidden" name="userID" value=<c:out value='${user.uid}' />>
					<input type="submit" value="<fmt:message key="lable.update.mark"/>" style="width:120px" class="buttonStyle" />
				</html:form>
			</td>
		</tr>
	</c:forEach>
</table>

