<%@ include file="/includes/taglibs.jsp"%>

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
					<c:set var="author" value="${topic.author}"/>
					<c:if test="${empty author}">
						<c:set var="author">
							<fmt:message key="label.default.user.name"/>
						</c:set>
					</c:if>
					${author}
				</td>
				<td>
					<c:out value="${topic.message.replyNumber}" />
				</td>
				<td>
					<lams:Date value="${topic.message.updated}"/>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
