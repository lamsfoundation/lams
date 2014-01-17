<%@ include file="/common/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.tool.gmap.util.GmapConstants"%>

<script type="text/javascript">
	function onSubmitHandler() {

		//boolean indicating whether we can proceed
    	var save = serialiseMarkers();
    	if (save) {
    		saveMapState();
    	}
    	return save;
	}
</script>

<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data" onsubmit="return onSubmitHandler()">

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
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
		<html:hidden property="markersXML" value="" styleId="markersXML" />
		<html:hidden property="mapZoom" value="" styleId="mapZoom" />
		<html:hidden property="mapCenterLatitude" value="" styleId="mapCenterLatitude" />
		<html:hidden property="mapCenterLongitude" value="" styleId="mapCenterLongitude" />
		<html:hidden property="mapType" value="" styleId="mapType" />

		<div id="message" style="text-align: center;">
			<logic:messagesPresent>
				<p class="warning">
				        <html:messages id="error">
				            <c:out value="${error}" escapeXml="false"/><br/>
				        </html:messages>
				</p>
			</logic:messagesPresent>			
		</div>

		<lams:help toolSignature="<%=GmapConstants.TOOL_SIGNATURE%>" module="authoring" />

		<%-- Page tabs --%>
		<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
		<c:if test="${sessionMap.mode == 'author'}">
			<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
		</c:if>

		<lams:AuthoringButton formID="authoringForm"
			clearSessionActionUrl="/clearsession.do" 
			toolSignature="lagmap10"
			cancelButtonLabelKey="button.cancel" 
			saveButtonLabelKey="button.save"
			toolContentID="${sessionMap.toolContentID}"
			accessMode="${sessionMap.mode}" 
			defineLater="${defineLater}"
			customiseSessionID="${sessionMap.sessionID}" 
			contentFolderID="${sessionMap.contentFolderID}" />
	</div>
</html:form>

<div id="footer"></div>