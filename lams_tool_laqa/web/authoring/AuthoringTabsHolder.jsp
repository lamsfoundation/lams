<!DOCTYPE html>
        
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.qa.QaAppConstants"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>		
	<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css/>
	<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
	
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	
	<script type="text/JavaScript">
	  function submitMethod(actionMethod) {
		   var form = document.QaAuthoringForm;
		   if (!form.dispatch) {
		    form = form[0];
		   }
		   form.dispatch.value=actionMethod; 
		   form.submit();
		  }
		  
		  function submitModifyAuthoringQuestion(questionIndexValue, actionMethod) {
		   var form = document.QaAuthoringForm;
		   if (!form.questionIndex) {
		    form = form[0];
		   }
		   form.questionIndex.value=questionIndexValue; 
		   submitMethod(actionMethod);
		  }
		  function doSelectTab(tabId) {
			selectTab(tabId);
		  }
	</script>
</lams:head>

<body class="stripes">
<html:form action="/authoring?validate=false" styleId="authoringForm" method="POST" enctype="multipart/form-data">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.httpSessionID]}" />
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	
	<html:hidden property="mode" value="${mode}" />
	<html:hidden property="dispatch" value="submitAllContent" />
	<html:hidden property="toolContentID" />
	<html:hidden property="contentFolderID" />
	<html:hidden property="httpSessionID"/>		
	
<lams:Page title="${title}" type="navbar">
	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= QaAppConstants.MY_SIGNATURE %>" helpModule="authoring">
		<lams:Tab id="1" key="label.basic" />
		<lams:Tab id="2" key="label.advanced" />
		<lams:Tab id="3" key="label.conditions" />
	</lams:Tabs>

	<lams:TabBodyArea>
		<logic:messagesPresent>
			<lams:Alert id="errors" type="danger" close="true">
			        <html:messages id="error">
			            <c:out value="${error}" escapeXml="false"/><br/>
			        </html:messages>
			</lams:Alert>
		</logic:messagesPresent>
		
		<lams:TabBodys>
			<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
			<lams:TabBody id="2" titleKey="label.advanced" page="AdvancedContent.jsp" />
			<lams:TabBody id="3" titleKey="label.conditions" page="conditions.jsp" />
	    </lams:TabBodys>
	    
		<lams:AuthoringButton formID="authoringForm"
			clearSessionActionUrl="/clearsession.do"
			toolSignature="<%=QaAppConstants.MY_SIGNATURE%>"
			accessMode="${mode}"
			defineLater="${mode == 'teacher'}"
			cancelButtonLabelKey="label.cancel"
			saveButtonLabelKey="label.save"
			toolContentID="${formBean.toolContentID}"
			contentFolderID="${formBean.contentFolderID}" />
	</lams:TabBodyArea>
	
	<div id="footer"></div>
	
</lams:Page>
</html:form>
</body>
</lams:html>
