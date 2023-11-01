<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="itemUid" value="${itemDTO.taskListItem.uid}" />
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/portrait5.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("time.timeago").timeago();
	});
</script>

<div class="card-subheader mt-3">
	<fmt:message key="label.preview.comments" />
</div>

<c:forEach var="comment" items="${itemDTO.comments}">
	<c:if test="${(sessionMap.userLogin == comment.createBy.loginName) || (sessionMap.mode == 'teacher') || (sessionMap.mode == 'author')}">
		<div class="card mt-2">
			<div class="card-header text-bg-warning bg-opacity-50">
				<c:if test="${not empty comment.createBy.userId}">
					<lams:Portrait userId="${comment.createBy.userId}"/>&nbsp;
				</c:if>
						
				<c:choose>
					<c:when test="${not empty comment.createBy.firstName or not empty comment.createBy.lastName}">
						<c:out escapeXml="true" value="${comment.createBy.firstName} ${comment.createBy.lastName}"/>
					</c:when>
					<c:otherwise>
						<fmt:message key="label.default.user.name" />
					</c:otherwise>
				</c:choose>
						
				-
				<lams:Date value="${comment.createDate}" timeago="true"/>
			</div>
					
			<div class="card-body">
				<c:out value="${comment.comment}" escapeXml="false" />
			</div>
		</div>
	</c:if>
</c:forEach>

<c:if test="${sessionMap.mode != 'teacher'}">
	<c:if test="${!itemDTO.commentRequirementsMet}">
		<lams:Alert5 id="commentRequired" close="true" type="info">
			<fmt:message key="label.learning.info.add.comment.required" />
		</lams:Alert5>
	</c:if>

	<div class="mb-3 mt-2 d-flex align-items-center text-nowrap">
		<label for="comment-${itemUid}" class="visually-hidden">
			<fmt:message key="label.preview.add.comment" />
		</label>
		<textarea name="comment-${itemUid}" id="comment-${itemUid}" class="form-control me-2" rows="2"></textarea>

		<button type="button" class="btn btn-secondary btn-disable-on-submit btn-icon-comment" onclick="addNewComment(${itemUid})">
			<fmt:message key="label.preview.post" />
		</button>
	</div>
</c:if>
