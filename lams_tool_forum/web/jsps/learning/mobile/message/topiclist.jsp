<%@ include file="/common/taglibs.jsp"%>


<ul data-role="listview" data-inset="true">

<table cellpadding="0"  cellspacing="0" class="alternative-color">
	<tbody>	
		<tr class="ui-bar-c">

			<th >
				<fmt:message key="lable.topic.title.subject" />
			</th>
			<th width="70px" >
				<fmt:message key="lable.topic.title.startedby" />
			</th>
			<th width="50px" align="center">
				<fmt:message key="lable.topic.title.replies" />
			</th>
			<th width="50px" align="center">
				<fmt:message key="lable.topic.title.repliesnew" />
			</th>
			<th width="20%" >
				<fmt:message key="lable.topic.title.update" />
			</th>
			<c:if test="${not sessionMap.allowNewTopics and sessionMap.minimumReply ne 0}">
				<th>
					&nbsp;
				</th>
			</c:if>

		</tr>	
		<c:forEach items="${topicList}" var="topic">
			<tr class="ui-btn-up-c">
				<td>
					<c:set var="viewtopic">
						<html:rewrite page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${topic.message.uid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
					</c:set>
					<a href="${viewtopic}" data-ajax="false">
						<c:choose>
							<c:when test="${fn:length(topic.message.subject) > 14}">
								<c:out value="${fn:substring(topic.message.subject, 0, 13)}..." escapeXml="true"/>
							</c:when>
							<c:otherwise>
								<c:out value="${topic.message.subject}" escapeXml="true"/>
							</c:otherwise>
						</c:choose>
					</a>
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
					<lams:Date value="${topic.message.lastReplyDate}" style="short"/>
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
</ul>
