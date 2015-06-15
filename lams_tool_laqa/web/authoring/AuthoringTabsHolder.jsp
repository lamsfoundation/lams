<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.qa.QaAppConstants"%>

    <% 
		Set tabs = new LinkedHashSet();
		tabs.add("label.basic");
		tabs.add("label.advanced");
		tabs.add("label.conditions");
		pageContext.setAttribute("tabs", tabs);
	%>

<lams:html>		
	<lams:head>
	<title><fmt:message key="activity.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>
	<link href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet" type="text/css" >
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	
 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
	<script language="JavaScript" type="text/JavaScript">

		function submitMethod(actionMethod) {
			document.QaAuthoringForm.dispatch.value=actionMethod; 
			document.QaAuthoringForm.submit();
		}
		
		function submitModifyAuthoringQuestion(questionIndexValue, actionMethod) {
			document.QaAuthoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
        
        function init() {
	         var tag = document.getElementById("currentTab");
		    if(tag.value != "")
		    	selectTab(tag.value);
	        else
	            selectTab(1); //select the default tab;
        }
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
        function doSubmit(method) {
        	document.QaAuthoringForm.dispatch.value=method;
        	document.QaAuthoringForm.submit();
        }

	</script>
</lams:head>

<body class="stripes" onLoad="init();">

<div id="page">
	<h1>  <fmt:message key="label.authoring.qa"/> </h1>
	
	<div id="header">			
		<lams:Tabs collection="${tabs}" useKey="true" control="true" />						
	</div>

	<div id="content">	
		<html:form  action="/authoring?validate=false" styleId="authoringForm" enctype="multipart/form-data" method="POST" target="_self">
			<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
			<c:set var="sessionMap" value="${sessionScope[formBean.httpSessionID]}" scope="request"/>
			
			<html:hidden property="dispatch" value="submitAllContent"/>
			<html:hidden property="toolContentID"/>
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="httpSessionID"/>									
			<html:hidden property="contentFolderID"/>
			<input type="hidden" name="mode" value="${mode}">											
			
			<%@ include file="/common/messages.jsp"%>
			
			<lams:help toolSignature="<%= QaAppConstants.MY_SIGNATURE %>" module="authoring"/>	
			<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
			      
			<lams:TabBody id="2" titleKey="label.advanced" page="AdvancedContent.jsp" />

			<lams:TabBody id="3" titleKey="label.conditions" page="conditions.jsp" />
			
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="laqa11" 
				cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${formBean.toolContentID}" 
				contentFolderID="${formBean.contentFolderID}" accessMode="${mode}" defineLater="${mode=='teacher'}"/>		
		</html:form>		
	</div>

	<div id="footer"></div>

</div>

</body>
</lams:html>
