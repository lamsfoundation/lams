<%@ include file="/common/taglibs.jsp"%>

<c:forEach var="msgDto" items="${topicThread}">
	<c:set var="indentSize" value="${msgDto.level}" />
	<c:set var="hidden" value="${msgDto.message.hideFlag}" />
	<c:set var="edittopic">
		<html:rewrite page="/learning/editTopic.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&rootUid=${sessinoMap.rootUid}&create=${msgDto.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
	</c:set>
	<c:set var="replytopic">
		<html:rewrite page="/learning/newReplyTopic.do?sessionMapID=${sessionMapID}&parentID=${msgDto.message.uid}&rootUid=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}" />
	</c:set>
	
	<div style="margin-left:<c:out value="${indentSize}"/>em;">
		<table cellspacing="0" class="forum">
			<tr>
				<th class="ui-bar-c ui-corner-top">
					<c:choose>
						<c:when test='${(sessionMap.mode == "teacher") || (not hidden)}'>
							<c:out value="${msgDto.message.subject}" />
						</c:when>
						<c:otherwise>
							<fmt:message key="topic.message.subject.hidden" />
						</c:otherwise>
					</c:choose>
				</th>
			</tr>
			
			<tr class="ui-btn-up-d">
				<td class="posted-by" >
					<c:if test='${(sessionMap.mode == "teacher") || (not hidden)}'>
						<fmt:message key="lable.topic.subject.by" />
						<c:set var="author" value="${msgDto.author}" />
						<c:if test="${empty author}">
							<c:set var="author">
								<fmt:message key="label.default.user.name" />
							</c:set>
						</c:if>
						<c:out value="${author}" escapeXml="true"/>
								-
						<lams:Date value="${msgDto.message.updated}" style="short"/>
					</c:if>
				</td>
			</tr>
			
			<tr class="ui-btn-up-d">
				<td>
					<c:if test='${(not hidden) || (hidden && sessionMap.mode == "teacher")}'>
						<c:out value="${msgDto.message.body}" escapeXml="false" />
					</c:if>
					<c:if test='${hidden}'>
						<i><fmt:message key="topic.message.body.hidden" /> </i>
					</c:if>
				</td>
			</tr>

			<c:if test="${((msgDto.released && msgDto.isAuthor) || sessionMap.mode=='teacher') && (not empty msgDto.mark)}">
				<tr class="ui-btn-up-d">
					<td>
						<span class="field-name">
							<fmt:message key="lable.topic.title.mark" />
						</span>
						<BR>
						<fmt:formatNumber value="${msgDto.mark}"  maxFractionDigits="2"/>
					</td>
				</tr>
				<tr class="ui-btn-up-d">
					<td>
						<span class="field-name">
							<fmt:message key="lable.topic.title.comment" />
						</span>
						<BR>
						<c:choose>
							<c:when test="${empty msgDto.comment}">
								<fmt:message key="message.not.avaliable" />
							</c:when>
							<c:otherwise>
								<c:out value="${msgDto.comment}" escapeXml="false" />
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:if>
			
			<tr class="ui-btn-up-d">
				<td>
					<div class="right-buttons">
						<!--  Rating stars -->
						<%@ include file="/jsps/learning/mobile/ratingStars.jsp"%>
					
						<!--  Edit Button -->
						<c:if test="${not hidden}">
						<div style="margin: 0; padding: o; float: right;" data-role="controlgroup" data-type="horizontal">
							<c:if test='${(sessionMap.mode == "teacher") || (msgDto.isAuthor && not sessionMap.finishedLock && sessionMap.allowEdit && (empty msgDto.mark))}'>
								<a href="${edittopic}" data-rel="dialog" data-role="button" class="ui-btn ui-corner-left ui-controlgroup-last ui-btn-up-c" data-theme="c">
									<fmt:message key="label.edit" />
								</a>
							</c:if>
					
							<!--  Reply Button -->
							<c:if test="${(not sessionMap.finishedLock) && (not noMorePosts)}">
								<a href="${replytopic}" data-rel="dialog" data-role="button" class="ui-btn ui-corner-right ui-controlgroup-last ui-btn-up-c" data-theme="c"
										onclick="this.href += '&reqID=' + (new Date()).getTime();">
									<fmt:message key="label.reply" />
								</a>
							</c:if>
						</div>
						</c:if>
					</div>
				</td>
			</tr>
			
		</table>
	</div>
</c:forEach>
