<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<html:html locale="true">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title> <bean:message key="activity.title"/> </title>
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
		
		function submitMethod(actionMethod) 
		{
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
<body>

<c:if test="${ requestLearningReportProgress != 'true'}"> 			
	<c:if test="${ requestLearningReportViewOnly != 'true'}"> 			
		<table width="80%" cellspacing="8" align="CENTER">
			<tr> <th scope="col">
				 <c:out value="${sessionScope.reportTitleLearner}" escapeXml="false"/> 
			 </th>
			</tr>
		</table>		
	</c:if> 				    

	<c:if test="${ requestLearningReportViewOnly == 'true'}"> 			
		<table width="80%" cellspacing="8" align="CENTER">
			<tr> <th scope="col">
				<bean:message key="label.learning.viewOnly"/>
			 </th>
			</tr>
		</table>				
	</c:if> 				    

		<c:set var="monitoringURL">
			<html:rewrite page="/monitoring.do" />
		</c:set>

	  <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="method"/>	 

			<jsp:include page="/monitoring/SummaryContent.jsp" />

			<c:if test="${ requestLearningReportViewOnly != 'true'}"> 			
				<table align=right> 	  
				<tr> <td> 
					<c:out value="${sessionScope.endLearningMessage}" escapeXml="false"/>
				</td> </tr>
			
				<tr>
					 <td> 
						<html:submit onclick="javascript:submitMethod('endLearning');" styleClass="button">
							<bean:message key="button.endLearning"/>
						</html:submit>	 				 		  					
					</td> 
				</tr>
				</table>
			</c:if> 			
	</html:form>
</c:if> 				    

<c:if test="${ requestLearningReportProgress == 'true'}"> 			
		<table width="80%" cellspacing="8" align="CENTER">
			<tr> <th scope="col">
				<bean:message key="label.learner.progress"/>
			 </th>
			</tr>
		</table>						 

		<c:set var="monitoringURL">
			<html:rewrite page="/monitoring.do" />
		</c:set>

	  <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="method"/>	 

				<jsp:include page="/monitoring/SummaryContent.jsp" />

	</html:form>
</c:if> 				    

</body>
</html:html>







