<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager5.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css">
	<link type="text/css" href="${lams}/css/defaultHTML_chart.css" rel="stylesheet" />
	<style media="screen,projection" type="text/css">
		table.alternative-color td:first-child {
			width: 25%;
		}
		
		.chartDiv {
			display: none;
			height: 220px;
			padding 0;
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$(".tablesorter").tablesorter({
				theme : 'bootstrap',
				headerTemplate : '{content} {icon}',
				widthFixed : true,
				widgets : [ 'uitheme', 'zebra' ]
			});

			$(".tablesorter").each(function() {
				$(this).tablesorterPager({
					// set to false otherwise it remembers setting from other jsFiddle demos
					savePages : false,
					ajaxUrl : "<c:url value='/learning/getOpenResponses.do'/>?page={page}&size={size}&{sortList:column}&sessionId=${sessionMap.toolSessionID}&questionUid="
							+ $(this).attr('data-question-uid'),
					ajaxProcessing : function(data) {

						if (data && data.hasOwnProperty('rows')) {
							var rows = [], json = {};

							for (i = 0; i < data.rows.length; i++) {
								var userData = data.rows[i];

								rows += '<tr>';
								rows += '<td>';
								rows += '<div class="user-answer">';
								rows += userData["answer"];
								rows += '</div>';
								rows += '</td>';
								rows += '</tr>';
							}

							json.total = data.total_rows; // only allow 100 rows in total
							//json.filteredRows = 100; // no filtering
							json.rows = $(rows);
							return json;
						}
					},
					container: $(this).find(".ts-pager"),
					output: '{startRow} to {endRow} ({totalRows})',
	                cssPageDisplay: '.pagedisplay',
	                cssPageSize: '.pagesize',
	                cssDisabled: 'disabled'
				})
			});
		});

		function disableButtons() {
			$('.btn').prop('disabled', true);
		}

		function finishSession() {
			disableButtons();
			document.location.href = '<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}

		function continueReflect() {
			disableButtons();
			document.location.href = '<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}

		function retakeSurvey() {
			disableButtons();
			document.location.href = '<c:url value="/learning/retake.do?sessionMapID=${sessionMapID}"/>';
		}
	</script>

	<div id="container-main">
		<c:if test="${sessionMap.lockOnFinish and sessionMap.mode != 'teacher'}">
			<lams:Alert5 id="activityLocked" type="info" close="true">
				<fmt:message key="message.activityLocked" />
			</lams:Alert5>
		</c:if>
		
		<div id="instructions" class="instructions">
			<c:out value="${sessionMap.instructions}" escapeXml="false" />
		</div>

		<%-- user personal results--%>
		<div class="card-subheader mb-4">
			<fmt:message key="label.your.answers" />
		</div>
		
		<c:forEach var="element" items="${sessionMap.questionList}">
			<div class="card lcard mt-n3">
				<div class="card-header">
					<c:if test="${not question.optional}">
						<abbr class="float-end badge text-bg-danger" title="<fmt:message key='label.answer.required'/>"><i
							class="fa fa-xs fa-asterisk"></i>
						</abbr>
					</c:if>

					<c:set var="question" value="${element.value}" />
					<c:out value="${question.description}" escapeXml="false" />
				</div>
				
				<div class="card-body">
					<c:set var="answerText" value="" />
					<c:if test="${not empty question.answer}">
						<c:set var="answerText" value="${question.answer.answerText}" />
					</c:if>
					<div class="div-hover">
						<c:forEach var="option" items="${question.options}">
							<c:set var="checked" value="false" />
							<c:if test="${not empty question.answer}">
								<c:forEach var="choice" items="${question.answer.choices}">
									<c:if test="${choice == option.uid}">
										<c:set var="checked" value="true" />
									</c:if>
								</c:forEach>
							</c:if>

							<c:if test="${checked}">
								<div>
									<c:out value="${option.description}" escapeXml="true" />
								</div>
							</c:if>
						</c:forEach>

						<c:if test="${question.type == 3}">
							<div>
								<lams:out value="${answerText}" escapeHtml="false" />
							</div>
						</c:if>
	
						<c:if test="${question.appendText && (not empty answerText)}">
							<div>
								<fmt:message key="label.open.response" />:&nbsp;
								<lams:out value="${answerText}" escapeHtml="true" />
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</c:forEach>

		<%-- other users personal results--%>
		<c:if test="${sessionMap.showOtherUsersAnswers}">
			<div class="card-subheader mb-3">
				<fmt:message key="label.other.answers" />
			
				<span class="alert alert-info badge ms-2 p-2">
					<fmt:message key="label.total.responses">
						<fmt:param>${countFinishedUser}</fmt:param>
					</fmt:message>
				</span>
			</div>

			<c:forEach var="question" items="${answerDtos}" varStatus="queStatus">
				<div class="card lcard mt-n3">
					<div class="card-header">
						<c:out value="${question.shortTitle}" /> <%-- Only show pie/bar chart when question is single/multiple choics type --%>
								
						<c:if test="${question.type != 3}">
							<div class="float-end">
								<c:set var="chartURL" value="${tool}showChart.do?toolSessionID=${sessionMap.toolSessionID}&questionUid=${question.uid}" />
								<button type="button" class="fa fa-pie-chart text-primary btn btn-light"
										title="<fmt:message key='message.view.pie.chart'/>"
										onclick="javascript:drawChart('pie', 'chartDiv${queStatus.index}', '${chartURL}')"></button>
							</div>
						</c:if>
					</div>
						
					<div class="card-body">
						<div class="div-hover mx-2">
							<c:set var="optSize" value="${fn:length(question.options)}" />
							<c:forEach var="option" items="${question.options}" varStatus="optStatus">
								<div class="row">
									<div class="col-9">
										<c:out value="${option.description}" escapeXml="true" />
									</div>
									<div class="col-3 text-center">
										<c:set var="imgTitle">
											<fmt:message key="message.learner.choose.answer.percentage">
												<fmt:param>${option.response}</fmt:param>
											</fmt:message>
										</c:set> 
										${option.responseCount} (${option.responseFormatStr}%)
									</div>
								</div>
							</c:forEach>
							
							<c:if test="${question.appendText}">
								<div class="row">
									<div class="col-9">
										<fmt:message key="label.open.response" />
									</div>
									<div class="col-3 text-center">
										<c:set var="imgTitle">
											<fmt:message key="message.learner.choose.answer.percentage">
												<fmt:param>${question.openResponseFormatStr}</fmt:param>
											</fmt:message>
										</c:set> 
										<c:set var="imgIdx">
											${(optSize % 5)  + 1}
										</c:set> 
										${question.openResponseCount}	(${question.openResponseFormatStr}%)
									</div>
								</div>
							</c:if>
						</div>
						
						<c:if test="${question.type == 3}">
							<lams:TSTable5 numColumns="1" dataId='data-question-uid="${question.uid}"'> 
								<th title="<fmt:message key='label.sort.by.answer'/>"><fmt:message key="label.answer" /></th>
							</lams:TSTable5>
						</c:if>
						<div id="chartDiv${queStatus.index}" colspan="2" class="chartDiv"></div>
							
					</div>
				</div>
			</c:forEach>
		</c:if>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<lams:NotebookReedit
				reflectInstructions="${sessionMap.reflectInstructions}"
				reflectEntry="${sessionMap.reflectEntry}"
				isEditButtonEnabled="true"
				notebookHeaderLabelKey="title.reflection"/>
		</c:if>

		<c:if test="${sessionMap.mode != 'teacher'}">
			<div class="activity-bottom-buttons">
				<c:choose>
					<c:when test="${sessionMap.reflectOn}">
						<button type="button" onclick="return continueReflect()" class="btn btn-primary na">
							<fmt:message key="label.continue" />
						</button>
					</c:when>

					<c:otherwise>
						<button type="submit" id="finishButton" onclick="return finishSession()" class="btn btn-primary na">
							<c:choose>
								<c:when test="${sessionMap.isLastActivity}">
									<fmt:message key="label.submit" />
								</c:when>

								<c:otherwise>
									<fmt:message key="label.finished" />
								</c:otherwise>
							</c:choose>
						</button>
					</c:otherwise>
				</c:choose>

				<c:if test="${not sessionMap.lockOnFinish}">
					<button type="button" onclick="return retakeSurvey()" class="btn btn-secondary btn-icon-return me-2">
						<fmt:message key="label.retake.survey" />
					</button>
				</c:if>
			</div>
		</c:if>

	</div>
</lams:PageLearner>
