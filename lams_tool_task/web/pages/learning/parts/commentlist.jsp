<c:set var="commentList" value="${itemDTO.comments}" />
<c:if test="${not empty commentList}">
	<div class="field-name">
		<fmt:message key="label.preview.comments" />
	</div>
</c:if>

<c:forEach var="comment" items="${commentList}">

	<c:if test="${item.showCommentsToAll || (sessionMap.userLogin == comment.createBy.loginName) || (sessionMap.mode == 'teacher') || (sessionMap.mode == 'author')}">
		<div>
			<table cellspacing="0" class="forum">
				<tr>
					<th >
						<fmt:message key="lable.preview.by" />
						<c:set var="author" value="${comment.createBy.firstName} ${comment.createBy.lastName}" />
						<c:if test="${empty author}">
							<c:set var="author">
								<fmt:message key="label.default.user.name" />
							</c:set>
						</c:if>
						<c:out value="${author}" escapeXml="true"/>
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
