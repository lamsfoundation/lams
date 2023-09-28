<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="itemUid" value="${itemDTO.taskListItem.uid}" />
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/portrait5.js"></script>

<c:if test="${not empty itemDTO.comments}">
	<h5>
		<fmt:message key="label.preview.comments" />
	</h5>
</c:if>

<c:forEach var="comment" items="${itemDTO.comments}">
	<c:if test="${(sessionMap.userLogin == comment.createBy.loginName) || (sessionMap.mode == 'teacher') || (sessionMap.mode == 'author')}">
		<div class="row mt-2">
			<div class="col-12">
				<div class="panel panel-default">
					<div class="panel-heading-sm panel-title bg-warning">
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
					
					<div class="panel-body-sm">
						<c:out value="${comment.comment}" escapeXml="false" />
					</div>
				</div>
			</div>
		</div>
	</c:if>
</c:forEach>
<script type="text/javascript">
	$(document).ready(function() {
		$("time.timeago").timeago();
	});
</script>

<c:if test="${sessionMap.mode != 'teacher'}">

	<c:if test="${!itemDTO.commentRequirementsMet}">
		<lams:Alert id="commentRequired" close="true" type="info">
			<fmt:message key="label.learning.info.add.comment.required" />
		</lams:Alert>
	</c:if>

	<div class="mb-3 mt-2">
		<label for="comment-${itemUid}">
			<fmt:message key="label.preview.add.comment" />
		</label>
		<textarea name="comment-${itemUid}" id="comment-${itemUid}" class="form-control" rows="2"></textarea>

		<input type="submit" value='<fmt:message key="label.preview.post" />'
				class="btn btn-secondary btn-disable-on-submit mt-2" onclick="addNewComment(${itemUid})"/>
	</div>
	
</c:if>
