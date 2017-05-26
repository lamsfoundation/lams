<%@ include file="/common/taglibs.jsp" %>
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />
<c:set var="questionDto" value="${sessionMap.questionDto}" />


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
	
		<c:set var="optionsCount" scope="request" value="${fn:length(questionDto.optionDtos)}"/>
		<c:set var="optionIndex" scope="request" value="0"/>

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
		    
	 	<c:forEach items="${questionDto.optionDtos}" var="optionDto" varStatus="status">
		   		<c:set var="optionIndex" scope="request" value="${optionIndex +1}"/>
		   	
				<tr>
					<td width="10px">
						<input type="hidden" name="caUid${optionIndex}" value="${optionDto.uid}">
						<c:out value="${optionIndex}" />
					</td>
				
					<td width="90%">
						<lams:CKEditor id="ca${optionIndex}" 
							value="${optionDto.candidateAnswer}"
							contentFolderID="${sessionMap.contentFolderID}">
						</lams:CKEditor>
					</td>
					
					<td width="20px" class="text-center">
						<input type="radio" name="correct" value="${optionIndex}" 
								<c:if test="${'Correct' == optionDto.correct}">CHECKED</c:if> >
					</td>
					
					<td width="5%">
				 		<c:if test="${optionsCount != 1}"> 		
				 		
							<c:if test="${optionIndex != 1}">
								<lams:Arrow state="up" title="<fmt:message key='label.tip.moveCandidateUp'/>" 
									onclick="javascript:submitModifyCandidate('${optionIndex}', 'moveCandidateUp');"/>
							</c:if>

							<c:if test="${optionIndex != optionsCount}">
								<lams:Arrow state="down" title="<fmt:message key='label.tip.moveCandidateDown'/>" 
									onclick="javascript:submitModifyCandidate('${optionIndex}', 'moveCandidateDown');"/>
							</c:if>
						
						</c:if> 			
					</td>

					<td  class="text-center"  width="5%"><i class="fa fa-times"	title="<fmt:message key='label.tip.removeCandidate'/>" 
						onclick="removeCandidate(${optionIndex});"></i>
					</td>
			
				</tr>
		</c:forEach>
	</table>
