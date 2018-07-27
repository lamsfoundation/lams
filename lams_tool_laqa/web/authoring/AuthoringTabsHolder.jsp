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
		   var form = document.forms.QaAuthoringForm;
		   if (!form.dispatch) {
		    form = form[0];
		   }
		   form.dispatch.value=actionMethod; 
		   form.submit();
		  }
		  
		  function submitModifyAuthoringQuestion(questionIndexValue, actionMethod) {
		   var form = document.forms.QaAuthoringForm;
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
<form:form action="submitAllContent.do" modelAttribute="authoringForm" method="POST" id="authoringForm">
	<c:set var="sessionMap" value="${sessionScope[authoringForm.httpSessionID]}" />
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	
	<form:hidden path="mode" value="${mode}" />
	<form:hidden path="toolContentID" />
	<form:hidden path="contentFolderID" />
	<form:hidden path="httpSessionID"/>		
	
<lams:Page title="${title}" type="navbar">
	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= QaAppConstants.MY_SIGNATURE %>" helpModule="authoring">
		<lams:Tab id="1" key="label.basic" />
		<lams:Tab id="2" key="label.advanced" />
		<lams:Tab id="3" key="label.conditions" />
	</lams:Tabs>

	<lams:TabBodyArea>
		 <c:set var="errorKey" value="GLOBAL" /> 
		 <c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
		     <lams:Alert id="error" type="danger" close="false"> 
		         <c:forEach var="error" items="${errorMap[errorKey]}"> 
		             <c:out value="${error}" /><br /> 
		         </c:forEach> 
		     </lams:Alert> 
		</c:if>
				
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
			toolContentID="${authoringForm.toolContentID}"
			contentFolderID="${authoringForm.contentFolderID}" />
	</lams:TabBodyArea>
	
	<div id="footer"></div>
	
</lams:Page>
</form:form>
</body>
</lams:html>
