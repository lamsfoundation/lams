<%@ include file="/taglibs.jsp"%>

<lams:css webapp="monitoring" suffix="monitorLesson"/>
<style>
	#content {
		padding: 0 20px;
	}
	#sequenceCanvas {
		height: auto !important;
	}
</style>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" >
	$(document).bind("mobileinit", function(){
	  $.mobile.loadingMessage = false;
	  $.mobile.ignoreContentEnabled = true;
	  $('body').attr('data-enhance', 'false');
	});
</script>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/readmore.min.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/d3.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/chart.js"></script>
<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/monitorLesson.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.tabcontroller.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/bootstrap-tourist.min.js"></script> 
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/dialog.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/portrait.js"></script>
<script>
	var lessonId = ${lesson.lessonId},
	userId = '<lams:user property="userID"/>',
	ldId = ${lesson.learningDesign.learningDesignId},
	lessonStateId = ${lesson.lessonStateId},
	// settings for progress bar
	isHorizontalBar = true,
	hasContentFrame = false,
	presenceEnabled =  false,
	hasDialog = false,
	enableExportPortfolio = false,
	sequenceTabShowInfo = true,
	LAMS_URL = '<lams:LAMSURL/>',
	decoderDiv = $('<div />'),
	LABELS = {
		<fmt:message key="force.complete.click" var="FORCE_COMPLETE_CLICK_VAR"/>
		FORCE_COMPLETE_CLICK : decoderDiv.html('<c:out value="${FORCE_COMPLETE_CLICK_VAR}" />').text(),
		<fmt:message key="button.force.complete" var="FORCE_COMPLETE_BUTTON_VAR"/>
		FORCE_COMPLETE_BUTTON : '<c:out value="${FORCE_COMPLETE_BUTTON_VAR}" />',
		<fmt:message key="force.complete.end.lesson.confirm" var="FORCE_COMPLETE_END_LESSON_CONFIRM_VAR"/>
		FORCE_COMPLETE_END_LESSON_CONFIRM : decoderDiv.html('<c:out value="${FORCE_COMPLETE_END_LESSON_CONFIRM_VAR}" />').text(),
		<fmt:message key="force.complete.activity.confirm" var="FORCE_COMPLETE_ACTIVITY_CONFIRM_VAR"/>
		FORCE_COMPLETE_ACTIVITY_CONFIRM : decoderDiv.html('<c:out value="${FORCE_COMPLETE_ACTIVITY_CONFIRM_VAR}" />').text(),
		<fmt:message key="force.complete.remove.content" var="FORCE_COMPLETE_REMOVE_CONTENT_VAR"/>
		FORCE_COMPLETE_REMOVE_CONTENT : decoderDiv.html('<c:out value="${FORCE_COMPLETE_REMOVE_CONTENT_VAR}" />').text(),
		<fmt:message key="force.complete.remove.content.yes" var="FORCE_COMPLETE_REMOVE_CONTENT_YES_VAR"/>
		FORCE_COMPLETE_REMOVE_CONTENT_YES : '<c:out value="${FORCE_COMPLETE_REMOVE_CONTENT_YES_VAR}" />',
		<fmt:message key="force.complete.remove.content.no" var="FORCE_COMPLETE_REMOVE_CONTENT_NO_VAR"/>
		FORCE_COMPLETE_REMOVE_CONTENT_NO : '<c:out value="${FORCE_COMPLETE_REMOVE_CONTENT_NO_VAR}" />',
		<fmt:message key="force.complete.drop.fail" var="FORCE_COMPLETE_DROP_FAIL_VAR"/>
		FORCE_COMPLETE_DROP_FAIL : '<c:out value="${FORCE_COMPLETE_DROP_FAIL_VAR}" />',
		<fmt:message key="learner.group.count" var="LEARNER_GROUP_COUNT_VAR"/>
		LEARNER_GROUP_COUNT : '<c:out value="${LEARNER_GROUP_COUNT_VAR}" />',
		<fmt:message key="learner.group.show" var="LEARNER_GROUP_SHOW_VAR"/>
		LEARNER_GROUP_SHOW : '<c:out value="${LEARNER_GROUP_SHOW_VAR}" />',
		<fmt:message key="learner.group.remove.progress" var="LEARNER_GROUP_REMOVE_PROGRESS_VAR"/>
		LEARNER_GROUP_REMOVE_PROGRESS : decoderDiv.html('<c:out value="${LEARNER_GROUP_REMOVE_PROGRESS_VAR}" />').text(),
		<fmt:message key="button.view.learner" var="VIEW_LEARNER_BUTTON_VAR"/>
		VIEW_LEARNER_BUTTON : '<c:out value="${VIEW_LEARNER_BUTTON_VAR}" />',
		<fmt:message key="button.email" var="EMAIL_BUTTON_VAR"/>
		EMAIL_BUTTON : '<c:out value="${EMAIL_BUTTON_VAR}" />',
		<fmt:message key="button.close" var="CLOSE_BUTTON_VAR"/>
		CLOSE_BUTTON : '<c:out value="${CLOSE_BUTTON_VAR}" />',
		<fmt:message key="email.notifications" var="NOTIFCATIONS_VAR"/>
		NOTIFCATIONS : '<c:out value="${NOTIFCATIONS_VAR}" />',
		<fmt:message key="button.save" var="SAVE_BUTTON_VAR"/>
		SAVE_BUTTON : '<c:out value="${SAVE_BUTTON_VAR}" />',
		<fmt:message key="button.cancel" var="CANCEL_BUTTON_VAR"/>
		CANCEL_BUTTON : '<c:out value="${CANCEL_BUTTON_VAR}" />',
		<fmt:message key="learner.finished.count" var="LEARNER_FINISHED_COUNT_VAR"/>
		LEARNER_FINISHED_COUNT : '<c:out value="${LEARNER_FINISHED_COUNT_VAR}" />',
		<fmt:message key="learner.finished.dialog.title" var="LEARNER_FINISHED_DIALOG_TITLE_VAR"/>
		LEARNER_FINISHED_DIALOG_TITLE : '<c:out value="${LEARNER_FINISHED_DIALOG_TITLE_VAR}" />',
		<fmt:message key="lesson.enable.presence.alert" var="LESSON_PRESENCE_ENABLE_ALERT_VAR"/>
		LESSON_PRESENCE_ENABLE_ALERT : decoderDiv.html('<c:out value="${LESSON_PRESENCE_ENABLE_ALERT_VAR}" />').text(),
		<fmt:message key="lesson.disable.presence.alert" var="LESSON_PRESENCE_DISABLE_ALERT_VAR"/>
		LESSON_PRESENCE_DISABLE_ALERT : decoderDiv.html('<c:out value="${LESSON_PRESENCE_DISABLE_ALERT_VAR}" />').text(),
		<fmt:message key="lesson.enable.im.alert" var="LESSON_IM_ENABLE_ALERT_VAR"/>
		LESSON_IM_ENABLE_ALERT : decoderDiv.html('<c:out value="${LESSON_IM_ENABLE_ALERT_VAR}" />').text(),
		<fmt:message key="lesson.disable.im.alert" var="LESSON_IM_DISABLE_ALERT_VAR"/>
		LESSON_IM_DISABLE_ALERT : decoderDiv.html('<c:out value="${LESSON_IM_DISABLE_ALERT_VAR}" />').text(),
		<fmt:message key="lesson.remove.alert" var="LESSON_REMOVE_ALERT_VAR"/>
		LESSON_REMOVE_ALERT : decoderDiv.html('<c:out value="${LESSON_REMOVE_ALERT_VAR}" />').text(),
		<fmt:message key="lesson.remove.doublecheck.alert" var="LESSON_REMOVE_DOUBLECHECK_ALERT_VAR"/>
		LESSON_REMOVE_DOUBLECHECK_ALERT : decoderDiv.html('<c:out value="${LESSON_REMOVE_DOUBLECHECK_ALERT_VAR}" />').text(),
		<fmt:message key="lesson.state.created" var="LESSON_STATE_CREATED_VAR"/>
		LESSON_STATE_CREATED : '<c:out value="${LESSON_STATE_CREATED_VAR}" />',
		<fmt:message key="lesson.state.scheduled" var="LESSON_STATE_SCHEDULED_VAR"/>
		LESSON_STATE_SCHEDULED : '<c:out value="${LESSON_STATE_SCHEDULED_VAR}" />',
		<fmt:message key="lesson.state.started" var="LESSON_STATE_STARTED_VAR"/>
		LESSON_STATE_STARTED : '<c:out value="${LESSON_STATE_STARTED_VAR}" />',
		<fmt:message key="lesson.state.suspended" var="LESSON_STATE_SUSPENDED_VAR"/>
		LESSON_STATE_SUSPENDED : '<c:out value="${LESSON_STATE_SUSPENDED_VAR}" />',
		<fmt:message key="lesson.state.finished" var="LESSON_STATE_FINISHED_VAR"/>
		LESSON_STATE_FINISHED : '<c:out value="${LESSON_STATE_FINISHED_VAR}" />',
		<fmt:message key="lesson.state.archived" var="LESSON_STATE_ARCHIVED_VAR"/>
		LESSON_STATE_ARCHIVED : '<c:out value="${LESSON_STATE_ARCHIVED_VAR}" />',
		<fmt:message key="lesson.state.removed" var="LESSON_STATE_REMOVED_VAR"/>
		LESSON_STATE_REMOVED : '<c:out value="${LESSON_STATE_REMOVED_VAR}" />',
		<fmt:message key="lesson.state.action.disable" var="LESSON_STATE_ACTION_DISABLE_VAR"/>
		LESSON_STATE_ACTION_DISABLE : '<c:out value="${LESSON_STATE_ACTION_DISABLE_VAR}" />',
		<fmt:message key="lesson.state.action.activate" var="LESSON_STATE_ACTION_ACTIVATE_VAR"/>
		LESSON_STATE_ACTION_ACTIVATE : '<c:out value="${LESSON_STATE_ACTION_ACTIVATE_VAR}" />',
		<fmt:message key="lesson.state.action.remove" var="LESSON_STATE_ACTION_REMOVE_VAR"/>
		LESSON_STATE_ACTION_REMOVE : '<c:out value="${LESSON_STATE_ACTION_REMOVE_VAR}" />',
		<fmt:message key="lesson.state.action.archive" var="LESSON_STATE_ACTION_ARCHIVE_VAR"/>
		LESSON_STATE_ACTION_ARCHIVE : '<c:out value="${LESSON_STATE_ACTION_ARCHIVE_VAR}" />',
		<fmt:message key="error.lesson.schedule.date" var="LESSON_ERROR_SCHEDULE_DATE_VAR"/>
		LESSON_ERROR_SCHEDULE_DATE : decoderDiv.html('<c:out value="${LESSON_ERROR_SCHEDULE_DATE_VAR}" />').text(),
		<fmt:message key="button.edit.class" var="LESSON_EDIT_CLASS_VAR"/>
		LESSON_EDIT_CLASS : '<c:out value="${LESSON_EDIT_CLASS_VAR}" />',
		<fmt:message key="lesson.group.dialog.class" var="LESSON_GROUP_DIALOG_CLASS_VAR"/>
		LESSON_GROUP_DIALOG_CLASS : '<c:out value="${LESSON_GROUP_DIALOG_CLASS_VAR}" />',
		<fmt:message key="label.learner.progress.activity.current.tooltip" var="CURRENT_ACTIVITY_VAR"/>
		CURRENT_ACTIVITY : '<c:out value="${CURRENT_ACTIVITY_VAR}" />',
		<fmt:message key="label.learner.progress.activity.completed.tooltip" var="COMPLETED_ACTIVITY_VAR"/>
		COMPLETED_ACTIVITY : '<c:out value="${COMPLETED_ACTIVITY_VAR}" />',
		<fmt:message key="label.learner.progress.activity.attempted.tooltip" var="ATTEMPTED_ACTIVITY_VAR"/>
		ATTEMPTED_ACTIVITY : '<c:out value="${ATTEMPTED_ACTIVITY_VAR}" />',
		<fmt:message key="label.learner.progress.activity.tostart.tooltip" var="TOSTART_ACTIVITY_VAR"/>
		TOSTART_ACTIVITY : '<c:out value="${TOSTART_ACTIVITY_VAR}" />',
		<fmt:message key="label.learner.progress.activity.support.tooltip" var="SUPPORT_ACTIVITY_VAR"/>
		SUPPORT_ACTIVITY : '<c:out value="${SUPPORT_ACTIVITY_VAR}" />',
		<fmt:message key="label.learner.progress.not.started" var="PROGRESS_NOT_STARTED_VAR"/>
		PROGRESS_NOT_STARTED : '<c:out value="${PROGRESS_NOT_STARTED_VAR}" />',
		<fmt:message key="button.export" var="EXPORT_PORTFOLIO_VAR"/>
		EXPORT_PORTFOLIO : '<c:out value="${EXPORT_PORTFOLIO_VAR}" />',
		<fmt:message key="button.export.learner.tooltip" var="EXPORT_PORTFOLIO_LEARNER_TOOLTIP_VAR"/>
		EXPORT_PORTFOLIO_LEARNER_TOOLTIP : '<c:out value="${EXPORT_PORTFOLIO_LEARNER_TOOLTIP_VAR}" />',
	    <fmt:message key="button.timechart" var="TIME_CHART_VAR"/>
	    TIME_CHART : '<c:out value="${TIME_CHART_VAR}" />',
	    <fmt:message key="button.timechart.tooltip" var="TIME_CHART_TOOLTIP_VAR"/>
	    TIME_CHART_TOOLTIP : '<c:out value="${TIME_CHART_TOOLTIP_VAR}" />',
		<fmt:message key="button.live.edit.confirm" var="LIVE_EDIT_CONFIRM_VAR"/>
		LIVE_EDIT_CONFIRM : decoderDiv.html('<c:out value="${LIVE_EDIT_CONFIRM_VAR}" />').text(),
		<fmt:message key="lesson.task.gate" var="CONTRIBUTE_GATE_VAR"/>
		CONTRIBUTE_GATE : '<c:out value="${CONTRIBUTE_GATE_VAR}" />',
		<fmt:message key="lesson.task.grouping" var="CONTRIBUTE_GROUPING_VAR"/>
		CONTRIBUTE_GROUPING : '<c:out value="${CONTRIBUTE_GROUPING_VAR}" />',
		<fmt:message key="lesson.task.branching" var="CONTRIBUTE_BRANCHING_VAR"/>
		CONTRIBUTE_BRANCHING : '<c:out value="${CONTRIBUTE_BRANCHING_VAR}" />',
		<fmt:message key="lesson.task.content.edited" var="CONTRIBUTE_CONTENT_EDITED_VAR"/>
		CONTRIBUTE_CONTENT_EDITED : '<c:out value="${CONTRIBUTE_CONTENT_EDITED_VAR}" />',
		<fmt:message key="button.task.go.tooltip" var="CONTRIBUTE_TOOLTIP_VAR"/>
		CONTRIBUTE_TOOLTIP : '<c:out value="${CONTRIBUTE_TOOLTIP_VAR}" />',
		<fmt:message key="button.task.go" var="CONTRIBUTE_BUTTON_VAR"/>
		CONTRIBUTE_BUTTON : '<c:out value="${CONTRIBUTE_BUTTON_VAR}" />',
		<fmt:message key="lesson.task.attention" var="CONTRIBUTE_ATTENTION_VAR"/>
		CONTRIBUTE_ATTENTION : '<c:out value="${CONTRIBUTE_ATTENTION_VAR}" />'
	}

	$(document).ready(function(){
		initSequenceTab();
		updateSequenceTab();

		//Disable sequence button handler 
		$('#disable-sequence-button').click(function () {
	        var method = (lessonStateId == 3) ? "suspendLesson.do" : "unsuspendLesson.do";
			$.ajax({
				url : LAMS_URL + 'monitoring/monitoring/'+method,
				type: 'POST',
				cache : false,
				dataType : 'xml',
				data : {
					'lessonID'  : '${lesson.lessonId}',
					'<csrf:tokenname/>' : '<csrf:tokenvalue/>',
				},
				success : function() {
			       	if (lessonStateId == 3) {
			       		lessonStateId = 4;
			       		$("#disable-sequence-label").html("<fmt:message key='lesson.state.action.activate'/>");
			       		
				    } else {
			       		lessonStateId = 3;
			       		$("#disable-sequence-label").html("<fmt:message key='lesson.state.action.disable'/>");
					}
				}
			});
		});

		// assigne svg 'auto' width and height 
		if (sequenceCanvas) {
			var svg = $('svg','#sequenceCanvas');
			if ( svg ) {
				var svgWidth = svg.attr('width'),
					svgHeight = svg.attr('height');
				if ( svgWidth > 280 ) {
					svg.attr('width', '100%');
					svg.attr('height', '100%');
					svg.css('max-width', svgWidth);
					svg.css('max-height', svgHeight);
				} 
				$('#sequenceCanvas').css('width', 'auto').css('height', 'auto');
			}
		}
	});
</script>
		
<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<fmt:message key="label.sequence.diagram"/>
		</h3>
	</div>
</div>
<!-- End header -->    

<!-- Notifications -->  
<c:if test="${(lesson.lessonStateId == 3) || (lesson.lessonStateId == 4) }">
	<div class="row no-gutter">
		<div class="col-xs-6 col-md-4 col-lg-4 ">
			<button type="button" id="disable-sequence-button" class="btn btn-sm btn-default">
				<span id="disable-sequence-label">
					<c:choose>
						<c:when test="${lesson.lessonStateId == 4}">
							<fmt:message key="lesson.state.action.activate"/>
						</c:when>
						<c:otherwise>
							<fmt:message key="lesson.state.action.disable"/>
						</c:otherwise>
					</c:choose>
				</span>
			</button>
		</div>                                 
	</div>
	<br>
</c:if>
<!-- End notifications -->              

<!-- Tables -->
	<div class="row no-gutter">
	<div class="col-xs-12">
	<div class="panel panel-success">
	
	<div id="tabSequence">
		<div id="sequenceCanvas"></div>
		<div id="completedLearnersContainer" title="<fmt:message key='force.complete.end.lesson.tooltip' />">
			<img id="completedLearnersDoorIcon" src="<lams:LAMSURL/>images/icons/door_open.png" />
		</div>
		<img id="sequenceCanvasLoading"
		     src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
		<img id="sequenceSearchedLearnerHighlighter"
		     src="<lams:LAMSURL/>images/pedag_down_arrow.gif" />
	</div>
		
	</div>  
	</div>
	</div>
<!-- End tables -->

	<div id="learnerGroupDialogContents" class="dialogContainer">
		<span id="learnerGroupMultiSelectLabel"><fmt:message key='learner.group.multi.select'/></span>
		<table id="listLearners" class="table table-condensed">
			<tr id="learnerGroupSearchRow">
				<td>
					<span class="dialogSearchPhraseIcon fa fa-xs fa-search"
						  title="<fmt:message key='search.learner.textbox' />"></span>
				</td>
				<td colspan="4">
					<input class="dialogSearchPhrase" 
						   title="<fmt:message key='search.learner.textbox' />"/>
				</td>
				<td>
					<span class="dialogSearchPhraseClear fa fa-xs fa-times-circle"
						  onClick="javascript:learnerGroupClearSearchPhrase()"
						  title="<fmt:message key='learners.search.phrase.clear.tooltip' />" 
					></span>
				</td>
			</tr>
			<tr>
				<td class="navCell pageMinus10Cell"
					title="<fmt:message key='learner.group.backward.10'/>"
					onClick="javascript:shiftLearnerGroupList(-10)">
						<span class="ui-icon ui-icon-seek-prev"></span>
				</td>
				<td class="navCell pageMinus1Cell"
					title="<fmt:message key='learner.group.backward.1'/>"
					onClick="javascript:shiftLearnerGroupList(-1)">
					<span class="ui-icon ui-icon-arrowthick-1-w"></span>
				</td>
				<td class="pageCell"
					title="<fmt:message key='learners.page'/>">
				</td>
				<td class="navCell pagePlus1Cell"
					title="<fmt:message key='learner.group.forward.1'/>"
					onClick="javascript:shiftLearnerGroupList(1)">
						<span class="ui-icon ui-icon-arrowthick-1-e"></span>
				</td>
				<td class="navCell pagePlus10Cell" 
					title="<fmt:message key='learner.group.forward.10'/>"
					onClick="javascript:shiftLearnerGroupList(10)">
						<span class="ui-icon ui-icon-seek-next"></span>
				</td>
				<td class="navCell sortCell" 
					title="<fmt:message key='learner.group.sort.button'/>" 
					onClick="javascript:sortLearnerGroupList()">
						<span class="ui-icon ui-icon-triangle-1-n"></span>
				</td>
			</tr>
			<tr>
				<td colspan="6" class="dialogList"></td>
			</tr>
		</table>
		<div class="btn-group pull-right">
			<button id="learnerGroupDialogForceCompleteButton" class="learnerGroupDialogSelectableButton btn btn-default roffset5">
				<span><fmt:message key="button.force.complete" /></span>
			</button>
			<button id="learnerGroupDialogViewButton" class="learnerGroupDialogSelectableButton btn btn-default roffset5">
				<span><fmt:message key="button.view.learner" /></span>
			</button>
			<button id="learnerGroupDialogEmailButton" class="learnerGroupDialogSelectableButton btn btn-default roffset5">
				<span><fmt:message key="button.email" /></span>
			</button>
			<button id="learnerGroupDialogCloseButton" class="btn btn-default">
				<span><fmt:message key="button.close" /></span>
			</button>
		</div>
	</div>
	
	<div id="saveSequenceDialogContents" class="dialogContainer" title="Save sequence">
	</div>
	
	<div id="sequenceInfoDialogContents" class="dialogContainer">
	  <fmt:message key="sequence.help.info"/>
	</div>
		
	<div id="forceBackwardsDialogContents" class="dialogContainer">
		<div id="forceBackwardsMsg"></div>
        <div class="btn-group pull-right voffset10">

               <button id="forceBackwardsRemoveContentNoButton" class="btn btn-default roffset5">
                       <span><fmt:message key="force.complete.remove.content.no"/></span>
               </button>

               <button id="forceBackwardsRemoveContentYesButton" class="btn btn-default roffset5">
                       <span><fmt:message key="force.complete.remove.content.yes" /></span>
               </button>

               <button id="forceBackwardsCloseButton" class="btn btn-default">
                       <span><fmt:message key="button.close" /></span>
               </button>
       </div>
	</div>
