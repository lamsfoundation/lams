
<table cellpadding="0" class="alternative-color">
	<tbody>
		<tr>
			<th width="250">
				<fmt:message key="lable.topic.title.subject" />
			</th>
			<th width="90">
				<fmt:message key="lable.topic.title.startedby" />
			</th>
			<th width="70">
				<fmt:message key="lable.topic.title.replies" />
			</th>
			<th>
				<fmt:message key="lable.topic.title.update" />
			</th>
		</tr>
		<c:forEach items="${topicList}" var="topic">
			<tr>
				<td class="first">
					<c:set var="viewtopic">
						<html:rewrite page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${topic.message.uid}&create=${topic.message.created.time}" />
					</c:set>
					<html:link href="${viewtopic}">
						<c:out value="${topic.message.subject}" />
					</html:link>
					<c:if test="${topic.hasAttachment}">
						<img src="<html:rewrite page="/images/paperclip.gif"/>">
					</c:if>
				</td>
				<td class="first">
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
		</c:forEach>
	</tbody>
</table>
