<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<lams:css style="main" />
	<link rel="stylesheet" href="css/yui/treeview.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/yui/folders.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/defaultHTML_learner.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/jquery-ui.timepicker.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/addLesson.css" type="text/css" media="screen" />

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="includes/javascript/yui/yahoo-dom-event.js" ></script>
	<script type="text/javascript" src="includes/javascript/yui/animation-min.js"></script>
	<script type="text/javascript" src="includes/javascript/yui/json-min.js" ></script> 
	<script type="text/javascript" src="includes/javascript/yui/treeview-min.js" ></script>
	<script type="text/javascript" src="includes/javascript/addLesson.js"></script>
	<script type="text/javascript">
		var userId = '<lams:user property="userID"/>',
			folderContents = ${folderContents},
			users = ${users},
			
			LAMS_URL = '<lams:LAMSURL/>',
			LD_THUMBNAIL_URL_BASE = LAMS_URL + 'home.do?method=getLearningDesignThumbnail&ldId=',
			
			CANVAS_RESIZE_OPTION_NONE = 0,
			CANVAS_RESIZE_OPTION_FIT = 1,
			CANVAS_RESIZE_OPTION_FULL = 2,
			CANVAS_RESIZE_LABEL_FULL = '<fmt:message key="label.tab.lesson.size.full" />',
			CANVAS_RESIZE_LABEL_FIT = '<fmt:message key="label.tab.lesson.size.fit" />',

			SPLIT_LEARNERS_DESCRIPTION = '<fmt:message key="label.tab.advanced.split.desc" />',
			LABEL_MISSING_LEARNERS = '<fmt:message key="error.tab.class.learners" />',
			LABEL_MISSING_MONITORS = '<fmt:message key="error.tab.class.monitors" />',
			LABEL_RUN_SEQUENCES_FOLDER = '<fmt:message key="label.tab.lesson.sequence.folder" />',
			LABEL_NAME_INVALID_CHARACTERS = '<fmt:message key="error.lessonname.invalid.characters" />';
				
		$(document).ready(function(){
			$('#tabs').tabs();
			
			initLessonTab();
			initClassTab();
			initAdvancedTab();
			initConditionsTab();
			
			// remove "loading..." screen
			$('#loadingOverlay').remove();
		});
	</script>
</lams:head>
<body>
<%-- "loading..." screen, gets removed on page full load --%>
<div id="loadingOverlay">
	<i class="fa fa-refresh fa-spin fa-3x fa-fw"></i>
</div>

<form id="lessonForm" action="<lams:LAMSURL/>monitoring/monitoring.do" method="POST">

<div id="exTab2" class="container-fluid">	
<ul class="nav nav-tabs">
			<li class="active">
        <a  href="#1" data-toggle="tab"><fmt:message key="label.tab.lesson" /></a>
			</li>
			<li><a href="#2" data-toggle="tab"><fmt:message key="label.tab.class" /></a>
			</li>
			<li><a href="#3" data-toggle="tab"><fmt:message key="label.tab.advanced" /></a>
			</li>
			<li><a href="#4" data-toggle="tab"><fmt:message key="label.tab.conditions" /></a>
			</li>
</ul>

			<div class="tab-content ">
			  <div class="tab-pane active" id="1">
	
				<div id="tabLessonTitle" class="tabTitle"><fmt:message key="label.tab.lesson.title" /></div>
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
					    			<i id="ldScreenshotLoading" class="ldChoiceDependentCanvasElement fa fa-refresh fa-spin fa-2x fa-fw"></i>
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
	
				<div class="tab-pane" id="2">
					<!-- Class panel -->
					<div id="tabClass" class="tabContent">
						<div id="tabClassTitle" class="tabTitle"><fmt:message key="label.tab.class.title" /></div>
						<table id="classTable" class="tabTable">
							<tr>
								<td class="userContainerCell" rowspan="2">
									<div class="userContainerTitle">
										<fmt:message key="label.tab.class.monitors.unselected" />
										
									</div>
									<div id="unselected-monitors" class="userContainer"><span id="sort-unselected-monitors" class="sortUsersButton">▲</span></div>
								</td>
								<td class="userConainterTransferCell userConainterTransferTopCell">
									<i id="monitorMoveToRight" class="fa fa-2x fa-arrow-right text-left text-primary" 
									     onClick="javascript:transferUsers('selected-monitors')" ></i>
								</td>
								<td class="userContainerCell" rowspan="2">
									<div class="userContainerTitle">
										<fmt:message key="label.tab.class.monitors.selected" />
									</div>
									<div id="selected-monitors" class="userContainer"><span id="sort-selected-monitors" class="sortUsersButton">▲</span></div>
								</td>
							</tr>
							<tr>
								<td class="userConainterTransferCell">
									<i id="monitorMoveToLeft" class="fa fa-2x fa-arrow-left text-left text-primary" 
									     onClick="javascript:transferUsers('unselected-monitors')"></i>	
								</td>
							</tr>
							<tr>
								<td class="userContainerCell" rowspan="2">
									<div class="userContainerTitle">
										<fmt:message key="label.tab.class.learners.unselected" />
									</div>
									<div id="unselected-learners" class="userContainer"><span id="sort-unselected-learners" class="sortUsersButton">▲</span></div>
								</td>
								<td class="userConainterTransferCell userConainterTransferTopCell" >
									<i id="learnerMoveToRight" class="fa fa-2x fa-arrow-right text-left text-primary"
									     onClick="javascript:transferUsers('selected-learners')" ></i>
								</td>
								<td class="userContainerCell" rowspan="2">
									<div class="userContainerTitle">
										<fmt:message key="label.tab.class.learners.selected" />
									</div>
									<div id="selected-learners" class="userContainer"><span id="sort-selected-learners" class="sortUsersButton">▲</span></div>
								</td>
							</tr>
							<tr>
								<td class="userConainterTransferCell">
									<i id="learnerMoveToLeft" class="fa fa-2x fa-arrow-left text-left text-primary" 
									     onClick="javascript:transferUsers('unselected-learners')"></i>
								</td>
							</tr>
						</table>
					</div>

				</div>
        <div class="tab-pane" id="3">
					<!-- Advanced Panel -->
					<div id="tabAdvanced" class="tabContent">
							<input name="method" value="addLesson" type="hidden" />
							<input name="organisationID" value="${param.organisationID}" type="hidden" />
							<input id="ldIdField" name="learningDesignID" type="hidden" />
							<input id="lessonNameField" name="lessonName" type="hidden" />
							<input id="learnersField" name="learners" type="hidden" />
							<input id="monitorsField" name="monitors" type="hidden" />
							<input id="splitNumberLessonsField" name="splitNumberLessons" type="hidden" />
							
							<div class="lead"><fmt:message key="label.tab.advanced.details" /></div>
								<div class="options">
									<div class="checkbox">
										<label>
											<input id="introEnableField" name="introEnable" value="true" type="checkbox"><fmt:message key="label.tab.advanced.intro.enable" />
										</label>
									</div>
								</div>							
								<div  id="introDescriptionDiv">
									<div id="introDescriptionLabelDiv">
									 <fmt:message key="label.tab.advanced.intro.description" />
									</div>
									<lams:CKEditor id="introDescription" toolbarSet="LessonDescription" value="">
									</lams:CKEditor>

									<div class="checkbox">
										<label>
											<input id="introImageField" name="introImage" value="true" type="checkbox"/><fmt:message key="label.tab.advanced.intro.image" />
										</label>
									</div>	  
								</div>
							
							
							<div class="lead"><fmt:message key="label.tab.advanced.section.advanced" /></div>
							<div class="options">
								<div class="checkbox">
									<label>
										<input id="startMonitorField" name="startMonitor" value="true" type="checkbox"><fmt:message key="label.tab.advanced.field.monitor" /></input><br />
									</label>
								</div>
								<div class="checkbox">
									<label>
										<input name="forceRestart" value="true" type="checkbox"/><fmt:message key="label.tab.advanced.field.force.restart" />
									</label>
								</div>		
								<div class="checkbox">
									<label>
										<input name="allowRestart" value="true" type="checkbox"/><fmt:message key="label.tab.advanced.field.allow.restart" />
									</label>
								</div>	
								<div class="checkbox">
									<label>
										<input name="liveEditEnable" value="true" type="checkbox" checked="checked"/><fmt:message key="label.tab.advanced.field.liveedit" />
									</label>
								</div>
								<div class="checkbox">
									<label>
										<input name="notificationsEnable" value="true" type="checkbox" checked="checked"/><fmt:message key="label.tab.advanced.field.notification" />
									</label>
								</div>
	
								<div class="checkbox">
									<label>
										<input id="presenceEnableField" name="presenceEnable" value="true" type="checkbox"/><fmt:message key="label.tab.advanced.field.presence" />
									</label>
								</div>
								
								<div class="checkbox">
									<label>
										<input id="imEnableField" name="imEnable" value="true" type="checkbox" disabled="disabled"/><fmt:message key="label.tab.advanced.field.im" />
									</label>
								</div>
	
								<div class="checkbox">
									<label>
										<input id="splitLearnersField" type="checkbox"/><fmt:message key="label.tab.advanced.field.split" />
									</label>
								</div>
								<div id="splitLearnersTable" style="display: none">
											<fmt:message key="label.tab.advanced.field.split.number" /> <input value="1" id="splitLearnersCountField" type="number" />
												<span class="roffset5" id="splitLearnersDescription"></span>
								</div>
								<div class="checkbox">
									<label>
										<input id="schedulingEnableField" name="schedulingEnable" value="true" type="checkbox"/><fmt:message key="label.tab.advanced.field.scheduling" />
									</label>
								</div>		
								<input id="schedulingDatetimeField" name="schedulingDatetime" /><br />
						</div>
					</div>
				</div>
        <div class="tab-pane" id="4">
					<!-- Conditions Panel -->
					<div id="tabConditions" class="tabContent">
						<div class="lead"><fmt:message key="label.tab.conditions.dependencies" /></div>
						<div class="options">
							<div class="fieldSectionDescription"><fmt:message key="label.tab.conditions.dependencies.desc" /></div>
							<div class="checkbox">
								<label>
									<input id="precedingLessonEnableField" name="precedingLessonEnable" value="true"
							       type="checkbox"/><fmt:message key="label.tab.conditions.enable" />
							  </label>
							 </div>      
							<select id="precedingLessonIdField" name="precedingLessonId">
								<c:forEach var="availableLesson" items="${availablePrecedingLessons}">
									<option value="${availableLesson.lessonID}"><c:out value="${availableLesson.lessonName}" /></option>
								</c:forEach>
							</select>
						</div>	
						
						<div class="lead"><fmt:message key="label.tab.conditions.timelimit" /></div>
						<div class="options">
							<div class="fieldSectionDescription"><fmt:message key="label.tab.conditions.timelimit.desc" /></div>
							
							<div class="checkbox">
								<label>
									<input id="timeLimitEnableField" name="timeLimitEnable" value="true" type="checkbox"/><fmt:message key="label.tab.conditions.enable" />
								</label>	
							</div>
							<div id="timeLimitDiv" style="display: none;">
								<fmt:message key="label.tab.conditions.timelimit.days" /> <input id="timeLimitDaysField" name="timeLimitDays" /><br />
								<div class="checkbox">
									<label>
										<input id="timeLimitIndividualField" name="timeLimitIndividual" value="true"
								       type="checkbox"/><fmt:message key="label.tab.conditions.timelimit.individual" />
								  </label>     
								</div>       
							</div>
						</div>	
					</div>
				</div>

			</div>
</div>
<hr class="separator">
<div class="container-fluid">
	<div class="pull-right">
		<button id="addButton" class="btn btn-primary" href="#" onClick="javascript:addLesson()"><i class="fa fa-plus"></i> 
			<fmt:message key="button.add.now" />
		</button>
	</div>
</div>



	

	

</form>

</body>
</lams:html>
