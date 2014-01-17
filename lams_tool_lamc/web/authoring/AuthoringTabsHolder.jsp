<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.mc.McAppConstants"%>

    <% 
		Set tabs = new LinkedHashSet();
		tabs.add("label.basic");
		tabs.add("label.advanced");
		pageContext.setAttribute("tabs", tabs);
		
		Set tabsBasic = new LinkedHashSet();
		tabsBasic.add("label.basic");
		pageContext.setAttribute("tabsBasic", tabsBasic);
	%>

<lams:html>		
	<lams:head>
	<title><fmt:message key="activity.title" /></title>
	
	<link href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet" type="text/css" media="screen">

	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
	
 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
	<script language="JavaScript" type="text/JavaScript">

		function submitMethod(actionMethod) {
			document.McAuthoringForm.dispatch.value=actionMethod; 
			document.McAuthoringForm.submit();
		}
		
		function submitModifyAuthoringQuestion(questionIndexValue, actionMethod) {
			document.McAuthoringForm.questionIndex.value=questionIndexValue; 
			document.McAuthoringForm.dispatch.value=actionMethod;
			
			$('#authoringForm').ajaxSubmit({ 
	    		target:  $('#resourceListArea'),
	    		iframe: true,
	    		success:    function() { 
	    			document.McAuthoringForm.dispatch.value="submitAllContent";
	    			refreshThickbox();
	    	    }
		    });
		}

		function updatePass(clickedObj) {
			if (clickedObj.checked) {
				document.McAuthoringForm.passmark.disabled= false;
				
			}else {
				document.McAuthoringForm.passmark.value= ' ';
				document.McAuthoringForm.passmark.disabled= true;
			}
		}

    	var imgRoot="${lams}images/";
	    var themeName="aqua";

        function init() {
	            
			if (document.McAuthoringForm.activeModule.value != 'defineLater')
			{
				updatePass(document.McAuthoringForm.retries);
				
	            var tag = document.getElementById("currentTab");
		    	if(tag.value != "")
		    		selectTab(tag.value);
	            else
	              selectTab(1); //select the default tab;
	            
			} else {
	            var tag = document.getElementById("currentTab");
		    	if(tag.value != "")
		    		selectTab(tag.value);
	            else
	                selectTab(1); //select the default tab;
			}
        }
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
        function doSubmit(method) {
        	document.McAuthoringForm.dispatch.value=method;
        	document.McAuthoringForm.submit();
        }
	</script>
</lams:head>


<body onLoad="init();" class="stripes">

<div id="page">
	<h1>  <fmt:message key="label.authoring.mc"/> </h1>
	
	<div id="header">
		<c:if test="${mcGeneralAuthoringDTO.activeModule != 'defineLater' }"> 			
			<lams:Tabs collection="${tabs}" useKey="true" control="true" />
		</c:if> 			
		<c:if test="${(mcGeneralAuthoringDTO.activeModule == 'defineLater') && (mcGeneralAuthoringDTO.defineLaterInEditMode != 'true') }"> 			
			<lams:Tabs collection="${tabsBasic}" useKey="true" control="true"/>
		</c:if> 					
		<c:if test="${(mcGeneralAuthoringDTO.activeModule == 'defineLater') && (mcGeneralAuthoringDTO.defineLaterInEditMode == 'true') }"> 					
			<lams:Tabs collection="${tabsBasic}" useKey="true" control="true"/>		
		</c:if> 						
	</div>

	<div id="content">	
		<html:form  action="/authoring?validate=false" styleId="authoringForm" enctype="multipart/form-data" method="POST" target="_self">
		<html:hidden property="dispatch" value="submitAllContent"/>
		<html:hidden property="toolContentID"/>
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="activeModule"/>
		<html:hidden property="httpSessionID"/>								
		<html:hidden property="defaultContentIdStr"/>								
		<html:hidden property="defineLaterInEditMode"/>										
		<html:hidden property="contentFolderID"/>												
		<html:hidden property="totalMarks"/>														
		
		<%@ include file="/common/messages.jsp"%>
		   
		<lams:help toolSignature="<%= McAppConstants.MY_SIGNATURE %>" module="authoring"/>
		
		<c:if test="${mcGeneralAuthoringDTO.activeModule != 'defineLater' }"> 			
			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
			<!-- end of content (Basic) -->
			      
			<!-- tab content 2 (Advanced) -->
			<lams:TabBody id="2" titleKey="label.advanced" page="AdvancedContent.jsp" />
			<!-- end of content (Advanced) -->


			<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lamc11" 
			cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${formBean.toolContentID}" 
			contentFolderID="${formBean.contentFolderID}" />
			
		</c:if> 			

		<c:if test="${ (mcGeneralAuthoringDTO.activeModule == 'defineLater') && (mcGeneralAuthoringDTO.defineLaterInEditMode != 'true') }"> 			
			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.basic" page="BasicContentViewOnly.jsp"/>
			<!-- end of content (Basic) -->
		</c:if> 			

		<c:if test="${ (mcGeneralAuthoringDTO.activeModule == 'defineLater') && (mcGeneralAuthoringDTO.defineLaterInEditMode == 'true') }"> 			
			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
			<!-- end of content (Basic) -->
		</c:if> 			
		</html:form>		
	</div>

	<div id="footer"></div>

</div>


</body>
</lams:html>