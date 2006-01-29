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
		<title> <bean:message key="label.monitoring"/> </title>
	<script lang="javascript">
		var imgRoot="<c:out value="${lams}"/>images/";
		var themeName="aqua";
	</script>
	
	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/tabcontroller.js"></script>
	<script src="<c:out value="${lams}"/>includes/javascript/common.js"></script>
	
	<!-- this is the custom CSS for hte tool -->
	<link href="<c:out value="${tool}"/>css/tool_custom.css" rel="stylesheet" type="text/css">
	<lams:css/>
	<script language="JavaScript" type="text/JavaScript">

		// general submit
		// actionMethod: name of the method to be called in the DispatchAction
		function submitMonitoringMethod(actionMethod) 
		{
			document.McMonitoringForm.method.value=actionMethod; 
			document.McMonitoringForm.submit();
		}
		
		function submitModifyQuestion(questionIndexValue, actionMethod) 
		{
			document.McMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitMethod(actionMethod) 
		{
			submitMonitoringMethod(actionMethod);
		}
		
		function deleteOption(deletableOptionIndex, actionMethod) {
			document.McMonitoringForm.deletableOptionIndex.value=deletableOptionIndex; 
			submitMethod(actionMethod);
		}
		
		
		function submitSession(selectedToolSessionId, actionMethod) {
			document.McMonitoringForm.selectedToolSessionId.value=selectedToolSessionId; 
			submitMonitoringMethod(actionMethod);
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

<%-- chooses which tab to highlight --%>
<c:choose>
         <c:when test='${currentMonitoringTab == "instructions"}'>
           <c:set var="tabId" value="i" />
        </c:when>
        <c:when test='${currentMonitoringTab == "editActivity"}'>
           <c:set var="tabId" value="e" />
        </c:when>
        <c:when test='${currentMonitoringTab == "stats"}'>
           <c:set var="tabId" value="st" />
        </c:when>
        <c:otherwise> 
      	   <c:set var="tabId" value="su" />
        </c:otherwise>
</c:choose>

<body onLoad='showMonitoringTab("<c:out value='${tabId}'/>")'>

	<b> <font size=2> <bean:message key="label.monitoring"/> </font></b>
	
		<c:set var="monitoringURL">
			<html:rewrite page="/monitoring.do" />
		</c:set>

	  <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<!-- start tabs -->
					<jsp:include page="/monitoring/MonitoringTabsHeader.jsp" />
		<!-- end tab buttons -->
		
		<html:hidden property="method"/>	 
		<c:choose>
		         <c:when test='${currentMonitoringTab == "instructions"}'>
						<div class="tabbody content_b" >
							<jsp:include page="/monitoring/Instructions.jsp" />
						</div>
						
		        </c:when>
		
		        <c:when test='${currentMonitoringTab == "editActivity"}'>
						<div class="tabbody content_b" >
							<c:if test="${sessionScope.editOptionsMode == 0}"> 			
								<jsp:include page="/authoring/BasicContent.jsp" />
							</c:if> 				
							<c:if test="${sessionScope.editOptionsMode == 1}"> 			
								<jsp:include page="/authoring/OptionsContent.jsp" />
							</c:if> 				
						</div>
		        </c:when>

		         <c:when test='${currentMonitoringTab == "stats"}'>
						<div class="tabbody content_b" >
							<jsp:include page="/monitoring/Stats.jsp" />
						</div>
		        </c:when>
		        
		        <c:otherwise> 
						<div class="tabbody content_b" >
							<jsp:include page="/monitoring/SummaryContent.jsp" />
								<table align=right> 	  
									<tr>   
										<td NOWRAP>
											<font size=2>
												<html:submit onclick="javascript:submitMethod('doneMonitoring');" styleClass="button">
													<bean:message key="button.done"/>
												</html:submit>	 				 		  					
											</font>
										</td>
									</tr> 
								</table>
						</div>		         							
		        </c:otherwise>
		</c:choose>
	</html:form>

</body>
</html:html>







