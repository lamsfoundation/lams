<%@ include file="/common/taglibs.jsp"%>

<h4>
	<fmt:message key="label.progressiveResults" />
</h4>

<!--present  a mini summary table here -->
<table class="table table-hover table-stripped" id="voteSummary">
	<tr>
		<th><fmt:message key="label.nomination" /></th>
		<th class="text-center"><fmt:message key="label.total.votes" /></th>
	</tr>
	<c:forEach var="currentNomination" items="${voteGeneralLearnerFlowDTO.mapStandardNominationsHTMLedContent}">
		<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}" />
		<tr>
			<td class="text-left"><c:out value="${currentNomination.value}" escapeXml="false" /></td>

			<td class="text-center"><c:forEach var="currentUserCount"
					items="${voteGeneralLearnerFlowDTO.mapStandardUserCount}">
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
				</c:forEach> <c:forEach var="currentRate" items="${voteGeneralLearnerFlowDTO.mapStandardRatesContent}">
					<c:set var="currentRateKey" scope="request" value="${currentRate.key}" />
					<c:if test="${currentNominationKey == currentRateKey}"> 				
						&nbsp(<fmt:formatNumber type="number" maxFractionDigits="2" value="${currentRate.value}" />
						<fmt:message key="label.percent" />) 
					</c:if>
				</c:forEach></td>
		</tr>
	</c:forEach>

</table>


<div class="float-end">
	<c:set var="chartURL" value="${tool}chartGenerator.do?currentSessionId=${VoteLearningForm.toolSessionID}" />
	<a title="<fmt:message key='label.tip.displayPieChart'/>" class="fa fa-pie-chart text-primary btn btn-md btn-primary"
		onclick="javascript:drawChart('pie', 'chartDiv', '${chartURL}')"></a> <a
		title="<fmt:message key='label.tip.displayBarChart'/>" class="fa fa-bar-chart text-primary btn btn-md btn-primary"
		onclick="javascript:drawChart('bar', 'chartDiv', '${chartURL}')"></a>
</div>

<div id="textEntries">
	<c:if test="${VoteLearningForm.allowTextEntry}">
		<strong> <fmt:message key="label.open.votes" />
		</strong>
		<c:forEach var="vote" items="${requestScope.listUserEntriesContent}">
			<c:if test="${vote.userEntry != ''}">
				<div class="d-flex">
					<div class="flex-shrink-0">
						<i class="fa fa-xs fa-check text-success"></i>
					</div>
					<div class="flex-grow-1 ms-3">
						<c:choose>
							<c:when test="${vote.visible}">
								<c:out value="${vote.userEntry}" escapeXml="true" />
							</c:when>
							<c:otherwise>
								<em>(<fmt:message key="label.hidden" />)
								</em>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:if>
		</c:forEach>
	</c:if>
</div>
<div id="nominations" class="mt-2">
	<c:choose>
		<c:when test="${fn:length(requestScope.listGeneralCheckedOptionsContent) > 1}">
			<strong><fmt:message key="label.learner.nominations" /> </strong>
		</c:when>
		<c:otherwise>
			<strong><fmt:message key="label.learner.nomination" /> </strong>
		</c:otherwise>
	</c:choose>

	<c:forEach var="entry" items="${requestScope.listGeneralCheckedOptionsContent}">
		<div class="d-flex">
			<div class="flex-shrink-0">
				<i class="fa fa-xs fa-check text-success"></i>
			</div>
			<div class="flex-grow-1 ms-3">
				<c:out value="${entry}" escapeXml="true" />
			</div>
		</div>
	</c:forEach>

	<c:if test="${not empty VoteLearningForm.userEntry}">
		<div class="d-flex">
			<div class="flex-shrink-0">
				<i class="fa fa-xs fa-check text-success"></i>
			</div>
			<div class="flex-grow-1 ms-3">
				<c:out value="${VoteLearningForm.userEntry}" escapeXml="false" />
			</div>
		</div>
	</c:if>
</div>
<div id="chartDiv" style="height: 220px; display: none;"></div>
