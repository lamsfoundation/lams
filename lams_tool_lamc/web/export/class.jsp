<%@ include file="/common/taglibs.jsp"%>

<table>				
 	<tr>
		<th scope="col" valign=top> 
			<fmt:message key="label.mcqSummary"/> 
		</th>
	</tr>
		
	<c:set var="queIndex" scope="request" value="0"/>
	<c:forEach var="currentDto" items="${listMonitoredAnswersContainerDto}">
		<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
  	 	<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
  	 	
  	 	<tr>
  	 		<td>&nbsp;</td>
  	 	</tr>
  	 	
		<tr>			
			<td NOWRAP valign=top class="align-left"><b>  <fmt:message key="label.question.only"/> <c:out value="${queIndex}"/>:</b>
				<c:out value="${currentDto.question}" escapeXml="false"/> &nbsp; (<fmt:message key="label.mark"/> 
				<c:out value="${currentDto.mark}"/>)
			</td>
		</tr>
		
		<tr>					
			<td NOWRAP valign=top class="align-left">  <b> <fmt:message key="label.mc.options.col"/>  </b> 
				<table class="align-left">
					<c:forEach var="answersData" items="${currentDto.candidateAnswersCorrect}">
						<tr>			
							<td NOWRAP valign=top class="align-left">
								<c:out value="${answersData.candidateAnswer}" escapeXml="false"/> 
									
								<c:if test="${answersData.correct == 'true'}"> 		
									&nbsp; (<fmt:message key="label.correct"/>)
								</c:if>																
							</td>	
						</tr>
					</c:forEach>		  	
				</table>
			</td>  
		</tr>			

	</c:forEach>		  	

 	 <tr>
 	 	<td NOWRAP valign=top class="align-left"> <b> 
 	 		<fmt:message key="label.passingMark"/> </b> <c:out value="${passMark}"/>  
 	 	</td>
 	 </tr>
 	 		
</table>

<h2>    <fmt:message key="label.studentMarks"/>  </h2>
	 
<c:if test="${portfolioExportDataFileName != null}">
	<p><html:link href="${portfolioExportDataFileName }"><c:out value="${portfolioExportDataFileName}"/></html:link></p>
</c:if>
	 
<table class="forms">
		
	<c:forEach var="sessionMarksDto" items="${listMonitoredMarksContainerDto}">
		<c:set var="currentSessionId" scope="request" value="${sessionMarksDto.sessionId}"/>
		<c:set var="mapUserMarksDto" scope="request" value="${sessionMarksDto.userMarks}"/>

		<tr>
			<td NOWRAP colspan=2 > <b> <fmt:message key="group.label"/> : </b>
			<c:out value="${sessionMarksDto.sessionName}"/>  </td>
		</tr>

		<tr>
			<td NOWRAP valign=top class="align-left"> 
				<b> <fmt:message key="label.user"/> </b> 
			</td>
			  
		  	<c:set var="queIndex" scope="request" value="0"/>
			<c:forEach var="currentDto" items="${listMonitoredAnswersContainerDto}">
				<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
				<td valign=top class="align-left"> <b>  <fmt:message key="label.question.only"/> 
					<c:out value="${queIndex}"/></b>
				</td>
			</c:forEach>		  	
					 
			<td NOWRAP valign=top class="align-left"> 
				<b> <fmt:message key="label.total"/>  </b> 
			</td>  
		</tr>						 

		<c:forEach var="markData" items="${mapUserMarksDto}">						
			<c:set var="data" scope="request" value="${markData.value}"/>
			<c:set var="currentUserSessionId" scope="request" value="${data.sessionId}"/>

			<c:if test="${currentUserSessionId == currentSessionId}"> 	
				<tr>									  	 		
					<td NOWRAP valign=top class="align-left"> 
						<c:out value="${data.fullName}"/> 
					</td>	
	
					<c:forEach var="mark" items="${data.marks}">
						<td NOWRAP valign=top class="align-left"> 
							<c:out value="${mark}"/> 								
						</td>
					</c:forEach>		  										
	
					<td NOWRAP valign=top class="align-left"> 
						<c:out value="${data.totalMark}"/> 																
					</td>							
				</tr>													
			</c:if>																

		</c:forEach>	

 		<tr> 
 			<td NOWRAP colspan="<c:out value='${hrColumnCount}'/>">  </td>
		</tr>
 		<tr> 
 			<td NOWRAP colspan="<c:out value='${hrColumnCount}'/>"> <hr size="2"></td>
		</tr>
 		<tr> 
 			<td NOWRAP colspan="<c:out value='${hrColumnCount}'/>">  </td>
		</tr>
	</c:forEach>		  	
</table>		  	 		
	