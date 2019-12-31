<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	
	<c:set var="lams"> <lams:LAMSURL /> </c:set>
	<c:set var="tool"> <lams:WebAppURL /> </c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
		<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>

		<link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>css/jquery.minicolors.css"></link>
		<link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link>
		<link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link>
		
		<script src="<lams:LAMSURL/>includes/javascript/jquery.minicolors.min.js"></script>
		<script src="<lams:LAMSURL/>includes/javascript/fullscreen.js"></script>
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
	
		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar" formID="authoringForm">
			<form:form action="updateContent.do" id="authoringForm" modelAttribute="authoringForm" method="post" enctype="multipart/form-data">
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
				
				<form:hidden path="currentTab" id="currentTab" />
				<form:hidden path="sessionMapID" />
				<form:hidden path="mindmapContent" id="mindmapContent" />
				
				<c:set var="title"><fmt:message key="activity.title" /></c:set>
				<lams:Tabs control="true" title="${title}" helpToolSignature="lamind10" helpModule="authoring">
					<lams:Tab id="1" key="button.basic" />
					<lams:Tab id="2" key="button.advanced" />
				</lams:Tabs>
				
				<lams:TabBodyArea>
					<lams:errors/>
					 		
					<!--  Set up tabs  -->
					<lams:TabBodys>
			   			<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
			 			<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
			  		</lams:TabBodys>
					
					<!-- Button Row -->
					<div id="saveCancelButtons">
						<lams:AuthoringButton formID="authoringForm"
							clearSessionActionUrl="/clearsession.do" toolSignature="lamind10"
							cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
							toolContentID="${sessionMap.toolContentID}"
							accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode=='teacher'}"
							contentFolderID="${sessionMap.contentFolderID}" />
					</div>
				</lams:TabBodyArea>
			</form:form>

		</lams:Page>
	</body>
</lams:html>
