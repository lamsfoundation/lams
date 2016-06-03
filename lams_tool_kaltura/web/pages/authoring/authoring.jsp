<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.kaltura.util.KalturaConstants"%>

<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	<c:set var="defineLater" value="no" />
	<c:if test="${sessionMap.mode == 'teacher'}">
		<c:set var="defineLater" value="yes" />
	</c:if>
	
	<html:hidden property="currentTab" styleId="currentTab" />
	<html:hidden property="dispatch" value="updateContent" />
	<html:hidden property="sessionMapID" />
	
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= KalturaConstants.TOOL_SIGNATURE %>" helpModule="authoring">
		<lams:Tab id="1" key="button.basic" />
		<c:if test="${sessionMap.mode == 'author'}">
			<lams:Tab id="2" key="button.advanced" />
		</c:if>
	</lams:Tabs>
	
	<lams:TabBodyArea>
		<%@ include file="/common/messages.jsp"%>
		 		
		<!--  Set up tabs  -->
		<lams:TabBodys>
   			<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
   			<c:if test="${sessionMap.mode == 'author'}">
 				<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
 			</c:if>
  		</lams:TabBodys>
		
		<!-- Button Row -->
		<div id="saveCancelButtons">
			<lams:AuthoringButton formID="authoringForm"
				clearSessionActionUrl="/clearsession.do" toolSignature="<%=KalturaConstants.TOOL_SIGNATURE%>"
				cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
				toolContentID="${sessionMap.toolContentID}"
				accessMode="${sessionMap.mode}" defineLater="${defineLater}"
				contentFolderID="${sessionMap.contentFolderID}" />
		</div>
	</lams:TabBodyArea>

</html:form>

<div id="footer"></div>

