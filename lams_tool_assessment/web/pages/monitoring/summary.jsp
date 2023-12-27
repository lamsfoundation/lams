<%@ include file="/common/taglibs.jsp"%>

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

<style>
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

	#questions-data {
		border-collapse: separate;
	}

	#questions-data .sticky-left-header {
		position: sticky;
		left: 0;
		background-color: inherit;
	}

	#questions-data tbody tr:nth-of-type(even){
		background-color: white;
	}
</style>

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
			resizeJqgrid(jQuery(".ui-jqgrid-btable:visible"));
		});

		//resize jqGrid on openning of bootstrap collapsible
		$('div[id^="collapse"]').on('shown.bs.collapse', function () {
			resizeJqgrid(jQuery(".ui-jqgrid-btable:visible", this));
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
				{name:'userName', index:'userName', width:570, searchoptions: { clearSearch: false }, formatter:userNameFormatter},
				{name:'total', index:'total', width:174, align:"right", formatter:'number', search:false},
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
				var tableName = $(this).data('sessionId');
				var sessionId = jQuery("#list" + tableName).getCell(rowid, 'sessionId');
				var userId = jQuery("#list" + tableName).getCell(rowid, 'userId');
				var userMasterDetailUrl = '<c:url value="/monitoring/userMasterDetail.do"/>';
				jQuery("#userSummary" + tableName).clearGridData().setGridParam({gridstate: "visible"}).trigger("reloadGrid");
				$("#masterDetailArea").load(
						userMasterDetailUrl,
						{
							userID: userId,
							sessionId: sessionId,
							tableName: tableName,
							sessionMapID: '${sessionMapID}'
						}
				);
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
			height: 180,
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
				{name:'id', index:'id', width:20, sorttype:"int"},
				{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
				{name:'title', index:'title', width: 200},
				{name:'response', index:'response', datatype:'html', width:400, sortable:false},
				<c:if test="${sessionMap.assessment.enableConfidenceLevels}">
				{name:'confidence', index:'confidence', width: 80, classes: 'vertical-align', formatter: gradientNumberFormatter},
				</c:if>
				{name:'grade', index:'grade', width:80, sorttype:"float", editable:true,
					editoptions: {size:4, maxlength: 4}, align:"right", classes: 'vertical-align', title : false },
				{name:'marker', index:'marker', width: 80, title: false},
				{name:'markerComment', index:'markerComment', width:120, editable:true, sortable: false,
					editoptions: {maxlength: 100}, align:"left", classes: 'vertical-align', title : false }
			],
			multiselect: false,

			cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>&<csrf:token/>',
			cellEdit: true,
			formatCell: function(rowid, name, value, iRow, iCol){
				if (name != "grade") {
					return value;
				}
				if (value == "-") {
					value = "0";
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
		$('#change-leader-modals').empty()
				.load('<c:url value="/monitoring/displayChangeLeaderForGroupDialogFromActivity.do" />',{
					toolSessionID : toolSessionId
				});
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

<div class="panel">
	<h4>
		<c:out value="${assessment.title}" escapeXml="true"/>
	</h4>

	<div class="instructions voffset5">
		<c:out value="${assessment.instructions}" escapeXml="false"/>
	</div>

	<c:choose>
		<c:when test="${empty sessionDtos}">
			<lams:Alert type="info" id="no-session-summary" close="false">
				<fmt:message key="message.monitoring.summary.no.session" />
			</lams:Alert>
		</c:when>
		<c:otherwise>
			<div id="completion-charts-container">
				<div class="col-sm-12 col-md-6">
					<canvas id="activity-completion-chart"></canvas>
				</div>

				<div class="col-sm-12 col-md-6">
					<canvas id="answered-questions-chart"></canvas>
				</div>
			</div>
		</c:otherwise>
	</c:choose>


	<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
	<div class="voffset5 help-block" id="messageArea"></div>
</div>

<div style="clear: both"></div>

<c:if test="${not empty sessionDtos}">
	<c:if test="${displayStudentChoices and not empty questions}">
		<h5><fmt:message key="label.student.choices" /></h5>
		<%@ include file="/pages/monitoring/parts/mcqStudentChoices.jsp" %>
	</c:if>
	<c:if test="${sessionMap.isGroupedActivity and sessionMap.assessment.useSelectLeaderToolOuput and not empty questionDtos}">
		<h5><fmt:message key="label.groups.choices" /></h5>
		<div class="row no-gutter">
			<div class="col-xs-12 col-md-12 col-lg-12">
				<div class="panel">
					<div class="panel-body">
						<div class="table-responsive" style="margin:0">
							<table id="questions-data" class="table table-responsive table-striped table-bordered table-hover table-condensed">
								<thead>
								<tr role="row">
									<th></th>
									<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
										<th class="text-center">
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
									<th class="sticky-left-header"><b>Question type</b></th>
									<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
										<td class="text-center">
												${tblQuestionDto.questionTypeLabel}
										</td>
									</c:forEach>
								</tr>

								<tr>
									<td class="sticky-left-header"><b>Correct answer</b></td>
									<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
										<td class="text-center">
											<c:out value="${tblQuestionDto.correctAnswer}" escapeXml="false" />
										</td>
									</c:forEach>
								</tr>

								<tr>
									<td colspan="${fn:length(questionDtos) + 1}" style="font-weight: bold;"><fmt:message key="label.teams"/></td>
								</tr>

								<c:forEach var="session" items="${sessions}" varStatus="i">
									<tr>
										<td class="text-center sticky-left-header">
												${session.sessionName}
										</td>

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
					</div>
				</div>
			</div>
		</div>
	</c:if>

	<button onclick="return exportSummary();" class="btn btn-default btn-sm btn-disable-on-submit pull-right">
		<i class="fa fa-download" aria-hidden="true"></i>
		<fmt:message key="label.monitoring.summary.export.summary" />
	</button>

	<h5><fmt:message key="label.monitoring.summary.summary" /></h5>

	<div class="comments">
		<fmt:message key="label.monitoring.summary.double.click" />
	</div>

	<div id="masterDetailArea" class="voffset10"></div>

	<a onclick="" href="return false;" class="thickbox initially-hidden" id="userSummaryHref"></a>

	<c:if test="${sessionMap.isGroupedActivity}">
		<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true">
	</c:if>

	<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">

		<c:choose>
			<c:when test="${sessionMap.isGroupedActivity}">
				<div class="panel panel-default" >
				<div class="panel-heading" id="heading${sessionDto.sessionId}">
			        	<span class="panel-title collapsable-icon-left">
			        		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${sessionDto.sessionId}"
							   aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionDto.sessionId}" >
								<fmt:message key="monitoring.label.group" />:	<c:out value="${sessionDto.sessionName}" />
							</a>
						</span>
					<c:if test="${assessment.useSelectLeaderToolOuput and sessionDto.numberOfLearners > 0 and not sessionDto.leaderFinished}">
						<button type="button" class="btn btn-default btn-xs pull-right"
								onClick="javascript:showChangeLeaderModal(${sessionDto.sessionId})">
							<fmt:message key='label.monitoring.change.leader'/>
						</button>
					</c:if>
				</div>

				<div id="collapse${sessionDto.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}"
				role="tabpanel" aria-labelledby="heading${sessionSummary.sessionId}">
			</c:when>
			<c:when test="${assessment.useSelectLeaderToolOuput and sessionDto.numberOfLearners > 0 and not sessionDto.leaderFinished}">
				<div style="text-align: right">
					<button type="button" class="btn btn-default btn-sm voffset10" style="margin-bottom: 10px;"
							onClick="javascript:showChangeLeaderModal(${sessionDto.sessionId})">
						<fmt:message key='label.monitoring.change.leader'/>
					</button>
				</div>
			</c:when>
		</c:choose>

		<table id="list${sessionDto.sessionId}"></table>
		<div class="voffset10"></div>
		<table id="userSummary${sessionDto.sessionId}"></table>
		<div id="listPager${sessionDto.sessionId}"></div>

		<c:if test="${sessionMap.isGroupedActivity}">
			</div> <!-- end collapse area  -->
			</div> <!-- end collapse panel  -->
			<div class="voffset5">&nbsp;</div>
		</c:if>

	</c:forEach>

	<c:if test="${sessionMap.isGroupedActivity}">
		<div class="panel panel-default" >
			<div class="panel-heading" id="heading-all-learners">
		        	<span class="panel-title collapsable-icon-left">
		        		<a class="collapsed" role="button" data-toggle="collapse" href="#collapse-all-learners"
						   aria-expanded="false" aria-controls="collapse-all-learners" >
							<fmt:message key="monitoring.label.all.learners" />
						</a>
					</span>
			</div>
			<div id="collapse-all-learners" class="panel-collapse collapse"
				 role="tabpanel" aria-labelledby="heading-all-learners">

				<table id="list-all-learners"></table>
				<div class="voffset10"></div>
				<table id="userSummary-all-learners"></table>
				<div id="listPager-all-learners"></div>

			</div>
		</div>
		</div> <!--  end panel group -->
	</c:if>
</c:if>

<h5><fmt:message key="label.monitoring.summary.report.by.question" /></h5>

<div class="assessment-questions-pane">
	<c:if test="${showQuestionMonitoringActionButtons and (assessment.allowDiscloseAnswers or vsaPresent)}">
		<%-- Release correct/groups answers for all questions in this assessment --%>
		<div class="row">
			<div class="col-xs-12 col-md-12 col-lg-12">
				<div class="btn-group-sm pull-right disclose-all-button-group">
					<div class="btn btn-default disclose-all-correct-button">
						<fmt:message key="label.disclose.all.correct.answers"/>
					</div>
					<div class="btn btn-default disclose-all-groups-button">
						<fmt:message key="label.disclose.all.groups.answers"/>
					</div>
				</div>

				<c:if test="${vsaPresent}">
					<a class="btn btn-sm pull-right btn-default roffset5" target="_blank"
					   href='<lams:LAMSURL />qb/vsa/displayVsaAllocate.do?toolContentID=${assessment.contentId}'>
						<fmt:message key="label.vsa.allocate.button" />
					</a>
				</c:if>
			</div>
		</div>
	</c:if>


	<div id="results" class="voffset10" data-tool-content-id="${assessment.contentId}"></div>
</div>


<br/>

<%@ include file="parts/advanceoptions.jsp"%>

<div id="time-limit-panel-placeholder"></div>

<%@ include file="parts/dateRestriction.jsp"%>

<div id="change-leader-modals"></div>