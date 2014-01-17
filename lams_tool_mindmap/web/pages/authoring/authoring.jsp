<%@ include file="/common/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.tool.mindmap.util.MindmapConstants"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
<script type="text/javascript" src="includes/javascript/mindmap.resize.js"></script>

<script type="text/javascript">
//<![CDATA[  
	flashvars = { xml: "${mindmapContentPath}", user: "${currentMindmapUser}", dictionary: "${localizationPath}" }
	
	$(window).resize(makeNice);
	
	embedFlashObject(700, 525);
	
	function setMindmapContent()
	{
		var mindmapContent = document.getElementById("mindmapContent");
		var tag = document.getElementById("currentTab");
		
		if (tag.value == 1 || tag.value == "") {
			mindmapContent.value = document['flashContent'].getMindmap();
		}
	}
	
	function flashLoaded()
	{
		var mindmapContent = document.getElementById("mindmapContent");
		document['flashContent'].setMindmap(mindmapContent.value);
	}
	
	function embedFlashObject(x, y)
	{
		swfobject.embedSWF("${mindmapType}", "flashContent", x, y, "9.0.0", false, flashvars);
	}

	// set Mindmap content before submitting authoring form
	$(document).ready(function(){
		// selects "save" button in lams:AuthoringButton tag
		$('a[href*="doSubmit_Form_Only()"]').click(setMindmapContent);
	});	
//]]>
</script>

<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">
	
	<c:set var="formBean"
		value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	
	<c:set var="defineLater" value="no" />
	<c:if test="${sessionMap.mode == 'teacher'}">
		<c:set var="defineLater" value="yes" />
	</c:if>
	
	<div id="header">
	
		<!--  TITLE KEY PAGE GOES HERE -->
		<lams:Tabs control="true">
			<lams:Tab id="1" key="button.basic" />
			<c:if test="${sessionMap.mode == 'author'}">
				<lams:Tab id="2" key="button.advanced" />
			</c:if>
		</lams:Tabs>
	
	</div>
	<!--closes header-->
	
	<div id="content">
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="dispatch" value="updateContent" />
		<html:hidden property="sessionMapID" />
		<html:hidden property="mindmapContent" styleId="mindmapContent" />
		
		<div id="message" style="text-align: center;">
			<logic:messagesPresent>
				<p class="warning">
				        <html:messages id="error">
				            <c:out value="${error}" escapeXml="false"/><br/>
				        </html:messages>
				</p>
			</logic:messagesPresent>
		</div>

		<lams:help toolSignature="<%=MindmapConstants.TOOL_SIGNATURE%>" module="authoring" />

		<%-- Page tabs --%>
		<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
		<c:if test="${sessionMap.mode == 'author'}">
			<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
		</c:if>
		
		<lams:AuthoringButton formID="authoringForm"
			clearSessionActionUrl="/clearsession.do" toolSignature="lamind10"
			cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
			toolContentID="${sessionMap.toolContentID}"
			accessMode="${sessionMap.mode}" defineLater="${defineLater}"
			customiseSessionID="${sessionMap.sessionID}" 
			contentFolderID="${sessionMap.contentFolderID}" />
	</div>
</html:form>

<div id="footer"></div>

