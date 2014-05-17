<%@ include file="/common/taglibs.jsp" %>
<div id="candidateArea">

	<c:set var="candidateIndex" >
		<c:choose>
			<c:when test="${not empty candidateIndex}">
				${candidateIndex}
			</c:when>
			<c:otherwise>
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				${formBean.candidateIndex}
			</c:otherwise>
		</c:choose>
	</c:set>
	
	<input type="hidden" name="candidateIndex" value="${candidateIndex}"/>
	
	<div class="field-name space-top">
		<fmt:message key="label.answers"></fmt:message>
	</div>
	
	<%@ include file="/common/messages.jsp"%>

	<table id="caTable">
	
		<c:set var="caCount" scope="request" value="${fn:length(newQuestionDTO.listCandidateAnswersDTO)}"/>
		<c:set var="listCandidateAnswersDTO" scope="request" value="${newQuestionDTO.listCandidateAnswersDTO}"/>	
		<c:set var="caIndex" scope="request" value="0"/>

		<tr>
			<td width="10px">
			</td>
	
			<td>
			</td>
	
			<td width="60px" class="align-center">
				<fmt:message key="label.isCorrect" />
			</td>
	
			<td width="40px">
			</td>
	
			<td width="20px">
			</td>
		</tr>
		    
	 	<c:forEach items="${listCandidateAnswersDTO}" var="currentCandidateDTO" varStatus="status">
		   		<c:set var="caIndex" scope="request" value="${caIndex +1}"/>
		   	
				<tr>
					<td>
						<input type="hidden" name="caUid${caIndex}" value="${currentCandidateDTO.uid}">
						<c:out value="${caIndex}" />  
					</td>		
				
					<td>
						<lams:CKEditor id="ca${caIndex}" 
							value="${currentCandidateDTO.candidateAnswer}"
							contentFolderID="${mcGeneralAuthoringDTO.contentFolderID}">
						</lams:CKEditor>
					</td>		
					
					<td class="align-center">
						<c:forEach var="correctEntry" items="${mcGeneralAuthoringDTO.correctMap}">
							<c:set var="SELECTED_ANSWER" scope="request" value="" />
							<c:set var="ISCORRECT" scope="request" value="Incorrect" />
								
							<c:if test="${correctEntry.value == currentCandidateDTO.correct}">
								<c:set var="SELECTED_ANSWER" scope="request" value="CHECKED" />
								<c:set var="ISCORRECT" scope="request" value="Correct" />
							</c:if>
	
							<input type="radio" name="correct" value="<c:out value="${caIndex}"/>" <c:out value="${SELECTED_ANSWER}"/> >  
	
						</c:forEach>
					</td>		
					
					<td>
				 		<c:if test="${caCount != 1}"> 		
				 		
							<c:if test="${caIndex == 1}">
								<img src="<c:out value="${tool}"/>images/down.gif" border="0"
									title="<fmt:message key='label.tip.moveCandidateDown'/>"
									onclick="javascript:submitModifyAddedCandidate('<c:out value="${caIndex}"/>', 'moveAddedCandidateDown');">
								<img src="<c:out value="${tool}"/>images/up_disabled.gif"
									border="0">
							</c:if>
	
							<c:if test="${caIndex == caCount}">
								<img src="<c:out value="${tool}"/>images/down_disabled.gif"
									border="0">
								<img src="<c:out value="${tool}"/>images/up.gif" border="0"
									title="<fmt:message key='label.tip.moveCandidateUp'/>"
									onclick="javascript:submitModifyAddedCandidate('<c:out value="${caIndex}"/>', 'moveAddedCandidateUp');">
							</c:if>
	
							<c:if test="${(caIndex != 1)  && (caIndex != caCount)}">
								<img src="<c:out value="${tool}"/>images/down.gif" border="0"
									title="<fmt:message key='label.tip.moveCandidateDown'/>"
									onclick="javascript:submitModifyAddedCandidate('<c:out value="${caIndex}"/>', 'moveAddedCandidateDown');">
	
								<img src="<c:out value="${tool}"/>images/up.gif" border="0"
									title="<fmt:message key='label.tip.moveCandidateUp'/>"
									onclick="javascript:submitModifyAddedCandidate('<c:out value="${caIndex}"/>', 'moveAddedCandidateUp');">
							</c:if>							
						
						</c:if> 			
					</td>
					
					<td>				
						<img src="<c:out value="${tool}"/>images/delete.gif" border="0"
							title="<fmt:message key='label.tip.removeCandidate'/>"
							onclick="removeAddedCandidate(${caIndex});">
					</td>
			
				</tr>
		</c:forEach>
	</table>
	
</div>