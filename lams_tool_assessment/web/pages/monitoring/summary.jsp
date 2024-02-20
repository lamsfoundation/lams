<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>
<c:set var="showQuestionMonitoringActionButtons" value="${not empty sessionDtos}" />
<c:set var="timeLimitPanelUrl"><lams:LAMSURL/>monitoring/timeLimit.jsp</c:set>
<c:url var="timeLimitPanelUrl" value="${timeLimitPanelUrl}">
	<c:param name="toolContentId" value="${assessment.contentId}"/>
	<c:param name="absoluteTimeLimitFinish" value="${assessment.absoluteTimeLimitFinishSeconds}"/>
	<c:param name="relativeTimeLimit" value="${assessment.relativeTimeLimit}"/>
	<c:param name="absoluteTimeLimit" value="${assessment.absoluteTimeLimit}"/>
	<c:param name="isTbl" value="false" />
	<c:param name="controllerContext" value="tool/laasse10/monitoring" />
</c:url>

<link href="${lams}css/jquery-ui-bootstrap-theme5.css" rel="stylesheet" type="text/css">
<link href="${lams}css/jquery-ui.timepicker.css" rel="stylesheet" type="text/css">
<link href="${lams}css/thickbox.css" rel="stylesheet" rel="stylesheet" type="text/css">
<link href="${lams}css/free.ui.jqgrid.custom.css" rel="stylesheet" type="text/css" >
<lams:css suffix="chart"/>
<link href="${lams}css/jquery.jqGrid.confidence-level-formattter.css" rel="stylesheet" type="text/css">
<c:if test="${not empty codeStyles}">
	<link rel="stylesheet" type="text/css" href="${lams}css/codemirror.css" />
</c:if>
<style>
	.ui-jqdialog.ui-jqgrid-bootstrap .ui-jqdialog-titlebar, .ui-jqgrid.ui-jqgrid-bootstrap .ui-jqgrid-caption {
		background-color: #f5f5f5;
	}
	
	#completion-charts-container>div {
		padding: 5rem 2rem;
	}
	
	pre {
		background-color: initial;
		border: none;
	}
	
	.requires-grading {
		background-color: rgba(255, 195, 55, .6);
	}

	.question-title {
		overflow: auto;
		min-width: 150px;
	}

	canvas {
		width: 100% !important;
		height: 100% !important;
	}

	#activity-completion-chart {
		max-width: 400px;
		margin: auto;
	}

	#questions-data .sticky-left-header {
		position: sticky;
		left: 0;
		background-color: inherit;
	}

	.complete-item-gif {
	    display: none;
	}
</style>

<script>
	var LAMS_URL    = '<lams:LAMSURL />',
		WEB_APP_URL = '<lams:WebAppURL />',
		LABELS = {
			ACTIVITY_COMPLETION_CHART_TITLE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion"/></spring:escapeBody>',
			ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion.possible"/></spring:escapeBody>',
			ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion.started"/></spring:escapeBody>',
			ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion.completed"/></spring:escapeBody>',
			ANSWERED_QUESTIONS_CHART_TITLE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions"/></spring:escapeBody>',
			ANSWERED_QUESTIONS_CHART_TITLE_GROUPS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.groups"/></spring:escapeBody>',
			ANSWERED_QUESTIONS_CHART_X_AXIS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.x.axis"/></spring:escapeBody>',
			ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.y.axis.students"/></spring:escapeBody>',
			ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.y.axis.groups"/></spring:escapeBody>'
		},
		// pass settings to monitorToolSummaryAdvanced.js
	    submissionDeadlineSettings = {
			lams: '<lams:LAMSURL />',
			submissionDeadline: '${submissionDeadline}',
			submissionDateString: '${submissionDateString}',
			setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
			toolContentID: '<c:out value="${param.toolContentID}" />',
			messageNotification: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.notification" /></spring:escapeBody>',
			messageRestrictionSet: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.set" /></spring:escapeBody>',
			messageRestrictionRemoved: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.removed" /></spring:escapeBody>'
		},
		confidenceLevelsSettings = {
			type: "${assessment.confidenceLevelsType}",
			LABEL_NOT_CONFIDENT: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.not.confident" /></spring:escapeBody>',
			LABEL_CONFIDENT: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.confident" /></spring:escapeBody>',
			LABEL_VERY_CONFIDENT: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.very.confident" /></spring:escapeBody>',
			LABEL_NOT_SURE: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.not.sure" /></spring:escapeBody>',
			LABEL_SURE: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.sure" /></spring:escapeBody>',
			LABEL_VERY_SURE: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.very.sure" /></spring:escapeBody>'
		};
</script>
<lams:JSImport src="includes/javascript/common.js" />
<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
<lams:JSImport src="includes/javascript/monitorToolSummaryAdvanced.js" />
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/chartjs/chart.umd.js"></script>
<lams:JSImport src="includes/javascript/chart.js" relative="true" />
<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/download.js"></script>
<lams:JSImport src="includes/javascript/dialog5.js" />
<lams:JSImport src="includes/javascript/portrait5.js" />
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.confidence-level-formattter.js"></script> 		
<c:if test="${not empty codeStyles}">
	<script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/runmode/runmode-standalone.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/runmode/colorize.js"></script>
</c:if>
<%-- codeStyles is a set, so each code style will be listed only once --%>
<c:forEach items="${codeStyles}" var="codeStyle">
	<c:choose>
		<c:when test="${codeStyle == 1}">
			<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/python.js"></script>
		</c:when>
		<c:when test="${codeStyle == 2}">
			<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/javascript.js"></script>
		</c:when>
		<c:when test="${codeStyle >= 3}">
			<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/clike.js"></script>
		</c:when>
	</c:choose>
</c:forEach>

<%@ include file="parts/discloseAnswers.jsp"%>
<script type="text/javascript">
	var activityCompletionChart = null,
			answeredQuestionsChart = null;
	// how often completion charts will be updated
	const COMPLETION_CHART_UPDATE_INTERVAL = 10 * 1000,
			// for allquestions.jsp to know whether to display an additional button
			showQuestionDetailsButton = true;

	$(document).ready(function(){
		loadResultsPane($('#results'), false);
		initializePortraitPopover("<lams:LAMSURL />");

		<c:forEach var="sessionDto" items="${sessionDtos}">
			buildJqgridLearnerTable(${sessionDto.sessionId});
		</c:forEach>
		<c:if test="${sessionMap.isGroupedActivity}">
			buildJqgridLearnerTable('-all-learners');
		</c:if>

		//jqgrid autowidth (http://stackoverflow.com/a/1610197)
		$(window).bind('resize', function() {
			resizeJqgrid(jQuery(".ui-jqgrid-btable"));
		});

		//resize jqGrid on openning of bootstrap collapsible
		$('div[id^="collapse"]').on('shown.bs.collapse', function () {
			resizeJqgrid(jQuery(".ui-jqgrid-btable", this));
		})

		// trigger the resize when the window first opens so that the grid uses all the space available.
		setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);

		drawCompletionCharts(${assessment.contentId}, ${assessment.useSelectLeaderToolOuput}, true);

		$('#time-limit-panel-placeholder').load('${timeLimitPanelUrl}');
	});

	function buildJqgridLearnerTable(sessionId) {
		jQuery("#list" + sessionId).data('sessionId', sessionId).jqGrid({
			multiselect: false,
			datatype: "json",
			url: "<c:url value="/monitoring/getUsers.do"/>?sessionMapID=${sessionMapID}"
					+ (sessionId === '-all-learners' ? "" : "&sessionId=" + sessionId),
			height: '100%',
			autowidth: true,
			shrinkToFit: false,
			pager: 'listPager' + sessionId,
			rowList:[10,20,30,40,50,100],
			rowNum:10,
			viewrecords:true,
			guiStyle: "bootstrap",
			iconSet: 'fontAwesome',
			colNames:[
				'userId',
				'sessionId',
				'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.user.name" /></spring:escapeBody>',
				'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.total" /></spring:escapeBody>',
				'portraitId'
			],
			colModel:[
				{name:'userId', index:'userId', width:0, hidden: true},
				{name:'sessionId', index:'sessionId', width:0, hidden: true},
				{name:'userName', index:'userName', width:570, labelAlign: 'left', 
						searchoptions: { clearSearch: false, attr: {style: "text-align:left;"} }, formatter:userNameFormatter},
				{name:'total', index:'total', width:174, labelAlign:"right", align:"right", formatter:'number', search:false},
				{name:'portraitId', index:'portraitId', width:0, hidden: true},
			],
			ondblClickRow: function(rowid) {
				var sessionId = $(this).data('sessionId');
				var userId = jQuery("#list" + sessionId).getCell(rowid, 'userId');
				var sessionId = jQuery("#list" + sessionId).getCell(rowid, 'sessionId');
				var userSummaryUrl = '<c:url value="/monitoring/userSummary.do?sessionMapID=${sessionMapID}"/>';
				var newUserSummaryHref = userSummaryUrl + "&userID=" + userId + "&sessionId=" + sessionId + "&KeepThis=true&TB_iframe=true&modal=true";
				$("#userSummaryHref").attr("href", newUserSummaryHref);
				$("#userSummaryHref").click();
			},
			onSelectRow: function(rowid) {
				if(rowid == null) {
					rowid=0;
				}
				let tableName = $(this).data('sessionId'),
					table = $("#userSummary" + tableName),
					sessionId = $("#list" + tableName).getCell(rowid, 'sessionId'),
					userId = $("#list" + tableName).getCell(rowid, 'userId'),
					userMasterDetailUrl = '<c:url value="/monitoring/userMasterDetail.do"/>';
				
				//table.clearGridData().setGridParam({gridstate: "visible"}).trigger("reloadGrid");
				$("#masterDetailArea").load(
					userMasterDetailUrl,
					{
						userID: userId,
						sessionId: sessionId,
						tableName: tableName,
						sessionMapID: '${sessionMapID}'
					}
				);
				table.setGridWidth($("#user-summary-container" + tableName).parent().width(), true);
			},
			loadError: function(xhr,st,err) {
				var sessionId = $(this).data('sessionId');
				jQuery("#list" + sessionId).clearGridData();
				$.jgrid.info_dialog("<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.error"/></spring:escapeBody>", "<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.loaderror"/></spring:escapeBody>", "<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.ok"/></spring:escapeBody>");
			},
			loadComplete: function () {
				initializePortraitPopover('<lams:LAMSURL/>');
			}
		})
		<c:if test="${!sessionMap.assessment.useSelectLeaderToolOuput}">
				.jqGrid('filterToolbar', {
					searchOnEnter: false
				})
		</c:if>
				.navGrid("#listPager" + sessionId, {edit:false,add:false,del:false,search:false});

		var oldValue = 0;
		jQuery("#userSummary" + sessionId).jqGrid({
			datatype: "local",
			autoencode:false,
			rowNum: 10000,
			gridstate:"hidden",
			autowidth: true,
			shrinkToFit: false,
			guiStyle: "bootstrap",
			iconSet: 'fontAwesome',
			colNames: [
				'#',
				'questionResultUid',
				'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.question.summary.question" /></spring:escapeBody>',
				'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.user.summary.response" /></spring:escapeBody>',
				<c:if test="${sessionMap.assessment.enableConfidenceLevels}">
					'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.confidence" /></spring:escapeBody>',
				</c:if>
				'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.authoring.basic.list.header.mark" /></spring:escapeBody>',
				'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.user.summary.marker" /></spring:escapeBody>',
				'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.user.summary.marker.comment" /></spring:escapeBody>'
			],
			colModel:[
				{name:'id', index:'id', width:20, labelAlign: 'left', sorttype:"int"},
				{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
				{name:'title', index:'title', width: 200, labelAlign: 'left'},
				{name:'response', index:'response', datatype:'html', width:400, labelAlign: 'left', sortable:false},
				<c:if test="${sessionMap.assessment.enableConfidenceLevels}">
					{name:'confidence', index:'confidence', width: 80, classes: 'vertical-align', formatter: gradientNumberFormatter},
				</c:if>
				{name:'grade', index:'grade', width:80, labelAlign: 'center', sorttype:"float", editable:true,
						editoptions: {size:4, maxlength: 4}, align:"center", classes: 'vertical-align', title : false },
				{name:'marker', index:'marker', width: 110, labelAlign: 'left', title: false},
				{name:'markerComment', index:'markerComment', width:300, labelAlign: 'left', editable:true, edittype: 'textarea',
						sortable: false, editoptions: {maxlength: 3000, rows: 6}, title : false,
						formatter:function(cellvalue, options, rowObject, event) {
							if (event == "edit") {
								cellvalue = cellvalue.replace(/\n/g, '\n<br>');
							}
							return cellvalue;
						},
						unformat:function(cellvalue, options, rowObject) {
							return rowObject.innerText;
						}
				}
			],
			multiselect: false,
			cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>&<csrf:token/>',
			cellEdit: true,
			formatCell: function(rowid, name, value, iRow, iCol){
				if (name != "grade") {
					return value;
				}
				if (value == "-") {
					value = "";
				}
				return value;
			},
			afterEditCell: function (rowid,name,val,iRow,iCol){
				if (name != "grade") {
					return;
				}
				oldValue = eval(val);
			},
			beforeSaveCell : function(rowid, name, val, iRow, iCol) {
				if (name != "grade") {
					return val;
				}
				if (isNaN(val)) {
					return null;
				}

				// get maxGrade attribute which was set in masterDetailLoadUp.jsp
				var maxGrade = jQuery("table#userSummary" + sessionId + " tr#" + iRow
						+ " td[aria-describedby$='_" + name + "']").attr("maxGrade");
				if (+val > +maxGrade) {
					return maxGrade;
				}
			},
			afterSaveCell : function (rowid,name,val,iRow,iCol){
				if (name != "grade") {
					return;
				}
				if (isNaN(val)) {
					jQuery("#userSummary" + sessionId).restoreCell(iRow,iCol);
				} else {
					var parentSelectedRowId = jQuery("#list" + sessionId).getGridParam("selrow");
					var previousTotal =  eval(jQuery("#list" + sessionId).getCell(parentSelectedRowId, 'total'));
					jQuery("#list" + sessionId).setCell(parentSelectedRowId, 'total', previousTotal - oldValue + eval(val), {}, {});
				}
			},
			beforeSubmitCell : function (rowid,name,val,iRow,iCol){
				if (name == "grade" && isNaN(val)) {
					return {nan:true};
				} else {
					var questionResultUid = jQuery("#userSummary" + sessionId).getCell(rowid, 'questionResultUid');
					return {
						questionResultUid:questionResultUid,
						column:name
					};
				}
			},
			afterSubmitCell : function (serverresponse, rowid, name, value, iRow, iCol) {
				if (serverresponse.statusText == "OK") {
					if (serverresponse.responseText != "") {
						$(this).setCell(rowid, 'marker', serverresponse.responseText, {}, {});
					}
					return [true, ""];
				}
			}
		});
	}

	function resizeJqgrid(jqgrids) {
		jqgrids.each(function(index) {
			var gridId = $(this).attr('id');
			var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
			jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
		});
	};

	function userNameFormatter (cellvalue, options, rowObject) {
		return definePortraitPopover(rowObject[4], rowObject[0],  rowObject[2]);
	}

	function exportSummary() {
		var url = "<c:url value='/monitoring/exportSummary.do'/>?<csrf:token/>&sessionMapID=${sessionMapID}&reqID="+(new Date()).getTime();
		return downloadFile(url, 'messageArea_Busy', '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.summary.downloaded"/></spring:escapeBody>', 'messageArea', 'btn-disable-on-submit');
	};

	function showChangeLeaderModal(toolSessionId) {
		let modalContainer = $('#change-leader-modals');
		modalContainer.empty().load(
			'<c:url value="/monitoring/displayChangeLeaderForGroupDialogFromActivity.do" />',
			{
				toolSessionID : toolSessionId
			}, 
			function(){
				modalContainer.children('.modal').modal('show');
			}
		);
	}

	function onChangeLeaderCallback(response, leaderUserId, toolSessionId){
		if (response.isSuccessful) {
			$.ajax({
				'url' : '<c:url value="/monitoring/changeLeaderForGroup.do"/>',
				'type': 'post',
				'cache' : 'false',
				'data': {
					'toolSessionID' : toolSessionId,
					'leaderUserId' : leaderUserId,
					'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
				},
				success : function(){
					alert("<spring:escapeBody javaScriptEscape="true"><fmt:message key='label.monitoring.leader.successfully.changed'/></spring:escapeBody>");
					location.reload();
				},
				error : function(){
					alert("<spring:escapeBody javaScriptEscape="true"><fmt:message key='label.monitoring.leader.not.changed'/></spring:escapeBody>");
				}
			});

		} else {
			alert("<spring:escapeBody javaScriptEscape="true"><fmt:message key='label.monitoring.leader.not.changed'/></spring:escapeBody>");
		}
	}
</script>

<div class="instructions">
	<div class="fs-4">
		<c:out value="${assessment.title}" escapeXml="true"/>
	</div>

	<div class="mt-2">
		<c:out value="${assessment.instructions}" escapeXml="false"/>
	</div>
</div>

<c:choose>
	<c:when test="${empty sessionDtos}">
		<lams:Alert5 type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert5>
	</c:when>
	
	<c:otherwise>
		<div id="completion-charts-container" class="row">
			<div class="col-sm-12 col-md-6">
				<canvas id="activity-completion-chart"></canvas>
			</div>

			<div class="col-sm-12 col-md-6">
				<canvas id="answered-questions-chart"></canvas>
			</div>
		</div>
	</c:otherwise>
</c:choose>
<div style="clear: both"></div>

<c:if test="${not empty sessionDtos}">
	<c:if test="${displayStudentChoices and not empty questions}">
		<div class="card-subheader fs-4">
			<fmt:message key="label.student.choices" />
		</div>
		<%@ include file="/pages/monitoring/parts/mcqStudentChoices.jsp" %>
	</c:if>
	
	<c:if test="${sessionMap.isGroupedActivity and sessionMap.assessment.useSelectLeaderToolOuput and not empty questionDtos}">
		<div class="card-subheader fs-4">
			<fmt:message key="label.groups.choices" />
		</div>

		<div class="table-responsive shadow rounded-4">
		<table id="questions-data" class="table table-hover table-sm">
			<thead class="text-bg-secondary">
				<tr role="row">
					<th></th>
					
					<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
						<th class="text-center py-3">
							<div class="question-title">
								<c:if test="${assessment.numbered}">
									${i.count}.
								</c:if>
								${tblQuestionDto.title}
							</div>
						</th>
					</c:forEach>
				</tr>
			</thead>
							
			<tbody>
				<tr role="row">
					<th class="sticky-left-header">
						Question type
					</th>
					
					<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
						<td class="text-center">
							${tblQuestionDto.questionTypeLabel}
						</td>
					</c:forEach>
				</tr>

				<tr>
					<th class="sticky-left-header">
						Correct answer
					</th>
					<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
						<td class="text-center">
							<c:out value="${tblQuestionDto.correctAnswer}" escapeXml="false" />
						</td>
					</c:forEach>
				</tr>

				<tr>
					<th colspan="${fn:length(questionDtos) + 1}" class="text-bg-secondary">
						<fmt:message key="label.teams"/>
					</th>
				</tr>

				<c:forEach var="session" items="${sessions}" varStatus="i">
					<tr>
						<th class="sticky-left-header">
							${session.sessionName}
						</th>

						<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="j">
							<c:set var="questionResultDto" value="${tblQuestionDto.sessionQuestionResults[i.index]}"/>
							<td class="text-center <c:if test="${questionResultDto.correct}">success</c:if>" >
								${questionResultDto.answer}
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</c:if>

	<div class="mt-4">
		<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
		<div class="clearfix">
			<div class="badge text-bg-info float-end px-5 py-3 mt-2" id="messageArea"></div>
		</div>
	
		<button type="button" onclick="exportSummary();" class="btn btn-secondary btn-sm btn-disable-on-submit float-end mt-2">
			<i class="fa fa-download" aria-hidden="true"></i>
			<fmt:message key="label.monitoring.summary.export.summary" />
		</button>
	
		<div class="card-subheader fs-4">
			<fmt:message key="label.monitoring.summary.summary" />
		</div>
	
		<div class="comments">
			<fmt:message key="label.monitoring.summary.double.click" />
		</div>
	</div>

	<div id="masterDetailArea" class="mt-3"></div>
	<a onclick="" href="return false;" class="thickbox initially-hidden" id="userSummaryHref"></a>

	<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
		<c:choose>
			<c:when test="${sessionMap.isGroupedActivity}">
				<div class="lcard" >
					<div class="card-header" id="heading${sessionDto.sessionId}">
			        	<span class="card-title collapsable-icon-left">
			        		<button class="btn btn-secondary-darker no-shadow ${status.first ? '' : 'collapsed'}" data-bs-toggle="collapse" data-bs-target="#collapse${sessionDto.sessionId}"
									aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionDto.sessionId}" >
								<fmt:message key="monitoring.label.group" />:	<c:out value="${sessionDto.sessionName}" />
							</button>
						</span>
						
						<c:if test="${assessment.useSelectLeaderToolOuput and sessionDto.numberOfLearners > 0 and not sessionDto.leaderFinished}">
							<button type="button" class="btn btn-light btn-sm float-end"
									onClick="javascript:showChangeLeaderModal(${sessionDto.sessionId})">
								<i class="fa-solid fa-user-pen me-1"></i>
								<fmt:message key='label.monitoring.change.leader'/>
							</button>
						</c:if>
					</div>

					<div id="collapse${sessionDto.sessionId}" class="card-body collapse ${status.first ? 'show' : ''}">
			</c:when>
			
			<c:when test="${assessment.useSelectLeaderToolOuput and sessionDto.numberOfLearners > 0 and not sessionDto.leaderFinished}">
				<div class="clearfix">
					<button type="button" class="btn btn-secondary btn-sm float-end mb-3 mt-n3"
							onClick="javascript:showChangeLeaderModal(${sessionDto.sessionId})">
						<i class="fa-solid fa-user-pen me-1"></i>
						<fmt:message key='label.monitoring.change.leader'/>
					</button>
				</div>
			</c:when>
		</c:choose>

		<table id="list${sessionDto.sessionId}"></table>
		
		<div id="user-summary-container${sessionDto.sessionId}" class="mt-4" style="display: none;">
			<table id="userSummary${sessionDto.sessionId}" class="w-100" ></table>
		</div>
		
		<div id="listPager${sessionDto.sessionId}"></div>

		<c:if test="${sessionMap.isGroupedActivity}">
					</div> <!-- end collapse area  -->
				</div> <!-- end collapse card  -->
		</c:if>
	</c:forEach>

	<c:if test="${sessionMap.isGroupedActivity}">
		<div class="lcard" >
			<div class="card-header" id="heading-all-learners">
		        <span class="card-title collapsable-icon-left">
		        	<button type="button" class="btn btn-secondary-darker no-shadow collapsed" data-bs-toggle="collapse" data-bs-target="#collapse-all-learners" 
							aria-expanded="false" aria-controls="collapse-all-learners">
						<fmt:message key="monitoring.label.all.learners" />
					</button>
				</span>
			</div>

			<div id="collapse-all-learners" class="card-body collapse">
				<table id="list-all-learners"></table>
				<div id="user-summary-container-all-learners" class="mt-4" style="display: none;">
					<table id="userSummary-all-learners"></table>
				</div>
				<div id="listPager-all-learners"></div>
			</div>
		</div>
	</c:if>
</c:if>

<div class="assessment-questions-pane mb-5">
	<div class="card-subheader fs-4 mt-4 pb-0">
		<fmt:message key="label.monitoring.summary.report.by.question" />
	</div>

	<c:if test="${showQuestionMonitoringActionButtons and (assessment.allowDiscloseAnswers or vsaPresent)}">
		<%-- Release correct/groups answers for all questions in this assessment --%>
		<div class="disclose-all-button-group clearfix">
			<div class="btn-group float-end" role="group">
				<button type="button" class="btn btn-light disclose-all-correct-button">
					<i class="fa-solid fa-eye me-1"></i>
					<fmt:message key="label.disclose.all.correct.answers"/>
				</button>
				<button type="button" class="btn btn-light disclose-all-groups-button">
					<i class="fa-solid fa-eye me-1"></i>
					<fmt:message key="label.disclose.all.groups.answers"/>
				</button>

				<c:if test="${vsaPresent}">
					<a class="btn btn-light" target="_blank"
							href='<lams:LAMSURL />qb/vsa/displayVsaAllocate.do?toolContentID=${assessment.contentId}'>
						<i class="fa-solid fa-arrow-down-1-9 me-1"></i>
						<fmt:message key="label.vsa.allocate.button" />
					</a>
				</c:if>
			</div>
		</div>
	</c:if>

	<div id="results" class="mt-3" data-tool-content-id="${assessment.contentId}"></div>
</div>

<%@ include file="parts/advanceoptions.jsp"%>

<div id="time-limit-panel-placeholder"></div>

<c:set var="restrictedTitle"><fmt:message key="monitor.summary.date.restriction" /></c:set>
<lams:RestrictedUsageAccordian title="${restrictedTitle}" submissionDeadline="${submissionDeadline}" cssClass="my-2"/>

<div id="change-leader-modals"></div>
