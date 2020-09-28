<%@ include file="/common/taglibs.jsp"%>

<table class="table table-sm" id="referencesTable">
	<tr>
		<th>
			#
		</th>
		<th>
			<fmt:message key="label.authoring.basic.list.header.question" />
		</th>
		<th colspan="3">
			<fmt:message key="label.authoring.basic.list.header.mark" />
		</th>
	</tr>	

	<c:forEach var="questionReference" items="${sessionMap.questionReferences}" varStatus="status">
		<c:set var="question" value="${questionReference.question}" />
		<tr>
			<td class="align-middle">
				${status.count})
			</td>
			<td class="align-middle">
				<input type="hidden" name="sequenceId${questionReference.sequenceId}" value="${status.index}" class="reference-sequence-id">
			
				<c:choose>
					<c:when test="${questionReference.randomQuestion}">
						<fmt:message key="label.authoring.basic.type.random.question" />
					</c:when>
					<c:otherwise>
						<c:out value="${question.qbQuestion.name}" escapeXml="true"/>
					</c:otherwise>
				</c:choose>
				
				<c:if test="${!questionReference.randomQuestion}">
					<span class='float-right btn btn-sm btn-info mx-2'>
				    	v.&nbsp;${question.qbQuestion.version}
				    </span>
		        </c:if>
		        
		       	<span class='float-right btn btn-sm btn-info'>
					<c:choose>
						<c:when test="${questionReference.randomQuestion}">
							<fmt:message key="label.authoring.basic.type.random.question" />
						</c:when>
						<c:when test="${question.type == 1}">
							<fmt:message key="label.authoring.basic.type.multiple.choice" />
						</c:when>
						<c:when test="${question.type == 2}">
							<fmt:message key="label.authoring.basic.type.matching.pairs" />
						</c:when>
						<c:when test="${question.type == 3}">
							<fmt:message key="label.authoring.basic.type.short.answer" />
						</c:when>
						<c:when test="${question.type == 4}">
							<fmt:message key="label.authoring.basic.type.numerical" />
						</c:when>
						<c:when test="${question.type == 5}">
							<fmt:message key="label.authoring.basic.type.true.false" />
						</c:when>
						<c:when test="${question.type == 6}">
							<fmt:message key="label.authoring.basic.type.essay" />
						</c:when>
						<c:when test="${question.type == 7}">
							<fmt:message key="label.authoring.basic.type.ordering" />
						</c:when>
						<c:when test="${question.type == 8}">
							<fmt:message key="label.authoring.basic.type.mark.hedging" />
						</c:when>
					</c:choose>
       			</span>
			</td>
			
			<td class="align-middle" style="width: 30px">
				<input name="maxMark" value="${questionReference.maxMark}" class="form-control form-control-sm max-mark-input">
			</td>
			
			<td class="align-middle" style="width: 30px">
				<i class="fa fa-xs fa-asterisk ${question.answerRequired ? 'text-danger' : ''}" 
							role="button"
							title="<fmt:message key="label.answer.required"/>" 
							alt="<fmt:message key="label.answer.required"/>"
							onClick="javascript:toggleQuestionRequired(this)"></i>
			</td>

			<td class="align-middle" style="width: 30px; padding-right: 20px;">
				<c:if test="${!questionReference.randomQuestion}">
					<a class="thickbox roffset5x edit-reference-link" onclick="javascript:editReference(this);" style="color: black;"> 
						<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.edit" />"></i>
					</a>			
				</c:if>
			</td>
		</tr>
	</c:forEach>
</table>