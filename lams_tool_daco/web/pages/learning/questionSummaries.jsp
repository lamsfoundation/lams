<%@ include file="/common/taglibs.jsp"%>
<div id="questionSummariesDiv">
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="daco" value="${sessionMap.daco}" />
<c:set var="questionSummaries" value="${sessionMap.questionSummaries}" />
<c:set var="ordinal"><fmt:message key="label.authoring.basic.answeroption.ordinal"/></c:set>

	<table>
		<tr>
			<td>
			<h1>${daco.title}</h1>
			</td>
		</tr>
		<tr>
			<td>${daco.instructions}</td>
		</tr>
	</table>
	
	<table cellspacing="0" class="alternative-color" id="summaryTable">
		<tr>
			<th><fmt:message key="label.learning.tableheader.questions" /></th>
			<th colspan="2"><fmt:message key="label.learning.tableheader.summary" /></th>
		</tr>
		<tr>
			<th></th>
			<th><fmt:message key="label.learning.tableheader.summary.learner" /></th>
			<th><fmt:message key="label.learning.tableheader.summary.all" /></th>
		</tr>
		<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
			<c:set var="questionSummary" value="${questionSummaries[questionStatus.index]}" />
			<tr>
				<td>
					<div class="bigNumber">${questionStatus.index+1}</div>
					${question.description}
					<div class="hint">
						<c:choose>
							<c:when test="${question.summary==1}">
								<fmt:message key="label.common.summary.sum" /> 
							</c:when>
							<c:when test="${question.summary==2}">
								<fmt:message key="label.common.summary.average" /> 
							</c:when>
							<c:when test="${question.summary==3}">
								<fmt:message key="label.common.summary.count" /> 
							</c:when>
							<c:otherwise>
								<fmt:message key="label.common.summary.none" /> 
							</c:otherwise>
						</c:choose>
					</div>
				</td>
				<c:choose>
					<c:when test="${question.type==3 && not empty question.summary}">
						<c:choose>
							<c:when test="${question.summary==1 || question.summary==2}">
								<c:choose>
									<c:when test="${empty questionSummary.userSummary[0][question.summary]}">
										<td class="singleSummaryCell hint">
											<fmt:message key="label.learning.heading.norecords" />
										</td>
										<td class="singleSummaryCell hint">
											<fmt:message key="label.learning.heading.norecords" />
										</td>
									</c:when>
									<c:otherwise>
										<td class="singleSummaryCell">
											${questionSummary.userSummary[0][question.summary]}
										</td>
										<td class="singleSummaryCell">
											${questionSummary.allSummary[0][question.summary]}
										</td>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${question.summary==3}">
								<td>
									<table class="alternative-color-inner-table">
										<c:forEach var="summary" items="${questionSummary.userSummary}" begin="1">	
											<tr>
												<c:choose>
													<c:when test="${empty summary[0]}">
														<td class="hint">
															<fmt:message key="label.learning.summary.emptyanswer" />
														</td>
													</c:when>
													<c:otherwise>
														<td>
															${summary[0]}
														</td>
													</c:otherwise>
												</c:choose>
												<td>
													${summary[2]}
												</td>
											</tr>
										</c:forEach>
									</table>
								</td>
								<td>
									<table class="alternative-color-inner-table">
										<c:forEach var="summary" items="${questionSummary.allSummary}" begin="1">	
											<tr>
												<td>
													${summary[0]}
												</td>
												<td>
													${summary[2]}
												</td>
											</tr>
										</c:forEach>
									</table>
								</td>
							</c:when>
						</c:choose>
					</c:when>
					<c:when test="${(question.type==7 || question.type==8 || question.type==9)  && not empty question.summary}">
						<td>
							<table class="alternative-color-inner-table">
								<c:forEach var="summary" items="${questionSummary.userSummary}">
									<tr>
										<td>
											${fn:substring(ordinal,summary[0]-1,summary[0])})
										</td>
										<td>
											${summary[question.summary]}	
										</td>
									</tr>
								</c:forEach>
							</table>
						</td>
						<td>
							<table class="alternative-color-inner-table">
								<c:forEach var="summary" items="${questionSummary.allSummary}">
									<tr>
										<td>
											${fn:substring(ordinal,summary[0]-1,summary[0])})
										</td>
										<td>
											${summary[question.summary]}
										</td>
									</tr>
								</c:forEach>
							</table>
						</td>
					</c:when>
					<c:otherwise>
					<td class="singleSummaryCell">X</td>
					<td class="singleSummaryCell">X</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</c:forEach>
	</table>
	<p>
		<a href="#" onclick="javascript:refreshQuestionSummaries('${sessionMapID}')">Refresh</a>
	</p>
</div>