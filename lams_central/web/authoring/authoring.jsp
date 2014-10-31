<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="authoring.fla.page.title" /></title>
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/yui/treeview.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/yui/folders.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/authoring.css" type="text/css" media="screen">

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.simple-color.js"></script>
	
	<!-- Fix for iPad
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.ui.touch-punch.js"></script>
	-->
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/yahoo-dom-event.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/animation-min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/json-min.js" ></script> 
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/treeview-min.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/raphael/raphael.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringGeneral.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringActivity.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringDecoration.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringProperty.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringHandler.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringMenu.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/canvg/rgbcolor.js"></script> 
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/canvg/StackBlur.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/canvg/canvg.js"></script> 
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',
			LD_THUMBNAIL_URL_BASE = LAMS_URL + 'home.do?method=createLearningDesignThumbnail&ldId=',
			LABELS = {
				// ActivityLib
				DEFAULT_GROUPING_TITLE : '<fmt:message key="authoring.fla.default.group.title" />',
				DEFAULT_GROUP_PREFIX : '<fmt:message key="authoring.fla.default.group.prefix" />',
				DEFAULT_BRANCHING_TITLE : '<fmt:message key="authoring.fla.default.branching.title" />',
				DEFAULT_BRANCH_PREFIX : '<fmt:message key="authoring.fla.default.branch.prefix" />',
				DEFAULT_OPTIONAL_ACTIVITY_TITLE : '<fmt:message key="authoring.fla.default.optional.activity.title" />',
				SUPPORT_ACTIVITY_TITLE : '<fmt:message key="authoring.fla.support.activity.title" />',
				GATE_ACTIVITY_LABEL : '<fmt:message key="authoring.fla.gate.activity.label" />',
				BRANCHING_START_SUFFIX : '<fmt:message key="authoring.fla.branching.start.suffix" />',
				BRANCHING_END_SUFFIX : '<fmt:message key="authoring.fla.branching.end.suffix" />',
				REMOVE_ACTIVITY_CONFIRM : '<fmt:message key="authoring.fla.remove.activity.confirm" />',
				BRANCHING_CREATE_CONFIRM : '<fmt:message key="authoring.fla.branching.create.confirm" />',
				TRANSITION_TO_EXISTS_ERROR : '<fmt:message key="authoring.fla.transition.to.exists.error" />',
				CIRCULAR_SEQUENCE_ERROR : '<fmt:message key="authoring.fla.circular.sequence.error" />',
				ACTIVITY_IN_CONTAINER_ERROR : '<fmt:message key="authoring.fla.activity.in.container.error" />',
				
				// DecorationLib
				DEFAULT_ANNOTATION_LABEL_TITLE : '<fmt:message key="authoring.fla.default.annotation.label.title" />',
				REGION_FIT_BUTTON_TOOLTIP : '<fmt:message key="authoring.fla.region.fit.button.tooltip" />',
				
				// General
				NEW_FOLDER_BUTTON : '<fmt:message key="authoring.fla.new.folder.button" />',
				COPY_BUTTON : '<fmt:message key="authoring.fla.copy.button" />',
				PASTE_BUTTON : '<fmt:message key="authoring.fla.paste.button" />',
				DELETE_BUTTON : '<fmt:message key="authoring.fla.delete.button" />',
				RENAME_BUTTON : '<fmt:message key="authoring.fla.rename.button" />',
				OPEN_BUTTON : '<fmt:message key="authoring.fla.open.button" />',
				SAVE_BUTTON : '<fmt:message key="authoring.fla.save.button" />',
				IMPORT_BUTTON : '<fmt:message key="authoring.fla.import.button" />',
				FOLDER : '<fmt:message key="authoring.fla.folder" />',
				SEQUENCE : '<fmt:message key="authoring.fla.sequence" />',
				SEQUENCE_NOT_VALID : '<fmt:message key="authoring.fla.sequence.not.valid" />',
				SEQUENCE_VALIDATION_ISSUES : '<fmt:message key="authoring.fla.sequence.validation.issues" />',
				SAVE_SUCCESSFUL : '<fmt:message key="authoring.fla.save.successful" />',
				NAVIGATE_AWAY_CONFIRM : '<fmt:message key="authoring.fla.navigate.away.confirm" />',
				DELETE_NODE_CONFIRM : '<fmt:message key="authoring.fla.delete.node.confirm" />',
				SEQUENCE_OVERWRITE_CONFIRM : '<fmt:message key="authoring.fla.sequence.overwrite.confirm" />',
				NEW_FOLDER_TITLE_PROMPT : '<fmt:message key="authoring.fla.new.folder.title.prompt" />',
				RENAME_TITLE_PROMPT : '<fmt:message key="authoring.fla.rename.title.prompt" />',
				SAVE_SEQUENCE_TITLE_PROMPT : '<fmt:message key="authoring.fla.save.sequence.title.prompt" />',
				IMPORT_PART_CHOOSE_PROMPT : '<fmt:message key="authoring.fla.import.part.choose.prompt" />',
				FOLDER_NOT_SELECTED_ERROR : '<fmt:message key="authoring.fla.folder.not.selected.error" />',
				TITLE_VALIDATION_ERROR : '<fmt:message key="authoring.fla.title.validation.error" />',
				FOLDER_EXISTS_ERROR : '<fmt:message key="authoring.fla.folder.exists.error" />',
				SEQUENCE_EXISTS_ERROR : '<fmt:message key="authoring.fla.sequence.exists.error" />',
				SEQUENCE_SAVE_ERROR : '<fmt:message key="authoring.fla.sequence.save.error" />',
				SEQUENCE_NOT_SELECTED_ERROR : '<fmt:message key="authoring.fla.sequence.not.selected.error" />',
				SEQUENCE_LOAD_ERROR : '<fmt:message key="authoring.fla.sequence.load.error" />',
				
				// HandlerLib
				TRANSITION_FROM_EXISTS_ERROR : '<fmt:message key="authoring.fla.transition.from.exists.error" />',
				
				// MenuLib
				EXPORT_IMAGE_DIALOG_TITLE : '<fmt:message key="authoring.fla.export.image.dialog.title" />',
				EXPORT_SEQUENCE_DIALOG_TITLE : '<fmt:message key="authoring.fla.export.sequence.dialog.title" />',
				ACTIVITY_COPY_TITLE_PREFIX : '<fmt:message key="authoring.fla.activity.copy.title.prefix" />',
				PREVIEW_LESSON_DEFAULT_TITLE : '<fmt:message key="authoring.fla.preview.lesson.default.title" />',
				SAVE_DIALOG_TITLE : '<fmt:message key="authoring.fla.save.dialog.title" />',
				OPEN_DIALOG_TITLE : '<fmt:message key="authoring.fla.open.dialog.title" />',
				IMPORT_PART_DIALOG_TITLE : '<fmt:message key="authoring.fla.import.part.dialog.title" />',
				RUN_SEQUENCES_FOLDER : '<fmt:message key="label.tab.lesson.sequence.folder" />',
				ARRANGE_CONFIRM : '<fmt:message key="authoring.fla.arrange.confirm" />',
				CLEAR_CANVAS_CONFIRM : '<fmt:message key="authoring.fla.clear.canvas.confirm" />',
				BRANCHING_START_PLACE_PROMPT : '<fmt:message key="authoring.fla.branching.start.place.prompt" />',
				BRANCHING_END_PLACE_PROMPT : '<fmt:message key="authoring.fla.branching.end.place.prompt" />',
				ANNOTATION_REGION_PLACE_PROMPT : '<fmt:message key="authoring.fla.annotation.region.place.prompt" />',
				ANNOTATION_LABEL_PLACE_PROMPT : '<fmt:message key="authoring.fla.annotation.label.place.prompt" />',
				OPTIONAL_ACTIVITY_PLACE_PROMPT : '<fmt:message key="authoring.fla.optional.activity.place.prompt" />',
				SUPPORT_ACTIVITY_PLACE_PROMPT : '<fmt:message key="authoring.fla.support.activity.place.prompt" />',
				TRANSITION_PLACE_PROMPT : '<fmt:message key="authoring.fla.transition.place.prompt" />',
				PASTE_ERROR : '<fmt:message key="authoring.fla.paste.error" />',
				PREVIEW_ERROR : '<fmt:message key="authoring.fla.preview.error" />',
				CROSS_BRANCHING_ERROR : '<fmt:message key="authoring.fla.cross.branching.error" />',
				END_MATCH_ERROR : '<fmt:message key="authoring.fla.end.match.error" />',
				
				
				// PropertyLib
				OK_BUTTON : '<fmt:message key="authoring.fla.ok.button" />',
				CANCEL_BUTTON : '<fmt:message key="authoring.fla.cancel.button" />',
				CLEAR_ALL_BUTTON : '<fmt:message key="authoring.fla.clear.all.button" />',
				REFRESH_BUTTON : '<fmt:message key="authoring.fla.refresh.button" />',
				REMOVE_CONDITION_BUTTON : '<fmt:message key="authoring.fla.remove.condition.button" />',
				PROPERTIES_DIALOG_TITLE : '<fmt:message key="authoring.fla.properties.dialog.title" />',
				GROUP_NAMING_DIALOG_TITLE : '<fmt:message key="authoring.fla.group.naming.dialog.title" />',
				GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE : '<fmt:message key="authoring.fla.groups.to.branches.match.dialog_title" />',
				BRANCH_MAPPING_GROUPS_HEADER : '<fmt:message key="authoring.fla.branch.mapping.groups.header" />',
				BRANCH_MAPPING_GROUP_HEADER : '<fmt:message key="authoring.fla.branch.mapping.group.header" />',
				CONDITIONS_DIALOG_TITLE : '<fmt:message key="authoring.fla.conditions.dialog.title" />',
				BRANCH_MAPPING_CONDITIONS_HEADER : '<fmt:message key="authoring.fla.branch.mapping.conditions.header" />',
				BRANCH_MAPPING_CONDITION_HEADER : '<fmt:message key="authoring.fla.branch.mapping.condition.header" />',
				BRANCH_MAPPING_GATE_HEADER : '<fmt:message key="authoring.fla.branch.mapping.gate.header" />',
				BRANCH_MAPPING_BRANCHES_HEADER : '<fmt:message key="authoring.fla.branch.mapping.branches.header" />',
				BRANCH_MAPPING_BRANCH_HEADER : '<fmt:message key="authoring.fla.branch.mapping.branch.header" />',
				GATE_STATE_MAPPING_DIALOG_TITLE : '<fmt:message key="authoring.fla.gate.state.mapping.dialog.title" />',
				BRANCH_MAPPING_DIALOG_TITLE : '<fmt:message key="authoring.fla.branch.mapping.dialog.title" />',
				GATE_STATE_OPEN : '<fmt:message key="authoring.fla.gate.state.open" />',
				GATE_STATE_CLOSED : '<fmt:message key="authoring.fla.gate.state.closed" />',
				BRANCH_MAPPING_DEFAULT_BRANCH_SUFFIX : '<fmt:message key="authoring.fla.branch.mapping.default.branch.suffix" />',
				COMPLEX_OUTPUT_SUFFIX : '<fmt:message key="authoring.fla.complex.output.suffix" />',
				RANGE_OUTPUT_SUFFIX : '<fmt:message key="authoring.fla.range.output.suffix" />',
				RANGE_CONDITION_DESCRIPTION : '<fmt:message key="authoring.fla.range.condition.description" />',
				EXACT_CONDITION_DESCRIPTION : '<fmt:message key="authoring.fla.exact.condition.description" />',
				LESS_CONDITION_DESCRIPTION : '<fmt:message key="authoring.fla.less.condition.description" />',
				GREATER_CONDITION_DESCRIPTION : '<fmt:message key="authoring.fla.greater.condition.description" />',
				DEFAULT_RANGE_CONDITION_TITLE_PREFIX : '<fmt:message key="authoring.fla.default.range.condition.title.prefix" />',
				CLEAR_ALL_CONFIRM : '<fmt:message key="authoring.fla.clear.all.confirm" />',
				REMOVE_CONDITION_CONFIRM : '<fmt:message key="authoring.fla.remove.condition.confirm" />',
				REFRESH_CONDITIONS_CONFIRM : '<fmt:message key="authoring.fla.refresh.conditions.confirm" />',
				CONDITIONS_TO_DEFAULT_GATE_STATE_CONFIRM : '<fmt:message key="authoring.fla.conditions.to.default.gate.state.confirm" />',
				CONDITIONS_TO_DEFAULT_BRANCH_CONFIRM : '<fmt:message key="authoring.fla.conditions.to.default.branch.confirm" />',
				GROUPS_TO_DEFAULT_BRANCH_CONFIRM : '<fmt:message key="authoring.fla.groups.to.default.branch.confirm" />',
				RANGE_CONDITION_ADD_START_ERROR : '<fmt:message key="authoring.fla.range.condition.add.start.error" />',
				RANGE_CONDITION_ADD_END_ERROR : '<fmt:message key="authoring.fla.range.condition.add.end.error" />',
				GROUP_TITLE_VALIDATION_ERORR : '<fmt:message key="authoring.fla.group.title.validation.erorr" />',
			},
			
			isReadOnlyMode = false,
			activitiesOnlySelectable = false,
			initContentFolderID = '${contentFolderID}',
			initLearningDesignID = '${param.learningDesignID}',
			learningLibraryGroups = ${learningLibraryGroups},
			initAccess = ${access};
	</script>
</lams:head>
<body onresize="javascript:GeneralLib.resizePaper()">
	<div id="toolbar" class="ui-corner-all">
		<div id="newButton" class="ui-button" onClick="javascript:GeneralLib.newLearningDesign(false, false)">
			<fmt:message key="authoring.fla.page.menu.new" />
		</div>
		<div>
			<div class="split-ui-button">
				<div id="openButton" onClick="javascript:MenuLib.openLearningDesign()">
					<span><fmt:message key="authoring.fla.page.menu.open" /></span>
				</div>
				<div id="openDropButton">&nbsp;</div>
			</div>
			<ul>
				<li id="importSequenceButton" onClick="javascript:MenuLib.importLearningDesign()"><fmt:message key="authoring.fla.page.menu.import" /></li>
				<li id="importPartSequenceButton" onClick="javascript:MenuLib.importPartLearningDesign()"><fmt:message key="authoring.fla.page.menu.import.part" /></li>
			</ul>
		</div>
		<div>
			<div class="split-ui-button">
				<div id="saveButton" onClick="javascript:MenuLib.saveLearningDesign()">
					<span><fmt:message key="authoring.fla.page.menu.save" /></span>
				</div>
				<div id="saveDropButton">&nbsp;</div>
			</div>
			<ul>
				<li id="saveAsButton" onClick="javascript:MenuLib.saveLearningDesign(true)"><fmt:message key="authoring.fla.page.menu.saveas" /></li>
				<li id="exportButton"><fmt:message key="authoring.fla.page.menu.export" /><span class="ui-icon ui-menu-icon ui-icon-carat-1-e"></span>
					<ul>
						<li id="exportLamsButton" class="exportSequenceButton"
							title="Standard LAMS ZIP format"
							onClick="javascript:MenuLib.exportLearningDesign(1)"><fmt:message key="authoring.fla.page.menu.export.lams" /></li>
						<li id="exportImsButton" class="exportSequenceButton"
							title="<fmt:message key='authoring.fla.page.menu.export.ims.tooltip' />"
							onClick="javascript:MenuLib.exportLearningDesign(2)"><fmt:message key="authoring.fla.page.menu.export.ims" /></li>
						<li id="exportPngButton" class="exportImageButton" onClick="javascript:MenuLib.exportPNG(true)"><fmt:message key="authoring.fla.page.menu.export.png" /></li>
						<li id="exportSvgButton" class="exportImageButton" onClick="javascript:MenuLib.exportSVG(true)"><fmt:message key="authoring.fla.page.menu.export.svg" /></li>
					</ul>
				</li>
			</ul>
		</div>
		<div id="copyButton" class="ui-button" onClick="javascript:MenuLib.copyActivity()">
			<fmt:message key="authoring.fla.page.menu.copy" />
		</div>
		<div id="pasteButton" class="ui-button" onClick="javascript:MenuLib.pasteActivity()">
			<fmt:message key="authoring.fla.page.menu.paste" />
		</div>
		<div id="transitionButton" class="ui-button" onClick="javascript:MenuLib.addTransition()">
			<fmt:message key="authoring.fla.page.menu.transition" />
		</div>
		<div>
			<div id="optionalButton" class="split-ui-button">
				<div>
					<span><fmt:message key="authoring.fla.page.menu.optional" /></span>
				</div>
				<div id="optionalDropButton">&nbsp;</div>
			</div>
			<ul>
				<li id="optionalActivityButton" onClick="javascript:MenuLib.addOptionalActivity()"><fmt:message key="authoring.fla.page.menu.optional.activity" /></li>
				<li id="floatingActivityButton" onClick="javascript:MenuLib.addFloatingActivity()"><fmt:message key="authoring.fla.page.menu.optional.support" /></li>
			</ul>
		</div>
		<div>
			<div id="flowButton" class="split-ui-button">
				<div>
					<span><fmt:message key="authoring.fla.page.menu.flow" /></span>
				</div>
				<div id="flowDropButton">&nbsp;</div>
			</div>
			<ul>
				<li id="gateButton" onClick="javascript:MenuLib.addGate()"><fmt:message key="authoring.fla.page.menu.flow.gate" /></li>
				<li id="branchingButton" onClick="javascript:MenuLib.addBranching()"><fmt:message key="authoring.fla.page.menu.flow.branch" /></li>
			</ul>
		</div>
		<div id="groupButton" class="ui-button" onClick="javascript:MenuLib.addGrouping()">
			<fmt:message key="authoring.fla.page.menu.group" />
		</div>
		<div>
			<div id="annotateButton" class="split-ui-button">
				<div>
					<span><fmt:message key="authoring.fla.page.menu.annotate" /></span>
				</div>
				<div id="annotateDropButton">&nbsp;</div>
			</div>
			<ul>
				<li id="annotateLabelButton" onClick="javascript:MenuLib.addAnnotationLabel()"><fmt:message key="authoring.fla.page.menu.annotate.label" /></li>
				<li id="annotateRegionButton" onClick="javascript:MenuLib.addAnnotationRegion()"><fmt:message key="authoring.fla.page.menu.annotate.region" /></li>
			</ul>
		</div>
		<div id="arrangeButton" class="ui-button" onClick="javascript:GeneralLib.arrangeActivities()">
			<fmt:message key="authoring.fla.page.menu.arrange" />
		</div>
		<div id="previewButton" class="ui-button" onClick="javascript:MenuLib.openPreview()">
			<fmt:message key="authoring.fla.page.menu.preview" />
		</div>
		<!-- 
		<div id="zoomButton" class="ui-button" onClick="javascript:MenuLib.zoom()">
			Zoom out
		</div>
 		-->
	</div>
	
	<table id="authoringTable">
		<tr>
			<td id="templateContainerCell">
				<select>
					<option><fmt:message key="authoring.fla.tool.groups.all" /></option>
				</select>
				<div class="templateContainer">
					<c:forEach var="tool" items="${tools}">
						<div
							 Id="tool${tool.toolDisplayName}"
							 toolId="${tool.toolId}"
							 learningLibraryId="${tool.learningLibraryId}"
							 supportsOutputs="${tool.supportsOutputs}"
							 activityCategoryId="${tool.activityCategoryID}"
							 childToolIds="
							 <c:forEach var='childId' items='${tool.childToolIds}'>
							 	${childId},
							 </c:forEach>
							 "
							 class="template">
							<c:if test="${not empty tool.iconPath}">
								<img src="<lams:LAMSURL/>${tool.iconPath}" />
							</c:if>
							<div><c:out value="${tool.toolDisplayName}" /></div>
						</div>
					</c:forEach>
				</div>
			</td>
			<td id="canvasContainerCell">
				<div id="ldDescriptionDiv">
					<div id="ldDescriptionTitleContainer" title="Click to show the sequence description"
						 onClick="javascript:MenuLib.toggleDescriptionDiv()">
						<span id="ldDescriptionFieldTitle"><fmt:message key="authoring.fla.page.ld.title" /></span>
						<span id="ldDescriptionFieldModified"></span>
						<span id="ldDescriptionHideTip">▼</span>
					</div>
					<div id="ldDescriptionDetails">
						<div class="ldDescriptionLabel"><fmt:message key="authoring.fla.page.ld.description" /></div>
						<div id="ldDescriptionEditorContainer">
							<lams:CKEditor id="ldDescriptionFieldDescription" value=""
								   height="300px"
								   contentFolderID="${contentFolderID}"/>
						</div>
						
						<div class="ldDescriptionLabel"><fmt:message key="authoring.fla.page.ld.license" /></div>
						<select id="ldDescriptionLicenseSelect">
							<option value="0" selected="selected"><fmt:message key="authoring.fla.page.ld.license.none" /></option>
							<c:forEach var="license" items="${licenses}">
								<option value="<c:out value='${license.licenseID}' escapeXml='true' />"
										pictureURL="<c:out value='${license.pictureURL}' escapeXml='true' />"
										url="<c:out value='${license.url}' escapeXml='true' />"
								>
									<c:out value='${license.name}' escapeXml='true' />
								</option>
							</c:forEach>
						</select>
						<img id="ldDescriptionLicenseImage" />
						<input id="ldDescriptionLicenseButton" class="button"
							   type="button" value="View" />
						<div id="ldDescriptionLicenseTextContainer">
							<div class="ldDescriptionLabel"><fmt:message key="authoring.fla.page.ld.license.info" /></div>
							<textarea id="ldDescriptionLicenseText" rows="5"></textarea>
						</div>
					</div>
				</div>
				<div id="canvas"></div>
			</td>
		</tr>
	</table>
	
	<!-- DIALOGS CONTENTS -->
	
	<!-- SEQUENCE LOAD/SAVE DIALOG -->
	<div id="ldStoreDialog" class="dialogContainer">
		<table>
			<tr>
				<td id="ldStoreDialogTreeCell">
					<div id="ldStoreDialogTree"></div>
				</td>
				<td id="ldStoreDialogCanvasCell" rowspan="2">
					<div id="ldStoreDialogCanvasDiv">
			    		<img id="ldScreenshotAuthor" class="ldChoiceDependentCanvasElement" />
		    			<img id="ldScreenshotLoading" class="ldChoiceDependentCanvasElement"
		    			     src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
		    			<iframe id="ldStoreDialogImportPartFrame"></iframe>
			    	</div>
				</td>
			</tr>
			<tr>
				<td id="ldStoreDialogAccessCell" >
					<div id="ldStoreDialogAccessTitle"><fmt:message key="authoring.fla.page.dialog.access" /></div>
				</td>
			</tr>
		</table>
		<%-- This will be moved to dialog's button pane using JS --%>
		<div id="ldStoreDialogNameContainer" class="ldStoreDialogSaveOnly">
			<span><fmt:message key="authoring.fla.page.dialog.ld.title" /></span><input id="ldStoreDialogNameField" type="text"/>
		</div>
	</div>
	
	
	<!-- GROUP OR CONDITION TO BRANCH MATCHING DIALOG -->
	
	<div id="branchMappingDialog" class="dialogContainer branchMappingDialog">
		<table>
			<tr>
				<td></td>
				<td></td>
				<td rowspan="2"></td>
				<td colspan="2" class="branchMappingLabelCell">
					<fmt:message key="authoring.fla.page.dialog.mappings" />
				</td>
			</tr>
			<tr>
				<td class="branchMappingLabelCell branchMappingFreeItemHeaderCell"></td>
				<td class="branchMappingLabelCell branchMappingFreeBranchHeaderCell"></td>
				<td class="branchMappingLabelCell branchMappingBoundItemHeaderCell"></td>
				<td class="branchMappingLabelCell branchMappingBoundBranchHeaderCell"></td>
			</tr>
			<tr>
				<td class="branchMappingFreeItemCell branchMappingListCell"></td>
				<td class="branchMappingFreeBranchCell branchMappingListCell"></td>
				<td>
					<div class="branchMappingAddButton branchMappingButton"></div>
					<div class="branchMappingRemoveButton branchMappingButton"></div>
				</td>
				<td class="branchMappingBoundItemCell branchMappingListCell"></td>
				<td class="branchMappingBoundBranchCell branchMappingListCell"></td>
			</tr>
		</table>
	</div>
	
	
	<!-- PROPERTY DIALOG CONTENTS FOR DIFFERENT OBJECT TYPES -->
	
	<div id="propertiesContentTransition" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.default" />
				</td>
				<td>
					<input class="propertiesContentFieldDefault" type="checkbox"></input>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="propertiesContentTool" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.grouping" />
				</td>
				<td>
					  <select class="propertiesContentFieldGrouping"></select>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentGrouping" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.grouping.type" />
				</td>
				<td>
					 <select class="propertiesContentFieldGroupingType">
					 	<option value="random"><fmt:message key="authoring.fla.page.prop.grouping.type.random" /></option>
					 	<option value="monitor"><fmt:message key="authoring.fla.page.prop.grouping.type.monitor" /></option>
					 	<option value="learner"><fmt:message key="authoring.fla.page.prop.grouping.type.learner" /></option>
					 </select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.number" />
				</td>
				<td>
					 <input class="propertiesContentFieldGroupCountEnable" type="radio"
					 		name="propertiesContentFieldGroupDivide"></input>
					 <input class="propertiesContentFieldGroupCount spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.learners" />
				</td>
				<td>
					<input class="propertiesContentFieldLearnerCountEnable" type="radio"
					 		name="propertiesContentFieldGroupDivide"></input>
					 <input class="propertiesContentFieldLearnerCount spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.equal" />
				</td>
				<td>
					 <input class="propertiesContentFieldEqualSizes" type="checkbox"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.view.learners" />
				</td>
				<td>
					 <input class="propertiesContentFieldViewLearners" type="checkbox"></input>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="propertiesContentFieldNameGroups"><fmt:message key="authoring.fla.page.prop.groups.name" /></div>
				</td>
			</tr>
		</table>
	</div>


	<div id="propertiesContentGate" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td colspan="3">
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.description" />
				</td>
				<td colspan="3">
					 <textarea class="propertiesContentFieldDescription"></textarea>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.gate.type" />
				</td>
				<td colspan="3">
					 <select class="propertiesContentFieldGateType">
					 	<option value="condition"><fmt:message key="authoring.fla.page.prop.gate.type.condition" /></option>
					 	<option value="sync"><fmt:message key="authoring.fla.page.prop.gate.type.sync" /></option>
					 	<option value="schedule"><fmt:message key="authoring.fla.page.prop.gate.type.schedule" /></option>
					 	<option value="permission"><fmt:message key="authoring.fla.page.prop.gate.type.permission" /></option>
					 </select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.input" />
				</td>
				<td>
					  <select class="propertiesContentFieldInput"></select>
				</td>
			</tr>
			<tr class="propertiesContentRowGateSchedule">
				<td>
					<fmt:message key="authoring.fla.page.prop.gate.delay" />
				</td>
				<td>
					<input class="propertiesContentFieldOffsetDay spinner" type="text"></input> <fmt:message key="authoring.fla.page.prop.days" />
				</td>
				<td>
					<input class="propertiesContentFieldOffsetHour spinner" type="text"></input> <fmt:message key="authoring.fla.page.prop.hours" />
				</td>
				<td>
					<input class="propertiesContentFieldOffsetMinute spinner" type="text"></input> <fmt:message key="authoring.fla.page.prop.minutes" />
				</td>
			</tr>
			<tr class="propertiesContentRowGateSchedule">
				<td colspan="3">
					<fmt:message key="authoring.fla.page.prop.gate.activity.finish.based" />
				</td>
				<td>
					 <input class="propertiesContentFieldActivityCompletionBased" type="checkbox"></input>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="3">
					<div class="propertiesContentFieldCreateConditions"><fmt:message key="authoring.fla.page.prop.conditions.create" /></div>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="3">
					<div class="propertiesContentFieldMatchConditions"><fmt:message key="authoring.fla.page.prop.gate.conditions.map" /></div>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentBranching" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.branching.type" />
				</td>
				<td>
					 <select class="propertiesContentFieldBranchingType">
					 	<option value="chosen"><fmt:message key="authoring.fla.page.prop.branching.type.chose" /></option>
					 	<option value="group"><fmt:message key="authoring.fla.page.prop.branching.type.group" /></option>
					 	<option value="tool"><fmt:message key="authoring.fla.page.prop.branching.type.tool" /></option>
					 	<option value="optional"><fmt:message key="authoring.fla.page.prop.branching.type.optional" /></option>
					 </select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.grouping" />
				</td>
				<td>
					<select class="propertiesContentFieldGrouping"></select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.input" />
				</td>
				<td>
					<select class="propertiesContentFieldInput"></select>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="2">
					<div class="propertiesContentFieldCreateConditions"><fmt:message key="authoring.fla.page.prop.conditions.create" /></div>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="2">
					<div class="propertiesContentFieldMatchConditions"><fmt:message key="authoring.fla.page.prop.branching.conditions.match" /></div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="propertiesContentFieldMatchGroups"><fmt:message key="authoring.fla.page.prop.branching.groups.match" /></div>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.branching.sequences.min" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalSequenceMin spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.branching.sequences.max" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalSequenceMax spinner" type="text"></input>
				</td>
			</tr>
		</table>
	</div>
	
		
	<div id="propertiesContentParallel" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.grouping" />
				</td>
				<td>
					  <select class="propertiesContentFieldGrouping"></select>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentOptionalActivity" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.optional.activities.min" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalActivityMin spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.optional.activities.max" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalActivityMax spinner" type="text"></input>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentRegion" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.color" />
				</td>
				<td>
					 <input class="propertiesContentFieldColor" type="text"></input>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentLabel" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
		</table>
	</div>
	
	
	<!-- TOOL OUTPUT CONDITIONS DIALOG -->
	<div id="outputConditionsDialog" class="dialogContainer">
		 <select id="outputSelect">
		 	<option value="none"><fmt:message key="authoring.fla.page.dialog.cond.output.choose" /></option>
		 </select>
		 <select id="rangeOptionSelect" class="outputSelectDependent">
		 	<option value="none" selected="selected"><fmt:message key="authoring.fla.page.dialog.cond.options.choose" /></option>
		 	<option value="greater"><fmt:message key="authoring.fla.page.dialog.cond.greater" /></option>
		 	<option value="less"><fmt:message key="authoring.fla.page.dialog.cond.less" /></option>
		 	<option value="range"><fmt:message key="authoring.fla.page.dialog.cond.range" /></option>
		 </select>
		 <div id="rangeAddDiv" class="outputSelectDependent">
			 <input id="singleRangeSpinner" class="outputSelectDependent spinner" />
			 <div id="multiRangeDiv" class="outputSelectDependent">
			 	<fmt:message key="authoring.fla.page.dialog.cond.range.from" /> <input id="multiRangeFromSpinner" class="spinner" />
			 	<fmt:message key="authoring.fla.page.dialog.cond.range.to" /> <input id="multiRangeToSpinner" class="spinner" />
			 </div>
			 <span id="rangeAddButton"><fmt:message key="authoring.fla.page.dialog.cond.add" /></span>
		 </div>
		 <div id="complexConditions" class="outputSelectDependent">
		 	<div><fmt:message key="authoring.fla.page.dialog.cond.name" /></div>
		 	<ul></ul>
		 </div>
		 <table id="rangeConditions" class="outputSelectDependent">
		 	<tr id="rangeConditionsHeaderRow">
		 		<th><fmt:message key="authoring.fla.page.dialog.cond.list.name" /></th>
		 		<th><fmt:message key="authoring.fla.page.dialog.cond.list.condition" /></th>
		 	</tr>
		 </table>
	</div>
	
	
	<!-- EXPORT CANVAS AS IMAGE DIALOG -->
	<div id="exportImageDialog" class="dialogContainer exportDialog">
		<a href="#"><fmt:message key="authoring.fla.page.download.image" /></a>
	</div>
	
	
	<!-- EXPORT LEARNING DESIGN DIALOG -->
	<div id="exportLDDialog" class="dialogContainer exportDialog">
		<span><fmt:message key="authoring.fla.page.download.wait" /><br /><fmt:message key="authoring.fla.page.download.close" /></span>
		<iframe></iframe>
	</div>
</body>
</lams:html>
