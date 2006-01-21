<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

	<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
	<html:html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<title><bean:message key="label.authoring"/></title>
	<script lang="javascript">
	var imgRoot="<c:out value="${lams}"/>images/";
	var themeName="aqua";
	</script>
	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/tabcontroller.js"></script>
	<script src="<c:out value="${lams}"/>includes/javascript/common.js"></script>
	
	<!-- this is the custom CSS for the tool -->
	<link href="<c:out value="${tool}"/>css/tool_custom.css" rel="stylesheet" type="text/css">
	<lams:css/>
	
	<script language="JavaScript" type="text/JavaScript">
		<!--
		
		// questionIndexValue: index of question affected
		// actionMethod: name of the method to be called in the DispatchAction	
		function submitModifyQuestion(questionIndexValue, actionMethod) 
		{
			document.McAuthoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		// The following method submit and the submit methods in the included jsp files submit the 
		// form as required for the DispatchAction. All form submissions must go via these scripts - do not
		// define an submit button with "dispatch" as the property or 
		// "document.McAuthoringForm.dispatch.value=buttonResourceText" will not work
		
		// general submit
		// actionMethod: name of the method to be called in the DispatchAction
		function submitMethod(actionMethod) 
		{
			document.McAuthoringForm.dispatch.value=actionMethod; 
			document.McAuthoringForm.submit();
		}
		
		function deleteOption(deletableOptionIndex, actionMethod) {
			document.McAuthoringForm.deletableOptionIndex.value=deletableOptionIndex; 
			submitMethod(actionMethod);
		}
		
		function submitDeleteFile(uuid, actionMethod) 
		{
			document.McAuthoringForm.uuid.value=uuid; 
			submitMethod(actionMethod);
		}
		
		function MM_reloadPage(init) {  //reloads the window if Nav4 resized
		  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
		    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
		  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
		}
		
		MM_reloadPage(true);
		//-->
	</script>
</head>
<body onLoad="initTabs()">

	<b> <bean:message key="label.authoring.mc"/> </b>
	    
		<c:if test="${ sessionScope.showAuthoringTabs == 'true'}"> 			
		<!-- start tabs -->
				<jsp:include page="/authoring/AuthoringTabsHeader.jsp" />
		<!-- end tab buttons -->				
		</c:if> 			
	
	<html:form  action="/authoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">
	<html:hidden property="dispatch"/>
	
	<div id='content_b' class="tabbody content_b" >
		<h2><bean:message key="label.authoring.mc.basic"/></h2>	
				<table align=center> 	  
				<tr>   
				<td NOWRAP class=error>
					<%@ include file="/Errorbox.jsp" %> <!-- show any error messages here -->
				</td>
				</tr> 
				</table>
	
			<div id="formtablecontainer1">
					<c:if test="${sessionScope.editOptionsMode == 0}"> 			
						<jsp:include page="/authoring/BasicContent.jsp" />
					</c:if> 				
					<c:if test="${sessionScope.editOptionsMode == 1}"> 			
						<jsp:include page="/authoring/OptionsContent.jsp" />
					</c:if> 				
			</div>

			<table align=right> 	  
				<tr>   
					<td NOWRAP>
						<html:submit onclick="javascript:submitMethod('cancelAuthoring');">
							<bean:message key="button.cancel"/>
						</html:submit>	 				 		  					
					</td>
				</tr> 
			</table>
	</div>
		
	<div id='content_a'  class="tabbody content_a">
			<h2><bean:message key="label.advanced.definitions"/></h2>	
		<table align=center> 	  
		<tr>   
		<td NOWRAP class=error>
			<%@ include file="/Errorbox.jsp" %> <!-- show any error messages here -->
		</td>
		</tr> 
		</table>
		
		<div id="formtablecontainer2">
			<jsp:include page="/authoring/AdvancedContent.jsp" />
		</div>
	</div>
	
	<div id='content_i'  class="tabbody content_i">
		<h2><bean:message key="label.authoring.instructions"/></h2>	
			<table align=center> 	  
			<tr>   
			<td NOWRAP class=error>
				<%@ include file="/Errorbox.jsp" %> <!-- show any error messages here -->
			</td>
			</tr> 
			</table>
			
			<div id="formtablecontainer3">
					<jsp:include page="/authoring/InstructionsContent.jsp" />
			</div>
	</div>
	</html:form>
</body>
</html:html>
