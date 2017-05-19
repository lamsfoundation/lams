<%@ include file="/common/taglibs.jsp" %>

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
	
	<input type="hidden" name="candidateIndex" value="${candidateIndex}"/>
	
	<%@ include file="/common/messages.jsp"%>

	<table id="caTable" class="table table-condensed table-no-border">
	
		<c:set var="caCount" scope="request" value="${fn:length(newQuestionDTO.listCandidateAnswersDTO)}"/>
		<c:set var="listCandidateAnswersDTO" scope="request" value="${newQuestionDTO.listCandidateAnswersDTO}"/>	
		<c:set var="caIndex" scope="request" value="0"/>

		<tr>
			<th colspan="2">
				<fmt:message key="label.answers"></fmt:message>
			</th>
			
			<th class="text-center">
				<fmt:message key="label.isCorrect" />
			</th>
	
			<th>
			</th>
	
			<th>
			</th>
		</tr>
		    
	 	<c:forEach items="${listCandidateAnswersDTO}" var="currentCandidateDTO" varStatus="status">
		   		<c:set var="caIndex" scope="request" value="${caIndex +1}"/>
		   	
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
					
					<td width="20px" class="text-center">	
						<input type="radio" name="correct" value="${caIndex}" 
								<c:if test="${'Correct' == currentCandidateDTO.correct}">CHECKED</c:if> >
					</td>		
					
					<td width="5%">
				 		<c:if test="${caCount != 1}"> 		
				 		
							<c:if test="${caIndex != 1}">
								<lams:Arrow state="up" title="<fmt:message key='label.tip.moveCandidateUp'/>" 
									onclick="javascript:submitModifyAddedCandidate('${caIndex}', 'moveAddedCandidateUp');"/>
							</c:if>

							<c:if test="${caIndex != caCount}">
								<lams:Arrow state="down" title="<fmt:message key='label.tip.moveCandidateDown'/>" 
									onclick="javascript:submitModifyAddedCandidate('${caIndex}', 'moveAddedCandidateDown');"/>
							</c:if>
						
						</c:if> 			
					</td>

					<td  class="text-center"  width="5%"><i class="fa fa-times"	title="<fmt:message key='label.tip.removeCandidate'/>" 
						onclick="removeAddedCandidate(${caIndex});"></i>
					</td>
			
				</tr>
		</c:forEach>
	</table>
	
</div>