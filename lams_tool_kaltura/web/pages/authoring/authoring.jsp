<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.kaltura.util.KalturaConstants"%>
<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
		<c:set var="lams">
			<lams:LAMSURL />
		</c:set>
		<c:set var="tool">
			<lams:WebAppURL />
		</c:set>
		<link href="${lams}css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
		
		<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
	</lams:head>
	<body class="stripes" onload="init();">

		<lams:Page title='<fmt:message key="pageTitle.authoring" />' type="navbar">
			<form:form action="/lams/tool/lakalt11/authoring/updateContent.do" modelAttribute="authoringForm" id="authoringForm" method="post" enctype="multipart/form-data">
				<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
				
				<form:hidden path="currentTab" id="currentTab" />
				<form:hidden path="sessionMapID" />
				
				<c:set var="title"><fmt:message key="activity.title" /></c:set>
				<lams:Tabs control="true" title="${title}" helpToolSignature="<%= KalturaConstants.TOOL_SIGNATURE %>" helpModule="authoring">
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
							clearSessionActionUrl="/clearsession.do" toolSignature="<%=KalturaConstants.TOOL_SIGNATURE%>"
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
