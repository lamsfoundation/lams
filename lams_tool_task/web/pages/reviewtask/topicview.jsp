<%@ include file="/common/taglibs.jsp"%>

<c:if test="${(fn:length(sessionMap.taskListItemCommentList) != 0)}">
	<div class="field-name">
		<fmt:message key="label.preview.comments" />
	</div>
</c:if>

<c:forEach var="comment" items="${sessionMap.taskListItemCommentList}">

	<c:if test="${sessionMap.taskListItem.showCommentsToAll || (sessionMap.userLogin == comment.createBy.loginName) || (sessionMap.mode == 'teacher')}">
	
		<div>
			<table cellspacing="0" class="forum">
				<tr>
					<th >
						<fmt:message key="lable.preview.by" />
						<c:set var="author" value="${comment.createBy.loginName}" />
						<c:if test="${empty author}">
							<c:set var="author">
								<fmt:message key="label.default.user.name" />
							</c:set>
						</c:if>
						${author}
								-				
						<lams:Date value="${comment.createDate}" />
					</th>
				</tr>
				
				<tr>
					<td class="posted-by">
					</td>
				</tr>
	
				<tr>
					<td>
						<c:out value="${comment.comment}" escapeXml="false" />
					</td>
				</tr>
				
			</table>
		</div>
		
	</c:if>
</c:forEach>
