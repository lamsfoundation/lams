<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<!DOCTYPE HTML>
<lams:html>
<lams:head>

	<lams:css style="main" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui.timepicker.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/progressBar.css" type="text/css" />
	<link rel="stylesheet" href="css/monitorLesson.css" type="text/css" media="screen" />

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/raphael/raphael.js"></script>
	<script type="text/javascript" src="includes/javascript/monitorLesson.js"></script>
	<script type="text/javascript">
		var userId = '<lams:user property="userID"/>';
		var lessonId = ${lesson.lessonID};
		var ldId = ${lesson.learningDesignID};
		var lessonStateId = ${lesson.lessonStateID};
		var createDateTimeStr = '${lesson.createDateTimeStr}';
		// settings for progress bar
		var isHorizontalBar = true;
		var hasContentFrame = false;
		var presenceEnabled =  false;
		var hasDialog = false;
		
		var LAMS_URL = '<lams:LAMSURL/>';
		
		var FORCE_COMPLETE_CLICK_LABEL = '<fmt:message key="force.complete.click"/>';
		var FORCE_COMPLETE_BUTTON_LABEL = '<fmt:message key="button.force.complete"/>';
		var FORCE_COMPLETE_END_LESSON_CONFIRM_LABEL = '<fmt:message key="force.complete.end.lesson.confirm"/>';
		var FORCE_COMPLETE_ACTIVITY_CONFIRM_LABEL = '<fmt:message key="force.complete.activity.confirm"/>';
		var FORCE_COMPLETE_DROP_FAIL_LABEL = '<fmt:message key="force.complete.drop.fail"/>';
		var LEARNER_GROUP_COUNT_LABEL = '<fmt:message key="learner.group.count"/>';
		var LEARNER_GROUP_SHOW_LABEL = '<fmt:message key="learner.group.show"/>';
		var LEARNER_GROUP_REMOVE_PROGRESS = '<fmt:message key="learner.group.remove.progress"/>';
		var LEARNER_GROUP_LIST_TITLE_LABEL = '<fmt:message key="learner.group.list.title"/>';
		var VIEW_LEARNER_BUTTON_LABEL = '<fmt:message key="button.view.learner"/>';
		var EMAIL_BUTTON_LABEL = '<fmt:message key="button.email"/>';
		var CLOSE_BUTTON_LABEL = '<fmt:message key="button.close"/>';
		var NOTIFCATIONS_LABEL = '<fmt:message key="email.notifications"/>';
		var LEARNER_FINISHED_COUNT_LABEL = '<fmt:message key="learner.finished.count"/>';
		var LEARNER_FINISHED_DIALOG_TITLE_LABEL = '<fmt:message key="learner.finished.dialog.title"/>';
		var LESSON_PRESENCE_ENABLE_ALERT_LABEL = '<fmt:message key="lesson.enable.presence.alert"/>';
		var LESSON_PRESENCE_DISABLE_ALERT_LABEL = '<fmt:message key="lesson.disable.presence.alert"/>';
		var LESSON_IM_ENABLE_ALERT_LABEL = '<fmt:message key="lesson.enable.im.alert"/>';
		var LESSON_IM_DISABLE_ALERT_LABEL = '<fmt:message key="lesson.disable.im.alert"/>';
		var LESSON_REMOVE_ALERT_LABEL = '<fmt:message key="lesson.remove.alert"/>';
		var LESSON_REMOVE_DOUBLECHECK_ALERT_LABEL = '<fmt:message key="lesson.remove.doublecheck.alert"/>';
		var LESSON_STATE_CREATED_LABEL = '<fmt:message key="lesson.state.created"/>';
		var LESSON_STATE_SCHEDULED_LABEL = '<fmt:message key="lesson.state.scheduled"/>';
		var LESSON_STATE_STARTED_LABEL = '<fmt:message key="lesson.state.started"/>';
		var LESSON_STATE_SUSPENDED_LABEL = '<fmt:message key="lesson.state.suspended"/>';
		var LESSON_STATE_FINISHED_LABEL = '<fmt:message key="lesson.state.finished"/>';
		var LESSON_STATE_ARCHIVED_LABEL = '<fmt:message key="lesson.state.archived"/>';
		var LESSON_STATE_REMOVED_LABEL = '<fmt:message key="lesson.state.removed"/>';
		var LESSON_STATE_ACTION_DISABLE_LABEL = '<fmt:message key="lesson.state.action.disable"/>';
		var LESSON_STATE_ACTION_ACTIVATE_LABEL = '<fmt:message key="lesson.state.action.activate"/>';
		var LESSON_STATE_ACTION_REMOVE_LABEL = '<fmt:message key="lesson.state.action.remove"/>';
		var LESSON_STATE_ACTION_ARCHIVE_LABEL = '<fmt:message key="lesson.state.action.archive"/>';
		var LESSON_ERROR_SCHEDULE_DATE_LABEL = '<fmt:message key="error.lesson.schedule.date"/>';
		var LESSON_EDIT_CLASS_LABEL = '<fmt:message key="button.edit.class"/>';
		var LESSON_GROUP_DIALOG_CLASS_LABEL = '<fmt:message key="lesson.group.dialog.class"/>';
		var CURRENT_ACTIVITY_LABEL = '<fmt:message key="label.learner.progress.activity.current.tooltip"/>';
		var COMPLETED_ACTIVITY_LABEL = '<fmt:message key="label.learner.progress.activity.completed.tooltip"/>';
		var ATTEMPTED_ACTIVITY_LABEL = '<fmt:message key="label.learner.progress.activity.attempted.tooltip"/>';
		var TOSTART_ACTIVITY_LABEL = '<fmt:message key="label.learner.progress.activity.tostart.tooltip"/>';
		var SUPPORT_ACTIVITY_LABEL = '<fmt:message key="label.learner.progress.activity.support.tooltip"/>';
		var EXPORT_PORTFOLIO_LABEL = '<fmt:message key="button.export"/>';
		var EXPORT_PORTFOLIO_LEARNER_TOOLTIP_LABEL = '<fmt:message key="button.export.learner.tooltip"/>';
	    var TIME_CHART_LABEL = '<fmt:message key="button.timechart"/>';
	    var TIME_CHART_TOOLTIP_LABEL = '<fmt:message key="button.timechart.tooltip"/>';
		var LIVE_EDIT_CONFIRM_LABEL = '<fmt:message key="button.live.edit.confirm"/>';
	    
	    
		$(document).ready(function(){
			initTabs();
			initLessonTab();
			initSequenceTab();
			refreshMonitor();
		});
	</script>
	<!-- Some settings need to be done in the script first and only then this file can be included -->
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/progressBar.js"></script>
</lams:head>
<body>
<div id="tabs">
	<a id="closeButton" href="#" onClick="javascript:window.parent.closeMonitorLessonDialog()">
		<span class="ui-icon ui-icon-closethick"></span>
	</a>
	
	<!-- Tab names -->
	<ul>
		<li><a href="#tabLesson"><fmt:message key="tab.lesson"/></a></li>
		<li><a href="#tabSequence"><fmt:message key="tab.sequence"/></a></li>
		<li><a href="#tabLearners"><fmt:message key="tab.learners"/></a></li>
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
				<td>
					<c:out value="${lesson.lessonName}" />
				</td>
			</tr>
			<tr>
				<td class="fieldLabel">
					<fmt:message key="lesson.description"/>
				</td>
				<td>
					${lesson.lessonDescription}
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
			<c:set var="showLearnerURL"><%=Configuration.get(ConfigurationKeys.ALLOW_DIRECT_LESSON_LAUNCH)%></c:set>
			<c:if test="${showLearnerURL}">
				<tr>
					<td class="fieldLabel">
						<fmt:message key="lesson.learner.url"/>
					</td>
					<td>
						<input id="learnerURLField" class="lessonManageField"
						       value="<lams:LAMSURL/>launchlearner.do?lessonID=${lesson.lessonID}"
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
					${lesson.organisationName}
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
					<a class="button lessonManageField" href="#"
					   onClick="javascript:showLessonLearnersDialog()"
					   title='<fmt:message key="button.view.learners.tooltip"/>'>
					   <fmt:message key="button.view.learners"/>
					</a>
					<a class="button lessonManageField" href="#"
					   onClick="javascript:showClassDialog()"
					   title='<fmt:message key="button.edit.class.tooltip"/>'>
					   <fmt:message key="button.edit.class"/>
					</a>
					<c:if test="${notificationsAvailable}">
						<a class="button lessonManageField" href="#"
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
					<input type="checkbox" id="exportAvailableField"
						<c:if test="${lesson.learnerExportAvailable}">
							checked="checked"
						</c:if> 
					/><fmt:message key="lesson.enable.portfolio"/><br />
					<input type="checkbox" id="presenceAvailableField"
						<c:if test="${lesson.learnerPresenceAvailable}">
							checked="checked"
						</c:if> 
					/><fmt:message key="lesson.enable.presence"/>
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
					/><fmt:message key="lesson.enable.im"/>
				</td>
			</tr>
			<c:if test="${not empty contributeActivities}">
				<tr>
					<td colspan="2" class="sectionHeader">
						<fmt:message key="lesson.required.tasks"/>
					</td>
				</tr>
				<c:forEach var="activity" items="${contributeActivities}">
					<tr>
						<td colspan="2" class="contributeActivityCell">
							<c:out value="${activity.title}" />
						</td>
					</tr>
					<c:forEach var="entry" items="${activity.contributeEntries}">
						<c:if test="${entry.isRequired}">
							<tr>
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
			</c:if>
		</table>
	</div>
	
	<div id="tabSequence">
		<div id="sequenceTopButtonsContainer" class="topButtonsContainer">
			<a target="_blank" class="button" title="<fmt:message key='button.help.tooltip'/>"
			   href="http://wiki.lamsfoundation.org/display/lamsdocs/monitoringsequence">
			   <fmt:message key="button.help"/></a>
			<a class="button" title="<fmt:message key='button.refresh.tooltip'/>"
			   href="#" onClick="javascript:refreshMonitor('sequence')">
			   <fmt:message key="button.refresh"/></a>
			<a class="button" title="<fmt:message key='button.export.tooltip'/>"
			   href="#"
			   onClick="javascript:openPopUp('<lams:LAMSURL/>learning/exportWaitingPage.jsp?mode=teacher&lessonID=${lesson.lessonID}', 'ExportPortfolio', 240, 640, true)">
			   <fmt:message key="button.export"/></a>
			<c:if test="${lesson.liveEditEnabled}">
				<a class="button" title="<fmt:message key='button.live.edit.tooltip'/>"
			       href="#"
			  	   onClick="javascript:openLiveEdit()">
				 <fmt:message key='button.live.edit'/></a>
			</c:if>
			<a class="button" title="<fmt:message key='button.close.branching.tooltip'/>"
			  id="closeBranchingButton"
			  href="#"
			  onClick="javascript:closeBranchingSequence()">
			 <fmt:message key='button.close.branching'/></a>
		</div>
		<div id="sequenceCanvas"></div>
		<div id="completedLearnersContainer" title="<fmt:message key='force.complete.end.lesson.tooltip' />">
			<img id="completedLearnersDoorIcon" src="<lams:LAMSURL/>images/icons/door_open.png" />
		</div>
		<img id="sequenceCanvasLoading"
		     src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
	</div>
	
	<div id="tabLearners">
		<table id="tabLearnerControlTable">
			<tr>
				<td class="learnersHeaderCell">
					<fmt:message key='learners.page' /><br />
					<span id="learnersPageCounter" />
				</td>
				<td id="learnersSearchPhraseCell" class="learnersHeaderCell">
					<fmt:message key='learners.search.phrase' /><br />
					<input id="learnersSearchPhrase"
					       title="<fmt:message key='learners.search.phrase.tooltip' />"
					/>
					<img src="<lams:LAMSURL/>images/css/accept.png"
					     onClick="javascript:learnersRunSearchPhrase()"
					     title="<fmt:message key='learners.search.phrase.go.tooltip' />"
					/>
					<img src="<lams:LAMSURL/>images/css/delete.png"
						 onClick="javascript:learnersClearSearchPhrase()"
						 title="<fmt:message key='learners.search.phrase.clear.tooltip' />" 
					/>
				</td>
				<td id="learnersPageLeft" class="learnersHeaderCell learnersPageShifter"
				    onClick="javascript:learnersPageShift(false)"
				>&lt;&lt;</td>
				<td id="learnersPageRight" class="learnersHeaderCell learnersPageShifter"
					onClick="javascript:learnersPageShift(true)"
				>&gt;&gt;</td>
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
		<div class="dialogTitle">
			<fmt:message key="learner.group.list.title"/>
			<span id="learnerGroupSortButton" class="dialogListSortButton"
				  title="<fmt:message key='learner.group.sort.button'/>">▲</span>
		</div>
		<div id="learnerGroupList" class="dialogList"></div>
	</div>
		
	<div id="classDialog" class="dialogContainer">
		<table id="classDialogTable">
			<tr>
				<td class="dialogTitle">
					<fmt:message key="lesson.learners"/>
					<span id="classLearnerSortButton" class="dialogListSortButton"
				  		  title="<fmt:message key='learner.group.sort.button'/>">▲</span>
				</td>
				<td class="dialogTitle">
					<fmt:message key="lesson.monitors"/>
					<span id="classMonitorSortButton" class="dialogListSortButton"
				  		  title="<fmt:message key='learner.group.sort.button'/>">▲</span>
				</td>
			</tr>
			<tr>
				<td>
					<input type="checkbox" id="classLearnerSelectAll"
						   onChange="javascript:selectAllInDialogList('classLearner')" />
					<fmt:message key="learner.group.select.all"/>
				</td>
				<td>
					<input type="checkbox" id="classMonitorSelectAll"
						   onChange="javascript:selectAllInDialogList('classMonitor')" />
					<fmt:message key="learner.group.select.all"/>
				</td>
			</tr>
			<tr>
				<td id="classLearnerList" class="dialogList">
				</td>
				<td id="classMonitorList" class="dialogList">
				</td>
			</tr>
		</table>
	</div>
	
	<div id="emailDialog" class="dialogContainer">
		<iframe id="emailFrame"></iframe>
	</div>
	
	<c:if test="${sequenceTabShowInfo}">
		<div id="sequenceInfoDialog" class="dialogContainer">
			<fmt:message key="sequence.help.info"/>
		</div>
	</c:if>
	
	<div id="tooltip"></div>
</div>
</body>
</lams:html>