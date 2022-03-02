<%@ include file="/common/taglibs.jsp"%>
<div id="questionSummariesDiv">
<%--  --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${empty recordList}">
	<%-- In some cases record list is passed as an attribute, in other - in session map. --%>
	<c:set var="recordList" value="${sessionMap.recordList}" />
</c:if>
<%-- This page modifies its content depending on the page it was included from. --%>
<c:if test="${empty includeMode}">
	<c:set var="includeMode" value="learning" />
</c:if>
<c:set var="daco" value="${sessionMap.daco}" />
<c:set var="questionSummaries" value="${sessionMap.questionSummaries}" />
<c:set var="ordinal"><fmt:message key="label.authoring.basic.answeroption.ordinal"/></c:set>

<c:set var="userRecordCount" value="${includeMode=='monitoring' ? sessionMap.recordCount : fn:length(recordList)}" />
<c:set var="groupRecordCount" value="${sessionMap.totalRecordCount}" />


<c:if test='${includeMode=="learning"}'>
<div class="voffset10">
<div class="panel">
	<c:out value="${daco.instructions}" escapeXml="false"/>
</div>
</div>
</c:if>

<!--  summary panel  -->
<div class="panel panel-default">
<div class="panel-heading panel-title">
	<fmt:message key="label.learning.tableheader.questions" />
</div>

<table class="table table-striped table-condensed" id="summaryTable">
	<tr class="active">
		<th></th>
		<th colspan="2" class="singleSummaryCell"><fmt:message key="label.learning.tableheader.summary" /></th>
	</tr>
	<tr class="active">
		<th></th>
		<th class="singleSummaryCell">
		<c:choose>
			<c:when test="${empty userFullName}">
				<fmt:message key="label.learning.tableheader.summary.learner" />
			</c:when>
			<c:otherwise>
				<c:out value="${userFullName}" escapeXml="true"/>
			</c:otherwise>
		</c:choose>
		</th>
		<th class="singleSummaryCell"><fmt:message key="label.learning.tableheader.summary.group" /></th>
	</tr>
	<tr class="active">
		<td>
			<fmt:message key="label.learning.heading.recordcount" />
		</td>
		<td class="singleSummaryCell">
			${userRecordCount }
		</td>
		<td class="singleSummaryCell">
			${groupRecordCount }
		</td>
	</tr>
	<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
		<c:set var="questionSummary" value="${questionSummaries[questionStatus.index]}" />
		<tr>
			<td>
				<!-- <div class="bigNumber">${questionStatus.index+1}</div> -->
				<c:out value="${question.description}" escapeXml="false"/>
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
						<%-- Part of the content is displayed depending on the summary type --%>
						<c:when test="${question.summary==1 || question.summary==2}">
							<c:choose>
								<%-- If no records were provided --%>
								<c:when test="${(question.summary==1 and empty questionSummary.userSummary[0].sum) 
											 || (question.summary==2 and empty questionSummary.userSummary[0].average)}">
									<td class="singleSummaryCell hint">
										<fmt:message key="label.learning.heading.norecords" />
									</td>
									<td class="singleSummaryCell hint">
										<fmt:message key="label.learning.heading.norecords" />
									</td>
								</c:when>
								<c:otherwise>
									<%-- First column shows the summaries for the learner,
										second one for the whole group (session) --%>
									<td class="singleSummaryCell">
										<c:choose>
											<c:when test="${question.summary==1}">
												${questionSummary.userSummary[0].sum}
											</c:when>
											<c:otherwise>
												${questionSummary.userSummary[0].average}
											</c:otherwise>
										</c:choose>
									</td>
									<td class="singleSummaryCell">
										<c:choose>
											<c:when test="${question.summary==1}">
												${questionSummary.groupSummary[0].sum}
											</c:when>
											<c:otherwise>
												${questionSummary.groupSummary[0].average}
											</c:otherwise>
										</c:choose>
									</td>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${question.summary==3}">
							<td>
								<table class="alternative-color-inner-table">
									<c:forEach var="singleAnswer" items="${questionSummary.userSummary}" begin="1">	
										<tr>
											<c:choose>
												<c:when test="${empty singleAnswer.answer}">
													<td class="hint" width="20px">
														<fmt:message key="label.learning.summary.emptyanswer" />:
													</td>
												</c:when>
												<c:otherwise>
													<td width="20px">
														${singleAnswer.answer}:
													</td>
												</c:otherwise>
											</c:choose>
											<td>
												${singleAnswer.count}
											</td>
										</tr>
									</c:forEach>
								</table>
							</td>
							<td>
								<table class="alternative-color-inner-table">
									<c:forEach var="singleAnswer" items="${questionSummary.groupSummary}" begin="1">	
										<tr>
											<td width="20px">
												${singleAnswer.answer}:
											</td>
											<td>
												${singleAnswer.count}
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
							<c:forEach var="singleAnswer" items="${questionSummary.userSummary}">
								<tr>
									<td>
										${fn:substring(ordinal,singleAnswer.answer-1,singleAnswer.answer)})
									</td>
									<td>
										<c:choose>
											<c:when test="${question.summary==1}">
												${singleAnswer.sum}
											</c:when>
											<c:otherwise>
												${singleAnswer.average}
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</table>
					</td>
					<td>
						<table class="alternative-color-inner-table">
							<c:forEach var="singleAnswer" items="${questionSummary.groupSummary}">
								<tr>
									<td>
										${fn:substring(ordinal,singleAnswer.answer-1,singleAnswer.answer)})
									</td>
									<td>
										<c:choose>
											<c:when test="${question.summary==1}">
												${singleAnswer.sum}
											</c:when>
											<c:otherwise>
												${singleAnswer.average}
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</c:when>
				<c:otherwise>
				<td class="singleSummaryCell">-</td>
				<td class="singleSummaryCell">-</td>
				</c:otherwise>
			</c:choose>
		</tr>
	</c:forEach>
</table>
</div> <!--  end summary panel -->

<c:if test='${includeMode=="learning"}'>
	<button class="btn btn-default btn-disable-on-submit" onclick="javascript:refreshQuestionSummaries('${sessionMapID}')"><fmt:message key="label.common.summary.refresh" /></a>
</c:if>
</div>
