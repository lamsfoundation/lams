<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE HTML>
<lams:html>
<lams:head>
	<lams:css style="main" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui.timepicker.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/monitorLesson.css" type="text/css" media="screen" />

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/monitorLesson.js"></script>
	<script type="text/javascript">
		var userId = '<lams:user property="userID"/>';
		var lessonId = ${param.lessonID};
		var ldId = ${ldId};
		
		var LAMS_URL = '<lams:LAMSURL/>';
		
		var FORCE_COMPLETE_CLICK_LABEL = '<fmt:message key="force.complete.click"/>';
		var FORCE_COMPLETE_BUTTON_LABEL = '<fmt:message key="button.force.complete"/>';
		var FORCE_COMPLETE_END_LESSON_CONFIRM_LABEL = '<fmt:message key="force.complete.end.lesson.confirm"/>';
		var FORCE_COMPLETE_ACTIVITY_CONFIRM_LABEL = '<fmt:message key="force.complete.activity.confirm"/>';
		var FORCE_COMPLETE_DROP_FAIL_LABEL = '<fmt:message key="force.complete.drop.fail"/>';
		var LEARNER_GROUP_COUNT_LABEL = '<fmt:message key="learner.group.count"/>';
		var LEARNER_GROUP_SHOW_LABEL = '<fmt:message key="learner.group.show"/>';
		var LEARNER_GROUP_LIST_TITLE_LABEL = '<fmt:message key="learner.group.list.title"/>';
		var VIEW_LEARNER_BUTTON_LABEL = '<fmt:message key="button.view.learner"/>';
		var CLOSE_BUTTON_LABEL = '<fmt:message key="button.close"/>';
		var LEARNER_FINISHED_COUNT_LABEL = '<fmt:message key="learner.finished.count"/>';
		var LEARNER_FINISHED_DIALOG_TITLE_LABEL = '<fmt:message key="learner.finished.dialog.title"/>';
		
		$(document).ready(function(){
			$('#tabs').tabs();
			
			initLessonTab();
			initSequenceTab();
			initLearnersTab();
		});
	</script>
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
	
	<div id="tabLesson" class="tabContent">

	</div>
	
	<div id="tabSequence" class="tabContent">
		<div class="topButtonsContainer">
			<a target="_blank" class="button" title="<fmt:message key='button.help.tooltip'/>"
			 href="http://wiki.lamsfoundation.org/display/lamsdocs/monitoringsequence">
			 <fmt:message key="button.help"/></a>
			 <a class="button" title="<fmt:message key='button.refresh.tooltip'/>"
			 href="#" onClick="javascript:refreshSequenceCanvas()">
			 <fmt:message key="button.refresh"/></a>
			 <a class="button" title="<fmt:message key='button.export.tooltip'/>"
			 href="#"
			 onClick="javascript:openWindow('<lams:LAMSURL/>learning/exportWaitingPage.jsp?mode=teacher&lessonID=${param.lessonID}', '<fmt:message key="button.export"/>', 640, 240)">
			 <fmt:message key="button.export"/></a>
		</div>
		<div id="sequenceCanvas"></div>
		<div id="completedLearnersContainer" title="<fmt:message key='force.complete.end.lesson.tooltip' />">
			<img id="completedLearnersDoorIcon" src="<lams:LAMSURL/>images/icons/door_open.png" />
		</div>
		<div id="learnerGroupDialog" class="dialogContainer">
			<div id="learnerGroupListTitle">
				<fmt:message key="learner.group.list.title"/>
				<span id="sortLearnerGroupListButton"
					  title="<fmt:message key='learner.group.sort.button'/>">▲</span>
			</div>
			<div id="learnerGroupList"></div>
		</div>
	</div>
	
	<div id="tabLearners" class="tabContent">
			
	</div>
</div>
</body>
</lams:html>