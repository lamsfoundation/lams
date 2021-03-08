<!DOCTYPE html>
        
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.qa.QaAppConstants"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>

<lams:html>		
	<lams:head>
	<title><fmt:message key="activity.title" /></title>
	
	<lams:css/>
	<link href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css" />
	<link href="${lams}css/thickbox.css" rel="stylesheet" type="text/css" media="screen">

	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
	<script type="text/JavaScript">
		function submitMethod(actionMethod) {
			var form = document.forms.authoringForm;
			form.action=actionMethod+".do"; 
			form.submit();
		}

		function doSelectTab(tabId) {
			selectTab(tabId);
		}

        function validateQuestionsNotEmpty() {
            //remind teacher he forgot to add questions
            var questionsCount = $("#itemList tr").length;
			if (questionsCount == 0) {
				alert("<fmt:message key="label.no.added.questions"/>");
				return false;
			}
			return true;
        }
	</script>
</lams:head>

<body class="stripes">
<form:form action="submitAllContent.do" modelAttribute="authoringForm" method="POST" id="authoringForm"
	onsubmit="return validateQuestionsNotEmpty();">
	<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	
	<form:hidden path="mode" value="${mode}" />
	<form:hidden path="qa.qaContentId" />
	<form:hidden path="toolContentID" />
	<form:hidden path="contentFolderID" />
	<form:hidden path="sessionMapID"/>
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	
	<lams:Page title="${title}" type="navbar">
		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= QaAppConstants.MY_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="label.basic" />
			<lams:Tab id="2" key="label.advanced" />
			<lams:Tab id="3" key="label.conditions" />
		</lams:Tabs>
	
		<lams:TabBodyArea>
			<lams:errors/>
					
			<lams:TabBodys>
				<lams:TabBody id="1" page="BasicContent.jsp"/>
				<lams:TabBody id="2" page="AdvancedContent.jsp" />
				<lams:TabBody id="3" page="conditions.jsp" />
		    </lams:TabBodys>
		    
			<lams:AuthoringButton formID="authoringForm"
				clearSessionActionUrl="/clearsession.do"
				toolSignature="<%=QaAppConstants.MY_SIGNATURE%>"
				accessMode="${mode}"
				defineLater="${mode == 'teacher'}"
				cancelButtonLabelKey="label.cancel"
				saveButtonLabelKey="label.save"
				toolContentID="${authoringForm.toolContentID}"
				contentFolderID="${authoringForm.contentFolderID}" />
		</lams:TabBodyArea>
		
		<div id="footer"></div>
		
	</lams:Page>
</form:form>
</body>
</lams:html>
