<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"
	import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<!DOCTYPE html>
<lams:html>
<lams:head>

	<link rel="stylesheet"
		href="<lams:LAMSURL/>css/jquery-ui.timepicker.css" type="text/css"
		media="screen" />
	<link href="/lams/css/defaultHTML_learner.css" rel="stylesheet"
		type="text/css">
	<link rel="stylesheet"
		href="<lams:LAMSURL />css/bootstrap-tour.min.css">
	<link rel="stylesheet"
		href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css"
		type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/progressBar.css"
		type="text/css" />
	<link rel="stylesheet" href="<lams:LAMSURL/>/css/chart.css"
		type="text/css" />
	<link rel="stylesheet" href="css/monitorLesson.css" type="text/css"
		media="screen" />

	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		$(document).bind("mobileinit", function(){
		  $.mobile.loadingMessage = false;
		  $.mobile.ignoreContentEnabled = true;
		  $('body').attr('data-enhance', 'false');
		});
	</script>

	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/readmore.min.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL />includes/javascript/d3.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL />includes/javascript/chart.js"></script>
	<script type="text/javascript"
		src="<lams:WebAppURL />includes/javascript/monitorLesson.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/bootstrap.tabcontroller.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL />includes/javascript/bootstrap-tour.min.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/dialog.js"></script>


	<script type="text/javascript">
		var lessonId = ${lesson.lessonID},
			userId = '<lams:user property="userID"/>',
			ldId = ${lesson.learningDesignID},
			lessonStateId = ${lesson.lessonStateID},
			createDateTimeStr = '${lesson.createDateTimeStr}',
			// settings for progress bar
			isHorizontalBar = true,
			hasContentFrame = false,
			presenceEnabled =  false,
			hasDialog = false,
			sequenceTabShowInfo = ${sequenceTabShowInfo eq true},
			tourInProgress = false;
			
			LAMS_URL = '<lams:LAMSURL/>',
			
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
				CONTRIBUTE_ATTENTION : '<c:out value="${CONTRIBUTE_ATTENTION_VAR}" />',
				<fmt:message key="button.help" var="BUTTON_HELP_VAR"/>
				HELP : '<c:out value="${BUTTON_HELP_VAR}" />',
				<fmt:message key="label.lesson.introduction" var="LESSON_INTRODUCTION_VAR"/>
				LESSON_INTRODUCTION : '<c:out value="${LESSON_INTRODUCTION_VAR}" />',
				<fmt:message key="label.email" var="EMAIL_TITLE_VAR"/>
				EMAIL_TITLE : '<c:out value="${EMAIL_TITLE_VAR}" />',
				<fmt:message key="tour.this.is.disabled" var="TOUR_DISABLED_ELEMENT_VAR"/>
				TOUR_DISABLED_ELEMENT : '<c:out value="${TOUR_DISABLED_ELEMENT_VAR}" />'
			}
	    
		$(document).ready(function(){
			initLessonTab();
			initSequenceTab();
			initLearnersTab();
			refreshMonitor();
			<c:if test="${not empty lesson.lessonDescription}">
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
        }
        

    	<%@ include file="monitorTour.jsp" %> 

	</script>


	<!-- Some settings need to be done in the script first and only then this file can be included -->
	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/progressBar.js"></script>
</lams:head>
<body>

	<%-- "loading..." screen, gets removed on page full load --%>
	<div id="loadingOverlay">
		<i class="fa fa-refresh fa-spin fa-2x fa-fw"></i>
	</div>

	<lams:Page type="navbar">
		<lams:Tabs control="true">
			<lams:Tab id="1" key="tab.lesson" />
			<lams:Tab id="2" key="tab.sequence" />
			<lams:Tab id="3" key="tab.learners" />
		</lams:Tabs>
		<lams:TabBodyArea>
			<lams:TabBodys>
				<lams:TabBody id="1" titleKey="label.basic">
					<div class="row">
						<div class="col-xs-12">
							<button onclick="javascript:startTour();return false;"
								class="btn btn-sm btn-default pull-right roffset10 tour-button">
								<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message
										key="label.tour" /></span>
							</button>

							<a id="tour-refresh-button"
								class="btn btn-sm btn-default pull-right roffset10"
								title="<fmt:message key='button.refresh.tooltip'/>" href="#"
								onClick="javascript:refreshMonitor('lesson')"> <i
								class="fa fa-refresh"></i> <span class="hidden-xs"><fmt:message
										key="button.refresh" /></span></a>
							<p id="tabLessonLessonName">
								<span class="lead"><strong><c:out
											value="${lesson.lessonName}" /></strong></span> <br /> <span
									class="text-muted"><small><c:out
											value="${lesson.organisationName}" escapeXml="true" /></small></span>
							</p>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-9 col-xs-6">

							<!-- Lesson details -->
							<dl id="lessonDetails" class="dl-horizontal">
								<c:if test="${not empty lesson.lessonDescription}">
									<dt>
										<fmt:message key="lesson.description" />
									</dt>
									<dd id="tabLessonLessonDescription">
										<div id="description">
											<c:out value="${lesson.lessonDescription}" escapeXml="false" />
										</div>
									</dd>
								</c:if>
								<dt>
									<fmt:message key="lesson.state" />
								</dt>
								<dd>
									<span data-toggle="collapse" data-target="#changeState"
										id="lessonStateLabel"></span>

									<!--  Change lesson status or start/schedule start -->
									<div class="collapse offset10" id="changeState">
										<div id="lessonScheduler">
											<form class="form-inline">
												<div class="form-group">
													<label for="scheduleDatetimeField"><fmt:message
															key="lesson.start" /></label> <input
														class="lessonManageField input-sm"
														id="scheduleDatetimeField" /> <span
														id="lessonStartDateSpan" class="lessonManageField"></span>
													<a id="scheduleLessonButton"
														class="btn btn-xs btn-default lessonManageField" href="#"
														onClick="javascript:scheduleLesson()"
														title='<fmt:message key="button.schedule.tooltip"/>'>
														<fmt:message key="button.schedule" />
													</a> <a id="startLessonButton" class="btn btn-xs btn-default"
														href="#" onClick="javascript:startLesson()"
														title='<fmt:message key="button.start.now.tooltip"/>'>
														<fmt:message key="button.start.now" />
													</a>
												</div>
											</form>
										</div>

										<div id="lessonStateChanger">
											<span id="lessonStartDateSpan" class="lessonManageField"></span>
											<select id="lessonStateField" class="btn btn-xs">
												<option value="-1"><fmt:message
														key="lesson.select.state" /></option>
											</select>

											<button type="button" class="btn btn-xs btn-primary"
												onClick="javascript:changeLessonState()"
												title='<fmt:message key="lesson.change.state.tooltip"/>'>
												<i class="fa fa-check"></i> <span class="hidden-xs"><fmt:message
														key="button.apply" /></span>
											</button>
										</div>
									</div>
								</dd>

								<dt>
									<fmt:message key="lesson.learners" />
									:
								</dt>
								<dd id="learnersStartedPossibleCell"></dd>
								<c:set var="showLearnerURL"
									value="${Configuration.get(ConfigurationKeys.ALLOW_DIRECT_LESSON_LAUNCH)}" />
								<c:if test="${showLearnerURL}">
									<c:set var="serverURL"
										value="${Configuration.get(ConfigurationKeys.SERVER_URL)}" />
									<c:if
										test="${fn:substring(serverURL, fn:length(serverURL)-1, fn:length(serverURL)) != '/'}">
										<c:set var="serverURL">${serverURL}/</c:set>
									</c:if>
									<dt class="voffset5">
										<fmt:message key="lesson.learner.url" />
									</dt>
									<dd class="voffset5">
										<input id="learnerURLField" class="lessonManageField"
											value="${serverURL}r/${lesson.encodedLessonID}"
											readonly="readonly" /> <a
											class="btn btn-sm btn-default lessonManageField" href="#"
											onClick="javascript:selectLearnerURL()"><fmt:message
												key="button.select" /></a> <span id="copyLearnerURL"><fmt:message
												key="lesson.copy.prompt" /></span>
									</dd>
								</c:if>
								<!--  lesson actions -->
								<dt>
									<fmt:message key="lesson.manage" />
									:
								</dt>
								<dd>
									<div class="btn-group btn-group-xs" role="group"
										id="lessonActions">
										<button id="viewLearnersButton"
											class="btn btn-default roffset10" type="button"
											onClick="javascript:showLessonLearnersDialog()"
											title='<fmt:message key="button.view.learners.tooltip"/>'>
											<i class="fa fa-sm fa-users"></i> <span class="hidden-xs"><fmt:message
													key="button.view.learners" /></span>
										</button>
										<button id="editClassButton" class="btn btn-default roffset10"
											type="button" onClick="javascript:showClassDialog()"
											title='<fmt:message key="button.edit.class.tooltip"/>'>
											<i class="fa fa-sm fa-user-times"></i> <span
												class="hidden-xs"><fmt:message
													key="button.edit.class" /></span>
										</button>
										<c:if test="${lesson.enabledLessonNotifications}">
											<button id="notificationButton"
												class="btn btn-default roffset10" type="button"
												onClick="javascript:showNotificationsDialog(null,${lesson.lessonID})">
												<i class="fa fa-sm fa-bullhorn"></i> <span class="hidden-xs"><fmt:message
														key="email.notifications" /></span>
											</button>
										</c:if>
										<c:if test="${lesson.enableLessonIntro}">
											<button id="editIntroButton" class="btn btn-default"
												type="button"
												onClick="javascript:showIntroductionDialog(${lesson.lessonID})">
												<i class="fa fa-sm fa-info"></i> <span class="hidden-xs"><fmt:message
														key="label.lesson.introduction" /></span>
											</button>
										</c:if>
									</div>
								</dd>

								<!-- IM & Presence -->
								<dt>
									<fmt:message key="lesson.im" />
									:
								</dt>
								<dd>
									<div class="btn-group btn-group-xs" role="group"
										id="tour-lesson-im">
										<button id="presenceButton"
											class="btn btn-default roffset10
											<c:if test="${lesson.learnerPresenceAvailable}">
												btn-success
											</c:if>
											">
											<i class="fa fa-sm fa-wifi"></i> <span class="hidden-xs"><fmt:message
													key="lesson.presence" /></span> <span id="presenceCounter"
												class="badge">0</span>
										</button>

										<button id="imButton"
											class="btn btn-default roffset10
											<c:if test="${lesson.learnerImAvailable}">
												btn-success
											</c:if>
											"
											<c:if test="${not lesson.learnerPresenceAvailable}">
												style="display: none"
											</c:if>>
											<i class="fa fa-sm fa-comments-o"></i> <span
												class="hidden-xs"><fmt:message key="lesson.im" /></span>
										</button>

										<button id="openImButton" class="btn btn-default"
											<c:if test="${not lesson.learnerImAvailable}">
												style="display: none"
											</c:if>>
											<i class="fa fa-sm fa-comments"></i> <span class="hidden-xs"><fmt:message
													key="button.open.im" /></span>
										</button>
									</div>
								</dd>
							</dl>
						</div>
						<div class="panel panel-default pull-right">
							<div class="panel-heading">
								<fmt:message key="lesson.chart.title" />
							</div>
							<div id="chartDiv" class="panel-body"></div>
						</div>
					</div>

					<!-- Required tasks -->
					<div id="requiredTasks" class="panel panel-warning"
						style="display: none;">
						<div class="panel-heading">
							<div class="panel-title">
								<fmt:message key="lesson.required.tasks" />
							</div>
						</div>
						<div class="panel-body">
							<span id="contributeHeader"></span>
						</div>
					</div>

					<table id="tabLessonTable" class="table table-striped">
						<tr id="contributeHeader">
							<td colspan="2" class="active"><fmt:message
									key="lesson.required.tasks" /></td>
						</tr>
						<c:forEach var="activity" items="${contributeActivities}">
							<tr class="contributeRow">
								<td colspan="2" class="contributeActivityCell"><c:out
										value="${activity.title}" /></td>
							</tr>
							<c:forEach var="entry" items="${activity.contributeEntries}">
								<c:if test="${entry.isRequired}">
									<tr class="contributeRow">
										<td colspan="2" class="contributeEntryCell"><c:choose>
												<c:when test="${entry.contributionType eq 3}">
													<fmt:message key="lesson.task.gate" />
												</c:when>
												<c:when test="${entry.contributionType eq 6}">
													<fmt:message key="lesson.task.grouping" />
												</c:when>
												<c:when test="${entry.contributionType eq 9}">
													<fmt:message key="lesson.task.branching" />
												</c:when>
											</c:choose> <a href="#" class="btn btn-sm btn-default"
											onClick="javascript:openPopUp('${entry.URL}','ContributeActivity', 648, 1152, true)"
											title='<fmt:message key="button.task.go.tooltip"/>'> <fmt:message
													key="button.task.go" />
										</a></td>
									</tr>
								</c:if>
							</c:forEach>
						</c:forEach>
					</table>
				</lams:TabBody>
				<lams:TabBody id="2" titleKey="label.advanced">
					<div id="sequenceTopButtonsContainer" class="topButtonsContainer">
						<button onclick="javascript:startTour();return false;"
							class="btn btn-sm btn-default pull-right roffset10 tour-button">
							<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message
									key="label.tour" /></span>
						</button>

						<a id="refreshButton" class="btn btn-sm btn-default"
							title="<fmt:message key='button.refresh.tooltip'/>" href="#"
							onClick="javascript:refreshMonitor('sequence')"> <i
							class="fa fa-refresh"></i> <span class="hidden-xs"><fmt:message
									key="button.refresh" /></span>
						</a>
						<c:if test="${enableLiveEdit && lesson.liveEditEnabled}">
							<a id="liveEditButton" class="btn btn-sm btn-default"
								title="<fmt:message key='button.live.edit.tooltip'/>" href="#"
								onClick="javascript:openLiveEdit()"> <i class="fa fa-pencil"></i>
								<span class="hidden-xs"><fmt:message
										key='button.live.edit' /></span>
							</a>
						</c:if>
						<span id="sequenceSearchPhraseClear"
							class="fa fa-xs fa-times-circle"
							onClick="javascript:sequenceClearSearchPhrase(true)"
							title="<fmt:message key='learners.search.phrase.clear.tooltip' />"></span>
						<input id="sequenceSearchPhrase"
							title="<fmt:message key='search.learner.textbox' />" /> <span
							id="sequenceSearchPhraseIcon" class="ui-icon ui-icon-search"
							title="<fmt:message key='search.learner.textbox' />"></span>
					</div>
					<div id="sequenceCanvas"></div>
					<div id="completedLearnersContainer"
						title="<fmt:message key='force.complete.end.lesson.tooltip' />">
						<img id="completedLearnersDoorIcon"
							src="<lams:LAMSURL/>images/icons/door_open.png" />
					</div>
					<img id="sequenceCanvasLoading"
						src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
					<img id="sequenceSearchedLearnerHighlighter"
						src="<lams:LAMSURL/>images/pedag_down_arrow.gif" />
				</lams:TabBody>
				<lams:TabBody id="3" titleKey="label.conditions">
					<table id="tabLearnerControlTable">
						<tr>
							<td class="learnersHeaderCell"><fmt:message
									key='learners.page' /><br /> <span id="learnersPageCounter" />
							</td>
							<td class="learnersHeaderCell"><span
								id="learnersSearchPhraseIcon" class="ui-icon ui-icon-search"
								title="<fmt:message key='search.learner.textbox' />"></span> <input
								id="learnersSearchPhrase"
								title="<fmt:message key='search.learner.textbox' />" /> <span
								id="learnersSearchPhraseClear" class="fa fa-xs fa-times-circle"
								onClick="javascript:learnersClearSearchPhrase()"
								title="<fmt:message key='learners.search.phrase.clear.tooltip' />"></span>
							</td>
							<td id="learnersPageLeft"
								class="learnersHeaderCell learnersPageShifter"
								title="<fmt:message key='learner.group.backward.10'/>"
								onClick="javascript:learnersPageShift(false)"><span
								class="ui-icon ui-icon-seek-prev"></span></td>
							<td id="learnersPageRight"
								class="learnersHeaderCell learnersPageShifter"
								title="<fmt:message key='learner.group.forward.10'/>"
								onClick="javascript:learnersPageShift(true)"><span
								class="ui-icon ui-icon-seek-next"></span></td>
							<td class="learnersHeaderCell"><fmt:message
									key='learners.order' /><br /> <input
								id="orderByCompletionCheckbox" type="checkbox"
								onChange="javascript:loadLearnerProgressPage()" /></td>
							<td class="topButtonsContainer">
								<button onclick="javascript:startTour();return false;"
									class="btn btn-sm btn-default pull-right roffset10 tour-button">
									<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message
											key="label.tour" /></span>
								</button> <a class="btn btn-sm btn-default"
								title="<fmt:message key='button.refresh.tooltip'/>" href="#"
								onClick="javascript:refreshMonitor('learners')"> <i
									class="fa fa-refresh"></i> <span class="hidden-xs"><fmt:message
											key="button.refresh" /></span></a> <a class="btn btn-sm btn-default"
								title="<fmt:message key='button.journal.entries.tooltip'/>"
								href="#" id="journalButton"
								onClick="javascript:openPopUp('<lams:LAMSURL/>learning/notebook.do?method=viewAllJournals&lessonID=${lesson.lessonID}', 'JournalEntries', 648, 1152, true)">
									<i class="fa fa-book"></i> <span class="hidden-xs"><fmt:message
											key="button.journal.entries" /></span>
							</a>
							</td>
						</tr>
					</table>

					<div id="tabLearnersContainer">
						<div class="table-responsive">
							<table id="tabLearnersTable"
								class="table table-condensed table-responsive"></table>
						</div>
					</div>
				</lams:TabBody>
			</lams:TabBodys>
		</lams:TabBodyArea>
	</lams:Page>

	<!-- Inner dialog placeholders -->

	<div id="learnerGroupDialogContents" class="dialogContainer">
		<span id="learnerGroupMultiSelectLabel"><fmt:message
				key='learner.group.multi.select' /></span>
		<table id="listLearners" class="table table-condensed">
			<tr id="learnerGroupSearchRow">
				<td><span class="dialogSearchPhraseIcon fa fa-xs fa-search"
					title="<fmt:message key='search.learner.textbox' />"></span></td>
				<td colspan="4"><input class="dialogSearchPhrase"
					title="<fmt:message key='search.learner.textbox' />" /></td>
				<td><span
					class="dialogSearchPhraseClear fa fa-xs fa-times-circle"
					onClick="javascript:learnerGroupClearSearchPhrase()"
					title="<fmt:message key='learners.search.phrase.clear.tooltip' />"></span>
				</td>
			</tr>
			<tr>
				<td class="navCell pageMinus10Cell"
					title="<fmt:message key='learner.group.backward.10'/>"
					onClick="javascript:shiftLearnerGroupList(-10)"><span
					class="ui-icon ui-icon-seek-prev"></span></td>
				<td class="navCell pageMinus1Cell"
					title="<fmt:message key='learner.group.backward.1'/>"
					onClick="javascript:shiftLearnerGroupList(-1)"><span
					class="ui-icon ui-icon-arrowthick-1-w"></span></td>
				<td class="pageCell" title="<fmt:message key='learners.page'/>">
				</td>
				<td class="navCell pagePlus1Cell"
					title="<fmt:message key='learner.group.forward.1'/>"
					onClick="javascript:shiftLearnerGroupList(1)"><span
					class="ui-icon ui-icon-arrowthick-1-e"></span></td>
				<td class="navCell pagePlus10Cell"
					title="<fmt:message key='learner.group.forward.10'/>"
					onClick="javascript:shiftLearnerGroupList(10)"><span
					class="ui-icon ui-icon-seek-next"></span></td>
				<td class="navCell sortCell"
					title="<fmt:message key='learner.group.sort.button'/>"
					onClick="javascript:sortLearnerGroupList()"><span
					class="ui-icon ui-icon-triangle-1-n"></span></td>
			</tr>
			<tr>
				<td colspan="6" class="dialogList"></td>
			</tr>
		</table>
		<div class="btn-group pull-right">
			<button id="learnerGroupDialogForceCompleteButton"
				class="learnerGroupDialogSelectableButton btn btn-default roffset5">
				<span><fmt:message key="button.force.complete" /></span>
			</button>
			<button id="learnerGroupDialogViewButton"
				class="learnerGroupDialogSelectableButton btn btn-default roffset5">
				<span><fmt:message key="button.view.learner" /></span>
			</button>
			<button id="learnerGroupDialogEmailButton"
				class="learnerGroupDialogSelectableButton btn btn-default roffset5">
				<span><fmt:message key="button.email" /></span>
			</button>
			<button id="learnerGroupDialogCloseButton" class="btn btn-default">
				<span><fmt:message key="button.close" /></span>
			</button>
		</div>
	</div>

	<div id="classDialogContents" class="dialogContainer">
		<div id="classDialogTable">
			<div class="row">
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
		<fmt:message key="sequence.help.info" />
	</div>

	<div id="forceBackwardsDialogContents" class="dialogContainer">
		<div id="forceBackwardsMsg"></div>
		<div class="btn-group pull-right voffset10">

			<button id="forceBackwardsRemoveContentNoButton"
				class="btn btn-default roffset5">
				<span><fmt:message key="force.complete.remove.content.no" /></span>
			</button>

			<button id="forceBackwardsRemoveContentYesButton"
				class="btn btn-default roffset5">
				<span><fmt:message key="force.complete.remove.content.yes" /></span>
			</button>

			<button id="forceBackwardsCloseButton" class="btn btn-default">
				<span><fmt:message key="button.close" /></span>
			</button>
		</div>
	</div>

	<div id="tooltip"></div>
</body>
</lams:html>
