<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="${lams}css/jquery-ui.timepicker.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${lams}css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${lams}css/bootstrap-treeview.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${lams}css/addLesson.css" type="text/css" media="screen" />

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap-treeview.js" ></script>
	<script type="text/javascript" src="${lams}includes/javascript/learning-design-treeview.js" ></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	<lams:JSImport src="includes/javascript/dialog.js" />
	<lams:JSImport src="includes/javascript/addLesson.js" />
	<script type="text/javascript">
		var userId = '<lams:user property="userID"/>',
			users = ${users},
			orgGroupingsAvailable = ${not empty orgGroupings},
			
			LAMS_URL = '<lams:LAMSURL/>',
			LD_THUMBNAIL_URL_BASE = LAMS_URL + 'home/getLearningDesignThumbnail.do?ldId=',
			
			CANVAS_RESIZE_OPTION_NONE = 0,
			CANVAS_RESIZE_OPTION_FIT = 1,
			CANVAS_RESIZE_OPTION_FULL = 2,
			CANVAS_RESIZE_LABEL_FULL = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.tab.lesson.size.full' /></spring:escapeBody>";
			CANVAS_RESIZE_LABEL_FIT = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.tab.lesson.size.fit' /></spring:escapeBody>";

			SPLIT_LEARNERS_DESCRIPTION = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.tab.advanced.split.desc' /></spring:escapeBody>";
			LABEL_MISSING_LEARNERS = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.tab.class.learners' /></spring:escapeBody>";
			LABEL_MISSING_MONITORS = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.tab.class.monitors' /></spring:escapeBody>";
			LABEL_RUN_SEQUENCES_FOLDER = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.tab.lesson.sequence.folder' /></spring:escapeBody>";
			LABEL_NAME_INVALID_CHARACTERS = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.lessonname.invalid.characters' /></spring:escapeBody>";
			LABEL_PREVIEW_LESSON_DEFAULT_TITLE = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='authoring.fla.preview.lesson.default.title' /></spring:escapeBody>";
			LABEL_PREVIEW_ERROR = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='authoring.fla.preview.error' /></spring:escapeBody>";
			LABEL_ORG_GROUPING_DIALOG_TITLE = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.course.groups.viewonly.title' /></spring:escapeBody>";

			
 		$(document).ready(function(){
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

<form id="lessonForm" action="<lams:LAMSURL/>monitoring/monitoring/addLesson.do" method="POST">
<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
<lams:Page type="navbar">
		<lams:Tabs>
			<lams:Tab id="1" key="label.tab.lesson" />
			<lams:Tab id="2" key="label.tab.class" />
			<c:if test="${not empty orgGroupings}">
				<lams:Tab id="3" key="label.tab.grouping" />
			</c:if>
			<lams:Tab id="4" key="label.tab.advanced" />
			<lams:Tab id="5" key="label.tab.conditions" />
		</lams:Tabs>
		<lams:TabBodyArea>
			<lams:TabBodys>
				<lams:TabBody id="1">
					<div id="tabLessonTitle" class="tabTitle bg-warning"><fmt:message key="label.tab.lesson.title" /></div>
					<table class="tabTable">
						<tr>
							<td id="learningDesignTreeCell" rowspan="3">
								<div id="learningDesignTree"></div>
								<div id="accessDiv">
									<div id="accessTitle"><fmt:message key="authoring.fla.page.dialog.access" /></div>
								</div>
							</td>
							<td id="canvasControlCell">
								<a id="toggleCanvasResizeLink" href="#"></a>
							</td>
						</tr>
						<tr>
							<td id="canvasCell" >
						    	<div id="canvasDiv">
						    		<div id="ldScreenshotAuthor" class="ldChoiceDependentCanvasElement"></div>
					    			<i id="ldScreenshotLoading" class="ldChoiceDependentCanvasElement fa fa-refresh fa-spin fa-2x fa-fw"></i>
					    			<div id="ldNotChosenError" class="ldChoiceDependentCanvasElement errorMessage text-danger" ><fmt:message key="error.tab.lesson.sequence" /></div>
					    			<div id="ldCannotLoadSVG" class="ldChoiceDependentCanvasElement" ><fmt:message key="error.cannot.load.thumbnail" /></div>
						    	</div>
							</td>
						</tr>
						<tr>
							<td id="lessonNameCell">
						    	<fmt:message key="label.tab.lesson.name" /><input id="lessonNameInput" type="text" />
							</td>
						</tr>
					</table>
				</lams:TabBody>
				
				<lams:TabBody id="2">
					<!-- Class panel -->
					<div id="tabClassTitle" class="tabTitle bg-warning"><fmt:message key="label.tab.class.title" /></div>
					<table id="classTable" class="tabTable">
						<tr>
							<td class="userContainerCell" rowspan="2">
								<div class="userContainerTitle">
									<fmt:message key="label.tab.class.monitors.unselected" />
									
								</div>
								<div id="unselected-monitors" class="userContainer"><span id="sort-unselected-monitors" class="sortUsersButton">?</span></div>
							</td>
							<td class="userConainterTransferCell userConainterTransferTopCell">
								<i id="monitorMoveToRight" class="fa fa-2x fa-arrow-right text-left text-primary" 
								     onClick="javascript:transferUsers('selected-monitors')" ></i>
							</td>
							<td class="userContainerCell" rowspan="2">
								<div class="userContainerTitle">
									<fmt:message key="label.tab.class.monitors.selected" />
								</div>
								<div id="selected-monitors" class="userContainer"><span id="sort-selected-monitors" class="sortUsersButton">?</span></div>
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
								<div id="unselected-learners" class="userContainer"><span id="sort-unselected-learners" class="sortUsersButton">?</span></div>
							</td>
							<td class="userConainterTransferCell userConainterTransferTopCell" >
								<i id="learnerMoveToRight" class="fa fa-2x fa-arrow-right text-left text-primary"
								     onClick="javascript:transferUsers('selected-learners')" ></i>
							</td>
							<td class="userContainerCell" rowspan="2">
								<div class="userContainerTitle">
									<fmt:message key="label.tab.class.learners.selected" />
								</div>
								<div id="selected-learners" class="userContainer"><span id="sort-selected-learners" class="sortUsersButton">?</span></div>
							</td>
						</tr>
						<tr>
							<td class="userConainterTransferCell">
								<i id="learnerMoveToLeft" class="fa fa-2x fa-arrow-left text-left text-primary" 
								     onClick="javascript:transferUsers('unselected-learners')"></i>
							</td>
						</tr>
					</table>
				</lams:TabBody>
				
				<c:if test="${not empty orgGroupings}">
					<lams:TabBody id="3">
						<!-- Course groupings panel -->
						<div class="row voffset10">
							<div class="col-sm-8 col-sm-offset-2" id="grouping-tab-desc">
								<p id="grouping-tab-desc-select"><fmt:message key="label.tab.grouping.desc" /></p>
								<div class="alert alert-info text-center" role="alert" id="grouping-tab-desc-none">
									<fmt:message key="label.tab.grouping.none" />
								</div>
							</div>
						</div>
						
						<div class="row loffset20" id="grouping-tab-select">
							<div class="col-sm-8 col-sm-offset-2">
								<div class="checkbox">
									<label>
										<input name="orgGroupingId" type="radio" value="" checked/>
										<fmt:message key="authoring.label.none" />
									</label>
								</div>
								<c:forEach var="orgGrouping" items="${orgGroupings}">
									<div class="checkbox">
										<label>
											<input name="orgGroupingId" type="radio" value="${orgGrouping.groupingId}"/>
											<c:out value="${orgGrouping.name}" />
											<span title='<fmt:message key="label.course.groups.grouping.count.tooltip" />'>
												(${orgGrouping.groupCount})
											</span>
										</label>
										<i class="fa fa-external-link show-grouping-link"
										   title='<fmt:message key="label.course.groups.viewonly.title" />' 
										   onClick="javascript:showOrgGrouping(${orgGrouping.groupingId})"></i>
										
									</div>
								</c:forEach>
							</div>
						</div>
					</lams:TabBody>
				</c:if>
				<lams:TabBody id="4">
					<!-- Advanced Panel -->
					<input id="organisation-id" name="organisationID" value="${param.organisationID}" type="hidden" />
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
						<lams:CKEditor id="introDescription" toolbarSet="LessonDescription" value=""></lams:CKEditor>

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
								<input id="gradebookOnCompleteField" name="gradebookOnComplete" value="true" checked="checked" type="checkbox"/>
								<fmt:message key="label.tab.advanced.field.gradebook.complete" />
							</label>
						</div>	
						<div class="checkbox">
							<label>
								<input id="startMonitorField" name="startMonitor" value="true" type="checkbox">
								<fmt:message key="label.tab.advanced.field.monitor" />
							</label>
						</div>
						<div class="checkbox">
							<label>
								<input name="forceRestart" value="true" type="checkbox"/>
								<fmt:message key="label.tab.advanced.field.force.restart" />
							</label>
						</div>		
						<div class="checkbox">
							<label>
								<input name="allowRestart" value="true" type="checkbox"/>
								<fmt:message key="label.tab.advanced.field.allow.restart" />
							</label>
						</div>	
						<div class="checkbox">
							<label>
								<input name="liveEditEnable" value="true" type="checkbox" checked="checked"/>
								<fmt:message key="label.tab.advanced.field.liveedit" />
							</label>
						</div>
						<div class="checkbox">
							<label>
								<input name="notificationsEnable" value="true" type="checkbox" checked="checked"/>
								<fmt:message key="label.tab.advanced.field.notification" />
							</label>
						</div>

						<div class="checkbox">
							<label>
								<input id="presenceEnableField" name="presenceEnable" value="true" type="checkbox"/>
								<fmt:message key="label.tab.advanced.field.presence" />
							</label>
						</div>
						
						<div class="checkbox">
							<label>
								<input id="imEnableField" name="imEnable" value="true" type="checkbox" disabled="disabled"/>
								<fmt:message key="label.tab.advanced.field.im" />
							</label>
						</div>

						<div class="checkbox">
							<label>
								<input id="splitLearnersField" type="checkbox"/>
								<fmt:message key="label.tab.advanced.field.split" />
							</label>
						</div>
						<div id="splitLearnersTable" style="display: none">
							<span><fmt:message key="label.tab.advanced.field.split.number" /> </span> 
							<input value="1" id="splitLearnersCountField" type="number" />
							<span class="roffset5" id="splitLearnersDescription"></span>
						</div>
						
						<!-- Scheduling -->
						<div class="checkbox">
							<label>
								<input id="schedulingEnableField" name="schedulingEnable" value="true" type="checkbox"/><fmt:message key="label.tab.advanced.field.scheduling" />
							</label>
						</div>		
						<div id="schedulingError" class="errorMessage">
							<fmt:message key="error.lesson.end.date.must.be.after.start.date"/>
						</div>
						<div id="scheduleStartTime" class="form-group">
							<label for="schedulingDatetimeField"><fmt:message key="label.start"/></label>
							<input id="schedulingDatetimeField" name="schedulingDatetime" type="text" autocomplete="nope" />
						</div>
						<div id="scheduleEndTime" class="form-group">
							<label for="schedulingEndDatetimeField"><fmt:message key="label.end"/></label>
							<input id="schedulingEndDatetimeField" name="schedulingEndDatetime" type="text" autocomplete="nope" />
						</div>
					</div>
					
					<!-- Add multiple lessons -->
					<c:if test="${not empty subgroups}">
						<div class="lead"><fmt:message key="label.multiple.lessons" /></div>
						<div class="options">
							<div class="fieldSectionDescription">
								<fmt:message key="label.add.lessons.to.subgroups" />
							</div>
							
							<c:forEach var="subgroup" items="${subgroups}">
								<div class="checkbox">
									<label>
										<input name="organisationID" class="multiple-lessons" value="${subgroup.organisationId}" type="checkbox"/>
										${subgroup.name}
									</label>
								</div>
							</c:forEach>
						</div>
					</c:if>
				</lams:TabBody>
				
				<lams:TabBody id="5">
					<!-- Conditions Panel -->
					<div class="lead"><fmt:message key="label.tab.conditions.dependencies" /></div>
					<div class="options">
						<div class="fieldSectionDescription">
							<fmt:message key="label.tab.conditions.dependencies.desc" />
						</div>
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
						<div class="fieldSectionDescription">
							<fmt:message key="label.tab.conditions.timelimit.desc" />
						</div>
						<div id="timelimitError" class="errorMessage">
							<fmt:message key="error.lesson.with.scheduled.end.only.have.individual.limits"/>
						</div>
						
						<div class="checkbox">
							<label>
								<input id="timeLimitEnableField" name="timeLimitEnable" value="true" type="checkbox"/><fmt:message key="label.tab.conditions.enable" />
							</label>	
						</div>
						<div id="timeLimitDiv" style="display: none;">
							<span><fmt:message key="label.tab.conditions.timelimit.days" /> </span> 
							<input id="timeLimitDaysField" name="timeLimitDays" type="number" /><br />
							<div class="checkbox">
								<label>
									<input id="timeLimitIndividualField" name="timeLimitIndividual" value="true" type="checkbox"/>
							       <fmt:message key="label.tab.conditions.timelimit.individual" />
							  </label>     
							</div>       
						</div>
					</div>	
				</lams:TabBody>
			</lams:TabBodys>
		</lams:TabBodyArea>
</lams:Page>

</form>

<hr class="separator" />
<div class="container-fluid">
	<button id="previewButton" class="btn btn-default" href="#" onClick="javascript:previewLesson()"
			data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key="authoring.fla.page.menu.preview" /></span>">
		<i class="fa fa-search-plus"></i>
		<fmt:message key="authoring.fla.page.menu.preview" />
	</button>
	<div class="pull-right">
		<button id="addButton" class="btn btn-primary" href="#" onClick="javascript:addLesson()"
				data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key="button.add.now" /></span>">
			<i class="fa fa-plus"></i> 
			<fmt:message key="button.add.now" />
		</button>
	</div>
</div>


</body>
</lams:html>
