<%@ page import="org.lamsfoundation.lams.tool.forum.util.ForumConstants"%>
<%@ include file="/common/taglibs.jsp"%>

<%--  msgDto, messageUid, msgLevel needs to be in the session elsewhere --%>

	<c:set var="hidden" value="${msgDto.message.hideFlag}" />
	
	<c:choose>
	<c:when test='${msgDto.message.uid == messageUid}'>
		<c:set var="highlightClass">highlight</c:set>
	</c:when>
	<c:otherwise>	
		<c:set var="highlightClass"></c:set>
	</c:otherwise> 
	</c:choose>
	
	<%--  outermsg${msgDto.message.uid} is used to replace existing message areas when a message is edited. --%>
	<c:choose>
	<c:when test='${(msgDto.level <= 1)}'>
		<div class="row no-gutter voffset5">
    	<div class="col-xs-12" id="outermsg${msgDto.message.uid}">
	</c:when>
	<c:otherwise>
		<div class="no-gutter voffset2">
    	<div style="margin-left:<c:out value="${(msgDto.level-1)*indent}"/>px;"  id="outermsg${msgDto.message.uid}">
	</c:otherwise>
	</c:choose>

      <div class="panel panel-default ${highlightClass} msg" id="msg${msgDto.message.uid}">
        <div class="panel-heading">
        <h4 class="panel-title">
        	<span style="float:right">
			<i class="fa fa-user"></i> 
			<c:if test='${(sessionMap.mode == "teacher") || (not hidden)}'>
				<c:set var="author" value="${msgDto.author}" />
				<c:if test="${empty author}">
					<c:set var="author"><fmt:message key="label.default.user.name" /></c:set>
				</c:if>
				<span id="author"><c:out value="${author}" escapeXml="true"/></span>						
				-
				<span id="date"><lams:Date value="${msgDto.message.updated}" /></span>
			</c:if>
			</span>
			<c:choose>
			<c:when test='${(sessionMap.mode == "teacher") || (not hidden)}'>
				<c:out value="${msgDto.message.subject}" />
			</c:when>
			<c:otherwise>
				&nbsp;<fmt:message key="topic.message.subject.hidden" />
			</c:otherwise>
			</c:choose>
		</h4>
		</div>
		
		<div class="panel-body" id="pb-msg${msgDto.message.uid}">
			<span>
			<c:if test='${(not hidden) || (hidden && sessionMap.mode == "teacher")}'>
				<c:out value="${msgDto.message.body}" escapeXml="false" />
			</c:if>
			<c:if test='${hidden}'>
				&nbsp;<em><fmt:message key="topic.message.body.hidden" /> </em>
			</c:if>
			</span>

			<c:if test="${not empty msgDto.message.attachments}">
			<div id="attachments${msgDto.message.uid}" class="attachments">
				<i class="fa fa-paperclip"></i>
				<c:if test='${(not hidden) || (hidden && sessionMap.mode == "teacher")}'> 
					<c:forEach var="file" items="${msgDto.message.attachments}">
						<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
						</c:set>
						<a href="<c:out value='${downloadURL}' escapeXml='false'/>"><c:out value="${file.fileName}" /> </a>
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
			    <span class="label label-default"><fmt:message key="lable.topic.title.mark" />&nbsp;</span>
			    <fmt:formatNumber value="${msgDto.mark}"  maxFractionDigits="2"/><BR/>
			    <span class="label label-default"><fmt:message key="lable.topic.title.comment" />&nbsp;</span>
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
          	<div class="msg-footer">

			<!--  Rating stars -->
			<%@ include file="/jsps/learning/ratingStars.jsp"%>

			<!--  Hide/Unhide Button -->
			<c:if test='${sessionMap.mode == "teacher"}'>
				<c:set var="updateMark">
					<html:rewrite page="/monitoring/editMark.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&updateMode=viewForum&hideReflection=${sessionMap.hideReflection}" />
				</c:set>
				<html:link href="${updateMark}" styleClass="comment"><fmt:message key="lable.topic.title.mark" /></html:link>
				&middot;
							
				<!--  call the hide action -->
				<c:choose>
				<c:when test="${hidden}">
					<!--  display a show link  -->
					<c:set var="hidetopic">
						<html:rewrite page="/learning/updateMessageHideFlag.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&hideFlag=false" />
					</c:set>
					<html:link href="${hidetopic}" styleClass="comment"><fmt:message key="label.show" /></html:link>
				</c:when>
				<c:otherwise>
					<!--  display a hide link -->
					<c:set var="hidetopic">
						<html:rewrite page="/learning/updateMessageHideFlag.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&hideFlag=true" />
					</c:set>
					<html:link href="${hidetopic}" styleClass="comment"><fmt:message key="label.hide" /></html:link>
				</c:otherwise>
				</c:choose>
				&middot;
			</c:if>

			<!--  Reply Button -->
			<c:if test="${(not sessionMap.finishedLock) && (not noMorePosts)}">
				<c:set var="replytopic">
					<html:rewrite
						page="/learning/newReplyTopic.do?sessionMapID=${sessionMapID}&parentID=${msgDto.message.uid}&rootUid=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}" />
				</c:set>
				<a href="#${msgDto.message.uid}" onClick="javascript:createReply(${msgDto.message.uid},'${replytopic}',${msgLevel})" class="comment"><fmt:message key="label.reply" /></a>
				&middot;
			</c:if>
			
			<!--  Edit Button -->
			<c:if test="${not hidden}">
				<c:if test='${(sessionMap.mode == "teacher") || (msgDto.isAuthor && not sessionMap.finishedLock && sessionMap.allowEdit && (empty msgDto.mark))}'>
					<c:set var="edittopic">
						<html:rewrite
							page="/learning/editTopic.do?sessionMapID=${sessionMapID}&topicID=${msgDto.message.uid}&rootUid=${sessinoMap.rootUid}&create=${msgDto.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
					</c:set>
					<a href="#${msgDto.message.uid}" onClick="javascript:createEdit(${msgDto.message.uid},'${edittopic}',${msgLevel})" class="comment"><fmt:message key="label.edit" /></a>
				</c:if>
			</c:if>			
			
	</div>
	</div>
	</div>
	</div>
	</div>

