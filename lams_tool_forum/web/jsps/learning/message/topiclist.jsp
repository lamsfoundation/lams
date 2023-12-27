<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<c:set var="pageSize" value="<%=ForumConstants.DEFAULT_PAGE_SIZE%>" />

<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("time.timeago").timeago();
	});
</script>

<div class="card lcard">
	<div class="card-header">
		<fmt:message key="label.topics" />
	</div>
	
	<div class="card-body">
		<div id="topicTable" class="ltable table-hover">
				<div class="row">
					<div class="col-5 flex-fill"><fmt:message key="lable.topic.title.subject" /></div>
					<div class="col-2"><fmt:message key="lable.topic.title.startedby" /></div>
					<div class="col-1 d-none d-sm-block text-center"><fmt:message key="lable.topic.title.replies" /></div>
					<div class="col-1 d-none d-sm-block text-center"><fmt:message key="lable.topic.title.repliesnew" /></div>
					<div class="col-2"><fmt:message key="lable.topic.title.update" /></div>
					<c:if test="${not sessionMap.allowNewTopics and sessionMap.minimumReply ne 0}">
						<div class="col-1">&nbsp;</div>
					</c:if>
				</div>

				<c:forEach items="${topicList}" var="topic">
					<div class="row">
						<div class="col-5 flex-fill">
							<c:set var="viewtopic">
								<lams:WebAppURL />learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${topic.message.uid}&create=${topic.message.created.time}&pageLastId=0&size=${pageSize}
							</c:set> 
							<a href="${viewtopic}" id="topicTitle">
								<c:choose>
									<c:when test="${topic.newPostingsNum > 0}">
										<b><c:out value="${topic.message.subject}" /></b>
									</c:when>
									<c:otherwise>
										<c:out value="${topic.message.subject}" />
									</c:otherwise>
								</c:choose>
							</a> 
							<c:if test="${topic.hasAttachment}">
								<i class="fa fa-paperclip ms-1" title="<fmt:message key='message.label.attachment'/>"></i>
							</c:if>
						</div>
						
						<div class="col-2"><%-- Author Name --%>
							<c:set var="msgAuthor" value="${topic.author}" />
							<c:set var="anonymous" value="${topic.message.isAnonymous}" />
							<c:set var="hidden" value="${topic.message.hideFlag}" />
							<c:if test="${empty msgAuthor}">
								<c:set var="msgAuthor"><fmt:message key="label.default.user.name" /></c:set>
							</c:if>
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
							<c:out value="${author}" escapeXml="true" />
						</div>

						<div class="col-1 text-center d-none d-sm-block">
							<c:out value="${topic.message.replyNumber}" />
						</div>

						<div class="col-1 text-center d-none d-sm-block">
							<c:out value="${topic.newPostingsNum}" />
						</div>

						<div class="col-2">
							<c:set var="displayDate" value="${topic.message.updated > topic.message.lastReplyDate ? topic.message.updated : topic.message.lastReplyDate}"/>
							<lams:Date value="${displayDate}" timeago="true"/>
						</div>
						
						<c:if test="${not sessionMap.allowNewTopics and sessionMap.minimumReply ne 0}">
							<div class="col-1">
								${topic.numOfPosts}/${sessionMap.minimumReply}
							</div>
						</c:if>
					</div>
				</c:forEach>
		</div>
	</div>
</div>
