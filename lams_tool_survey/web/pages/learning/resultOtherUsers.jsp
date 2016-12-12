<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link type="text/css" href="${lams}/css/chart.css" rel="stylesheet" />
	<style media="screen,projection" type="text/css">
#other-users-answers-title {
	margin-top: 20px;
}

table.alternative-color td:first-child {
	width: 25%;
}

.chartDiv {
	display: none;
	height: 220px;
	padding 0;
}
</style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
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

		function finishSession() {
			document.getElementById("finishButton").disabled = true;
			document.location.href = '<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}

		function continueReflect() {
			document.location.href = '<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}

		function retakeSurvey() {
			document.location.href = '<c:url value="/learning/retake.do?sessionMapID=${sessionMapID}"/>';
		}
	</script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${sessionMap.title}">
		<div class="panel">
			<c:out value="${sessionMap.instructions}" escapeXml="false" />
		</div>

		<c:if test="${sessionMap.lockOnFinish and sessionMap.mode != 'teacher'}">
			<lams:Alert id="activityLocked" type="info" close="true">
				<fmt:message key="message.activityLocked" />
			</lams:Alert>
		</c:if>

		<%-- user personal results--%>
		<c:forEach var="element" items="${sessionMap.questionList}">
			<div class="sbox voffset10">
				<div class="sbox-heading clearfix">
					<c:if test="${not question.optional}">
						<abbr class="pull-right" title="<fmt:message key='label.answer.required'/>"><i
							class="fa fa-xs fa-asterisk text-danger pull-right"></i></abbr>
					</c:if>

					<c:set var="question" value="${element.value}" />

					<c:out value="${question.description}" escapeXml="false" />

				</div>
				<div class="sbox-body">

					<c:set var="answerText" value="" />
					<c:if test="${not empty question.answer}">
						<c:set var="answerText" value="${question.answer.answerText}" />
					</c:if>
					<c:forEach var="option" items="${question.options}">
						<div>
							<c:set var="checked" value="false" />

							<c:if test="${not empty question.answer}">
								<c:forEach var="choice" items="${question.answer.choices}">
									<c:if test="${choice == option.uid}">
										<c:set var="checked" value="true" />
									</c:if>
								</c:forEach>
							</c:if>

							<c:if test="${checked}">
								<c:out value="${option.description}" escapeXml="true" />
							</c:if>
						</div>
					</c:forEach>


					<c:if test="${question.type == 3}">
						<lams:out value="${answerText}" escapeHtml="false" />
					</c:if>

					<c:if test="${question.appendText && (not empty answerText)}">
						<fmt:message key="label.append.text" />
						<lams:out value="${answerText}" escapeHtml="true" />
					</c:if>
				</div>
			</div>
		</c:forEach>

		<c:if test="${not sessionMap.lockOnFinish}">
			<html:button property="RetakeButton" onclick="return retakeSurvey()" styleClass="btn btn-sm btn-default pull-left">
				<fmt:message key="label.retake.survey" />
			</html:button>
		</c:if>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="voffset10 panel panel-default">
				<div class="panel-heading panel-title">
					<fmt:message key="title.reflection" />
				</div>
				<div class="panel-body">
					<div class="panel">
						<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
					</div>

					<c:choose>
						<c:when test="${empty sessionMap.reflectEntry}">
							<p>
								<fmt:message key="message.no.reflection.available" />
							</p>
						</c:when>
						<c:otherwise>
							<div class="panel-body bg-warning">
								<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
							</div>
						</c:otherwise>
					</c:choose>

					<html:button property="ContinueButton" onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</div>
			</div>
		</c:if>

		<%-- other users personal results--%>
		<c:if test="${sessionMap.showOtherUsersAnswers}">
			<h4 id="other-users-answers-title">
				<fmt:message key="label.other.answers" />
			</h4>

			<div>
				<fmt:message key="label.total.responses">
					<fmt:param>${countFinishedUser}</fmt:param>
				</fmt:message>
			</div>

			<c:forEach var="question" items="${answerDtos}" varStatus="queStatus">
				<div class="table-responsive">
					<table class="table table-hover table-condensed">
						<tr>
							<th class="first" colspan="2"><c:out value="${question.shortTitle}" /> <%-- Only show pie/bar chart when question is single/multiple choics type --%>
								<c:if test="${question.type != 3}">
									<div class="pull-right">
										<c:set var="chartURL"
											value="${tool}showChart.do?toolSessionID=${sessionMap.toolSessionID}&questionUid=${question.uid}" />
										<a class="fa fa-lg fa-pie-chart text-primary btn btn-md btn-primary"
											title="<fmt:message key='message.view.pie.chart'/>"
											onclick="javascript:drawChart('pie', 'chartDiv${queStatus.index}', '${chartURL}')"></a>
									</div>
								</c:if></th>
						</tr>

						<c:set var="optSize" value="${fn:length(question.options)}" />
						<c:forEach var="option" items="${question.options}" varStatus="optStatus">
							<tr>
								<td><c:out value="${option.description}" escapeXml="true" /></td>
								<td class="text-center"><c:set var="imgTitle">
										<fmt:message key="message.learner.choose.answer.percentage">
											<fmt:param>${option.response}</fmt:param>
										</fmt:message>
									</c:set> ${option.responseCount} (${option.responseFormatStr}%)</td>
							</tr>
						</c:forEach>
						<tr>
							<td id="chartDiv${queStatus.index}" colspan="2" class="chartDiv"></td>
						</tr>
						<c:if test="${question.appendText}">
							<tr>
								<td><fmt:message key="label.open.response" /></td>
								<td class="text-center"><c:set var="imgTitle">
										<fmt:message key="message.learner.choose.answer.percentage">
											<fmt:param>${question.openResponseFormatStr}</fmt:param>
										</fmt:message>
									</c:set> <c:set var="imgIdx">
								${(optSize % 5)  + 1}
							</c:set> ${question.openResponseCount} (${question.openResponseFormatStr}%)</td>
							</tr>
						</c:if>
						<c:if test="${question.type == 3}">
							<tr>
								<td colspan="2">
									<lams:TSTable numColumns="1" dataId='data-question-uid="${question.uid}"' test="2"> 
										<th title="<fmt:message key='label.sort.by.answer'/>"><fmt:message key="label.answer" /></th>
									</lams:TSTable>
									
								</td>
							</tr>
						</c:if>

					</table>
			</c:forEach>
		</c:if>

		<c:if test="${sessionMap.mode != 'teacher'}">
			<div class="voffset10">
				<c:choose>

					<c:when test="${sessionMap.reflectOn}">
						<html:button property="ContinueButton" onclick="return continueReflect()" styleClass="button">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>

					<c:otherwise>
						<html:link href="#nogo" property="FinishButton" styleId="finishButton" onclick="return finishSession()"
							styleClass="btn btn-sm btn-primary pull-right na">
							<span class="nextActivity"> <c:choose>
									<c:when test="${sessionMap.activityPosition.last}">
										<fmt:message key="label.submit" />
									</c:when>

									<c:otherwise>
										<fmt:message key="label.finished" />
									</c:otherwise>
								</c:choose>
							</span>
						</html:link>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

		<div id="footer"></div>
		<!--closes footer-->
	</lams:Page>
</body>
</lams:html>


