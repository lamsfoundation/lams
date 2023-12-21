<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>
<c:set var="hidden" value="${msgDto.message.hideFlag}" />
<c:set var="anonymous" value="${msgDto.message.isAnonymous}" />
<c:choose>
	<c:when test='${msgDto.message.uid == messageUid}'>
		<c:set var="highlightClass">highlight</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="highlightClass"></c:set>
	</c:otherwise>
</c:choose>

<%--  msgDto, messageUid, msgLevel needs to be in the session elsewhere --%>
<%--  outermsg${msgDto.message.uid} is used to replace existing message areas when a message is edited. --%>
<div id="outermsg${msgDto.message.uid}"
	<c:if test='${(msgDto.level > 1)}'>style="margin-left: ${(msgDto.level-1)*indent}px;" </c:if> >

<div class="card lcard ${highlightClass} msg mb-2" id="msg${msgDto.message.uid}">
	<div class="card-header">
		<div class="portrait-container-small">

			<c:choose>
				<c:when test="${msgDto.message.isMonitor}">
 	            	<c:set var="textClass" value="text-info"/>
 	                <c:set var="bgClass" value="bg-info"/>
 	            </c:when>
				<c:otherwise>
 	            	<c:set var="textClass" value=""/>
 	                <c:set var="bgClass" value=""/>
 	            </c:otherwise>
 	        </c:choose>

			<c:if test='${(sessionMap.mode == "teacher") or not (anonymous or hidden)}'>
				<div class="float-end">
				    <lams:Portrait userId="${msgDto.authorUserId}"/>
				</div>
			</c:if>
			
			<span class="${textClass} subject">
				<c:choose>
					<c:when test='${(sessionMap.mode == "teacher") || (not hidden)}'>
						<c:out value="${msgDto.message.subject}" />
					</c:when>
					<c:otherwise>
					&nbsp;<fmt:message key="topic.message.subject.hidden" />
					</c:otherwise>
				</c:choose>
            </span><br/>

            <div class="${textClass} font-size-init">
				<c:set var="msgAuthor" value="${msgDto.author}" />
				<c:if test="${empty msgAuthor}">
					<c:set var="msgAuthor"><fmt:message key="label.default.user.name" /></c:set>
				</c:if>
				<fmt:message key="lable.topic.subject.by" />:
					<c:choose>
					<c:when test='${sessionMap.mode == "teacher"}'>
						<c:choose>
						<c:when test="${anonymous}">
							<c:set var="author">${msgAuthor} (<fmt:message key="label.anonymous" />)</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="author">${msgAuthor}</c:set>
						</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:choose>
						<c:when test="${hidden}">
							<c:set var="author"></c:set>
						</c:when>
						<c:when test="${not hidden and anonymous}">
							<c:set var="author"><fmt:message key="label.anonymous" /></c:set>
						</c:when>
						<c:otherwise>
							<c:set var="author">${msgAuthor}</c:set>
						</c:otherwise>
						</c:choose>
					</c:otherwise>
					</c:choose>
	
	            <span id="author">
	            	<b><c:out value="${author}" escapeXml="true" /></b>
	            </span>
	            - <lams:Date value="${msgDto.message.updated}" timeago="true"/>
			</div>
		</div>
	</div>

	<div class="card-body ${bgClass}" id="pb-msg${msgDto.message.uid}">
		<span> 
			<c:if test='${(not hidden) || (hidden && sessionMap.mode == "teacher")}'>
				<c:out value="${msgDto.message.body}" escapeXml="false" />
			</c:if> 
			<c:if test='${hidden}'>
				&nbsp;<em><fmt:message key="topic.message.body.hidden" /> </em>
			</c:if>
		</span>

		<c:if test="${not empty msgDto.message.attachments}">
			<div id="attachments${msgDto.message.uid}" class="attachments ms-4x">
				<c:if test='${(not hidden) || (hidden && sessionMap.mode == "teacher")}'>
					<c:forEach var="file" items="${msgDto.message.attachments}">
						<span class="badge text-bg-warning bg-opacity-50 me-1 mt-3" title="<fmt:message key='message.label.attachment'/>">
							<fmt:message key='message.label.attachment'/>&nbsp;<c:out value="${file.fileName}" />
						</span>
						
						<c:set var="downloadURL">
							<lams:WebAppURL />download/?uuid=${file.fileDisplayUuid}&versionID=${file.fileVersionId}&preferDownload=true
						</c:set>
						<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="btn btn-sm btn-light">
							<fmt:message key="label.download" />
							<i class="fa-solid fa-download ms-1"></i>
						</a>
					</c:forEach>
				</c:if>
				
				<c:if test='${hidden}'>
					&nbsp;<em><fmt:message key="topic.message.attachment.hidden" /></em>
				</c:if>
			</div>
		</c:if>

		<c:if test="${((msgDto.released && msgDto.isAuthor) || sessionMap.mode=='teacher') && (not empty msgDto.mark)}">
			<hr class="msg-hr">
			<div>
				<span class="label label-default">
					<fmt:message key="lable.topic.title.mark" />&nbsp;
				</span>
				
				<fmt:formatNumber value="${msgDto.mark}" maxFractionDigits="2" />
				<BR /> 
				<span class="label label-default">
					<fmt:message key="lable.topic.title.comment" />&nbsp;
				</span>
				
				<c:choose>
					<c:when test="${empty msgDto.comment}">
						<fmt:message key="message.not.avaliable" />
					</c:when>
					<c:otherwise>
						<c:out value="${msgDto.comment}" escapeXml="false" />
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

		<hr class="msg-hr">
		<div class="float-end">

			<!--  Rating stars -->
			<%@ include file="/jsps/learning/ratingStars.jsp"%>

			<!--  Hide/Unhide Buttons -->
			<c:if test='${sessionMap.mode == "teacher"}'>
				<c:set var="updateMark">
					<lams:WebAppURL />monitoring/editMark.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&updateMode=viewForum&hideReflection=${sessionMap.hideReflection}
				</c:set>
				<a href="${updateMark}" class="comment">
					<fmt:message key="lable.topic.title.mark" />
				</a>
				&middot;
							
				<!--  call the hide action -->
				<c:choose>
					<c:when test="${hidden}">
						<!--  display a show link  -->
						<c:set var="hidetopic">
							<lams:WebAppURL />learning/updateMessageHideFlag.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&hideFlag=false
						</c:set>
						<a href="${hidetopic}" class="comment">
							<fmt:message key="label.show" />
						</a>
					</c:when>
					<c:otherwise>
						<!--  display a hide link -->
						<c:set var="hidetopic">
							<lams:WebAppURL />learning/updateMessageHideFlag.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&hideFlag=true
						</c:set>
						<a href="${hidetopic}" class="comment">
							<fmt:message key="label.hide" />
						</a>
					</c:otherwise>
				</c:choose>
				&middot;
			</c:if>

			<!--  Edit Button -->
			<c:if test="${not hidden && ((sessionMap.mode == 'teacher') || (msgDto.isAuthor && not sessionMap.finishedLock && sessionMap.allowEdit && (empty msgDto.mark)))}">
				<c:set var="edittopic">
					<lams:WebAppURL />learning/editTopic.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&rootUid=${sessinoMap.rootUid}&create=${msgDto.message.created.time}&hideReflection=${sessionMap.hideReflection}
				</c:set>
				<button type="button" onClick="javascript:createEdit(${msgDto.message.uid},'${edittopic}',${msgLevel})" class="btn btn-light comment align-top ms-2">
					<i class="fa-regular fa-pen-to-square me-1"></i>
					<fmt:message key="label.edit" />
				</button>
			</c:if>

			<%--  Reply Button. Must have class replybutton so that the button is hidden when the user reaches the maximum number of posts. See $('#messageForm').submit() --%>
			<c:if test="${(not sessionMap.finishedLock) && (not noMorePosts)}">
				<c:set var="replytopic">
					<lams:WebAppURL />learning/newReplyTopic.do?sessionMapID=${sessionMapID}&parentID=${msgDto.message.uid}&rootUid=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}
				</c:set>
				<button type="button" onClick="javascript:createReply(${msgDto.message.uid},'${replytopic}',${msgLevel})" class="btn btn-light comment replybutton align-top ms-2">
					<i class="fa-solid fa-reply me-1"></i>
					<fmt:message key="label.reply" />
				</button>
			</c:if>

		</div> <!--  end msg-footer -->
	</div> <!--  end panel-body -->
</div> <!--  end panel -->
</div> <!--  end div outermsg -->

