<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.mindmap.util.MindmapConstants"%>

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

<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	
	<html:hidden property="currentTab" styleId="currentTab" />
	<html:hidden property="dispatch" value="updateContent" />
	<html:hidden property="sessionMapID" />
	<html:hidden property="mindmapContent" styleId="mindmapContent" />
	
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
</html:form>

<div id="footer"></div>
