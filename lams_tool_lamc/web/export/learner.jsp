<%@ include file="/common/taglibs.jsp"%>

<table width="80%" cellspacing="8" align="center">
 	 	<c:set var="mainQueIndex" scope="request" value="0"/>
		<c:forEach var="currentDto" items="${listMonitoredAnswersContainerDto}">
		<c:set var="mainQueIndex" scope="request" value="${mainQueIndex +1}"/>
  	 	<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
  	 	<tr>
  	 		<td> &nbsp;</td>
  	 	</tr>
  	 	
		<tr>			
			<td NOWRAP valign=top class="align-left">
				<b>  <fmt:message key="label.question.only"/> <c:out value="${mainQueIndex}"/>:</b>
				<c:out value="${currentDto.question}"  escapeXml="false"/> &nbsp; (<fmt:message key="label.mark"/> 
				<c:out value="${currentDto.mark}"/>)
			</td>
		</tr>
			
		<tr>					
			<td NOWRAP valign=top class="align-left">  <b> <fmt:message key="label.mc.options.col"/>  </b> 
				<table class="align-left">
					<c:forEach var="answersData" items="${currentDto.candidateAnswersCorrect}">
						<tr>			
							<td NOWRAP valign=top class="align-left">
								<c:out value="${answersData.candidateAnswer}"  escapeXml="false"/> 
									
								<c:if test="${answersData.correct == 'true'}"> 		
									&nbsp; (<fmt:message key="label.correct"/>)
								</c:if>																		
							</td>	
						</tr>
					</c:forEach>		  	
				</table>
			</td>  
		</tr>			

		<tr>
			<%--  only expect one entry for the following two maps as it is one entry per session and then one per user --%>
			<td  NOWRAP class="align-left" valign=top> 											
			<table class="align-left">
				<c:forEach var="sessionEntry" items="${currentDto.questionAttempts}">
					<c:forEach var="userEntry" items="${sessionEntry.value}">
					 	<tr>
							<td NOWRAP class="align-left" valign=top><b> <fmt:message key="label.yourAnswers"/>  </b></td>
							<td align=right valign=top>(<c:out value="${userEntry.mark}"/>)</td>
						</tr>
						<tr>
							<td NOWRAP class="align-left" valign=top>
								<fmt:message key="label.attempt"/> <c:out value="${userEntry.userAnswer}"/>
							</td>
						</tr>
					</c:forEach>	
				</c:forEach>			
				</table>
				</td>
		</tr>								

	</c:forEach>

	<tr>
		<td NOWRAP valign=top class="align-left"> <b> 
			<fmt:message key="label.passingMark"/>: </b> <c:out value="${passMark}"/> 
		</td>
	</tr>

	<tr>
		<td NOWRAP valign=top class="align-left"> <b> 
			<fmt:message key="label.studentMark"/>: </b> <c:out value="${learnerMark}"/> 
		</td>
	</tr>
</table>					