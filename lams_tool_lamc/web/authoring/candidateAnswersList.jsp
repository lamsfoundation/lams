<%@ include file="/common/taglibs.jsp"%>

<div id="candidateArea">

<c:set var="candidateIndex" >
	<c:choose>
		<c:when test="${not empty candidateIndex}">
			${candidateIndex}
		</c:when>
		<c:otherwise>
			${formBean.candidateIndex}
		</c:otherwise>
	</c:choose>
</c:set>
<c:set var="questionIndex" >
	<c:choose>
		<c:when test="${not empty questionIndex}">
			${questionIndex}
		</c:when>
		<c:otherwise>
			${formBean.questionIndex}
		</c:otherwise>
	</c:choose>
</c:set>

<input type="hidden" name="candidateIndex" value="${candidateIndex}"/>
<input type="hidden" name="questionIndex" value="${questionIndex}"/>

<%@ include file="/common/messages.jsp"%>

<table id="caTable" class="table table-condensed table-no-border">

	<c:set var="queIndex" value="0" />
	
	<tr>
		<th colspan="2">
			<fmt:message key="label.answers"></fmt:message>
		</th>
			
		<th class="text-center">
			<fmt:message key="label.isCorrect" />
		</th>

		<td width="40px">
		</td>

		<td width="20px">
		</td>
	</tr>
	
	<c:forEach items="${listQuestionContentDTO}" var="currentDTO" varStatus="status">
		<c:set var="queIndex" value="${queIndex +1}" />
		<c:set var="caCount" value="${fn:length(currentDTO.listCandidateAnswersDTO)}" />

		<c:if test="${currentEditableQuestionIndex == queIndex}">

			<c:set var="listCandidateAnswersDTO" value="${currentDTO.listCandidateAnswersDTO}" />

			<c:set var="caIndex" value="0" />

			<c:forEach items="${listCandidateAnswersDTO}" var="currentCandidateDTO" varStatus="status">
				<c:set var="caIndex" value="${caIndex +1}" />

				<tr>
					<td width="10px">
						<input type="hidden" name="caUid${caIndex}" value="${currentCandidateDTO.uid}">
						<c:out value="${caIndex}" />
					</td>

					<td width="90%">
						<lams:CKEditor id="ca${caIndex}" 
							value="${currentCandidateDTO.candidateAnswer}"
							contentFolderID="${sessionMap.contentFolderID}">
						</lams:CKEditor>
					</td>

					<td width="60px" class="text-center">
						<input type="radio" name="correct" value="<c:out value="${caIndex}"/>" 
								<c:if test="${'Correct' == currentCandidateDTO.correct}">CHECKED</c:if> >
					</td>

					<td width="5%">
				 		<c:if test="${caCount != 1}"> 		
				 		
							<c:if test="${caIndex != 1}">
								<lams:Arrow state="up" title="<fmt:message key='label.tip.moveCandidateUp'/>" 
									onclick="javascript:submitModifyAuthoringCandidate(${queIndex}, ${caIndex}, 'moveCandidateUp');"/>
							</c:if>

							<c:if test="${caIndex != caCount}">
								<lams:Arrow state="down" title="<fmt:message key='label.tip.moveCandidateDown'/>" 
									onclick="javascript:submitModifyAuthoringCandidate(${queIndex}, ${caIndex}, 'moveCandidateDown');"/>
							</c:if>
						
						</c:if> 			
					</td>

					<td  class="text-center"  width="5%"><i class="fa fa-times"	title="<fmt:message key='label.tip.removeCandidate'/>" 
						onclick="removeCandidate(${queIndex}, ${caIndex});"></i>
					</td>

				</tr>
			</c:forEach>

		</c:if>
	</c:forEach>

</table>
</div>
