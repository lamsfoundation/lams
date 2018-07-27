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

	<html:hidden property="currentTab" styleId="currentTab" />
	<html:hidden property="dispatch" value="updateContent" />
	<html:hidden property="sessionMapID" />
	<html:hidden property="markersXML" value="" styleId="markersXML" />
	<html:hidden property="mapZoom" value="" styleId="mapZoom" />
	<html:hidden property="mapCenterLatitude" value="" styleId="mapCenterLatitude" />
	<html:hidden property="mapCenterLongitude" value="" styleId="mapCenterLongitude" />
	<html:hidden property="mapType" value="" styleId="mapType" />

	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= GmapConstants.TOOL_SIGNATURE %>" helpModule="authoring">
		<lams:Tab id="1" key="button.basic" />
		<lams:Tab id="2" key="button.advanced" />
	</lams:Tabs>
	
	<%-- Page tabs --%>
	<lams:TabBodyArea>
		<logic:messagesPresent>
		<lams:Alert id="errorMessages" type="danger" close="false">
		    <html:messages id="error">
		        <c:out value="${error}" escapeXml="false"/><br/>
		    </html:messages>
		 </lams:Alert>
		</logic:messagesPresent>
                
        <lams:TabBodys>
			<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
			<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
		</lams:TabBodys>

		<lams:AuthoringButton formID="authoringForm"
			clearSessionActionUrl="/clearsession.do" 
			toolSignature="lagmap10"
			cancelButtonLabelKey="button.cancel" 
			saveButtonLabelKey="button.save"
			toolContentID="${sessionMap.toolContentID}"
			accessMode="${sessionMap.mode}" 
			defineLater="${sessionMap.mode=='teacher'}"
			customiseSessionID="${sessionMap.sessionID}" 
			contentFolderID="${sessionMap.contentFolderID}" />
	
	</lams:TabBodyArea>
</html:form>
