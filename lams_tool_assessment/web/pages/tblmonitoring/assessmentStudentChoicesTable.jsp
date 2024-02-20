<%@ include file="/common/taglibs.jsp"%>

<c:forEach var="session" items="${sessions}" varStatus="i">
	<tr>
		<td class="text-center sticky-left-header">
				${session.sessionName}
		</td>

		<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="j">
			<c:set var="questionResultDto" value="${tblQuestionDto.sessionQuestionResults[i.index]}"/>
			<td class="text-center <c:if test="${questionResultDto.correct}">bg-success</c:if>" >
					${questionResultDto.answer}
			</td>
		</c:forEach>
	</tr>
</c:forEach>
