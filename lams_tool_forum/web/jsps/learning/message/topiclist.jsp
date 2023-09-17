<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>

<c:set var="pageSize" value="<%=ForumConstants.DEFAULT_PAGE_SIZE%>" />
<div class="panel panel-default">
	<div class="panel-heading panel-title">
		<fmt:message key="label.topics" />

		<div class="btn-group float-end">
			<a href="javascript:location.href='${refresh}';" type="button" class="btn btn-sm btn-secondary"><i class="fa fa-xm fa-refresh"></i>
				<fmt:message key="label.refresh" /></a>

			<c:if test='${(not sessionMap.finishedLock) && (sessionMap.allowNewTopics)}'>
				<a href="javascript:location.href='${newtopic}';" type="button" class="btn btn-sm btn-secondary"> <i
					class="fa fa-xm fa-plus"></i> <fmt:message key="label.newtopic" /></a>
			</c:if>
		</div>


	</div>
	<div class="panel-body">
		<div class="table-responsive">
			<table id="topicTable" class="table table-hover table-sm">
				<thead>
					<tr>
						<th class="col-6"><fmt:message key="lable.topic.title.subject" /></th>
						<th class="col-2"><fmt:message key="lable.topic.title.startedby" /></th>
						<th class="col-1 d-none d-sm-block"><fmt:message key="lable.topic.title.replies" /></th>
						<th class="col-1 d-none d-sm-block"><fmt:message key="lable.topic.title.repliesnew" /></th>
						<th class="col-2"><fmt:message key="lable.topic.title.update" /></th>
						<c:if test="${not sessionMap.allowNewTopics and sessionMap.minimumReply ne 0}">
							<th class="col-2">&nbsp;</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${topicList}" var="topic">
						<tr>
							<td><c:set var="viewtopic">
									<lams:WebAppURL />learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${topic.message.uid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}&pageLastId=0&size=${pageSize}
								</c:set> <a href="${viewtopic}" id="topicTitle">
									<c:choose>
										<c:when test="${topic.newPostingsNum > 0}">
											<b><c:out value="${topic.message.subject}" /></b>
										</c:when>
										<c:otherwise>
											<c:out value="${topic.message.subject}" />
										</c:otherwise>
									</c:choose>
								</a> <c:if test="${topic.hasAttachment}">
									<i class="fa fa-paperclip ms-1" title="<fmt:message key='message.label.attachment'/>"></i>
								</c:if></td>
							<td><%-- Author Name --%>
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
							</td>

							<td class="text-center d-none d-sm-block"><c:out value="${topic.message.replyNumber}" /></td>

							<td class="text-center d-none d-sm-block"><c:out value="${topic.newPostingsNum}" /></td>
							
							<c:set var="displayDate" value="${topic.message.updated > topic.message.lastReplyDate ? topic.message.updated : topic.message.lastReplyDate}"/>
							<td><lams:Date value="${displayDate}" timeago="true"/></td>
							<c:if test="${not sessionMap.allowNewTopics and sessionMap.minimumReply ne 0}">
								<td>${topic.numOfPosts}/${sessionMap.minimumReply}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("time.timeago").timeago();
	});
</script>
