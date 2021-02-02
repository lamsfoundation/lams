<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<c:set var="ALLOW_DIRECT_LESSON_LAUNCH"><%=Configuration.get(ConfigurationKeys.ALLOW_DIRECT_LESSON_LAUNCH)%></c:set>
<c:set var="serverURL"><%=Configuration.get(ConfigurationKeys.SERVER_URL)%></c:set>
<c:if test="${fn:substring(serverURL, fn:length(serverURL)-1, fn:length(serverURL)) != '/'}">
	<c:set var="serverURL">${serverURL}/</c:set>
</c:if>

<lams:html>
<lams:head>

	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui.timepicker.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap-tourist.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/x-editable.css"> 
	<link rel="stylesheet" href="<lams:LAMSURL/>css/x-editable-lams.css"> 
	<lams:css suffix="progressBar"/>
	<lams:css suffix="chart"/>
	<lams:css webapp="monitoring" suffix="monitorLesson"/>
  
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" >
		<!-- Some of the following settings will be used in progressBar.js -->
		var lessonId = ${lesson.lessonID},
			userId = '<lams:user property="userID"/>',
			ldId = ${lesson.learningDesignID},
			lessonStateId = ${lesson.lessonStateID},
			createDateTimeStr = '${lesson.createDateTimeStr}',
			lessonStartDate = '${lesson.scheduleStartDate}',
			lessonEndDate = '${lesson.scheduleEndDate}',
			liveEditEnabled = ${enableLiveEdit && lesson.liveEditEnabled},
			lockedForEdit = '${lesson.lockedForEdit}', 
			lockedForEditUserId = '${lesson.lockedForEditUserId}', 
			lockedForEditUsername = '${lesson.lockedForEditUsername}',
			// settings for progress bar
			isHorizontalBar = true,
			hasContentFrame = false,
			presenceEnabled =  false,
			hasDialog = false,
			sequenceTabShowInfo = ${sequenceTabShowInfo eq true},
			tourInProgress = false,
			LAMS_URL = '<lams:LAMSURL/>',
			csrfToken = '<csrf:tokenname/> : <csrf:tokenvalue/>',
			csrfTokenName = '<csrf:tokenname/>',
			csrfTokenValue = '<csrf:tokenvalue/>',
			decoderDiv = $('<div />'),
			LABELS = {
				<fmt:message key="index.emailnotifications" var="EMAIL_NOTIFICATIONS_TITLE_VAR"/>
				EMAIL_NOTIFICATIONS_TITLE : '<c:out value="${EMAIL_NOTIFICATIONS_TITLE_VAR}" />',
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
				<fmt:message key="force.complete.drop.fail" var="FORCE_COMPLETE_DROP_FAIL_VAR"/>
				FORCE_COMPLETE_DROP_FAIL : '<c:out value="${FORCE_COMPLETE_DROP_FAIL_VAR}" />',
				<fmt:message key="learner.group.count" var="LEARNER_GROUP_COUNT_VAR"/>
				LEARNER_GROUP_COUNT : '<c:out value="${LEARNER_GROUP_COUNT_VAR}" />',
				<fmt:message key="learner.group.show" var="LEARNER_GROUP_SHOW_VAR"/>
				LEARNER_GROUP_SHOW : '<c:out value="${LEARNER_GROUP_SHOW_VAR}" />',
				<fmt:message key="learner.group.remove.progress" var="LEARNER_GROUP_REMOVE_PROGRESS_VAR"/>
				LEARNER_GROUP_REMOVE_PROGRESS : decoderDiv.html('<c:out value="${LEARNER_GROUP_REMOVE_PROGRESS_VAR}" />').text(),
		        <fmt:message key="button.email" var="EMAIL_BUTTON_VAR"/>
		        EMAIL_BUTTON : '<c:out value="${EMAIL_BUTTON_VAR}" />',
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
				<fmt:message key="class.add.all.confirm" var="CLASS_ADD_ALL_CONFIRM_VAR"/>
				CLASS_ADD_ALL_CONFIRM : '<c:out value="${CLASS_ADD_ALL_CONFIRM_VAR}" />',
				<fmt:message key="class.add.all.success" var="CLASS_ADD_ALL_SUCCESS_VAR"/>
				CLASS_ADD_ALL_SUCCESS : '<c:out value="${CLASS_ADD_ALL_SUCCESS_VAR}" />',
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
			    <fmt:message key="button.timechart" var="TIME_CHART_VAR"/>
			    TIME_CHART : '<c:out value="${TIME_CHART_VAR}" />',
			    <fmt:message key="button.timechart.tooltip" var="TIME_CHART_TOOLTIP_VAR"/>
			    TIME_CHART_TOOLTIP : '<c:out value="${TIME_CHART_TOOLTIP_VAR}" />',
				<fmt:message key="button.live.edit.confirm" var="LIVE_EDIT_CONFIRM_VAR"/>
				LIVE_EDIT_CONFIRM : decoderDiv.html('<c:out value="${LIVE_EDIT_CONFIRM_VAR}" />').text(),
				<fmt:message key="lesson.task.gate" var="CONTRIBUTE_GATE_VAR"/>
				CONTRIBUTE_GATE : '<c:out value="${CONTRIBUTE_GATE_VAR}" />',
				<fmt:message key="lesson.task.gate.password" var="CONTRIBUTE_GATE_PASSWORD_VAR"/>
				CONTRIBUTE_GATE_PASSWORD : '<c:out value="${CONTRIBUTE_GATE_PASSWORD_VAR}" />',
				<fmt:message key="lesson.task.grouping" var="CONTRIBUTE_GROUPING_VAR"/>
				CONTRIBUTE_GROUPING : '<c:out value="${CONTRIBUTE_GROUPING_VAR}" />',
				<fmt:message key="lesson.task.branching" var="CONTRIBUTE_BRANCHING_VAR"/>
				CONTRIBUTE_BRANCHING : '<c:out value="${CONTRIBUTE_BRANCHING_VAR}" />',
				<fmt:message key="lesson.task.content.edited" var="CONTRIBUTE_CONTENT_EDITED_VAR"/>
				CONTRIBUTE_CONTENT_EDITED : '<c:out value="${CONTRIBUTE_CONTENT_EDITED_VAR}" />',
				<fmt:message key="lesson.task.tool" var="CONTRIBUTE_TOOL_VAR"/>
				CONTRIBUTE_TOOL : '<c:out value="${CONTRIBUTE_TOOL_VAR}" />',
				<fmt:message key="button.task.go.tooltip" var="CONTRIBUTE_TOOLTIP_VAR"/>
				CONTRIBUTE_TOOLTIP : '<c:out value="${CONTRIBUTE_TOOLTIP_VAR}" />',
				<fmt:message key="button.task.go" var="CONTRIBUTE_BUTTON_VAR"/>
				CONTRIBUTE_BUTTON : '<c:out value="${CONTRIBUTE_BUTTON_VAR}" />',
				<fmt:message key="button.task.gate.open.now" var="CONTRIBUTE_OPEN_GATE_NOW_BUTTON_VAR"/>
				CONTRIBUTE_OPEN_GATE_NOW_BUTTON : '<c:out value="${CONTRIBUTE_OPEN_GATE_NOW_BUTTON_VAR}" />',
				<fmt:message key="button.task.gate.open.now.tooltip" var="CONTRIBUTE_OPEN_GATE_NOW_TOOLTIP_VAR"/>
				CONTRIBUTE_OPEN_GATE_NOW_TOOLTIP : '<c:out value="${CONTRIBUTE_OPEN_GATE_NOW_TOOLTIP_VAR}" />',
				<fmt:message key="button.task.gate.open" var="CONTRIBUTE_OPEN_GATE_BUTTON_VAR"/>
				CONTRIBUTE_OPEN_GATE_BUTTON : '<c:out value="${CONTRIBUTE_OPEN_GATE_BUTTON_VAR}" />',
				<fmt:message key="button.task.gate.open.tooltip" var="CONTRIBUTE_OPEN_GATE_TOOLTIP_VAR"/>
				CONTRIBUTE_OPEN_GATE_TOOLTIP : '<c:out value="${CONTRIBUTE_OPEN_GATE_TOOLTIP_VAR}" />',
				<fmt:message key="button.task.gate.opened" var="CONTRIBUTE_OPENED_GATE_VAR"/>
				CONTRIBUTE_OPENED_GATE : '<c:out value="${CONTRIBUTE_OPENED_GATE_VAR}" />',
				<fmt:message key="button.task.gate.opened.tooltip" var="CONTRIBUTE_OPENED_GATE_TOOLTIP_VAR"/>
				CONTRIBUTE_OPENED_GATE_TOOLTIP : '<c:out value="${CONTRIBUTE_OPENED_GATE_TOOLTIP_VAR}" />',
				<fmt:message key="lesson.task.attention" var="CONTRIBUTE_ATTENTION_VAR"/>
				CONTRIBUTE_ATTENTION : '<c:out value="${CONTRIBUTE_ATTENTION_VAR}" />',
				<fmt:message key="button.help" var="BUTTON_HELP_VAR"/>
				HELP : '<c:out value="${BUTTON_HELP_VAR}" />',
				<fmt:message key="label.lesson.introduction" var="LESSON_INTRODUCTION_VAR"/>
				LESSON_INTRODUCTION : '<c:out value="${LESSON_INTRODUCTION_VAR}" />',
				<fmt:message key="label.email" var="EMAIL_TITLE_VAR"/>
				EMAIL_TITLE : '<c:out value="${EMAIL_TITLE_VAR}" />',
				<fmt:message key="tour.this.is.disabled" var="TOUR_DISABLED_ELEMENT_VAR"/>
				TOUR_DISABLED_ELEMENT : '<c:out value="${TOUR_DISABLED_ELEMENT_VAR}" />',
				<fmt:message key="progress.email.sent.success" var="PROGRESS_EMAIL_SUCCESS_VAR"/>
				PROGRESS_EMAIL_SUCCESS : '<c:out value="${PROGRESS_EMAIL_SUCCESS_VAR}" />',
				<fmt:message key="progress.email.send.now.question" var="PROGRESS_EMAIL_SEND_NOW_QUESTION_VAR"/>
				PROGRESS_EMAIL_SEND_NOW_QUESTION : '<c:out value="${PROGRESS_EMAIL_SEND_NOW_QUESTION_VAR}" />',
				<fmt:message key="progress.email.send.failed" var="PROGRESS_EMAIL_SEND_FAILED_VAR"/>
				PROGRESS_EMAIL_SEND_FAILED : '<c:out value="${PROGRESS_EMAIL_SEND_FAILED_VAR}" />',
				<fmt:message key="progress.email.select.date.first" var="PROGRESS_EMAIL_SELECT_DATE_FIRST_VAR"/>
				PROGRESS_SELECT_DATE_FIRST : '<c:out value="${PROGRESS_EMAIL_SELECT_DATE_FIRST_VAR}" />',
				<fmt:message key="progress.email.enter.two.dates.first" var="PROGRESS_EMAIL_ENTER_TWO_DATES_FIRST_VAR"/>
				PROGRESS_ENTER_TWO_DATES_FIRST : '<c:out value="${PROGRESS_EMAIL_ENTER_TWO_DATES_FIRST_VAR}" />',
				<fmt:message key="progress.email.would.you.like.to.generate" var="PROGRESS_EMAIL_GENERATE_ONE_VAR"/>
				PROGRESS_EMAIL_GENERATE_ONE : '<c:out value="${PROGRESS_EMAIL_GENERATE_ONE_VAR}" />',
				<fmt:message key="progress.email.how.many.dates.to.generate" var="PROGRESS_EMAIL_GENERATE_TWO_VAR"/>
				PROGRESS_EMAIL_GENERATE_TWO : '<c:out value="${PROGRESS_EMAIL_GENERATE_TWO_VAR}" />',
				<fmt:message key="progress.email.title" var="PROGRESS_EMAIL_TITLE_VAR"/>
				PROGRESS_EMAIL_TITLE : '<c:out value="${PROGRESS_EMAIL_TITLE_VAR}" />',
				<fmt:message key="error.date.in.past" var="ERROR_DATE_IN_PAST_VAR"/>
				ERROR_DATE_IN_PAST : '<c:out value="${ERROR_DATE_IN_PAST_VAR}" />',
				<fmt:message key="label.lesson.starts" var="LESSON_START_VAR"><fmt:param value="%0"/></fmt:message>
				LESSON_START : '<c:out value="${LESSON_START_VAR}"/>',
				<fmt:message key="label.lesson.finishes" var="LESSON_FINISH_VAR"><fmt:param value="%0"/></fmt:message>
				LESSON_FINISH : '<c:out value="${LESSON_FINISH_VAR}" />',
				<fmt:message key="lesson.display.activity.scores.alert" var="LESSON_ACTIVITY_SCORES_ENABLE_ALERT_VAR"/>
				LESSON_ACTIVITY_SCORES_ENABLE_ALERT : decoderDiv.html('<c:out value="${LESSON_ACTIVITY_SCORES_ENABLE_ALERT_VAR}" />').text(),
				<fmt:message key="lesson.hide.activity.scores.alert" var="LESSON_ACTIVITY_SCORES_DISABLE_ALERT_VAR"/>
				LESSON_ACTIVITY_SCORES_DISABLE_ALERT : decoderDiv.html('<c:out value="${LESSON_ACTIVITY_SCORES_DISABLE_ALERT_VAR}" />').text(),
				<fmt:message key="label.reschedule" var="RESCHEDULE_VAR"/>
				RESCHEDULE : decoderDiv.html('<c:out value="${RESCHEDULE_VAR}" />').text(),
				<fmt:message key="error.lesson.end.date.must.be.after.start.date" var="LESSON_ERROR_START_END_DATE_VAR"/>
				LESSON_ERROR_START_END_DATE : decoderDiv.html('<c:out value="${LESSON_ERROR_START_END_DATE_VAR}" />').text(),
				<fmt:message key="button.live.edit" var="LIVE_EDIT_BUTTON_VAR"/>
				LIVE_EDIT_BUTTON: '<c:out value="${LIVE_EDIT_BUTTON_VAR}" />',
				<fmt:message key="button.live.edit.tooltip" var="LIVE_EDIT_TOOLTIP_VAR"/>
				LIVE_EDIT_TOOLTIP: '<c:out value="${LIVE_EDIT_TOOLTIP_VAR}" />',
				<fmt:message key="label.person.editing.lesson" var="LIVE_EDIT_WARNING_VAR"><fmt:param value="%0"/></fmt:message>
				LIVE_EDIT_WARNING: '<c:out value="${LIVE_EDIT_WARNING_VAR}" />'
			};
	</script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/progressBar.js"></script>
	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/monitorLesson.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/readmore.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/d3.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/chart.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.tabcontroller.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/bootstrap-tourist.min.js"></script> 
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/dialog.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/portrait.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/x-editable.js"></script>
	<script type="text/javascript">
		$(document).bind("mobileinit", function(){
			$.mobile.loadingMessage = false;
			$.mobile.ignoreContentEnabled = true;
			 $('body').attr('data-enhance', 'false');
		});
				
		$(document).ready(function(){
			initLessonTab();
			initSequenceTab();
			initLearnersTab();
			initGradebookTab();
			refreshMonitor();
			<c:if test="${not empty lesson.lessonIntro}">
				$('#intro').readmore({
					speed: 500,
					collapsedHeight: 85
				});
			</c:if>
			<c:if test="${not empty lesson.learningDesignDescription}">
				$('#description').readmore({
					speed: 500,
					collapsedHeight: 85
				});
			</c:if>
			
			// remove "loading..." screen
			$('#loadingOverlay').remove();
		});
			
        function doSelectTab(tabId) {
	        if ( tourInProgress )  {
	        	alert(LABELS.TOUR_DISABLED_ELEMENT);
	        	return;
	        }
	        actualDoSelectTab(tabId);
        }

        function actualDoSelectTab(tabId) {
	    	selectTab(tabId);
			var sequenceInfoDialog = $('#sequenceInfoDialog');
	    	if ( tabId == '2' ) {
				if (sequenceTabShowInfo) {
					sequenceInfoDialog.modal("show");
					sequenceTabShowInfo = false; // only show it once
                }
                
			} else {
				sequenceInfoDialog.modal("hide");
            }
            
	    	if ( tabId == '4' ) {
	    		updateGradebookTab();
	    	}
        }

        function switchToTblMonitor() {
        	$("#content").load(
                LAMS_URL + 'monitoring/tblmonitor/start.do', 
        		{
        			lessonID: ${lesson.lessonID}
        		}
        	);
        }
        
    	<%@ include file="monitorTour.jsp" %> 
	</script>
</lams:head>

<body>
	
	<%-- "loading..." screen, gets removed on page full load --%>
	<div id="loadingOverlay">
		<i class="fa fa-refresh fa-spin fa-2x fa-fw"></i>
	</div>
	
	<lams:Page type="navbar">
		<c:if test="${isTBLSequence}">
			<c:set var="tblMonitorButton">
				<i class="fa fa fa-heartbeat" title="<fmt:message key="label.tbl.monitor" />"
			 		onclick="javascript:switchToTblMonitor();" id="tbl-monitor-control">  <fmt:message key="label.tbl.monitor"/></i>
			</c:set>
		</c:if>
	
		<lams:Tabs control="true" extraControl="${tblMonitorButton}">
			<lams:Tab id="1" key="tab.lesson" />
			<lams:Tab id="2" key="tab.sequence" />
			<lams:Tab id="3" key="tab.learners" />
			<lams:Tab id="4" key="tab.gradebook" />
		</lams:Tabs>
		<lams:TabBodyArea>
			<lams:TabBodys>
				<lams:TabBody id="1" titleKey="label.basic">
					<div class="row">
						<div class="col-xs-12">
							<button onclick="javascript:startTour();return false;" class="btn btn-sm btn-default pull-right roffset10 tour-button"> 
							<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message key="label.tour"/></span></button>
							
							<a id="tour-refresh-button" class="btn btn-sm btn-default pull-right roffset10" title="<fmt:message key='button.refresh.tooltip'/>"
							   href="#" onClick="javascript:refreshMonitor('lesson')">
							<i class="fa fa-refresh"></i> <span class="hidden-xs"><fmt:message key="button.refresh"/></span></a>				
							<p id="tabLessonLessonName">
								<span class="lead">
									<strong id="lesson-name-strong"><c:out value="${lesson.lessonName}" /></strong>
									<span>&nbsp;</span><i class='fa fa-sm fa-pencil'></i>
								</span>
								<br/>
								<span class="text-muted"><small><c:out value="${lesson.organisationName}" escapeXml="true"/></small></span>
							</p>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-9 col-xs-7">
						
							<!-- Lesson details -->
							<dl id="lessonDetails" class="dl-horizontal">
								<c:if test="${not empty lesson.learningDesignDescription}">
									<dt><fmt:message key="lesson.description"/></dt>
									<dd id="tabLessonLessonDescription">
										<div id="description">
											<c:out value="${lesson.learningDesignDescription}" escapeXml="false"/>
										</div>
									</dd>
								</c:if>
								<c:if test="${not empty lesson.lessonIntro}">
									<dt><fmt:message key="lesson.intro"/></dt>
									<dd id="tabLessonLessonIntro">
										<div id="intro">
											<c:out value="${lesson.lessonIntro}" escapeXml="false"/>
										</div>
									</dd>
								</c:if>
								<dt><fmt:message key="lesson.state"/>
								</dt>
								<dd>
									<span data-toggle="collapse" data-target="#changeState" id="lessonStateLabel" class="lessonManageField"></span>
								  	<div style="display:inline-block;vertical-align: middle;"><span id="lessonStartDateSpan" class="lessonManageField loffset5"></span>
								  	<span id="lessonFinishDateSpan" class="lessonManageField loffset5"></span></div>
								  	 
									<!--  Change lesson status or start/schedule start -->
									<div class="collapse offset10" id="changeState">
										<div id="lessonScheduler">
											<form class="form-horizontal">
												<div class="form-group" id="lessonStartApply">
													<label for="scheduleDatetimeField" class="col-sm-1"><fmt:message key="lesson.start"/></label>
													<div class="col-sm-8">
													<input class="lessonManageField input-sm" id="scheduleDatetimeField" type="text"/>
													<a id="scheduleLessonButton" class="btn btn-xs btn-default lessonManageField" href="#"
														   onClick="javascript:scheduleLesson()"
														   title='<fmt:message key="button.schedule.tooltip"/>'>
													   <fmt:message key="button.schedule"/>
													</a>
													<a id="startLessonButton" class="btn btn-xs btn-default" href="#"
														   onClick="javascript:startLesson()"
														   title='<fmt:message key="button.start.now.tooltip"/>'>
													   <fmt:message key="button.start.now"/>
													</a>
													</div>
												</div>
												<div class="form-group" id="lessonDisableApply">
													<label for="disableDatetimeField" class="col-sm-1"><fmt:message key="lesson.end"/></label>
													<div class="col-sm-8">
														<input class="lessonManageField input-sm" id="disableDatetimeField" type="text"/>
														<a id="scheduleDisableLessonButton" class="btn btn-xs btn-default lessonManageField" href="#"
															   onClick="javascript:scheduleDisableLesson()"
															   title='<fmt:message key="button.schedule.disable.tooltip"/>'>
													   	<fmt:message key="button.schedule"/>
														</a>
														<a id="disableLessonButton" class="btn btn-xs btn-default" href="#"
															   onClick="javascript:disableLesson()"
															   title='<fmt:message key="button.disable.now.tooltip"/>'>
														   <fmt:message key="button.disable.now"/>
														</a>
													</div>
												</div>
											</form>
										</div>
										
										<div id="lessonStateChanger">
											<select id="lessonStateField" class="btn btn-xs" onchange="lessonStateFieldChanged()">
												<option value="-1"><fmt:message key="lesson.select.state"/></option>
											</select>
											<span id="lessonStateApply">
												<button type="button" class="lessonManageField btn btn-xs btn-primary"
														onClick="javascript:changeLessonState()"
														title='<fmt:message key="lesson.change.state.tooltip"/>'>
											   		<i class="fa fa-check"></i> 
											   		<span class="hidden-xs"><fmt:message key="button.apply"/></span>
										    	</button>
										    </span>
								    	</div>					
									</div>
								</dd>
								
								<dt><fmt:message key="lesson.learners"/>:</dt>
								<dd title='<fmt:message key="lesson.ratio.learners.tooltip"/>' id="learnersStartedPossibleCell"></dd>
								
								<!--  lesson actions -->
								<dt><fmt:message key="lesson.manage"/>:</dt>
								<dd>
									<div class="btn-group btn-group-xs" role="group" id="lessonActions">
										<button id="viewLearnersButton" class="btn btn-default roffset10"
												type="button"onClick="javascript:showLessonLearnersDialog()"
												title='<fmt:message key="button.view.learners.tooltip"/>'>
											<i class="fa fa-sm fa-users"></i>
											<span class="hidden-xs"><fmt:message key="button.view.learners"/></span>
										</button>
										
										<button id="editClassButton" class="btn btn-default roffset10"
												type="button" onClick="javascript:showClassDialog()"
												title='<fmt:message key="button.edit.class.tooltip"/>'>
											<i class="fa fa-sm fa-user-times"></i>
											<span class="hidden-xs"><fmt:message key="button.edit.class"/></span>
										</button>
										
										<c:if test="${lesson.enabledLessonNotifications}">
											<button id="notificationButton" class="btn btn-default roffset10"
													type="button" onClick="javascript:showNotificationsDialog(null,${lesson.lessonID})">
												<i class="fa fa-sm fa-bullhorn"></i>
												<span class="hidden-xs"><fmt:message key="email.notifications"/></span>
											</button>
										</c:if>
									</div>
									
									<div class="btn-group btn-group-xs" role="group" id="lessonActions2">
										<c:if test="${lesson.enableLessonIntro}">
											<button id="editIntroButton" class="btn btn-default roffset10"
													type="button" onClick="javascript:showIntroductionDialog(${lesson.lessonID})">
												<i class="fa fa-sm fa-info"></i>
												<span class="hidden-xs"><fmt:message key="label.lesson.introduction"/></span>
											</button>
										</c:if>							  

										<button id="gradebookOnCompleteButton" class="btn btn-default
											<c:if test="${lesson.gradebookOnComplete}">
												btn-success
											</c:if>
											">
											<i class="fa fa-sm fa-list-ol"></i><span class="hidden-xs">&nbsp;<fmt:message key="label.display.activity.scores"/></span> 
 										</button>
									</div>
										
								</dd>
		
								<!-- IM & Presence -->
								<dt><fmt:message key="lesson.im"/>:</dt>
								<dd>
									<div class="btn-group btn-group-xs" role="group" id="tour-lesson-im">
										<button id="presenceButton" class="btn btn-default roffset10
											<c:if test="${lesson.learnerPresenceAvailable}">
												btn-success
											</c:if>
											"><i class="fa fa-sm fa-wifi"></i>
											<span class="hidden-xs"><fmt:message key="lesson.presence"/></span> 
											<span id="presenceCounter" class="badge">0</span>
										</button>
	
										<button id="imButton" class="btn btn-default roffset10
											<c:if test="${lesson.learnerImAvailable}">
												btn-success
											</c:if>
											"
											<c:if test="${not lesson.learnerPresenceAvailable}">
												style="display: none"
											</c:if>
										><i class="fa fa-sm fa-comments-o"></i>
										 <span class="hidden-xs"><fmt:message key="lesson.im"/></span>
										</button>
										
										<button id="openImButton" class="btn btn-default"
											<c:if test="${not lesson.learnerImAvailable}">
												style="display: none"
											</c:if>
										><i class="fa fa-sm fa-comments"></i>
										 <span class="hidden-xs"><fmt:message key="button.open.im"/></span> 
										</button>
									</div>
								</dd>
								
								<!-- Progress Emails -->
								<dt><fmt:message key="lesson.progress.email"/>:</dt>
								<dd>
									<div class="btn-group btn-group-xs" role="group">
										<button id="sendProgressEmail" class="btn btn-default roffset10"
											onClick="javascript:sendProgressEmail()"/>
											<i class="fa fa-sm fa-envelope"></i>
											<span class="hidden-xs"><fmt:message key="progress.email.send"/></span> 
										</button>
										<button id="configureProgressEmail" class="btn btn-default roffset10"
											onClick="javascript:configureProgressEmail()"/>
											<i class="fa fa-sm fa-cog"></i>
											<span class="hidden-xs"><fmt:message key="progress.email.configure"/></span> 
										</button>
									</div>
								</dd>

								<!--  encodedLessonID -->
								<c:if test="${ALLOW_DIRECT_LESSON_LAUNCH}">
                                    <dt class="text-muted"><small><fmt:message key="lesson.learner.url"/></small></dt>
									<dd class="text-muted">
                                    <small><c:out value="${serverURL}r/${lesson.encodedLessonID}" escapeXml="true"/></small>
									</dd>
								</c:if>
								
							</dl>	
						</div>
						<div class="panel panel-default pull-right">
							<div class="panel-heading"><fmt:message key="lesson.chart.title"/></div>
							<div id="chartDiv" class="panel-body"></div>
						</div>
					</div>
					
					<!-- Required tasks -->
					<div id="requiredTasks" class="panel panel-warning" style="display: none;">
						<div class="panel-heading">
							<div class="panel-title"><fmt:message key="lesson.required.tasks"/></div>
						</div>
						<div class="panel-body">
							<span id="contributeHeader"></span>
						</div>
					</div>
				</lams:TabBody>
				
				<lams:TabBody id="2" titleKey="label.advanced">
					<span class="pull-left" style="display:none" id="liveEditWarning"></span>
					
					<div id="sequenceTopButtonsContainer" class="topButtonsContainer">
						<button onclick="javascript:startTour();return false;" class="btn btn-sm btn-default pull-right roffset10 tour-button"> 
						<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message key="label.tour"/></span></button>

						<a id="refreshButton" class="btn btn-sm btn-default" title="<fmt:message key='button.refresh.tooltip'/>"
						   href="#" onClick="javascript:refreshMonitor('sequence')">
							<i class="fa fa-refresh"></i> <span class="hidden-xs"><fmt:message key="button.refresh"/></span>
						</a>
						<a id="liveEditButton" class="btn btn-sm btn-default style="display:none" title="<fmt:message key='button.live.edit.tooltip'/>"
					       href="#" onClick="javascript:openLiveEdit()">
							<i class="fa fa-pencil"></i> <span class="hidden-xs"><fmt:message key='button.live.edit'/></span>
						</a>
						<span id="sequenceSearchPhraseClear"
							 class="fa fa-xs fa-times-circle"
							 onClick="javascript:sequenceClearSearchPhrase(true)"
							 title="<fmt:message key='learners.search.phrase.clear.tooltip' />" 
						></span>
						<input id="sequenceSearchPhrase"
							   title="<fmt:message key='search.learner.textbox' />" />
						<span id="sequenceSearchPhraseIcon"
							  class="ui-icon ui-icon-search"
							  title="<fmt:message key='search.learner.textbox' />"></span>
					</div>
					<div id="sequenceCanvas"></div>
					<div id="completedLearnersContainer" title="<fmt:message key='force.complete.end.lesson.tooltip' />">
						<img id="completedLearnersDoorIcon" src="<lams:LAMSURL/>images/icons/door_open.png" />
					</div>
					<img id="sequenceCanvasLoading"
					     src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
					<img id="sequenceSearchedLearnerHighlighter"
					     src="<lams:LAMSURL/>images/pedag_down_arrow.gif" />
				</lams:TabBody>
				
				<lams:TabBody id="3" titleKey="label.conditions">
					<table id="tabLearnerControlTable">
						<tr>
							<td class="learnersHeaderCell">
								<fmt:message key='learners.page' /><br />
								<span id="learnersPageCounter" />
							</td>
							<td class="learnersHeaderCell">
								<span id="learnersSearchPhraseIcon" 
									  class="ui-icon ui-icon-search"
									  title="<fmt:message key='search.learner.textbox' />"></span>
								<input id="learnersSearchPhrase" 
									   title="<fmt:message key='search.learner.textbox' />"/>
								<span id="learnersSearchPhraseClear"
									  class="fa fa-xs fa-times-circle"
									  onClick="javascript:learnersClearSearchPhrase()"
									  title="<fmt:message key='learners.search.phrase.clear.tooltip' />" 
								></span>
							</td>
							<td id="learnersPageLeft"
								class="learnersHeaderCell learnersPageShifter"
								title="<fmt:message key='learner.group.backward.10'/>"
							    onClick="javascript:learnersPageShift(false)"
							><span class="ui-icon ui-icon-seek-prev"></span></td>
							<td id="learnersPageRight"
								class="learnersHeaderCell learnersPageShifter"
								title="<fmt:message key='learner.group.forward.10'/>"
								onClick="javascript:learnersPageShift(true)"
							><span class="ui-icon ui-icon-seek-next"></span></td>
							<td class="learnersHeaderCell">
								<fmt:message key='learners.order' /><br />
								<input id="orderByCompletionCheckbox" type="checkbox" 
									   onChange="javascript:loadLearnerProgressPage()" />
							</td>
							<td class="topButtonsContainer">
								<button onclick="javascript:startTour();return false;" class="btn btn-sm btn-default pull-right roffset10 tour-button"> 
								<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message key="label.tour"/></span></button>
							
								<a class="btn btn-sm btn-default" title="<fmt:message key='button.refresh.tooltip'/>"
								   href="#" onClick="javascript:refreshMonitor('learners')">
								   <i class="fa fa-refresh"></i> <span class="hidden-xs"><fmt:message key="button.refresh"/></span></a>
								<a class="btn btn-sm btn-default" title="<fmt:message key='button.journal.entries.tooltip'/>"
						   		   href="#" id="journalButton"
						           onClick="javascript:openPopUp('<lams:LAMSURL/>learning/notebook/viewAllJournals.do?lessonID=${lesson.lessonID}', 'JournalEntries', 648, 1152, true)">
						           <i class="fa fa-book"></i> <span class="hidden-xs"><fmt:message key="button.journal.entries"/></span></a>
							</td>
						</tr>
					</table>
					
					<div id="tabLearnersContainer">
						<div class="table-responsive">
							<table id="tabLearnersTable" class="table table-condensed table-responsive"></table>
						</div>
					</div>
				</lams:TabBody>
				
				<lams:TabBody id="4" titleKey="label.gradebook">
					<div id="gradebookTopButtonsContainer" class="topButtonsContainer pull-right">
						<button onclick="javascript:startTour();return false;" class="btn btn-sm btn-default pull-right roffset10 tour-button"> 
						<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message key="label.tour"/></span></button>

						<a id="refreshButton" class="btn btn-sm btn-default" title="<fmt:message key='button.refresh.tooltip'/>"
						   href="#" onClick="javascript:refreshMonitor('gradebook')">
							<i class="fa fa-refresh"></i> <span class="hidden-xs"><fmt:message key="button.refresh"/></span>
						</a>
					</div>
					<div id="gradebookDiv"></div>
					<img id="gradebookLoading" src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
				</lams:TabBody>
		    </lams:TabBodys>
		 </lams:TabBodyArea>
	 </lams:Page>
	 
	<!-- Inner dialog placeholders -->
	
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
			<button id="learnerGroupDialogForceCompleteAllButton" class="btn btn-default roffset5 pull-right">
				<span><fmt:message key="button.force.complete.all" /></span>
			</button>
			<button id="learnerGroupDialogForceCompleteButton" class="learnerGroupDialogSelectableButton btn btn-default roffset5 pull-right">
				<span><fmt:message key="button.force.complete" /></span>
			</button>
			<br>
			<button id="learnerGroupDialogCloseButton" class="btn btn-default voffset10 pull-right">
				<span><fmt:message key="button.close" /></span>
			</button>
			<button id="learnerGroupDialogEmailButton" class="learnerGroupDialogSelectableButton btn btn-default roffset5 voffset10 pull-right">
				<span><fmt:message key="button.email" /></span>
			</button>
			<button id="learnerGroupDialogViewButton" class="learnerGroupDialogSelectableButton btn btn-default roffset5 voffset10 pull-right">
				<span><fmt:message key="button.view.learner" /></span>
			</button>
		</div>
	</div>
		
	<div id="classDialogContents" class="dialogContainer">
		<div id="classDialogTable">
			<div class="row no-margin">
				<div id="leftLearnerTable" class="col-xs-6">
					<table id="classLearnerTable" class="table table-condensed">
						<tr class="active">
							<td class="dialogTitle" colspan="6"><fmt:message
									key="lesson.learners" /></td>
						</tr>
						<tr>
							<td><span class="dialogSearchPhraseIcon fa fa-xs fa-search"
								title="<fmt:message key='search.learner.textbox' />"></span></td>
							<td colspan="4"><input class="dialogSearchPhrase" style="padding: 0px"
								title="<fmt:message key='search.learner.textbox' />" /></td>
							<td><span
								class="dialogSearchPhraseClear fa fa-xs fa-times-circle"
								onClick="javascript:classClearSearchPhrase()"
								title="<fmt:message key='learners.search.phrase.clear.tooltip' />"></span>
							</td>
						</tr>
						<tr>
							<td class="navCell pageMinus10Cell"
								title="<fmt:message key='learner.group.backward.10'/>"
								onClick="javascript:shiftClassList('Learner', -10)"><span
								class="ui-icon ui-icon-seek-prev"></span></td>
							<td class="navCell pageMinus1Cell"
								title="<fmt:message key='learner.group.backward.1'/>"
								onClick="javascript:shiftClassList('Learner', -1)"><span
								class="ui-icon ui-icon-arrowthick-1-w"></span></td>
							<td class="pageCell" title="<fmt:message key='learners.page'/>">
							</td>
							<td class="navCell pagePlus1Cell"
								title="<fmt:message key='learner.group.forward.1'/>"
								onClick="javascript:shiftClassList('Learner', 1)"><span
								class="ui-icon ui-icon-arrowthick-1-e"></span></td>
							<td class="navCell pagePlus10Cell"
								title="<fmt:message key='learner.group.forward.10'/>"
								onClick="javascript:shiftClassList('Learner', 10)"><span
								class="ui-icon ui-icon-seek-next"></span></td>
							<td class="navCell sortCell"
								title="<fmt:message key='learner.group.sort.button'/>"
								onClick="javascript:sortClassList('Learner')"><span
								class="ui-icon ui-icon-triangle-1-n"></span></td>
						</tr>
						<tr>
							<td class="dialogList" colspan="6"></td>
						</tr>
						<tr>
							<td colspan="6">
								<button id="addAllLearnersButton"
									class="btn btn-sm btn-default pull-right"
									onClick="javascript:addAllLearners()">
									<fmt:message key="button.edit.class.add.all" />
								</button>
							</td>
						</tr>
					</table>
				</div>
				<div id="rightMonitorTable" class="col-xs-6">
					<table id="classMonitorTable" class="table table-condensed">
						<tr class="active">
							<td class="dialogTitle" colspan="6"><fmt:message
									key="lesson.monitors" /></td>
						</tr>
						<tr>
							<td colspan="5">
							<td id="classMonitorSearchRow" colspan="6">&nbsp;</td>
							</td>
						</tr>
						<tr>
							<td class="navCell pageMinus10Cell"
								title="<fmt:message key='learner.group.backward.10'/>"
								onClick="javascript:shiftClassList('Monitor', -10)"><span
								class="ui-icon ui-icon-seek-prev"></span></td>
							<td class="navCell pageMinus1Cell"
								title="<fmt:message key='learner.group.backward.1'/>"
								onClick="javascript:shiftClassList('Monitor', -1)"><span
								class="ui-icon ui-icon-arrowthick-1-w"></span></td>
							<td class="pageCell" title="<fmt:message key='learners.page'/>">
							</td>
							<td class="navCell pagePlus1Cell"
								title="<fmt:message key='learner.group.forward.1'/>"
								onClick="javascript:shiftClassList('Monitor', 1)"><span
								class="ui-icon ui-icon-arrowthick-1-e"></span></td>
							<td class="navCell pagePlus10Cell"
								title="<fmt:message key='learner.group.forward.10'/>"
								onClick="javascript:shiftClassList('Monitor', 10)"><span
								class="ui-icon ui-icon-seek-next"></span></td>
							<td class="navCell sortCell"
								title="<fmt:message key='learner.group.sort.button'/>"
								onClick="javascript:sortClassList('Monitor')"><span
								class="ui-icon ui-icon-triangle-1-n"></span></td>
						</tr>
						<tr>
							<td class="dialogList" colspan="6"></td>
						</tr>
						<tr>
							<td colspan="6"></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
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
	
	<div id="emailProgressDialogContents" class="dialogContainer">
		<div id="emailProgressDialogTable">
			<div class="row">
				<div class="col-xs-12">
					<table id="emailProgressTable" class="table table-condensed">
						<tr class="active">
							<td class="dialogTitle" colspan="6"><fmt:message key="progress.email.will.be.sent.on"/></td>
						</tr>
						<tr>
							<td class="dialogList" colspan="6"></td>
						</tr>
					</table>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6">
					<div class="form-group">
						<label for="emaildatePicker"><fmt:message key="progress.email.select.date"/></label>
						<input type="text" class="form-control" name="emaildatePicker" id="emaildatePicker" value="" autocomplete="off" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6">
					<button id="addEmailProgressDateButton"
						class="btn btn-sm btn-default pull-left"
						onClick="javascript:addEmailProgressDate()">
						<fmt:message key="progress.email.add.date"/>
					</button>
				</div>
				<div class="col-xs-6">
					<button id="addEmailProgressSeriesButton"
						class="btn btn-sm btn-default pull-right"
						onClick="javascript:addEmailProgressSeries(true)">
						<fmt:message key="progress.email.generate.date.list"/>
					</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="tooltip" id="tooltip"></div>
</body>
</lams:html>
