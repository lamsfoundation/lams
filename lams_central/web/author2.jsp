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
					
			initContentFolderID = '${contentFolderID}',
			initAccess = ${access};
	</script>
</lams:head>
<body onresize="javascript:resizePaper()">
	<div id="toolbar" class="ui-widget-header ui-corner-all">
		<div class="ui-button" onClick="javascript:MenuLib.newLearningDesign(false, false)">
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
				<li id="exportButton" onClick="javascript:MenuLib.exportLearningDesign()">Export</li>
				<li onClick="javascript:MenuLib.convertToPNG()">Image PNG</li>
				<li onClick="javascript:MenuLib.convertToSVG()">Image SVG</li>
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
		<div class="ui-button" onClick="javascript:MenuLib.arrangeActivities()">
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
	<div id="exportImageDialog" class="dialogContainer">
		<div class="dialogTitle">Right-click on the image and save it</div>
		<div id="exportCanvas"></div>
	</div>
</body>
</lams:html>