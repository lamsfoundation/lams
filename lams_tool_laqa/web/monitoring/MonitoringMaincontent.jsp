<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
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
			document.QaMonitoringForm.method.value=actionMethod; 
			document.QaMonitoringForm.submit();
		}
		
		function submitModifyQuestion(questionIndexValue, actionMethod) 
		{
			document.QaMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitEditResponse(responseId, actionMethod) 
		{
			document.QaMonitoringForm.responseId.value=responseId; 
			submitMethod(actionMethod);
		}
		
		function submitMethod(actionMethod) 
		{
			submitMonitoringMethod(actionMethod);
		}
		
		function deleteOption(deletableOptionIndex, actionMethod) {
			document.QaMonitoringForm.deletableOptionIndex.value=deletableOptionIndex; 
			submitMethod(actionMethod);
		}
		
		function submitSession(selectedToolSessionId, actionMethod) {
			document.QaMonitoringForm.selectedToolSessionId.value=selectedToolSessionId; 
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
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_su" ><label>
						<a href="<c:out value='${monitoringURL}'/>?method=getSummary" id="su" >
							<bean:message key="label.summary"/>
						</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=getSummary">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_su" width="8" height="25" border="0" id="tab_right_su"/></a></td>
					  </tr>
					</table>
				</td>

			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_i" width="8" height="25" border="0" id="tab_left_i"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_i" ><label><a
							href="<c:out value='${monitoringURL}'/>?method=getInstructions" id="i" >
								<bean:message key="label.instructions"/>
							</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=getInstructions">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_i" width="8" height="25" border="0" id="tab_right_i"/></a></td>
					  </tr>
					</table>
				</td>

				
			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_e" width="8" height="25" border="0" id="tab_left_e"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_e" ><label><a
							href="<c:out value='${monitoringURL}'/>?method=editActivity" id="e" >
								<bean:message key="label.editActivity"/>								
							</a></label></td>
						<td><a href="<c:out value='${monitoringURL}'/>?method=editActivity">
							<img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_e" width="8" height="25" border="0" id="tab_right_e"/></a></td>
					  </tr>
					</table>
				</td>

							
			    <td valign="bottom">
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_s" width="8" height="25" border="0" id="tab_left_s"/></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_s" ><label><a
							href="<c:out value='${monitoringURL}'/>?method=getStats" id="e" >
								<bean:message key="label.stats"/>								
							</a></label></td>
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
								edit activity screen
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







