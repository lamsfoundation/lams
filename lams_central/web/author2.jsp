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
	
	<div id="openLearningDesignDialog" class="dialogContainer">
		<div class="dialogTitle">Open sequence</div>
		<table>
			<tr>
				<td id="learningDesignTreeCell">
					<div id="learningDesignTree"></div>
				</td>
				<td id="canvasCell" rowspan="2">
					<div id="canvasDiv">
			    		<img id="ldScreenshotAuthor" class="ldChoiceDependentCanvasElement" />
		    			<img id="ldScreenshotLoading" class="ldChoiceDependentCanvasElement" src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
			    	</div>
				</td>
			</tr>
			<tr>
				<td id="recentlyUsedCell" >
					Recently used sequences
				</td>
			</tr>
		</table>
	</div>
	
	<div id="infoDialog" class="dialogContainer">
	</div>
</body>
</lams:html>