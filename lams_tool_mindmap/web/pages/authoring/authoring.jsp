<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.mindmap.util.MindmapConstants"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
<script type="text/javascript" src="includes/javascript/mindmap.resize.js"></script>

<script type="text/javascript">
//<![CDATA[  
           
    <c:set var="MindmapUser">
		<c:out value="${currentMindmapUser}" escapeXml="true"/>
	</c:set>
	
	flashvars = { xml: "${mindmapContentPath}", user: "${MindmapUser}", dictionary: "${localizationPath}" }
	
	$(window).resize(makeNice);
	
	embedFlashObject(700, 525);
	
	function setMindmapContent() {
		var mindmapContent = document.getElementById("mindmapContent");
		var tag = document.getElementById("currentTab");
		
		if (tag.value == 1 || tag.value == "") {
			mindmapContent.value = document['flashContent'].getMindmap();
		}
	}
	
	function flashLoaded() {
		var mindmapContent = document.getElementById("mindmapContent");
		document['flashContent'].setMindmap(mindmapContent.value);
	}
	
	function embedFlashObject(x, y) {
		swfobject.embedSWF("${mindmapType}", "flashContent", x, y, "9.0.0", false, flashvars);
	}

	// set Mindmap content before submitting authoring form
	$(document).ready(function() {
		var hasFlash = ((typeof navigator.plugins != "undefined" && typeof navigator.plugins["Shockwave Flash"] == "object") || (window.ActiveXObject && (new ActiveXObject("ShockwaveFlash.ShockwaveFlash")) != false));
		if(hasFlash != true){
			 $("#saveButton").hide();
			}
		// selects "save" button in lams:AuthoringButton tag
		$('a[href*="doSubmit_Form_Only()"]').click(setMindmapContent);
	});	
//]]>
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
