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
		var userId = '<lams:user property="userID"/>',
			lessonId = ${lesson.lessonID},
			ldId = ${lesson.learningDesignID},
			lessonStateId = ${lesson.lessonStateID},
			createDateTimeStr = '${lesson.createDateTimeStr}',
			// settings for progress bar
			isHorizontalBar = true,
			hasContentFrame = false,
			presenceEnabled =  false,
			hasDialog = false,
			
			LAMS_URL = '<lams:LAMSURL/>',
			
			LABELS = {
				FORCE_COMPLETE_CLICK : '<fmt:message key="force.complete.click"/>',
				FORCE_COMPLETE_BUTTON : '<fmt:message key="button.force.complete"/>',
				FORCE_COMPLETE_END_LESSON_CONFIRM : '<fmt:message key="force.complete.end.lesson.confirm"/>',
				FORCE_COMPLETE_ACTIVITY_CONFIRM : '<fmt:message key="force.complete.activity.confirm"/>',
				FORCE_COMPLETE_REMOVE_CONTENT : '<fmt:message key="force.complete.remove.content"/>',
				FORCE_COMPLETE_REMOVE_CONTENT_YES : '<fmt:message key="force.complete.remove.content.yes"/>',
				FORCE_COMPLETE_REMOVE_CONTENT_NO : '<fmt:message key="force.complete.remove.content.no"/>',
				FORCE_COMPLETE_DROP_FAIL : '<fmt:message key="force.complete.drop.fail"/>',
				LEARNER_GROUP_COUNT : '<fmt:message key="learner.group.count"/>',
				LEARNER_GROUP_SHOW : '<fmt:message key="learner.group.show"/>',
				LEARNER_GROUP_REMOVE_PROGRESS : '<fmt:message key="learner.group.remove.progress"/>',
				LEARNER_GROUP_LIST_TITLE : '<fmt:message key="learner.group.list.title"/>',
				VIEW_LEARNER_BUTTON : '<fmt:message key="button.view.learner"/>',
				EMAIL_BUTTON : '<fmt:message key="button.email"/>',
				CLOSE_BUTTON : '<fmt:message key="button.close"/>',
				NOTIFCATIONS : '<fmt:message key="email.notifications"/>',
				LEARNER_FINISHED_COUNT : '<fmt:message key="learner.finished.count"/>',
				LEARNER_FINISHED_DIALOG_TITLE : '<fmt:message key="learner.finished.dialog.title"/>',
				LESSON_PRESENCE_ENABLE_ALERT : '<fmt:message key="lesson.enable.presence.alert"/>',
				LESSON_PRESENCE_DISABLE_ALERT : '<fmt:message key="lesson.disable.presence.alert"/>',
				LESSON_IM_ENABLE_ALERT : '<fmt:message key="lesson.enable.im.alert"/>',
				LESSON_IM_DISABLE_ALERT : '<fmt:message key="lesson.disable.im.alert"/>',
				LESSON_REMOVE_ALERT : '<fmt:message key="lesson.remove.alert"/>',
				LESSON_REMOVE_DOUBLECHECK_ALERT : '<fmt:message key="lesson.remove.doublecheck.alert"/>',
				LESSON_STATE_CREATED : '<fmt:message key="lesson.state.created"/>',
				LESSON_STATE_SCHEDULED : '<fmt:message key="lesson.state.scheduled"/>',
				LESSON_STATE_STARTED : '<fmt:message key="lesson.state.started"/>',
				LESSON_STATE_SUSPENDED : '<fmt:message key="lesson.state.suspended"/>',
				LESSON_STATE_FINISHED : '<fmt:message key="lesson.state.finished"/>',
				LESSON_STATE_ARCHIVED : '<fmt:message key="lesson.state.archived"/>',
				LESSON_STATE_REMOVED : '<fmt:message key="lesson.state.removed"/>',
				LESSON_STATE_ACTION_DISABLE : '<fmt:message key="lesson.state.action.disable"/>',
				LESSON_STATE_ACTION_ACTIVATE : '<fmt:message key="lesson.state.action.activate"/>',
				LESSON_STATE_ACTION_REMOVE : '<fmt:message key="lesson.state.action.remove"/>',
				LESSON_STATE_ACTION_ARCHIVE : '<fmt:message key="lesson.state.action.archive"/>',
				LESSON_ERROR_SCHEDULE_DATE : '<fmt:message key="error.lesson.schedule.date"/>',
				LESSON_EDIT_CLASS : '<fmt:message key="button.edit.class"/>',
				LESSON_GROUP_DIALOG_CLASS : '<fmt:message key="lesson.group.dialog.class"/>',
				CURRENT_ACTIVITY : '<fmt:message key="label.learner.progress.activity.current.tooltip"/>',
				COMPLETED_ACTIVITY : '<fmt:message key="label.learner.progress.activity.completed.tooltip"/>',
				ATTEMPTED_ACTIVITY : '<fmt:message key="label.learner.progress.activity.attempted.tooltip"/>',
				TOSTART_ACTIVITY : '<fmt:message key="label.learner.progress.activity.tostart.tooltip"/>',
				SUPPORT_ACTIVITY : '<fmt:message key="label.learner.progress.activity.support.tooltip"/>',
				EXPORT_PORTFOLIO : '<fmt:message key="button.export"/>',
				EXPORT_PORTFOLIO_LEARNER_TOOLTIP : '<fmt:message key="button.export.learner.tooltip"/>',
			    TIME_CHART : '<fmt:message key="button.timechart"/>',
			    TIME_CHART_TOOLTIP : '<fmt:message key="button.timechart.tooltip"/>',
				LIVE_EDIT_CONFIRM : '<fmt:message key="button.live.edit.confirm"/>',
				CONTRIBUTE_GATE : '<fmt:message key="lesson.task.gate"/>',
				CONTRIBUTE_GROUPING : '<fmt:message key="lesson.task.grouping"/>',
				CONTRIBUTE_BRANCHING : '<fmt:message key="lesson.task.branching"/>',
				CONTRIBUTE_CONTENT_EDITED : '<fmt:message key="lesson.task.content.edited"/>',
				CONTRIBUTE_TOOLTIP : '<fmt:message key="button.task.go.tooltip"/>',
				CONTRIBUTE_BUTTON : '<fmt:message key="button.task.go"/>',
				CONTRIBUTE_ATTENTION : '<fmt:message key="lesson.task.attention"/>'
			}
	    
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
	
	<div id="forceBackwardsDialog" class="dialogContainer"></div>
	
	<div id="tooltip"></div>
</div>
</body>
</lams:html>