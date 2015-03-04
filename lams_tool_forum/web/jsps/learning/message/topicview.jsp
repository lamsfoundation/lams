<%@ include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-28 --%>

<c:forEach var="msgDto" items="${topicThread}">
	<c:set var="indentSize" value="${msgDto.level}" />
	<c:set var="hidden" value="${msgDto.message.hideFlag}" />
	<div id="message" name="msg${msgDto.message.uid}" style="margin-left:<c:out value="${indentSize}"/>em;">
		<table cellspacing="0" class="forum">
			<tr>
				<th id="subject">
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
			<tr>
				<td class="posted-by">
					<c:if test='${(sessionMap.mode == "teacher") || (not hidden)}'>
						<fmt:message key="lable.topic.subject.by" />
						<c:set var="author" value="${msgDto.author}" />
						<c:if test="${empty author}">
							<c:set var="author">
								<fmt:message key="label.default.user.name" />
							</c:set>
						</c:if>
						<span id="author"><c:out value="${author}" escapeXml="true"/></span>
								-
						<span id="date"><lams:Date value="${msgDto.message.updated}" /></span>
					</c:if>
				</td>
			</tr>
			<tr>
				<td>
					<span id="msgBody">
					<c:if
						test='${(not hidden) || (hidden && sessionMap.mode == "teacher")}'>
						<c:out value="${msgDto.message.body}" escapeXml="false" />
					</c:if>
					<c:if test='${hidden}'>
						<i><fmt:message key="topic.message.body.hidden" /> </i>
					</c:if>
					</span>
				</td>
			</tr>

			<c:if test="${not empty msgDto.message.attachments}">
				<tr>
					<td>
						<div id="attachments">
						<img src="<html:rewrite page="/images/paperclip.gif"/>" class="space-left float-left">
						<c:if
							test='${(not hidden) || (hidden && sessionMap.mode == "teacher")}'> 
							<c:forEach var="file" items="${msgDto.message.attachments}">
								<c:set var="downloadURL">
									<html:rewrite
										page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
									<c:out value="${file.fileName}" /> </a>
							</c:forEach>
						</c:if>
						<c:if test='${hidden}'>
							<fmt:message key="topic.message.attachment.hidden" />
						</c:if>
						</div>
					</td>
				</tr>
			</c:if>
			<c:if
				test="${((msgDto.released && msgDto.isAuthor) || sessionMap.mode=='teacher') && (not empty msgDto.mark)}">
				<tr>
					<td>
						<span class="field-name"><fmt:message
								key="lable.topic.title.mark" />
						</span>
						<BR>
						<fmt:formatNumber value="${msgDto.mark}"  maxFractionDigits="2"/>
					</td>
				</tr>
				<tr>
					<td>
						<span class="field-name"><fmt:message
								key="lable.topic.title.comment" />
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
			<tr>
				<td>
					<div class="right-buttons">
						<!--  Rating stars -->
						<%@ include file="/jsps/learning/ratingStars.jsp"%>
					
						<!--  Hide/Unhide Button -->
						<c:if test='${sessionMap.mode == "teacher"}'>
							<c:set var="updateMark">
								<html:rewrite
									page="/monitoring/editMark.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&updateMode=viewForum&hideReflection=${sessionMap.hideReflection}" />
							</c:set>
							<html:link href="${updateMark}" styleClass="button">
								<fmt:message key="lable.topic.title.mark" />
							</html:link>
							
							<!--  call the hide action -->
							<c:choose>
								<c:when test="${hidden}">
									<!--  display a show link  -->
									<c:set var="hidetopic">
										<html:rewrite
											page="/learning/updateMessageHideFlag.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&hideFlag=false" />
									</c:set>
									<html:link href="${hidetopic}" styleClass="button">
										<fmt:message key="label.show" />
									</html:link>
								</c:when>
								<c:otherwise>
									<!--  display a hide link -->
									<c:set var="hidetopic">
										<html:rewrite
											page="/learning/updateMessageHideFlag.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&hideFlag=true" />
									</c:set>
									<html:link href="${hidetopic}" styleClass="button">
										<fmt:message key="label.hide" />
									</html:link>
								</c:otherwise>
							</c:choose>
						</c:if>

						<!--  Edit Button -->
						<c:if test="${not hidden}">
							<c:if
								test='${(sessionMap.mode == "teacher") || (msgDto.isAuthor && not sessionMap.finishedLock && sessionMap.allowEdit && (empty msgDto.mark))}'>
								<c:set var="edittopic">
									<html:rewrite
										page="/learning/editTopic.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&rootUid=${sessinoMap.rootUid}&create=${msgDto.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
								</c:set>
								<html:link href="${edittopic}" styleClass="button" styleId="editButton">
									<fmt:message key="label.edit" />
								</html:link>
							</c:if>
					
							<!--  Reply Button -->
							<c:if
								test="${(not sessionMap.finishedLock) && (not noMorePosts)}">
								<c:set var="replytopic">
									<html:rewrite
										page="/learning/newReplyTopic.do?sessionMapID=${sessionMapID}&parentID=${msgDto.message.uid}&rootUid=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}" />
								</c:set>
								<html:link href="${replytopic}" styleClass="button" styleId="replyButton">
									<fmt:message key="label.reply" />
								</html:link>
							</c:if>
						</c:if>
					</div>
				</td>
			</tr>
		</table>
	</div>
</c:forEach>

