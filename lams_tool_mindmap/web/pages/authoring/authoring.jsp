<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.mindmap.util.MindmapConstants"%>

<lams:html>

	<c:set var="lams"> <lams:LAMSURL /> </c:set>
	<c:set var="tool"> <lams:WebAppURL /> </c:set>
	
	<lams:head>
		<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
		
		<link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>css/jquery.minicolors.css"></link>
			<link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link>
			<link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link>
			
			<script src="<lams:LAMSURL/>includes/javascript/jquery.minicolors.min.js"></script>
			<script src="${tool}includes/javascript/jquery.timer.js"></script>
			<script src="${tool}includes/javascript/mapjs/main.js"></script>
			<script src="${tool}includes/javascript/mapjs/underscore-min.js"></script>
			
			<script type="text/javascript">
			
				var mode = "author";
			
				function setMindmapContent() {
					var mindmapContent = document.getElementById("mindmapContent");
					mindmapContent.value = JSON.stringify(contentAggregate);
				}
			
				// set Mindmap content before submitting authoring form
				$(document).ready(function() {
					// selects "save" button in lams:AuthoringButton tag
					$('a[href*="doSubmit_Form_Only()"]').click(setMindmapContent);
				});	
			</script>
	</lams:head>

	<body class="stripes" onload="init();">
	
		<title><fmt:message key="activity.title" /></title>
		<lams:Page title="${activity.title}" type="navbar" formID="authoringForm">
			<form:form action="updateContent.do" modelAttribute="authoringForm" id="authoringForm" method="post" enctype="multipart/form-data">
				
				<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
				<form:hidden path="currentTab" id="currentTab" />
				<form:hidden path="sessionMapID" />
				<form:hidden path="mindmapContent" id="mindmapContent" />
				
				<c:set var="title"><fmt:message key="activity.title" /></c:set>
				<lams:Tabs control="true" title="${title}" helpToolSignature="<%= MindmapConstants.TOOL_SIGNATURE %>" helpModule="authoring">
					<lams:Tab id="1" key="button.basic" />
					<lams:Tab id="2" key="button.advanced" />
				</lams:Tabs>
				
				<lams:TabBodyArea>
					<%@ include file="/common/messages.jsp"%>
					 		
					<!--  Set up tabs  -->
					<lams:TabBodys>
			   			<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
			 			<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
			  		</lams:TabBodys>
					
					<!-- Button Row -->
					<div id="saveCancelButtons">
						<lams:AuthoringButton formID="authoringForm"
							clearSessionActionUrl="/clearsession.do" toolSignature="<%=MindmapConstants.TOOL_SIGNATURE%>"
							cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
							toolContentID="${sessionMap.toolContentID}"
							accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode=='teacher'}"
							contentFolderID="${sessionMap.contentFolderID}" />
					</div>
				</lams:TabBodyArea>
			</form:form>
			
			<div id="footer"></div>
		</lams:Page>
	</body>
</lams:html>

