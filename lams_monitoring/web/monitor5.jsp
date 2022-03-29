<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<lams:html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>LAMS Monitor</title>
	

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>css/components-monitoring.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>css/components-monitoring-responsive.css">
	<lams:css suffix="chart"/>
		
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
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
		$(document).ready(function(){
			$('.hamburger').click(function(){
				$(this).toggleClass('active');
				$('.component-sidebar, .monitoring-page-content').toggleClass('active');
			});
			
			initSequenceTab();
			refreshMonitor();
		});
	</script>
</head>
<body class="component">
<div class="monitoring-page-wrapper">
	<div class="component-sidebar">
		<ul>
			<li class="logo-li">
				<a href="#"><img src="<lams:LAMSURL/>images/components/lams_logo_white.png" alt="#" /></a>
			</li>
			<li class="menu-li active">
				<a href="#"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><defs><style>.a,.b,.c,.d{fill:none;}.b,.c,.d{stroke:#3c42e0;stroke-width:2px;}.b,.c{stroke-linejoin:round;}.c,.d{stroke-linecap:round;}</style></defs><g transform="translate(-44 -351)"><rect class="a" width="24" height="24" transform="translate(44 351)"/><g transform="translate(1 13)"><rect class="b" width="5" height="5" transform="translate(53 354)"/><rect class="b" width="5" height="5" transform="translate(53 341)"/><rect class="b" width="5" height="5" transform="translate(45 354)"/><rect class="b" width="5" height="5" transform="translate(61 354)"/><path class="c" d="M47.5,353.938V350H63.594v4"/><path class="d" d="M47.5,358v-8" transform="translate(8 -4)"/></g></g></svg></a>
			</li>
			<li class="menu-li">
				<a href="<lams:WebAppURL/>monitor-sequence-tab.jsp"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><defs><style>.a,.b{fill:none;}.a{stroke:#acb5cc;stroke-linecap:round;stroke-linejoin:round;stroke-width:2px;}</style></defs><g transform="translate(-45 -200)"><g transform="translate(45.5 199.5)"><path class="a" d="M16.045,27.955V26.136A3.636,3.636,0,0,0,12.409,22.5H5.136A3.636,3.636,0,0,0,1.5,26.136v1.818" transform="translate(0 -7.091)"/><path class="a" d="M14.773,8.136A3.636,3.636,0,1,1,11.136,4.5,3.636,3.636,0,0,1,14.773,8.136Z" transform="translate(-2.364)"/><path class="a" d="M32.727,28.031V26.213A3.636,3.636,0,0,0,30,22.7" transform="translate(-11.227 -7.168)"/><path class="a" d="M24,4.7a3.636,3.636,0,0,1,0,7.045" transform="translate(-8.864 -0.077)"/></g><rect class="b" width="24" height="24" transform="translate(45 200)"/></g></svg></a>
			</li>
			<li class="menu-li">
				<a href="#"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><defs><style>.a{fill:none;}.b{fill:#acb5cc;}</style></defs><path class="a" d="M0,0H24V24H0Z"/><path class="b" d="M18,2H6A2.006,2.006,0,0,0,4,4V20a2.006,2.006,0,0,0,2,2H18a2.006,2.006,0,0,0,2-2V4A2.006,2.006,0,0,0,18,2ZM9,4h2V9l-1-.75L9,9Zm9,16H6V4H7v9l3-2.25L13,13V4h5Z"/></svg></a>
			</li>
		</ul>
		<!-- 
		<div class="user-col active">
			<img src="<lams:LAMSURL/>images/components/img1.png" alt="#" />
			<span></span>
		</div>
		 -->
	</div>
	<div class="component-page-wrapper monitoring-page-content">
		<header class="d-flex justify-content-between">
			<div class="hamburger-box">
				<div class="hamburger">
					<span></span>
					<span></span>
					<span></span>
				</div>
				<p>Lesson Name</p>
			</div>
			<div class="top-menu">
				<form>
					<input type="text" name="" placeholder="Search Student">
					<img src="<lams:LAMSURL/>images/components/search.svg" alt="#" />
				</form>
				<div class="top-menu-btn">
					<a href="#"><img src="<lams:LAMSURL/>images/components/icon1.svg" alt="#" /></a>
					<a href="#" onClick="javscript:refreshMonitor()"><img src="<lams:LAMSURL/>images/components/icon2.svg" alt="#" /></a>
					<a href="#"><img src="<lams:LAMSURL/>images/components/icon3.svg" alt="#" /></a>
				</div>
			</div>
		</header>
		<div class="row pt-5">
			<div class="col-12 col-md-3 content-left">
				<div class="graph-col monitoring-panel">
					<h6><fmt:message key="lesson.chart.title"/></h6>
					<div id="chartDiv" class="panel-body"></div>
				
				<!--
					<div class="graph-con">
						<canvas id="myChart" class="chartjs-render-monitor"></canvas>
					</div>
					<ul>
						<li>
							<h6>completion</h6>
						</li>
						<li>
							<span class="graph-count"></span> - Not started
						</li>
						<li>
							<span class="graph-count"></span> - In process
						</li>
						<li>
							<span class="graph-count"></span> - Completed
						</li>
					</ul>
					-->
				</div>
				

				<div class="graph-grades">
					<div class="graph-star-col">
						<img src="<lams:LAMSURL/>images/components/star.png" alt="#" />
						<p>grades</p>
					</div>
					<div class="grades-progress-col">
						<div class="grades-left">
							<div class="grades-score d-flex">
								<p><span>High:</span> 99</p>
								<div class="grades-progress-bar">
									<div class="progress-div" id="progress-bar-1" style="width: 99%">
										<span></span>
									</div>
								</div>
							</div>
							<div class="grades-score d-flex">
								<p><span>Median:</span> 68</p>
								<div class="grades-progress-bar">
									<div class="progress-div" id="progress-bar-2" style="width: 68%">
										<span></span>
									</div>
								</div>
							</div>
							<div class="grades-score d-flex">
								<p><span>Low:</span> 28</p>
								<div class="grades-progress-bar">
									<div class="progress-div" id="progress-bar-3" style="width: 28%">
										<span></span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="score-col">
					<div class="row">
						<div class="col-12 col-sm-6">
							<div class="score-col-inner" id="score-col1">
								<h1>8<span>%</span></h1>
								<p>Students <br>at risk</p>
								<img src="<lams:LAMSURL/>images/components/icon1.png" alt="#" />
							</div>
						</div>
						<div class="col-12 col-sm-6">
							<div class="score-col-inner" id="score-col2">
								<h1>98<span>%</span></h1>
								<p>Average <br>Score</p>
								<img src="<lams:LAMSURL/>images/components/icon2.png" alt="#" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="canvas-container" class="svg-learner-draggable-area h-100 col-12 col-md-9 content-right">
				<div id="sequenceTopButtonsContainer" class="topButtonsContainer">
					<a id="liveEditButton" class="btn btn-sm btn-default" style="display:none" title="<fmt:message key='button.live.edit.tooltip'/>"
				       href="#" onClick="javascript:openLiveEdit()">
						<i class="fa fa-pencil"></i> <span class="hidden-xs"><fmt:message key='button.live.edit'/></span>
					</a>
					<a id="canvasFitScreenButton" class="btn btn-sm btn-default" title="<fmt:message key='button.canvas.fit.screen.tooltip'/>"
				       href="#" onClick="javascript:canvasFitScreen(true)">
						<i class="fa fa-arrows-alt"></i> <span class="hidden-xs"><fmt:message key='button.canvas.fit.screen'/></span>
					</a>
					<a id="canvasOriginalSizeButton" class="btn btn-sm btn-default" title="<fmt:message key='button.canvas.original.size.tooltip'/>"
				       href="#" onClick="javascript:canvasFitScreen(false)">
						<i class="fa fa-arrow-circle-o-up"></i> <span class="hidden-xs"><fmt:message key='button.canvas.original.size'/></span>
					</a>
				</div>
				<div id="sequenceCanvas"></div>
				<div id="completedLearnersContainer" class="mt-2" title="<fmt:message key='force.complete.end.lesson.tooltip' />">
					<img id="completedLessonLearnersIcon" src="<lams:LAMSURL/>images/completed.svg" />
				</div>
				<img id="sequenceCanvasLoading"
				     src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
				<img id="sequenceSearchedLearnerHighlighter"
				     src="<lams:LAMSURL/>images/pedag_down_arrow.gif" />
				<!--
				<img src="<lams:LAMSURL/>images/components/user-map.png" alt="#" />
				<div class="map-pn">
					<span><img src="<lams:LAMSURL/>images/components/plus.svg" alt="#" /></span>
					<span><img src="<lams:LAMSURL/>images/components/minus.svg" alt="#" /></span>
				</div>
				-->
			</div>
		</div>
		<div class="row">
			<div id="required-tasks" class="col-12 col-md-6 mb-4">
				<div id="required-tasks-content" class="monitoring-panel">
					<h6><fmt:message key="lesson.required.tasks"/></h6>
				</div>
			</div>
			<div class="col-12 col-md-6 mb-4">
				<div class="monitoring-panel insight-col">
					<h6>insights</h6>
					<div class="insight-col-content">
						<p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem</p>
						<p><a href="#">read more...</a></p>
					</div>
				</div>
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
</body>
</lams:html>