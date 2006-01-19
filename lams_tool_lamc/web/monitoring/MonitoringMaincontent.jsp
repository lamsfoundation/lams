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
	<title>MCQ tool Monitoring</title>
	
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
		
		function submitMethod(actionMethod) 
		{
			submitMonitoringMethod(actionMethod);
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

tabId: <c:out value='${tabId}'/>

<body onLoad='showMonitoringTab("<c:out value='${tabId}'/>")'>

	<b> <bean:message key="label.monitoring"/> </b>
	
		<c:set var="monitoringURL">
			<html:rewrite page="/monitoring.do" />
		</c:set>

	  <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<!-- start tabs -->
		<!-- tab holder table -->
		<table border="0" cellspacing="0" cellpadding="0">
		  <tr>
		  	<td> &nbsp&nbsp</td>
		  </tr>
			
		  <tr>
			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_su" width="8" height="25" border="0" id="tab_left_su"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_su" ><label for="su" accesskey="su">
						<a href="<c:out value='${monitoringURL}'/>?method=getSummary" id="su" >Summary</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=getSummary">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_su" width="8" height="25" border="0" id="tab_right_su"/></a></td>
					  </tr>
					</table>
				</td>

			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_i" width="8" height="25" border="0" id="tab_left_i"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_i" ><label for="i" accesskey="i"><a
							href="<c:out value='${monitoringURL}'/>?method=getInstructions" id="i" >Instructions</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=getInstructions">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_i" width="8" height="25" border="0" id="tab_right_i"/></a></td>
					  </tr>
					</table>
				</td>

				
			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_e" width="8" height="25" border="0" id="tab_left_e"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_e" ><label for="e" accesskey="e"><a
							href="<c:out value='${monitoringURL}'/>?method=editActivity" id="e" >Edit Activity</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=editActivity">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_e" width="8" height="25" border="0" id="tab_right_e"/></a></td>
					  </tr>
					</table>
				</td>

							
			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_s" width="8" height="25" border="0" id="tab_left_s"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_s" ><label for="s" accesskey="s"><a
							href="<c:out value='${monitoringURL}'/>?method=getStats" id="e" >Stats</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=getStats">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_s" width="8" height="25" border="0" id="tab_right_s"/></a></td>
					  </tr>
					</table>
				</td>
		  </tr>
		</table>
	
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
							<jsp:include page="/authoring/BasicContent.jsp" />
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
						</div>		         							
		        </c:otherwise>
		</c:choose>
	
	</html:form>
	<p></p>
</body>
</html:html>







