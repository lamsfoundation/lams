<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE HTML>
<lams:html>
<lams:head>
	<lams:css style="main" />
	<link rel="stylesheet" href="css/yui/treeview.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/yui/folders.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/jquery-ui.timepicker.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/addLesson.css" type="text/css" media="screen" />

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="includes/javascript/yui/yahoo-dom-event.js" ></script>
	<script type="text/javascript" src="includes/javascript/yui/animation-min.js"></script>
	<script type="text/javascript" src="includes/javascript/yui/json-min.js" ></script> 
	<script type="text/javascript" src="includes/javascript/yui/treeview-min.js" ></script>
	<script type="text/javascript" src="includes/javascript/addLesson.js"></script>
	<script type="text/javascript">
		var userId = '<lams:user property="userID"/>';
		var folderContents = ${folderContents};
		var users = ${users};
		
		var LAMS_URL = '<lams:LAMSURL/>';
		var LD_THUMBNAIL_URL_BASE = LAMS_URL + 'home.do?method=createLearningDesignThumbnail&ldId=';

		var CANVAS_RESIZE_OPTION_NONE = 0;
		var CANVAS_RESIZE_OPTION_FIT = 1;
		var CANVAS_RESIZE_OPTION_FULL = 2;
		var CANVAS_RESIZE_LABEL_FULL = '<fmt:message key="label.tab.lesson.size.full" />';
		var CANVAS_RESIZE_LABEL_FIT = '<fmt:message key="label.tab.lesson.size.fit" />';

		var SPLIT_LEARNERS_DESCRIPTION = '<fmt:message key="label.tab.advanced.split.desc" />';
		var LABEL_MISSING_LEARNERS = '<fmt:message key="error.tab.class.learners" />';
		var LABEL_MISSING_MONITORS = '<fmt:message key="error.tab.class.monitors" />';
		var LABEL_RUN_SEQUENCES_FOLDER = '<fmt:message key="label.tab.lesson.sequence.folder" />';
		
		$(document).ready(function(){
			$('#tabs').tabs();
			
			initLessonTab();
			initClassTab();
			initAdvancedTab();
			initConditionsTab();
		});
	</script>
</lams:head>
<body>
<div id="tabs">
	<a id="closeButton" href="#" onClick="javascript:window.parent.closeAddLessonDialog()">
		<span class="ui-icon ui-icon-closethick"></span>
	</a>
	
	<a id="addButton" href="#" onClick="javascript:addLesson()">
		<fmt:message key="button.add.now" />
	</a>
	
	<!-- Tab names -->
	<ul>
		<li><a href="#tabLesson"><fmt:message key="label.tab.lesson" /></a></li>
		<li><a href="#tabClass"><fmt:message key="label.tab.class" /></a></li>
		<li><a href="#tabAdvanced"><fmt:message key="label.tab.advanced" /></a></li>
		<li><a href="#tabConditions"><fmt:message key="label.tab.conditions" /></a></li>
	</ul>
	
	<!-- Tab contents -->
	
	<div id="tabLesson" class="tabContent">
		<div class="tabTitle"><fmt:message key="label.tab.lesson.title" /></div>
		<table class="tabTable">
			<tr>
				<td id="learningDesignTreeCell" rowspan="3">
					<div id="learningDesignTree"></div>
				</td>
				<td id="canvasControlCell">
					<a id="toggleCanvasResizeLink" href="#"></a>
				</td>
			</tr>
			<tr>
				<td id="canvasCell" >
			    	<div id="canvasDiv">
			    		<img id="ldScreenshotAuthor" class="ldChoiceDependentCanvasElement" />
		    			<img id="ldScreenshotLoading" class="ldChoiceDependentCanvasElement" src="images/ajax-loader-big.gif" />
		    			<div id="ldNotChosenError" class="ldChoiceDependentCanvasElement errorMessage" ><fmt:message key="error.tab.lesson.sequence" /></div>
			    	</div>
				</td>
			</tr>
			<tr>
				<td id="lessonNameCell">
			    	<fmt:message key="label.tab.lesson.name" /><input id="lessonNameInput" type="text" />
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="tabClass" class="tabContent">
		<div class="tabTitle"><fmt:message key="label.tab.class.title" /></div>
		<table id="classTable" class="tabTable">
			<tr>
				<td class="userContainerCell" rowspan="2">
					<div class="userContainerTitle">
						<fmt:message key="label.tab.class.monitors.unselected" />
						<span id="sort-unselected-monitors" class="sortUsersButton">▲</span>
					</div>
					<div id="unselected-monitors" class="userContainer"></div>
				</td>
				<td class="userConainterTransferCell userConainterTransferTopCell">
					<img src="images/css/blue_arrow_right.gif"
					     onClick="javascript:transferUsers('selected-monitors')" />
				</td>
				<td class="userContainerCell" rowspan="2">
					<div class="userContainerTitle">
						<fmt:message key="label.tab.class.monitors.selected" />
						<span id="sort-selected-monitors" class="sortUsersButton">▲</span>
					</div>
					<div id="selected-monitors" class="userContainer"></div>
				</td>
			</tr>
			<tr>
				<td class="userConainterTransferCell">
					<img src="images/blue_arrow_left.gif" 
					     onClick="javascript:transferUsers('unselected-monitors')" />	
				</td>
			</tr>
			<tr>
				<td class="userContainerCell" rowspan="2">
					<div class="userContainerTitle">
						<span id="sort-unselected-learners" class="sortUsersButton">▲</span>
						<fmt:message key="label.tab.class.learners.unselected" />
					</div>
					<div id="unselected-learners" class="userContainer"></div>
				</td>
				<td class="userConainterTransferCell userConainterTransferTopCell" >
					<img src="images/css/blue_arrow_right.gif"
					     onClick="javascript:transferUsers('selected-learners')" />
				</td>
				<td class="userContainerCell" rowspan="2">
					<div class="userContainerTitle">
						<span id="sort-selected-learners" class="sortUsersButton">▲</span>
						<fmt:message key="label.tab.class.learners.selected" />
					</div>
					<div id="selected-learners" class="userContainer"></div>
				</td>
			</tr>
			<tr>
				<td class="userConainterTransferCell">
					<img src="images/blue_arrow_left.gif" 
					     onClick="javascript:transferUsers('unselected-learners')" />
				</td>
			</tr>
		</table>
	</div>
	
	<form id="lessonForm" action="<lams:LAMSURL/>monitoring/monitoring.do" method="POST">
	<div id="tabAdvanced" class="tabContent">
			<input name="method" value="addLesson" type="hidden" />
			<input name="organisationID" value="${param.organisationID}" type="hidden" />
			<input id="ldIdField" name="learningDesignID" type="hidden" />
			<input id="lessonNameField" name="lessonName" type="hidden" />
			<input id="learnersField" name="learners" type="hidden" />
			<input id="monitorsField" name="monitors" type="hidden" />
			<input id="splitNumberLessonsField" name="splitNumberLessons" type="hidden" />
			

			<div class="fieldSectionTitle"><fmt:message key="label.tab.advanced.details" /></div>
			<input id="introEnableField" name="introEnable" value="true" type="checkbox"><fmt:message key="label.tab.advanced.intro.enable" /></input>
			<div   id="introDescriptionDiv">
				<div id="introDescriptionLabelDiv">
				 <fmt:message key="label.tab.advanced.intro.description" />
				</div>
				<lams:CKEditor id="introDescription" toolbarSet="LessonDescription" 
				               height="150px" value="">
				</lams:CKEditor>
				<input id="introImageField" name="introImage" value="true" type="checkbox">
					  <fmt:message key="label.tab.advanced.intro.image" />
				</input>
			</div>
			
			<div class="fieldSectionTitle"><fmt:message key="label.tab.advanced.section.advanced" /></div>
			<input id="startMonitorField" name="startMonitor" value="true" type="checkbox"><fmt:message key="label.tab.advanced.field.monitor" /></input><br />
				<input name="learnerRestart" value="true" type="checkbox"><fmt:message key="label.tab.advanced.field.restart" /></input><br />
			<input name="liveEditEnable" value="true" type="checkbox" checked="checked"><fmt:message key="label.tab.advanced.field.liveedit" /></input><br />
			<input name="notificationsEnable" value="true" type="checkbox" checked="checked"><fmt:message key="label.tab.advanced.field.notification" /></input><br />
			<input name="portfolioEnable" value="true" type="checkbox"><fmt:message key="label.tab.advanced.field.export" /></input><br />
			<input id="presenceEnableField" name="presenceEnable" value="true" type="checkbox"><fmt:message key="label.tab.advanced.field.presence" /></input><br />
			<input id="imEnableField" name="imEnable" value="true" type="checkbox" disabled="disabled"><fmt:message key="label.tab.advanced.field.im" /></input><br />
			<input id="splitLearnersField" type="checkbox"><fmt:message key="label.tab.advanced.field.split" /></input><br />
			<table id="splitLearnersTable">
				<tr>
					<td id="splitLearnersCell">
						<fmt:message key="label.tab.advanced.field.split.number" /> <input id="splitLearnersCountField" type="text" />
					</td>
					<td id="splitLearnersDescriptionCell">
						<span id="splitLearnersDescription"></span>
					</td>
				</tr>
			</table>
			<input id="schedulingEnableField" name="schedulingEnable" value="true" type="checkbox"><fmt:message key="label.tab.advanced.field.scheduling" /></input>
			<input id="schedulingDatetimeField" name="schedulingDatetime" /><br />
	</div>
	
	<div id="tabConditions" class="tabContent">
		<div class="fieldSectionTitle"><fmt:message key="label.tab.conditions.dependencies" /></div>
		<div class="fieldSectionDescription"><fmt:message key="label.tab.conditions.dependencies.desc" /></div>
		<input id="precedingLessonEnableField" name="precedingLessonEnable" value="true"
		       type="checkbox"><fmt:message key="label.tab.conditions.enable" /></input>
		<select id="precedingLessonIdField" name="precedingLessonId">
			<c:forEach var="availableLesson" items="${availablePrecedingLessons}">
				<option value="${availableLesson.lessonID}"><c:out value="${availableLesson.lessonName}" /></option>
			</c:forEach>
		</select>
		
		<div class="fieldSectionTitle"><fmt:message key="label.tab.conditions.timelimit" /></div>
		<div class="fieldSectionDescription"><fmt:message key="label.tab.conditions.timelimit.desc" /></div>
		<input id="timeLimitEnableField" name="timeLimitEnable" value="true" type="checkbox"><fmt:message key="label.tab.conditions.enable" /></input>
		<div id="timeLimitDiv">
			<fmt:message key="label.tab.conditions.timelimit.days" /> <input id="timeLimitDaysField" name="timeLimitDays" /><br />
			<input id="timeLimitIndividualField" name="timeLimitIndividual" value="true"
			       type="checkbox"><fmt:message key="label.tab.conditions.timelimit.individual" /></input>
		</div>
	</div>
	</form>
</div>
</body>
</lams:html>