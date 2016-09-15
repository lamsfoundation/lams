<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<!DOCTYPE html>
<lams:html>
<lams:head>

	<lams:css style="main" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui.timepicker.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/progressBar.css" type="text/css" />
	<link rel="stylesheet" href="css/monitorLesson.css" type="text/css" media="screen" />

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" >
		$(document).bind("mobileinit", function(){
		  $.mobile.loadingMessage = false;
		  $.mobile.ignoreContentEnabled = true;
		  $('body').attr('data-enhance', 'false');
		});
	</script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.mobile.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
	<script type="text/javascript" src="includes/javascript/monitorLesson.js"></script>
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
			initTabs();
			initLessonTab();
			initSequenceTab();
			initLearnersTab();
			refreshMonitor();
			
			// remove "loading..." screen
			$('#loadingOverlay').remove();
		});
	</script>
	<!-- Some settings need to be done in the script first and only then this file can be included -->
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/progressBar.js"></script>
</lams:head>
<body data-enhance="false">

<%-- "loading..." screen, gets removed on page full load --%>
<div id="loadingOverlay">
	<img src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
</div>

<div id="tabs">
	<!-- Tab names -->
	<ul>
		<li><a id="tabLessonLink" href="#tabLesson"><fmt:message key="tab.lesson"/></a></li>
		<li><a id="tabSequenceLink" href="#tabSequence"><fmt:message key="tab.sequence"/></a></li>
		<li><a id="tabLearnersLink" href="#tabLearners"><fmt:message key="tab.learners"/></a></li>
	</ul>
	
	<!-- Tab contents -->
	
	<div id="tabLesson">
		<table id="tabLessonTable">
			<tr>
				<td class="fieldLabel">
				</td>
				<td class="topButtonsContainer">
					<a target="_blank" class="button" title="<fmt:message key='button.help.tooltip'/>"
					   href="http://wiki.lamsfoundation.org/display/lamsdocs/monitoringlesson">
					<fmt:message key="button.help"/></a>
					<a class="button" title="<fmt:message key='button.refresh.tooltip'/>"
					   href="#" onClick="javascript:refreshMonitor('lesson')">
					<fmt:message key="button.refresh"/></a>
				</td>
			</tr>
			<tr>
				<td class="fieldLabel">
					<fmt:message key="lesson.name"/>
				</td>
				<td id="tabLessonLessonName">
					<c:out value="${lesson.lessonName}" />
				</td>
			</tr>
			<tr>
				<td class="fieldLabel">
					<fmt:message key="lesson.description"/>
				</td>
				<td id="tabLessonLessonDescription">
					<c:out value="${lesson.lessonDescription}" escapeXml="false"/>
				</td>
			</tr>
			<tr>
				<td class="fieldLabel">
					<fmt:message key="lesson.state"/>
				</td>
				<td id="lessonStateLabel"></td>
			<tr>
				<td class="fieldLabel">
					<fmt:message key="lesson.learners"/>
				</td>
				<td id="learnersStartedPossibleCell"></td>
			</tr>
			
			<c:set var="serverURL"><%=Configuration.get(ConfigurationKeys.SERVER_URL)%></c:set>
			<c:if test="${fn:substring(serverURL, fn:length(serverURL)-1, fn:length(serverURL)) != '/'}">
				<c:set var="serverURL">${serverURL}/</c:set>
			</c:if>
			<c:set var="showLearnerURL"><%=Configuration.get(ConfigurationKeys.ALLOW_DIRECT_LESSON_LAUNCH)%></c:set>
			<c:if test="${showLearnerURL}">
				<tr>
					<td class="fieldLabel">
						<fmt:message key="lesson.learner.url"/>
					</td>
					<td>
						<input id="learnerURLField" class="lessonManageField"
						       value="${serverURL}r/${lesson.encodedLessonID}"
						       readonly="readonly" />
						<a class="button lessonManageField" href="#"
						   onClick="javascript:selectLearnerURL()"><fmt:message key="button.select"/></a>
						<span id="copyLearnerURL"><fmt:message key="lesson.copy.prompt"/></span>
					</td>
				</tr>
			</c:if>
			
			<tr>
				<td class="fieldLabel">
					<fmt:message key="lesson.class"/>
				</td>
				<td>
					<c:out value="${lesson.organisationName}" escapeXml="true"/>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="sectionHeader">
					<fmt:message key="lesson.manage"/>
				</td>
			</tr>
			<tr>
				<td class="fieldLabel">
					<fmt:message key="lesson.class"/>
				</td>
				<td>
					<a id="viewLearnersButton" class="button lessonManageField" href="#"
					   onClick="javascript:showLessonLearnersDialog()"
					   title='<fmt:message key="button.view.learners.tooltip"/>'>
					   <fmt:message key="button.view.learners"/>
					</a>
					<a id="editClassButton" class="button lessonManageField" href="#"
					   onClick="javascript:showClassDialog()"
					   title='<fmt:message key="button.edit.class.tooltip"/>'>
					   <fmt:message key="button.edit.class"/>
					</a>
					<c:if test="${notificationsAvailable && lesson.enabledLessonNotifications}">
						<a id="notificationButton" class="button lessonManageField" href="#"
						   onClick="javascript:window.parent.showNotificationsDialog(null,${lesson.lessonID})">
						   <fmt:message key="email.notifications"/>
						</a>
					</c:if>
					
					<a id="openImButton" class="button" href="#"
					   onClick="javascript:openChatWindow()"
						<c:if test="${not lesson.learnerImAvailable}">
							style="display: none"
						</c:if>
						>
					   <fmt:message key="button.open.im"/>
				    </a>
				</td>
			</tr>
			<tr>
				<td class="fieldLabel">
					<fmt:message key="lesson.change.state"/>
				</td>
				<td>
					<select id="lessonStateField" class="lessonManageField">
						<option value="-1"><fmt:message key="lesson.select.state"/></option>
					</select>
					<a class="button" href="#"
					   onClick="javascript:changeLessonState()"
					   title='<fmt:message key="lesson.change.state.tooltip"/>'>
					   <fmt:message key="button.apply"/>
				    </a>
				</td>
			</tr>
			<tr>
				<td class="fieldLabel">
					<fmt:message key="lesson.start"/>
				</td>
				<td id="lessonStartDateCell">
					<span id="lessonStartDateSpan" class="lessonManageField"></span>
					<input id="scheduleDatetimeField" class="lessonManageField"/>
					<a id="scheduleLessonButton" class="button lessonManageField" href="#"
					   onClick="javascript:scheduleLesson()"
					   title='<fmt:message key="button.schedule.tooltip"/>'>
					   <fmt:message key="button.schedule"/>
					</a>
					<a id="startLessonButton" class="button" href="#"
					   onClick="javascript:startLesson()"
					   title='<fmt:message key="button.start.now.tooltip"/>'>
					   <fmt:message key="button.start.now"/>
					</a>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<input type="checkbox" id="presenceAvailableField"
						<c:if test="${lesson.learnerPresenceAvailable}">
							checked="checked"
						</c:if> 
					/>
					<fmt:message key="lesson.enable.presence"/>
					<span id="presenceAvailableCount">(<span>0</span>
						<fmt:message key="lesson.presence.count"/>)
					</span>
					<br />
					<input type="checkbox" id="imAvailableField"
						<c:if test="${not lesson.learnerPresenceAvailable}">
							disabled="disabled"
						</c:if>
						<c:if test="${lesson.learnerImAvailable}">
							checked="checked"
						</c:if> 
					/>
					<fmt:message key="lesson.enable.im"/>
				</td>
			</tr>
			<tr id="contributeHeader">
				<td colspan="2" class="sectionHeader">
					<fmt:message key="lesson.required.tasks"/>
				</td>
			</tr>
			<c:forEach var="activity" items="${contributeActivities}">
				<tr class="contributeRow">
					<td colspan="2" class="contributeActivityCell">
						<c:out value="${activity.title}" />
					</td>
				</tr>
				<c:forEach var="entry" items="${activity.contributeEntries}">
					<c:if test="${entry.isRequired}">
						<tr class="contributeRow">
							<td colspan="2" class="contributeEntryCell">
								<c:choose>
									<c:when test="${entry.contributionType eq 3}">
										<fmt:message key="lesson.task.gate"/>
									</c:when>
									<c:when test="${entry.contributionType eq 6}">
										<fmt:message key="lesson.task.grouping"/>
									</c:when>
									<c:when test="${entry.contributionType eq 9}">
										<fmt:message key="lesson.task.branching"/>
									</c:when>
								</c:choose>
								<a href="#" class="button"
								   onClick="javascript:openPopUp('${entry.URL}','ContributeActivity', 600, 800, true)"
								   title='<fmt:message key="button.task.go.tooltip"/>'>
								   <fmt:message key="button.task.go"/>
								</a>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</c:forEach>
		</table>
	</div>
	
	<div id="tabSequence">
		<div id="sequenceTopButtonsContainer" class="topButtonsContainer">
			<a id="helpButton" target="_blank" class="button" title="<fmt:message key='button.help.tooltip'/>"
			   href="http://wiki.lamsfoundation.org/display/lamsdocs/monitoringsequence">
				<fmt:message key="button.help"/>
			</a>
			<a id="refreshButton" class="button" title="<fmt:message key='button.refresh.tooltip'/>"
			   href="#" onClick="javascript:refreshMonitor('sequence')">
				<fmt:message key="button.refresh"/>
			</a>
			<c:if test="${enableLiveEdit && lesson.liveEditEnabled}">
				<a id="liveEditButton" class="button" title="<fmt:message key='button.live.edit.tooltip'/>"
			       href="#"
			  	   onClick="javascript:openLiveEdit()">
					<fmt:message key='button.live.edit'/>
				</a>
			</c:if>
			<span id="sequenceSearchPhraseClear"
				 class="ui-icon ui-icon-circle-close"
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
	</div>
	
	<div id="tabLearners">
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
						  class="ui-icon ui-icon-circle-close"
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
					<a target="_blank" class="button" title="<fmt:message key='button.help.tooltip'/>"
			   		   href="http://wiki.lamsfoundation.org/display/lamsdocs/monitoringlearners">
			   		   <fmt:message key="button.help"/></a>
					<a class="button" title="<fmt:message key='button.refresh.tooltip'/>"
					   href="#" onClick="javascript:refreshMonitor('learners')">
					   <fmt:message key="button.refresh"/></a>
					<a class="button" title="<fmt:message key='button.journal.entries.tooltip'/>"
			   		   href="#"
			           onClick="javascript:openPopUp('<lams:LAMSURL/>learning/notebook.do?method=viewAllJournals&lessonID=${lesson.lessonID}', 'JournalEntries', 570, 796, true)">
			           <fmt:message key="button.journal.entries"/></a>
				</td>
			</tr>
		</table>
		
		<div id="tabLearnersContainer">
			<table id="tabLearnersTable"></table>
		</div>
	</div>
	
	<!-- Inner dialog placeholders -->
	
	<div id="learnerGroupDialog" class="dialogContainer">
		<span id="learnerGroupMultiSelectLabel"><fmt:message key='learner.group.multi.select'/></span>
		<table>
			<tr id="learnerGroupSearchRow">
				<td>
					<span class="dialogSearchPhraseIcon ui-icon ui-icon-search"
						  title="<fmt:message key='search.learner.textbox' />"></span>
				</td>
				<td colspan="4">
					<input class="dialogSearchPhrase" 
						   title="<fmt:message key='search.learner.textbox' />"/>
				</td>
				<td>
					<span class="dialogSearchPhraseClear ui-icon ui-icon-circle-close"
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
	</div>
		
	<div id="classDialog" class="dialogContainer">
		<table id="classDialogTable">
			<tr>
				<td>
					<table id="classLearnerTable">
						<tr>
							<td class="dialogTitle" colspan="6">
								<fmt:message key="lesson.learners"/>
							</td>
						</tr>
						<tr>
							<td>
								<span class="dialogSearchPhraseIcon ui-icon ui-icon-search"
									  title="<fmt:message key='search.learner.textbox' />"></span>
							</td>
							<td colspan="4">
								<input class="dialogSearchPhrase" 
									   title="<fmt:message key='search.learner.textbox' />"/>
							</td>
							<td>
								<span class="dialogSearchPhraseClear ui-icon ui-icon-circle-close"
									  onClick="javascript:classClearSearchPhrase()"
									  title="<fmt:message key='learners.search.phrase.clear.tooltip' />" 
								></span>
							</td>
						</tr>
						<tr>
							<td class="navCell pageMinus10Cell"
								title="<fmt:message key='learner.group.backward.10'/>"
								onClick="javascript:shiftClassList('Learner', -10)">
									<span class="ui-icon ui-icon-seek-prev"></span>
							</td>
							<td class="navCell pageMinus1Cell"
								title="<fmt:message key='learner.group.backward.1'/>"
								onClick="javascript:shiftClassList('Learner', -1)">
								<span class="ui-icon ui-icon-arrowthick-1-w"></span>
							</td>
							<td class="pageCell"
								title="<fmt:message key='learners.page'/>">
							</td>
							<td class="navCell pagePlus1Cell"
								title="<fmt:message key='learner.group.forward.1'/>"
								onClick="javascript:shiftClassList('Learner', 1)">
									<span class="ui-icon ui-icon-arrowthick-1-e"></span>
							</td>
							<td class="navCell pagePlus10Cell" 
								title="<fmt:message key='learner.group.forward.10'/>"
								onClick="javascript:shiftClassList('Learner', 10)">
									<span class="ui-icon ui-icon-seek-next"></span>
							</td>
							<td class="navCell sortCell" 
								title="<fmt:message key='learner.group.sort.button'/>" 
								onClick="javascript:sortClassList('Learner')">
									<span class="ui-icon ui-icon-triangle-1-n"></span>
							</td>
						</tr>
						<tr>
							<td class="dialogList" colspan="6">
							</td>
						</tr>
					</table>
				</td>
				<td>
					<table id="classMonitorTable">
						<tr>
							<td class="dialogTitle" colspan="6">
								<fmt:message key="lesson.monitors"/>
							</td>
						</tr>
						<tr>
							<td id="classMonitorSearchRow" colspan="6">&nbsp;</td>
						</tr>
						<tr>
							<td class="navCell pageMinus10Cell"
								title="<fmt:message key='learner.group.backward.10'/>"
								onClick="javascript:shiftClassList('Monitor', -10)">
									<span class="ui-icon ui-icon-seek-prev"></span>
							</td>
							<td class="navCell pageMinus1Cell"
								title="<fmt:message key='learner.group.backward.1'/>"
								onClick="javascript:shiftClassList('Monitor', -1)">
								<span class="ui-icon ui-icon-arrowthick-1-w"></span>
							</td>
							<td class="pageCell"
								title="<fmt:message key='learners.page'/>">
							</td>
							<td class="navCell pagePlus1Cell"
								title="<fmt:message key='learner.group.forward.1'/>"
								onClick="javascript:shiftClassList('Monitor', 1)">
									<span class="ui-icon ui-icon-arrowthick-1-e"></span>
							</td>
							<td class="navCell pagePlus10Cell" 
								title="<fmt:message key='learner.group.forward.10'/>"
								onClick="javascript:shiftClassList('Monitor', 10)">
									<span class="ui-icon ui-icon-seek-next"></span>
							</td>
							<td class="navCell sortCell" 
								title="<fmt:message key='learner.group.sort.button'/>" 
								onClick="javascript:sortClassList('Monitor')">
									<span class="ui-icon ui-icon-triangle-1-n"></span>
							</td>
						</tr>
						<tr>
							<td class="dialogList" colspan="6">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="emailDialog" class="dialogContainer">
		<iframe id="emailFrame"></iframe>
	</div>
	
	<div id="sequenceInfoDialog" class="dialogContainer">
		<fmt:message key="sequence.help.info"/>
	</div>
	
	<div id="forceBackwardsDialog" class="dialogContainer"></div>
	
	<div id="tooltip"></div>
</div>
</body>
</lams:html>