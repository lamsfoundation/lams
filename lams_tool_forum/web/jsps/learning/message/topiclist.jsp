<%@ include file="/includes/taglibs.jsp"%>

<table cellpadding="0" class="alternative-color" cellspacing="0">
	<tbody>
		<tr>
			<th width="">
				<fmt:message key="lable.topic.title.subject" />
			</th>
			<th width="100px">
				<fmt:message key="lable.topic.title.startedby" />
			</th>
			<th width="60px">
				<fmt:message key="lable.topic.title.replies" />
			</th>
			<th width="25%">
				<fmt:message key="lable.topic.title.update" />
			</th>
		</tr>
		<c:forEach items="${topicList}" var="topic">
			<tr>
				<td>
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
				<td>
					<c:set var="author" value="${topic.author}"/>
					<c:if test="${empty author}">
						<c:set var="author">
							<fmt:message key="label.default.user.name"/>
						</c:set>
					</c:if>
					${author}
				</td>
				<td  align="center">
					<c:out value="${topic.message.replyNumber}" />
				</td>
				<td>
					<lams:Date value="${topic.message.updated}"/>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
