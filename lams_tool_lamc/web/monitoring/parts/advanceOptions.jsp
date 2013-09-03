<h1 style="padding-bottom: 10px;">
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="radiobox.onepq" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${questionsSequenced}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.showMarks" />
		</td>
		<td>
			<c:choose>
				<c:when test="${showMarks}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.randomize" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${randomize}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	
	<tr>
		<td>
			<fmt:message key="label.displayAnswers" />
		</td>	
		<td>
			<c:choose>
				<c:when test="${displayAnswers}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	 		
	<tr>
	 	<td>
			<fmt:message key="radiobox.retries" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${retries}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="radiobox.passmark" />
		</td>
		<td>	
			${passMark}
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="monitor.summary.td.addNotebook" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${reflect}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${reflect}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>	
					${reflectionSubject}
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>

		<c:if test="${mcGeneralMonitoringDTO.userExceptionNoToolSessions != 'true'}"> 	
				
			<table class="forms">
					
		  	 	<c:set var="queIndex" scope="request" value="0"/>
				<c:forEach var="currentDto" items="${listMonitoredAnswersContainerDto}">
					<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>

						<tr>			
							<td NOWRAP valign=top class="align-left"><b>  <fmt:message key="label.question.only"/> <c:out value="${queIndex}"/>:</b>
								<c:out value="${currentDto.question}" escapeXml="false"/> &nbsp (<fmt:message key="label.mark"/> <c:out value="${currentDto.mark}"/> )
							 </td>
						</tr>	
						<tr>					
							<td NOWRAP valign=top class="align-left">  <b> <fmt:message key="label.mc.options.col"/>  </b> 
								<table class="align-left">
									<c:forEach var="answersData" items="${currentDto.candidateAnswersCorrect}">
										<tr>			
											<td NOWRAP valign=top class="align-left">
												&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
												<c:out value="${answersData.candidateAnswer}" escapeXml="false"/> 
												
												<c:if test="${answersData.correct == 'true'}"> 		
													&nbsp (<fmt:message key="label.correct"/>)
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
		  	 			<fmt:message key="label.passingMark"/>: </b> <c:out value="${passMark}"/> 
		  	 		</td>
		  	 	</tr>
			</table>
		</c:if>
</div>
