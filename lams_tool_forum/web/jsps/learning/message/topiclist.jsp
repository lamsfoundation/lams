<%@ include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-28 --%>

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
			<th width="60px">
				<fmt:message key="lable.topic.title.repliesnew" />
			</th>
			<th width="25%">
				<fmt:message key="lable.topic.title.update" />
			</th>
			<c:if test="${not sessionMap.allowNewTopics and sessionMap.minimumReply ne 0}">
				<th>
					&nbsp;
				</th>
			</c:if>
		</tr>
		<c:forEach items="${topicList}" var="topic">
			<tr>
				<td>
					<c:set var="viewtopic">
						<html:rewrite page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${topic.message.uid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
					</c:set>
					<html:link href="${viewtopic}">
						<c:choose>
							<c:when test="${topic.newPostingsNum > 0}">
								<b><c:out value="${topic.message.subject}" /></b>
							</c:when>
							<c:otherwise>
								<c:out value="${topic.message.subject}" />
							</c:otherwise>
						</c:choose>
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
					<c:out value="${author}" escapeXml="true"/>
				</td>
				
				<td align="center">
					<c:out value="${topic.message.replyNumber}" />
				</td>
				
				<td align="center">
					<c:out value="${topic.newPostingsNum}" />
				</td>
				<td>
					<lams:Date value="${topic.message.lastReplyDate}"/>
				</td>
				<c:if test="${not sessionMap.allowNewTopics and sessionMap.minimumReply ne 0}">
					<td>
						${topic.numOfPosts} / ${sessionMap.minimumReply}
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
