<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<lams:html>
<lams:head>
	<lams:css />
	<link href="<c:url value="/"/>css/main.css" rel="stylesheet" type="text/css" />
	<link href="<lams:LAMSURL/>css/progressBar.css" rel="stylesheet" type="text/css" />

	<title><fmt:message key="learner.title" /></title>

	<c:if test="${not empty notifyCloseURL}">
		<c:set var="notifyCloseURL" value="${notifyCloseURL}" scope="request" />
	</c:if>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.layout.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>includes/javascript/main.js"></script>
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',
			APP_URL = LAMS_URL + 'learning/',
			
			LABELS = {
				<fmt:message key="label.learner.progress.activity.current.tooltip" var="CURRENT_ACTIVITY_VAR"/>
				CURRENT_ACTIVITY : '<c:out value="${CURRENT_ACTIVITY_VAR}" />',
				<fmt:message key="label.learner.progress.activity.completed.tooltip" var="COMPLETED_ACTIVITY_VAR"/>
				COMPLETED_ACTIVITY : '<c:out value="${COMPLETED_ACTIVITY_VAR}" />',
				<fmt:message key="label.learner.progress.activity.attempted.tooltip" var="ATTEMPTED_ACTIVITY_VAR"/>
				ATTEMPTED_ACTIVITY : '<c:out value="${ATTEMPTED_ACTIVITY_VAR}" />',
				<fmt:message key="label.learner.progress.activity.tostart.tooltip" var="TOSTART_ACTIVITY_VAR"/>
				TOSTART_ACTIVITY : '<c:out value="${TOSTART_ACTIVITY_VAR}" />',
				<fmt:message key="label.learner.progress.activity.support.tooltip" var="SUPPORT_ACTIVITY_VAR"/>
				SUPPORT_ACTIVITY : '<c:out value="${SUPPORT_ACTIVITY_VAR}" />'
			},
			
			parentURL = "${notifyCloseURL}",
			lessonId = '${lessonID}',
			progressPanelEnabled = '<lams:Configuration key="LearnerCollapsProgressPanel" />' != 'false',
			
			// settings for progress bar
			presenceEnabled = '${presenceEnabledPatch}' != 'false',
			isHorizontalBar = false,
			hasContentFrame = true,
			hasDialog = false,
			
			bars = {
				'learnerMainBar' : {
					'containerId' : 'progressBarDiv'
				}
			};
		
		$(document).ready(function() {
			window.onresize = resizeElements;
			
			// show if panel is not disabled in LAMS Configuration
			if (progressPanelEnabled) {
				$('body').layout({
					west : {
						applyDefaultStyles : true,
						initClosed : false,
						resizable : false,
						slidable : false,
						size : 160,
						spacing_open : 10,
						spacing_closed : 10,
						<fmt:message key="label.learner.progress.open" var="togglerContent_open_VAR"/>
						togglerContent_open : '<c:out value="${togglerContent_open_VAR}" />',
						<fmt:message key="label.learner.progress.closed" var="togglerContent_closed_VAR"/>
						togglerContent_closed : '<c:out value="${togglerContent_closed_VAR}" />',
						togglerLength_open : 80,
						togglerLength_closed : 130,
						<fmt:message key="label.learner.progress.open.tooltip" var="togglerTip_open_VAR"/>
						togglerTip_open : '<c:out value="${togglerTip_open_VAR}" />',
						<fmt:message key="label.learner.progress.closed.tooltip" var="togglerTip_closed_VAR"/>
						togglerTip_closed : '<c:out value="${togglerTip_closed_VAR}" />',
						onopen_start : function() {$('#controlFrame').css('visibility','visible');}
					}
				});
				
				$('#notebookForm').ajaxForm({
				    'clearForm' : true,
				    'beforeSubmit' : validateNotebookForm
				});
			}
		});
	</script>
	<!-- Some settings need to be done in the script first and only then this file can be included -->
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/progressBar.js"></script>
</lams:head>

<body class="stripes">
	<div id="controlFrame" class="ui-layout-west">
		<table id="logoTable" class="progressStaticHeight" cellspacing="0">
			<tr>
				<td>
					<img id="logoImage" src="<lams:LAMSURL />images/css/lams_logo.gif" />
				</td>
				<td id="exitButtonCell">
					<input name="exitButton" onClick="javascript:closeWindow()" type="button" class="button progressButton" value='<fmt:message key="button.exit" />' />
				</td>
			</tr>
		</table>
		
		<div id="lessonTitleRow" class="separatorRow progressStaticHeight"><c:out value="${title}" /></div>
		
		<div id="progressBarDiv" class="progressBarContainer"></div>
		
		<div id="supportSeparatorRow" class="separatorRow progressStaticHeight"
		     onClick="javascript:toggleBarPart('support')">
			<span><fmt:message key="label.learner.progress.support" /></span>
			<span id="supportTogglerCell" class="togglerCell" />▼</span>
		</div>
		<div id="supportPart" class="progressStaticHeight"></div>
		
		<div id="notebookSeparatorRow" class="separatorRow progressStaticHeight" 
		     onClick="javascript:toggleBarPart('notebook')">
			<span><fmt:message key="label.learner.progress.notebook" /></span>
			<span id="notebookTogglerCell" class="togglerCell"/>▲</span>
		</div>
		<form id="notebookForm" action="<lams:WebAppURL />notebook.do?method=processNewEntry" method="post">
			<input type="hidden" name="lessonID" value="${lessonID}" />
			<input type="hidden" name="signature" value="SCRATCHPAD" />
			<input type="hidden" name="skipViewAll" value="true" />
			
			<table id="notebookPart" class="progressStaticHeight">
				<tr>
					<td id="notebookTitleCell">
						<fmt:message key="mynotes.entry.title.label" />
					</td>
					<td>
						<input name="title" class="notebookInput" type="text" />
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<textarea id="entry" name="entry" rows="4" class="notebookInput"></textarea>
					</td>
				</tr>
				<tr>
					<td id="viewAllButtonCell">
						<input type="button" onClick="javascript:viewNotebookEntries()" 
						       class="button progressButton" value='<fmt:message key="mynotes.view.all.button" />' />
					</td>
					<td id="saveButtonCell">
						<input type="submit" class="button progressButton" value='<fmt:message key="button.save" />' />
					</td>
				</tr>
			</table>
		</form>
	</div>

	<c:if test="${presenceEnabledPatch}">
		<%@ include file="presenceChat.jsp"%>
	</c:if>

	<iframe id="contentFrame" name="contentFrame" onload="javascript:fillProgressBar('learnerMainBar')" class="ui-layout-center"
		src="<c:url value="/"/>content.do?lessonID=<c:out value="${lessonID}"/>" allowfullscreen> </iframe>

	<div id="tooltip"></div>
</body>
</lams:html>