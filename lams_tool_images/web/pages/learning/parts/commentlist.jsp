<c:set var="comments" value="${commentsSessionMap.comments}" />
<c:if test="${not empty comments}">
	<div class="field-name">
		<fmt:message key="label.learning.comments" />
	</div>
</c:if>

<c:forEach var="comment" items="${comments}">

		<div>
			<table cellspacing="0" class="forum">
				<tr >
					<th >
						<fmt:message key="label.learning.by" />
						<c:set var="author" value="${comment.createBy.firstName} ${comment.createBy.lastName}" />
						<c:if test="${empty author}">
							<c:set var="author">
								<fmt:message key="label.default.user.name" />
							</c:set>
						</c:if>
						<c:out value="${author}" escapeXml="true" /> - <lams:Date value="${comment.createDate}" />
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
		
</c:forEach>
