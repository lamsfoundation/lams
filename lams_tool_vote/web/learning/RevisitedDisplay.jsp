<%@ include file="/common/taglibs.jsp"%>

<h2>
	<fmt:message key="label.progressiveResults" />
</h2>

<!--present  a mini summary table here -->
<table cellspacing="0" class="alternative-color">
	<tr>
		<th>
			<fmt:message key="label.nomination" />
		</th>
		<th>
			<fmt:message key="label.total.votes" />
		</th>
	</tr>

	<c:forEach var="currentNomination" items="${voteGeneralLearnerFlowDTO.mapStandardNominationsHTMLedContent}">
		<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}" />
		<tr>
			<td class="space">
				<c:out value="${currentNomination.value}" escapeXml="false" />
			</td>

			<td>

				<c:forEach var="currentUserCount" items="${voteGeneralLearnerFlowDTO.mapStandardUserCount}">
					<c:set var="currentUserKey" scope="request" value="${currentUserCount.key}" />
					<c:if test="${currentNominationKey == currentUserKey}">

						<c:if test="${currentUserCount.value != '0' }">
							<c:forEach var="currentQuestionUid" items="${voteGeneralLearnerFlowDTO.mapStandardQuestionUid}">
								<c:set var="currentQuestionUidKey" scope="request" value="${currentQuestionUid.key}" />
								<c:if test="${currentQuestionUidKey == currentUserKey}">

									<c:forEach var="currentSessionUid" items="${voteGeneralLearnerFlowDTO.mapStandardToolSessionUid}">
										<c:set var="currentSessionUidKey" scope="request" value="${currentSessionUid.key}" />
										<c:if test="${currentSessionUidKey == currentQuestionUidKey}">
	
											<c:if test="${currentNomination.value != 'Open Vote'}">
												<c:set scope="request" var="viewURL">
													<lams:WebAppURL />monitoring.do?method=getVoteNomination&questionUid=${currentQuestionUid.value}&sessionUid=${currentSessionUid.value}
												</c:set>
		
												<c:out value="${currentUserCount.value}" />
											</c:if>
											
											<c:if test="${currentNomination.value == 'Open Vote'}">
												<c:out value="${currentUserCount.value}" />
											</c:if>
										</c:if>
									</c:forEach>
								</c:if>
							</c:forEach>
						</c:if>
									
						<c:if test="${currentUserCount.value == '0' }">
							<c:out value="${currentUserCount.value}" />
						</c:if>
					</c:if>
				</c:forEach>

				<c:forEach var="currentRate" items="${voteGeneralLearnerFlowDTO.mapStandardRatesContent}">
					<c:set var="currentRateKey" scope="request" value="${currentRate.key}" />
					<c:if test="${currentNominationKey == currentRateKey}"> 				
						&nbsp(<fmt:formatNumber type="number" maxFractionDigits="2" value="${currentRate.value}" />
						<fmt:message key="label.percent" />) 
					</c:if>
				</c:forEach>
			</td>
		</tr>
	</c:forEach>

</table>

<div>
	<div class="float-right">
		<c:set var="chartURL" value="${tool}chartGenerator.do?currentSessionId=${VoteLearningForm.toolSessionID}" />
		<img src='<c:out value="${tool}"/>images/piechart.gif' width="30"
			title="<fmt:message key='label.tip.displayPieChart'/>"
			style="cursor: pointer;" height="30" border="0"
			onclick="javascript:drawChart('pie', 'chartDiv', '${chartURL}')">
		<img src='<c:out value="${tool}"/>images/columnchart.gif' width="30"
			title="<fmt:message key='label.tip.displayBarChart'/>" 
			style="cursor: pointer;" height="30" border="0"
			onclick="javascript:drawChart('pie', 'chartDiv', '${chartURL}')">
	</div>
				
	<c:if test="${VoteLearningForm.allowTextEntry}">
		<strong> <fmt:message key="label.open.votes"/> </strong>
		<c:forEach var="vote" items="${requestScope.listUserEntriesContent}">
			<div>
			<c:choose>
			<c:when test="${vote.visible}">
				<c:out value="${vote.userEntry}" escapeXml="true" />
			</c:when>
			<c:otherwise>
				<em>(<fmt:message key="label.hidden"/>)</em>
			</c:otherwise>
			</c:choose>
			</div>
		</c:forEach>
		<div>&nbsp;</div>
	</c:if>

	<c:choose>
		<c:when test="${fn:length(requestScope.listGeneralCheckedOptionsContent) > 1}">
        	<strong><fmt:message key="label.learner.nominations" />  </strong>
		</c:when>
	    <c:otherwise>
			<strong><fmt:message key="label.learner.nomination" />  </strong>
		</c:otherwise>
	</c:choose>

	<c:forEach var="entry"items="${requestScope.listGeneralCheckedOptionsContent}">
		<div>
			<c:out value="${entry}" escapeXml="false" />
		</div>
	</c:forEach>
	
	<div>
		<c:out value="${VoteLearningForm.userEntry}" escapeXml="true"/>
	</div>

	<div id="chartDiv" style="height: 220px; display: none;"></div>
</div>
