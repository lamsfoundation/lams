<div class="panel">
<div class="panel-body table-responsive">
	<table id="questions-data" class="table table-responsive table-striped table-bordered table-hover table-condensed">
		<thead>
			<tr role="row">
				<th></th>
				<c:forEach items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
					<th class="text-center">
						<fmt:message key="label.authoring.basic.list.header.question"/>&nbsp;${status.index + 1}
					</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<tr role="row">
				<td><b><fmt:message key="label.authoring.basic.list.header.type"/></b></td>
				<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}">
					<td class="text-center">
						<c:choose>
							<c:when test="${question.type == 1}">
								<fmt:message key="label.authoring.basic.type.multiple.choice"/>
							</c:when>
							<c:when test="${question.type == 6}">
								<fmt:message key="label.authoring.basic.type.essay"/>
							</c:when>
						</c:choose>
					</td>
				</c:forEach>
			</tr>
			
			<tr role="row">
				<td>
					<b>
						<fmt:message key="label.learning.summary.team.answers"/>
					</b>
				</td>
				<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}">
					<c:set var="cssClass" value="" />
					<c:set var="answer" value="" />
					<c:choose>
						<c:when test="${question.type == 1.0}">
							<c:forEach var="option" items="${question.optionDtos}">
								<c:if test='${option.answerBoolean}'>
									<c:if test="${option.grade == 1}">
										<c:set var="cssClass" value="bg-success" />
									</c:if>
									<c:set var="answer" value="${option.optionString}" />
								</c:if>
							</c:forEach>
						</c:when>
						<c:when test="${question.type == 6}">
							<c:set var="answer" value="${question.answerString}" />
						</c:when>
					</c:choose>
					<td class="text-center ${cssClass}">
						${answer}
					</td>
				</c:forEach>
			</tr>
			
			<tr role="row">
				<td colspan="7" style="font-weight: bold;">
					<fmt:message key="label.learning.summary.other.team.answers"/>
				</td> 
			</tr>
			
			<tr role="row">
				<td class="text-center">
					0
				</td>
			</tr>
		</tbody>
	</table>
</div>
</div>