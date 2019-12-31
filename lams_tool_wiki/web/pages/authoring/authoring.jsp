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
		<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

		<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
		<script type="text/javascript" src="${tool}includes/javascript/wikiCommon.js"></script>
		<script type="text/javascript" src="${tool}includes/javascript/validation.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		
		<script type="text/javascript">
			$(document).ready(function() {
				$("#saveButton").attr('href','javascript:doSubmit_Form_Only_Custom();');
				$("time.timeago").timeago();
			});
			
			<%-- Ensure the wikibody has been  any javascript references may trigger Chrome security --%>
			<%-- The wikibody text is updated via the updateContent call --%>
		    function doSubmit_Form_Only_Custom() {
				replaceJavascriptTokenAndSubmit("authoringForm") 
		    }
		</script>

		
	</lams:head>

	<body class="stripes">
		<form:form action="updateContent.do" id="authoringForm" modelAttribute="authoringForm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
		
			<form:hidden path="currentTab" id="currentTab" />
			<form:hidden path="sessionMapID" />
			<form:hidden path="contentFolderID"/>
			<form:hidden path="toolContentID"/>
			<form:hidden path="mode" value="${sessionMap.mode}"/>
			<input type ="hidden" name="currentWikiPage" value="${currentWikiPage.uid}" id="currentWikiPage" />
			<form:hidden path="newPageName" id="newPageName"/>
			<form:hidden path="historyPageContentId" id="historyPageContentId"/>
				
			<c:set var="title"><fmt:message key="activity.title" /></c:set>
			<lams:Page title="${title}" type="navbar" formID="authoringForm">
		
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
			   	 <lams:errors/>
				
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
		</form:form>
	</body>
</lams:html>

