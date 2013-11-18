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
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.ui.touch-punch.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/yahoo-dom-event.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/animation-min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/json-min.js" ></script> 
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/treeview-min.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/raphael/raphael.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringGeneral.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringActivity.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringHandler.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringMenu.js"></script>
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>';
		var LD_THUMBNAIL_URL_BASE = LAMS_URL + 'home.do?method=createLearningDesignThumbnail&ldId=';
		
		var contentFolderID = '${contentFolderID}';
		if (contentFolderID == '') {
			contentFolderID = null;
		}
	</script>
</lams:head>
<body>
	<div id="toolbar" class="ui-widget-header ui-corner-all">
		<div class="ui-button" onClick="javascript:MenuLib.newLearningDesign()">
			New
		</div>
		<div class="ui-button" onClick="javascript:MenuLib.openLearningDesign()">
			Open
		</div>
		<div>
			<div class="split-ui-button">
				<div>
					<span>Flow</span>
				</div>
				<div>&nbsp;</div>
			</div>
			<ul>
				<li><div onClick="javascript:MenuLib.addGate()">Gate</div></li>
				<li><div onClick="javascript:MenuLib.addBranching()">Branch</div></li>
			</ul>
		</div>
		<div class="ui-button" onClick="javascript:MenuLib.addGrouping()">
			Group
		</div>
		<div class="ui-button" onClick="javascript:MenuLib.arrangeActivities()">
			Arrange
		</div>
		<div class="ui-button" onClick="javascript:MenuLib.copyActivity()">
			Copy
		</div>
		<div class="ui-button" onClick="javascript:MenuLib.pasteActivity()">
			Paste
		</div>
	</div>
	<table id="authoringTable">
		<tr>
			<td id="templateContainerCell">
				<div id="templateContainer">
					<c:forEach var="tool" items="${tools}">
						<div toolId="${tool.toolId}" class="template">
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
	
	
	
	<!-- DIALOGS CONTENTS -->
	
	<div id="ldStoreDialog" class="dialogContainer">
		<div class="dialogTitle">Open sequence</div>
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
	</div>
	
	
	<!-- PROPERTY DIALOG CONTENTS FOR DIFFERENT ACTIVITY TYPES -->
	
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
			<tr>
				<td>
					Offline Activity:
				</td>
				<td>
					  <input type="checkbox" class="propertiesContentFieldOffline" /> 
				</td>
			</tr>
			<tr>
				<td>
					Define in Monitor:
				</td>
				<td>
					  <input type="checkbox" class="propertiesContentFieldDefineMonitor" />
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
			<tr class="" style="display: none">
				<td>
					Equal group sizes?
				</td>
				<td>
					 <input class="propertiesContentFieldEqualSizes" type="checkbox"></input>
				</td>
			</tr>
			<tr style="display: none">
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
	
</body>
</lams:html>