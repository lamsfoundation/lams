<%@ include file="/common/taglibs.jsp"%>

<table cellpadding="0" class="alternative-color" cellspacing="0">
	<tbody>
		<tr>
			<th width="">
				<fmt:message key="lable.topic.title.comment" />
			</th>
			<th width="120px">
				<fmt:message key="lable.topic.title.author" />
			</th>
			<th width="25%">
				<fmt:message key="lable.topic.title.createDate" />
			</th>
		</tr>
		<c:forEach items="${sessionMap.taskListItemCommentList}" var="comment">
			<tr>
				<td>
					<c:out value="${comment.comment}" />
				</td>
				<td>
					<c:set var="author" value="${comment.createBy.loginName}"/>
					<c:if test="${empty author}">
						<c:set var="author">
							<fmt:message key="label.default.user.name"/>
						</c:set>
					</c:if>
					${author}
				</td>
				<td>
					<lams:Date value="${comment.createDate}"/>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
