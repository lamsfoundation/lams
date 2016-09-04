<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>

<c:set var="commentList" value="${itemDTO.comments}" />
<c:if test="${not empty commentList}">
	<h5>
		<fmt:message key="label.preview.comments" />
	</h5>
</c:if>

<c:forEach var="comment" items="${commentList}">

	<c:if
		test="${item.showCommentsToAll || (sessionMap.userLogin == comment.createBy.loginName) || (sessionMap.mode == 'teacher') || (sessionMap.mode == 'author')}">
		<div class="row voffset5">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-heading-sm panel-title bg-warning">
						<c:set var="author" value="${comment.createBy.firstName} ${comment.createBy.lastName}" />
						<c:if test="${empty author}">
							<c:set var="author">
								<fmt:message key="label.default.user.name" />
							</c:set>
						</c:if>
						<div class="user">
							<c:out value="${author}" escapeXml="true" />
						</div>
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
