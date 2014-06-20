<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title>Flashless Authoring</title>
	
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
				DEFAULT_GROUPING_TITLE : 'Grouping',
				DEFAULT_GROUP_PREFIX : 'Group ',
				DEFAULT_BRANCHING_TITLE :'Branching',
				DEFAULT_BRANCH_PREFIX : 'Branch ',
				DEFAULT_OPTIONAL_ACTIVITY_TITLE : 'Optional Activity',
				SUPPORT_ACTIVITY_TITLE : 'Support Activity',
				GATE_ACTIVITY_LABEL : 'STOP',
				BRANCHING_START_SUFFIX : 'start',
				BRANCHING_END_SUFFIX : 'end',
				REMOVE_ACTIVITY_CONFIRM : 'Are you sure you want to remove the whole branching activity?',
				BRANCHING_CREATE_CONFIRM : 'Transition from this activity already exists.\nDo you want to create branching here?',
				TRANSITION_TO_EXISTS_ERROR : 'Transition to this activity already exists',
				
				// DecorationLib
				DEFAULT_ANNOTATION_LABEL_TITLE : 'Label',
				REGION_FIT_BUTTON_TOOLTIP : 'Fit',
				
				// General
				NEW_FOLDER_BUTTON : 'New',
				COPY_BUTTON : 'Copy',
				PASTE_BUTTON : 'Paste',
				DELETE_BUTTON : 'Delete',
				RENAME_BUTTON : 'Rename',
				OPEN_BUTTON : 'Open',
				SAVE_BUTTON : 'Save',
				FOLDER : 'folder',
				SEQUENCE : 'sequence',
				SEQUENCE_NOT_VALID : 'The sequence is not valid.<br />It needs to be corrected before it can be used in lessons.',
				SEQUENCE_VALIDATION_ISSUES : 'While saving the sequence there were following validation issues:',
				SAVE_SUCCESSFUL : 'Congratulations! Your design is valid and has been saved.',
				NAVIGATE_AWAY_CONFIRM : 'Your design is not saved.\nAny changes you made since you last saved will be lost.',
				DELETE_NODE_CONFIRM : 'Are you sure you want to delete this ',
				SEQUENCE_OVERWRITE_CONFIRM : 'Are you sure you want to overwrite the existing sequence?',
				NEW_FOLDER_TITLE_PROMPT : 'Please enter the name for a new folder',
				RENAME_TITLE_PROMPT : 'Please enter the new name for ',
				SAVE_SEQUENCE_TITLE_PROMPT : 'Please enter a title for the sequence',
				FOLDER_NOT_SELECTED_ERROR : 'Please choose a folder',
				TITLE_VALIDATION_ERROR : 'The title can not contain any of these characters < > ^ * @ % $',
				FOLDER_EXISTS_ERROR : 'A folder with this name already exists.',
				SEQUENCE_EXISTS_ERROR : 'A sequence with this name already exists.',
				SEQUENCE_SAVE_ERROR : 'Error while loading sequence',
				SEQUENCE_NOT_SELECTED_ERROR : 'Please choose a sequence',
				SEQUENCE_LOAD_ERROR : 'Error while loading the sequence',
				
				// HandlerLib
				TRANSITION_FROM_EXISTS_ERROR : 'Transition from this activity already exists',
				
				// MenuLib
				EXPORT_IMAGE_DIALOG_TITLE : 'Image export',
				EXPORT_SEQUENCE_DIALOG_TITLE : 'Sequence export',
				ACTIVITY_COPY_TITLE_PREFIX : 'Copy [0] of ',
				PREVIEW_LESSON_DEFAULT_TITLE : 'Preview',
				SAVE_DIALOG_TITLE : 'Save sequence',
				OPEN_DIALOG_TITLE : 'Open sequence',
				RUN_SEQUENCES_FOLDER : '<fmt:message key="label.tab.lesson.sequence.folder" />',
				ARRANGE_CONFIRM : 'There are annotations on the canvas.\n'
	   				+ 'They will be not arranged automatically, you will have to adjust them manually later.\n'
	   				+ 'Do you want to continue?',
				CLEAR_CANVAS_CONFIRM : 'Are you sure you want to remove all existing elements?',
				BRANCHING_START_PLACE_PROMPT : 'Place the branching point',
				BRANCHING_END_PLACE_PROMPT : 'Place the converge point',
				ANNOTATION_REGION_PLACE_PROMPT : 'Click and hold to start drawing an annotation region',
				ANNOTATION_LABEL_PLACE_PROMPT : 'Click to add an annotation label',
				OPTIONAL_ACTIVITY_PLACE_PROMPT : 'Click to add an optional activity container.',
				SUPPORT_ACTIVITY_PLACE_PROMPT : 'Click to add a support activity container.',
				TRANSITION_PLACE_PROMPT : 'Click on an activity',
				PASTE_ERROR : 'Sorry, you can not paste this type of activity',
				PREVIEW_ERROR : 'Error while initialising lesson for preview',
				
				// PropertyLib
				OK_BUTTON : 'OK',
				CANCEL_BUTTON : 'Cancel',
				CLEAR_ALL_BUTTON : 'Clear all',
				REFRESH_BUTTON : 'Refresh',
				REMOVE_CONDITION_BUTTON : 'Remove',
				PROPERTIES_DIALOG_TITLE : 'Properties',
				GROUP_NAMING_DIALOG_TITLE : 'Group Naming',
				GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE : 'Match Groups to Branches',
				BRANCH_MAPPING_GROUPS_HEADER : 'Groups',
				BRANCH_MAPPING_GROUP_HEADER : 'Group',
				CONDITIONS_DIALOG_TITLE : 'Select Output Conditions for Input',
				BRANCH_MAPPING_CONDITIONS_HEADER : 'Conditions',
				BRANCH_MAPPING_CONDITION_HEADER : 'Condition',
				BRANCH_MAPPING_GATE_HEADER : 'Gate',
				BRANCH_MAPPING_BRANCHES_HEADER : 'Branches',
				BRANCH_MAPPING_BRANCH_HEADER : 'Branch',
				GATE_STATE_MAPPING_DIALOG_TITLE : 'Map gate conditions',
				BRANCH_MAPPING_DIALOG_TITLE : 'Match conditions to branches',
				GATE_STATE_OPEN : 'open',
				GATE_STATE_CLOSED : 'closed',
				BRANCH_MAPPING_DEFAULT_BRANCH_SUFFIX : ' (default)',
				COMPLEX_OUTPUT_SUFFIX : ' (user defined)',
				RANGE_OUTPUT_SUFFIX : ' (range)',
				RANGE_CONDITION_DESCRIPTION : 'Range [0] to [1]',
				EXACT_CONDITION_DESCRIPTION : 'Exact value of ',
				LESS_CONDITION_DESCRIPTION : 'Less than or eq ',
				GREATER_CONDITION_DESCRIPTION : 'Greater than or eq ',
				DEFAULT_RANGE_CONDITION_TITLE_PREFIX : 'Untitled ',
				CLEAR_ALL_CONFIRM : 'There are conditions linked to an existing branch.\nDo you wish to remove them?',
				REMOVE_CONDITION_CONFIRM : 'This condition is linked to an existing branch.\nDo you wish to remove it?',
				REFRESH_CONDITIONS_CONFIRM : 'You are about to update your conditions for the selected output definition.\n'
										   + 'This will clear all links to existing branches.\nDo you wish to continue?',
				CONDITIONS_TO_DEFAULT_GATE_STATE_CONFIRM : 'All remaining conditions will be mapped to the selected gate\'s closed state',
				CONDITIONS_TO_DEFAULT_BRANCH_CONFIRM : 'All remaining conditions will be mapped to the default branch',
				GROUPS_TO_DEFAULT_BRANCH_CONFIRM : 'All remaining groups will be mapped to the default branch',
				RANGE_CONDITION_ADD_START_ERROR : 'The start value can not be within the range of an existing condition',
				RANGE_CONDITION_ADD_END_ERROR : 'The end value can not be within the range of an existing condition',
				GROUP_TITLE_VALIDATION_ERORR : 'Group name can not contain any of these characters < > ^ * @ % $'
			},
			
			isReadOnlyMode = false,
			initContentFolderID = '${contentFolderID}',
			initLearningDesignID = '${param.learningDesignID}',
			initAccess = ${access};
	</script>
</lams:head>
<body onresize="javascript:GeneralLib.resizePaper()">
	<div id="toolbar" class="ui-corner-all">
		<div class="ui-button" onClick="javascript:GeneralLib.newLearningDesign(false, false)">
			New
		</div>
		<div>
			<div class="split-ui-button">
				<div onClick="javascript:MenuLib.openLearningDesign()">
					<span>Open</span>
				</div>
				<div>&nbsp;</div>
			</div>
			<ul>
				<li onClick="javascript:MenuLib.importLearningDesign()">Import</li>
			</ul>
		</div>
		<div>
			<div class="split-ui-button">
				<div onClick="javascript:MenuLib.saveLearningDesign()">
					<span>Save</span>
				</div>
				<div>&nbsp;</div>
			</div>
			<ul>
				<li onClick="javascript:MenuLib.saveLearningDesign(true)">Save as</li>
				<li id="exportButton">Export<span class="ui-icon ui-menu-icon ui-icon-carat-1-e"></span>
					<ul>
						<li class="exportSequenceButton"
							title="Standard LAMS ZIP format"
							onClick="javascript:MenuLib.exportLearningDesign(1)">Sequence LAMS</li>
						<li class="exportSequenceButton"
							title="IMS Learning Design Level A Format (This format cannot be reimported back into LAMS. Export only!)"
							onClick="javascript:MenuLib.exportLearningDesign(2)">Sequence IMS</li>
						<li class="exportImageButton" onClick="javascript:MenuLib.exportPNG(true)">Image PNG</li>
						<li class="exportImageButton" onClick="javascript:MenuLib.exportSVG(true)">Image SVG</li>
					</ul>
				</li>
			</ul>
		</div>
		<div class="ui-button" onClick="javascript:MenuLib.copyActivity()">
			Copy
		</div>
		<div class="ui-button" onClick="javascript:MenuLib.pasteActivity()">
			Paste
		</div>
		<div class="ui-button" onClick="javascript:MenuLib.addTransition()">
			Transition
		</div>
		<div>
			<div class="split-ui-button">
				<div>
					<span>Optional</span>
				</div>
				<div>&nbsp;</div>
			</div>
			<ul>
				<li onClick="javascript:MenuLib.addOptionalActivity()">Activity</li>
				<li id="floatingActivityButton" onClick="javascript:MenuLib.addFloatingActivity()">Support</li>
			</ul>
		</div>
		<div>
			<div class="split-ui-button">
				<div>
					<span>Flow</span>
				</div>
				<div>&nbsp;</div>
			</div>
			<ul>
				<li onClick="javascript:MenuLib.addGate()">Gate</li>
				<li onClick="javascript:MenuLib.addBranching()">Branch</li>
			</ul>
		</div>
		<div class="ui-button" onClick="javascript:MenuLib.addGrouping()">
			Group
		</div>
		<div>
			<div class="split-ui-button">
				<div>
					<span>Annotate</span>
				</div>
				<div>&nbsp;</div>
			</div>
			<ul>
				<li onClick="javascript:MenuLib.addAnnotationLabel()">Label</li>
				<li onClick="javascript:MenuLib.addAnnotationRegion()">Region</li>
			</ul>
		</div>
		<div class="ui-button" onClick="javascript:GeneralLib.arrangeActivities()">
			Arrange
		</div>
		<div id="previewButton" class="ui-button" onClick="javascript:MenuLib.openPreview()">
			Preview
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
				<div id="templateContainer">
					<c:forEach var="tool" items="${tools}">
						<div
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
				<div id="ldDescriptionDiv"
					 title="Click to show the sequence description">
					<div id="ldDescriptionTitleContainer" onClick="javascript:MenuLib.toggleDescriptionDiv()">
						<span id="ldDescriptionFieldTitle">Untitled</span>
						<span id="ldDescriptionFieldModified"></span>
						<span id="ldDescriptionHideTip">▼</span>
					</div>
					<div id="ldDescriptionDetails">
						<div id="ldDescriptionLabelDescription">Description:
						
						</div>
						<div id="ldDescriptionEditorContainer">
							<lams:CKEditor id="ldDescriptionFieldDescription" value=""
								   height="300px"
								   contentFolderID="${contentFolderID}"/>
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
			    	</div>
				</td>
			</tr>
			<tr>
				<td id="ldStoreDialogAccessCell" >
					<div id="ldStoreDialogAccessTitle">Recently used sequences</div>
				</td>
			</tr>
		</table>
		<%-- This will be moved to dialog's button pane --%>
		<div id="ldStoreDialogNameContainer" class="ldStoreDialogSaveOnly">
			<span>Title:</span><input id="ldStoreDialogNameField" type="text"/>
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
					Mappings
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
					Title:
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Default?
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
					Title:
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Grouping:
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
					Title:
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Grouping type:
				</td>
				<td>
					 <select class="propertiesContentFieldGroupingType">
					 	<option value="random">Random</option>
					 	<option value="monitor">Choose in Monitor</option>
					 	<option value="learner">Learner's choice</option>
					 </select>
				</td>
			</tr>
			<tr>
				<td>
					Number of groups:
				</td>
				<td>
					 <input class="propertiesContentFieldGroupCountEnable" type="radio"
					 		name="propertiesContentFieldGroupDivide"></input>
					 <input class="propertiesContentFieldGroupCount spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Number of learners:
				</td>
				<td>
					<input class="propertiesContentFieldLearnerCountEnable" type="radio"
					 		name="propertiesContentFieldGroupDivide"></input>
					 <input class="propertiesContentFieldLearnerCount spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Equal group sizes?
				</td>
				<td>
					 <input class="propertiesContentFieldEqualSizes" type="checkbox"></input>
				</td>
			</tr>
			<tr>
				<td>
					View learners before selection?
				</td>
				<td>
					 <input class="propertiesContentFieldViewLearners" type="checkbox"></input>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="propertiesContentFieldNameGroups">Name Groups</div>
				</td>
			</tr>
		</table>
	</div>


	<div id="propertiesContentGate" class="dialogContainer">
		<table>
			<tr>
				<td>
					Title:
				</td>
				<td colspan="3">
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Description:
				</td>
				<td colspan="3">
					 <textarea class="propertiesContentFieldDescription"></textarea>
				</td>
			</tr>
			<tr>
				<td>
					Type:
				</td>
				<td colspan="3">
					 <select class="propertiesContentFieldGateType">
					 	<option value="condition">Condition</option>
					 	<option value="sync">Synchronise</option>
					 	<option value="schedule">Schedule</option>
					 	<option value="permission">Permission</option>
					 </select>
				</td>
			</tr>
			<tr>
				<td>
					Input (Tool):
				</td>
				<td>
					  <select class="propertiesContentFieldInput"></select>
				</td>
			</tr>
			<tr class="propertiesContentRowGateSchedule">
				<td>
					Delay:
				</td>
				<td>
					<input class="propertiesContentFieldOffsetDay spinner" type="text"></input> days
				</td>
				<td>
					<input class="propertiesContentFieldOffsetHour spinner" type="text"></input> hours
				</td>
				<td>
					<input class="propertiesContentFieldOffsetMinute spinner" type="text"></input> minutes
				</td>
			</tr>
			<tr class="propertiesContentRowGateSchedule">
				<td colspan="3">
					Since learner finished previous activity?
				</td>
				<td>
					 <input class="propertiesContentFieldActivityCompletionBased" type="checkbox"></input>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="3">
					<div class="propertiesContentFieldCreateConditions">Create conditions</div>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="3">
					<div class="propertiesContentFieldMatchConditions">Map gate conditions</div>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentBranching" class="dialogContainer">
		<table>
			<tr>
				<td>
					Title:
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Branching type:
				</td>
				<td>
					 <select class="propertiesContentFieldBranchingType">
					 	<option value="chosen">Instructor's choice</option>
					 	<option value="group">Group-based</option>
					 	<option value="tool">Learner's output</option>
					 	<option value="optional">Learner's choice</option>
					 </select>
				</td>
			</tr>
			<tr>
				<td>
					Grouping:
				</td>
				<td>
					  <select class="propertiesContentFieldGrouping"></select>
				</td>
			</tr>
			<tr>
				<td>
					Input (Tool):
				</td>
				<td>
					  <select class="propertiesContentFieldInput"></select>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="2">
					<div class="propertiesContentFieldCreateConditions">Create conditions</div>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="2">
					<div class="propertiesContentFieldMatchConditions">Match conditions to branches</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="propertiesContentFieldMatchGroups">Match Groups to Branches</div>
				</td>
			</tr>
			<tr>
				<td>
					Min sequences:
				</td>
				<td>
					<input class="propertiesContentFieldOptionalSequenceMin spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Max sequences:
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
					Title:
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Grouping:
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
					Title:
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Min activities:
				</td>
				<td>
					<input class="propertiesContentFieldOptionalActivityMin spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Max activities:
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
					Title:
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Color:
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
					Title:
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
		 	<option value="none">[ Choose Output ]</option>
		 </select>
		 <select id="rangeOptionSelect" class="outputSelectDependent">
		 	<option value="none" selected="selected">[ Options ]</option>
		 	<option value="greater">Greater than or equal to</option>
		 	<option value="less">Less than or equal to</option>
		 	<option value="range">Range</option>
		 </select>
		 <div id="rangeAddDiv" class="outputSelectDependent">
			 <input id="singleRangeSpinner" class="outputSelectDependent spinner" />
			 <div id="multiRangeDiv" class="outputSelectDependent">
			 	From: <input id="multiRangeFromSpinner" class="spinner" /> To: <input id="multiRangeToSpinner" class="spinner" />
			 </div>
			 <span id="rangeAddButton">Add</span>
		 </div>
		 <div id="complexConditions" class="outputSelectDependent">
		 	<div>Name:</div>
		 	<ul></ul>
		 </div>
		 <table id="rangeConditions" class="outputSelectDependent">
		 	<tr id="rangeConditionsHeaderRow">
		 		<th>Name</th>
		 		<th>Condition</th>
		 	</tr>
		 </table>
	</div>
	
	
	<!-- EXPORT CANVAS AS IMAGE DIALOG -->
	<div id="exportImageDialog" class="dialogContainer exportDialog">
		<a href="#">Click here to download the image.</a>
	</div>
	
	
	<!-- EXPORT LEARNING DESIGN DIALOG -->
	<div id="exportLDDialog" class="dialogContainer exportDialog">
		<span>Please wait for the download.<br />Close the dialog when the download is finished.</span>
		<iframe></iframe>
	</div>
</body>
</lams:html>