<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<c:forEach var="sessionDto" items="${voteGeneralMonitoringDTO.sessionDTOs}">

	<c:if test="${isGroupedActivity}">
		<h1>
			<c:out value="${sessionDto.sessionName}"/>
		</h1>
		<br>
	</c:if>
						
	<table cellspacing="0" class="alternative-color">							

		<tr>
			<th><fmt:message key="label.nomination"/></th>
			<th style="width: 90px;"><fmt:message key="label.total.votes"/></th>
		</tr>
												
		<c:forEach var="currentNomination" items="${sessionDto.mapStandardNominationsHTMLedContent}">
			<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}"/>
			<tr>
				<td  valign=top class="align-left">
					<c:out value="${currentNomination.value}" escapeXml="false"/>
				</td>
						
				<td  valign=top class="align-left">				  	 		
					<c:forEach var="currentUserCount" items="${sessionDto.mapStandardUserCount}">
						<c:set var="currentUserKey" scope="request" value="${currentUserCount.key}"/>
						<c:if test="${currentNominationKey == currentUserKey}"> 				

							<c:if test="${currentUserCount.value != '0' }"> 	
								<c:forEach var="currentQuestionUid" items="${sessionDto.mapStandardQuestionUid}">
									<c:set var="currentQuestionUidKey" scope="request" value="${currentQuestionUid.key}"/>
									<c:if test="${currentQuestionUidKey == currentUserKey}">
				
										<c:forEach var="currentSessionUid" items="${sessionDto.mapStandardToolSessionUid}">
											<c:set var="currentSessionUidKey" scope="request" value="${currentSessionUid.key}"/>
											<c:if test="${currentSessionUidKey == currentQuestionUidKey}"> 				
						
												<c:set var="openVoteLabel" scope="request"><fmt:message key='label.open.vote'/></c:set>
												<c:choose>
												 	<c:when test="${currentNomination.value != openVoteLabel}">
												 	
													 	<c:choose>
													 		<c:when test="${sessionDto.sessionId == 0}">
													 			<c:set var="sessionUidParam" value=""/>
													 		</c:when>
													 		<c:otherwise>
													 			<c:set var="sessionUidParam" value="${currentSessionUid.value}"/>
													 		</c:otherwise>
													 	</c:choose>
													
														<c:set scope="request" var="viewURL">
															<lams:WebAppURL/>monitoring.do?dispatch=getVoteNomination&questionUid=${currentQuestionUid.value}&sessionUid=${sessionUidParam}
														</c:set>
																				  																					
														<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
															<c:out value="${currentUserCount.value}"/>  
														</a>
														
												 	</c:when>
												 	<c:otherwise>
												 		<c:out value="${currentUserCount.value}"/>
												 	</c:otherwise>
												 </c:choose>	    
																							
											</c:if> 	    
										</c:forEach>		  
				
									</c:if> 	    
								</c:forEach>		  
							</c:if> 	    								
							<c:if test="${currentUserCount.value == 0 }"> 		  				
								<c:out value="${currentUserCount.value}"/>  
							</c:if> 	
																	   																								
						</c:if> 	    
					</c:forEach>		  
						
					<c:forEach var="currentRate" items="${sessionDto.mapStandardRatesContent}">
						<c:set var="currentRateKey" scope="request" value="${currentRate.key}"/>
						<c:if test="${currentNominationKey == currentRateKey}"> 				
							&nbsp(<fmt:formatNumber type="number" maxFractionDigits="2" value="${currentRate.value}" />  <fmt:message key="label.percent"/>) 
						</c:if> 	    
					</c:forEach>		  
				</td>			
			</tr>	
		</c:forEach>	
	</table>
						
	<c:if test="${sessionDto.existsOpenVote}">
		<table cellspacing="0" class="alternative-color">
			<tr>
				<th colspan=4>  <fmt:message key="label.openVotes"/>  </th>
			</tr>
	
			<tr> 
				<th><fmt:message key="label.vote"/></th>
				<th> <fmt:message key="label.user"/></th>
				<th> <fmt:message key="label.attemptTime"/></th>
				<th style="width: 70px;"> <fmt:message key="label.visible"/></th>								  						 
			</tr>				 
											
			<c:forEach var="openVoteDto" items="${sessionDto.openVotes}">
				<c:forEach var="questionAttemptData" items="${openVoteDto.questionAttempts}">
					<c:set var="userData" scope="request" value="${questionAttemptData.value}"/>
		  	 		<c:set var="currentUid" scope="request" value="${userData.uid}"/>
					<tr> 
						<td  valign=top class="align-left"> 
							<c:out value="${openVoteDto.question}" escapeXml="true"/> 
							<c:if test="${userData.visible != 'true' }"> 			
								<i><fmt:message key="label.hidden"/> </i> 
							</c:if> 								
						</td>
																 
						<td valign=top class="align-left">    <c:out value="${userData.userName}" escapeXml="true"/>   </td>  
						<td valign=top class="align-left">    <lams:Date value="${userData.attemptTime}"/>  </td>
																  							
						<td valign=top class="align-left">
							<c:if test="${userData.visible == 'true' }"> 			
								<html:submit property="hideOpenVote" styleClass="linkbutton" onclick="submitOpenVote(${currentUid}, 'hideOpenVote');">						                                             
									<fmt:message key="label.hide"/>
								</html:submit>
							</c:if> 													
							
							<c:if test="${userData.visible != 'true' }"> 			
								<html:submit property="showOpenVote" styleClass="linkbutton" onclick="submitOpenVote(${currentUid}, 'showOpenVote');">						                                             
									<fmt:message key="label.show"/>
								</html:submit>
							</c:if> 						
						</td>  																	
					</tr>		
				</c:forEach>		  	
			</c:forEach>		
		</table>
	</c:if>
	
	<p style="font-size: 12px;" class="indent">
		<fmt:message key="label.total.number.students.voted">
			<fmt:param>
				<c:out value="${sessionDto.sessionUserCount}"/>
			</fmt:param>
			<fmt:param>
				<c:out value="${sessionDto.completedSessionUserCount}"/>
			</fmt:param>		
		</fmt:message>
	</p>
	
	<c:if test="${!statsTabActive}">
		<p class="float-right " style="margin-right: 8%;">
			<img src='<c:out value="${tool}"/>images/piechart.gif' width="30"
				title="<fmt:message key='label.tip.displayPieChart'/>"
				style="cursor: pointer; height: 30px; border: none"
				onclick="javascript:drawChart('pie', ${sessionDto.sessionId})">
	
			<img src='<c:out value="${tool}"/>images/columnchart.gif' width="30"
				title="<fmt:message key='label.tip.displayBarChart'/>" 
				style="cursor: pointer;" height="30" border="0"
				onclick="javascript:drawChart('bar', ${sessionDto.sessionId})">
		</p>
	
		<p id="chartDiv${sessionDto.sessionId}" style="height: 220px; display: none;" class="space-bottom"></p>
	</c:if>

	<c:if test="${fn:length(voteGeneralMonitoringDTO.sessionDTOs) > 1}">
		<br><br><hr size="2">
	</c:if>
	
	<br><br>

</c:forEach>				

