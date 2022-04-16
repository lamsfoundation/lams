<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<lams:html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>LAMS Monitor</title>
	

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui.timepicker.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/vertical-timeline.css"> 
	<link rel="stylesheet" href="<lams:LAMSURL/>css/x-editable5.css"> 
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>css/components-monitoring.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>css/components-monitoring-responsive.css">

	<lams:css suffix="chart"/>
		
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/x-editable5.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/d3.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/chart.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/portrait.js"></script>
	<lams:JSImport src="includes/javascript/dialog5.js" />
	<lams:JSImport src="includes/javascript/monitorLesson5.js" relative="true" />
	<script type="text/javascript">
		<!-- Some of the following settings will be used in progressBar.js -->
		var lessonId = ${lesson.lessonID},
			userId = '<lams:user property="userID"/>',
			ldId = ${lesson.learningDesignID},
			lessonStateId = ${lesson.lessonStateID},
			createDateTimeStr = '${lesson.createDateTimeStr}',
			lessonStartDate = '${lesson.scheduleStartDate}',
			lessonEndDate = '${lesson.scheduleEndDate}',
			liveEditEnabled = ${enableLiveEdit && lesson.liveEditEnabled},
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
</head>
<body class="component">
<div class="monitoring-page-wrapper component-page-wrapper">
	<div class="component-sidebar active">
		<a href="/" class="lams-logo"><img src="<lams:LAMSURL/>images/components/lams_logo_white.png" alt="#" /></a>
		
		<div class="component-menu">
			<div class="component-menu-btn">
				<a href="/" title="Back to lesson list"><i class="fa fa-arrow-left fa-lg"></i></a>
				<a id="edit-lesson-btn" href="#"><i class="fa fa-pen fa-lg"></i></a>
				<a id="load-sequence-tab-btn" href="#" class="navigate-btn active"><i class="fa fa-sitemap fa-lg"></i></a>
				<a id="load-learners-tab-btn" href="#" class="navigate-btn"><i class="fa fa-solid fa-user fa-lg"></i></a>
				<a id="load-gradebook-tab-btn" href="#" class="navigate-btn"><i class="fa fa-solid fa-list-ol fa-lg"></i></a>
			</div>
			
			<div class="lesson-properties">
				<dl id="lessonDetails" class="dl-horizontal">
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
										<input class="lessonManageField input-sm" id="scheduleDatetimeField" type="text" autocomplete="nope" />
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
						<div>
							<button id="viewLearnersButton" class="btn btn-sm btn-primary"
									type="button"onClick="javascript:showLessonLearnersDialog()"
									title='<fmt:message key="button.view.learners.tooltip"/>'>
								<i class="fa fa-users"></i>
								<span class="hidden-xs"><fmt:message key="button.view.learners"/></span>
							</button>
							
							<button id="editClassButton" class="btn btn-sm btn-primary"
									type="button" onClick="javascript:showClassDialog()"
									title='<fmt:message key="button.edit.class.tooltip"/>'>
								<i class="fa fa-user-times"></i>
								<span class="hidden-xs"><fmt:message key="button.edit.class"/></span>
							</button>
							
							<c:if test="${lesson.enabledLessonNotifications}">
								<button id="notificationButton" class="btn btn-sm btn-primary"
										type="button" onClick="javascript:showNotificationsDialog(null,${lesson.lessonID})">
									<i class="fa fa-bullhorn"></i>
									<span class="hidden-xs"><fmt:message key="email.notifications"/></span>
								</button>
							</c:if>
						</div>
						
						<div>
							<c:if test="${lesson.enableLessonIntro}">
								<button id="editIntroButton" class="btn btn-sm btn-primary"
										type="button" onClick="javascript:showIntroductionDialog(${lesson.lessonID})">
									<i class="fa fa-sm fa-info"></i>
									<span class="hidden-xs"><fmt:message key="label.lesson.introduction"/></span>
								</button>
							</c:if>			
											   
							<lams:Switch id="gradebookOnCompleteButton" checked="${lesson.gradebookOnComplete}"
								labelKey="label.display.activity.scores" iconClass="fa fa-sm fa-list-ol" />
						</div>
					</dd>

					<!-- IM & Presence -->
					<dt><fmt:message key="lesson.im"/>:</dt>
					<dd>
						
						<lams:Switch id="presenceButton" checked="${lesson.learnerPresenceAvailable}"
							labelKey="lesson.presence" iconClass="fa fa-sm fa-wifi"	/>
						
						<!-- <span id="presenceCounter" class="badge">0</span> -->
	
						<div id="imButtonWrapper"
							<c:if test="${not lesson.learnerPresenceAvailable}">
								style="display: none"
							</c:if>
							>
							<lams:Switch id="imButton" checked="${lesson.learnerImAvailable}"
								labelKey="lesson.im" iconClass="fa fa-sm fa-comments"	/>
						</div>
						
						<button id="openImButton" class="btn btn-primary btn-sm"
							<c:if test="${not lesson.learnerImAvailable}">
								style="display: none"
							</c:if>
						><i class="fa fa-sm fa-comments"></i>
						 <span class="hidden-xs"><fmt:message key="button.open.im"/></span> 
						</button>
					</dd>
					
					<!-- Progress Emails -->
					<dt><fmt:message key="lesson.progress.email"/>:</dt>
					<dd>
						<button id="sendProgressEmail" class="btn btn-primary btn-sm"
							onClick="javascript:sendProgressEmail()"/>
							<i class="fa fa-sm fa-envelope"></i>
							<span class="hidden-xs"><fmt:message key="progress.email.send"/></span> 
						</button>
						<button id="configureProgressEmail" class="btn btn-primary btn-sm"
							onClick="javascript:configureProgressEmail()"/>
							<i class="fa fa-sm fa-cog"></i>
							<span class="hidden-xs"><fmt:message key="progress.email.configure"/></span> 
						</button>
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
		</div>
	</div>
	
	<div class="monitoring-page-content active">
		<header class="d-flex justify-content-between">
			<div class="hamburger-box">
				<div class="hamburger">
					<span></span>
					<span></span>
					<span></span>
				</div>
				<p id="lesson-name">Lesson Name</p>
			</div>
			<div class="top-menu">
				<form>
					<input type="text" name="" placeholder="Search Student">
					<img src="<lams:LAMSURL/>images/components/search.svg" alt="#" />
				</form>
				<div class="top-menu-btn component-menu-btn">
					<a href="#" onClick="javscript:refreshMonitor()"><img src="<lams:LAMSURL/>images/components/icon2.svg" alt="#" /></a>
				</div>
			</div>
		</header>
		
		<div class="tab-content pt-4">
			
		</div>
	</div>
</div>


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
       <div class="pull-right mt-3">

              <button id="forceBackwardsRemoveContentNoButton" class="btn btn-primary me-1">
                      <span><fmt:message key="force.complete.remove.content.no"/></span>
              </button>

              <button id="forceBackwardsRemoveContentYesButton" class="btn btn-primary me-1">
                      <span><fmt:message key="force.complete.remove.content.yes" /></span>
              </button>

              <button id="forceBackwardsCloseButton" class="btn btn-secondary">
                      <span><fmt:message key="button.close" /></span>
              </button>
      </div>
</div>

<div class="toast-container position-fixed top-0 start-50 translate-middle-x p-3" id="toast-container">
</div>

<div id="toast-template" class="toast align-items-center bg-white" role="alert" aria-live="assertive" aria-atomic="true">
	<div class="d-flex">
		<div class="toast-body"></div>
		<button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
  	</div>
</div>
</body>
</lams:html>