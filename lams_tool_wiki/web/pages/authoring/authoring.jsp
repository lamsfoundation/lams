<%@ include file="/common/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.tool.wiki.util.WikiConstants"%>
	
<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />

	<html:hidden property="currentTab" styleId="currentTab" />
	<html:hidden property="dispatch" styleId="dispatch" value="updateContent" />
	<html:hidden property="sessionMapID" />
	<html:hidden property="contentFolderID"/>
	<html:hidden property="toolContentID"/>
	<html:hidden property="mode" value="${sessionMap.mode}"/>
	<html:hidden property="currentWikiPage" value="${currentWikiPage.uid}" styleId="currentWikiPage" />
	<html:hidden property="newPageName" styleId="newPageName"/>
	<html:hidden property="historyPageContentId" styleId="historyPageContentId"/>
		
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">

		 <lams:Tabs control="true" title="${title}" helpToolSignature="<%= WikiConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="button.basic" />
			<lams:Tab id="2" key="button.advanced" />
		</lams:Tabs>

		<c:if test="${currentWikiPage.deleted}">
			<lams:Alert type="danger" id="wikiRemoved" close="false">	
				<fmt:message key="label.wiki.removed" />
			</lams:Alert>
		</c:if>
	
	   <lams:TabBodyArea>
			<logic:messagesPresent>
				<lams:Alert type="danger" id="authorErrors" close="false">	
			        <html:messages id="error">
			            <c:out value="${error}" escapeXml="false"/><br/>
			        </html:messages>
				</lams:Alert>
			</logic:messagesPresent>			
		
			<%-- Page tabs --%>
			<lams:TabBodys>
				<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
				<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
			</lams:TabBodys>
				
			<div id="finishButtonDiv">
				<lams:AuthoringButton formID="authoringForm"
					clearSessionActionUrl="/clearsession.do" toolSignature="lawiki10"
					cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
					toolContentID="${sessionMap.toolContentID}"
					accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode == 'teacher'}"
					customiseSessionID="${sessionMap.sessionID}" 
					contentFolderID="${sessionMap.contentFolderID}" />
			</div>

		</lams:TabBodyArea>
			
	<div id="footer"></div>

	</lams:Page>
</html:form>

