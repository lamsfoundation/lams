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
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',
			LD_THUMBNAIL_URL_BASE = LAMS_URL + 'home.do?method=createLearningDesignThumbnail&ldId=',
			initContentFolderID = '${contentFolderID}';
	</script>
</lams:head>
<body>
	<div id="toolbar" class="ui-widget-header ui-corner-all">
		<div class="ui-button" onClick="javascript:MenuLib.newLearningDesign(false, false)">
			New
		</div>
		<div class="ui-button" onClick="javascript:MenuLib.openLearningDesign()">
			Open
		</div>
		<div>
			<div class="split-ui-button">
				<div onClick="javascript:MenuLib.saveLearningDesign()">
					<span>Save</span>
				</div>
				<div>&nbsp;</div>
			</div>
			<ul>
				<li onClick="javascript:MenuLib.saveLearningDesign(true)">Save as...</li>
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
		<!-- 
		<div id="zoomButton" class="ui-button" onClick="javascript:MenuLib.zoom()">
			Zoom out
		</div>
 		-->
	</div>
	
	<div id="tabs" class="tabs-bottom">
		<ul>
			<li><a href="#tabCanvas">Canvas</a></li>
			<li><a href="#tabDescription">Description</a></li>
		</ul>
		<div class="tabs-spacer"></div>
		
		<div id="tabCanvas">
			<table id="authoringTable">
				<tr>
					<td id="templateContainerCell">
						<div id="templateContainer">
							<c:forEach var="tool" items="${tools}">
								<div learningLibraryId="${tool.learningLibraryId}"
									 supportsOutputs="${tool.supportsOutputs}"
									 activityCategoryId="${tool.activityCategoryID}"
									 class="template">
									<c:if test="${not empty tool.iconPath}">
										<img src="<lams:LAMSURL/>${tool.iconPath}" />
									</c:if>
									<div><c:out value="${tool.toolDisplayName}" /></div>
								</div>
							</c:forEach>
						</div>
					</td>
					<td>
						<div id="canvas"></div>
					</td>
				</tr>
			</table>
		</div>
		
		<div id="tabDescription">
			<table>
				<tr>
					<td class="ldDescriptionLabel ldDescriptionCell">Title</td>
					<td id="ldDescriptionFieldTitle" class="ldDescriptionCell"></td>
				</tr>
				<tr>
					<td class="ldDescriptionLabel ldDescriptionCell">Description</td>
					<td class="ldDescriptionCell">
						<lams:CKEditor id="ldDescriptionFieldDescription" value=""
									   height="300px"
									   contentFolderID="${contentFolderID}"/>
					</td>
				</tr>
			</table>
		</div>
		
	</div>

	
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
				<td id="ldStoreDialogRecentlyUsedCell" >
					Recently used sequences
				</td>
			</tr>
		</table>
		<%-- This will be moved to dialog's button pane --%>
		<div id="ldStoreDialogNameContainer" class="ldStoreDialogSaveOnly">
			<span>Title:</span><input id="ldStoreDialogNameField" type="text" />
		</div>
	</div>
	
	
	<!-- GROUP TO BRANCH MATCHING DIALOG -->
	
	<div id="gtbDialog" class="dialogContainer">
		<table>
			<tr>
				<td></td>
				<td></td>
				<td rowspan="2"></td>
				<td colspan="2" class="gtbLabelCell">
					Mappings
				</td>
			</tr>
			<tr>
				<td class="gtbLabelCell">Groups</td>
				<td class="gtbLabelCell">Branches</td>
				<td class="gtbLabelCell">Group</td>
				<td class="gtbLabelCell">Branch</td>
			</tr>
			<tr>
				<td id="gtbGroupsCell" class="gtbListCell"></td>
				<td id="gtbBranchesCell" class="gtbListCell"></td>
				<td id="gtbButtonCell">
					<div id="gtbAddButton"
					     onClick="javascript:PropertyLib.addGroupToBranchMapping()"></div>
					<div id="gtbRemoveButton"
					     onClick="javascript:PropertyLib.removeGroupToBranchMapping()"></div>
				</td>
				<td id="gtbMappingGroupCell" class="gtbListCell"></td>
				<td id="gtbMappingBranchCell" class="gtbListCell"></td>
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
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					Type:
				</td>
				<td>
					 <select class="propertiesContentFieldGateType">
					 	<option value="condition">Condition</option>
					 	<option value="sync">Synchronise</option>
					 	<option value="schedule">Schedule</option>
					 	<option value="permission">Permission</option>
					 </select>
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
			<tr>
				<td colspan="2">
					<div class="propertiesContentFieldMatchGroups">Match Groups to Branches</div>
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
</body>
</lams:html>