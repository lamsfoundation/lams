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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<head>
	<title> <fmt:message key="activity.title"/> </title>

	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/fckeditorheader.jsp"%>
	
	
	<script lang="javascript">
		var imgRoot="<c:out value="${lams}"/>images/";
		var themeName="aqua";
	</script>

	<script language="JavaScript" type="text/JavaScript">

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
	</script>	
</head>
<body class="stripes">

<div id="page">

<c:if test="${ requestLearningReportProgress != 'true'}"> 			
	<c:if test="${ requestLearningReportViewOnly != 'true'}"> 			
     	<table class="forms"> 	  
			<tr> <th scope="col">
				 <c:out value="${sessionScope.reportTitleLearner}" escapeXml="false"/> 
			 </th>
			</tr>
		</table>		
	</c:if> 				    

	<c:if test="${ requestLearningReportViewOnly == 'true'}"> 			
       	<table class="forms"> 	  
			<tr> <th scope="col">
				<fmt:message key="label.learning.viewOnly"/>
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

				<c:if test="${ requestLearningReportViewOnly == 'true'}"> 			
			       	<table class="forms align-right"> 	  
							<tr> <td class="align-right"> 
								 <fmt:message key="label.learning.forceFinish"/> &nbsp&nbsp

								<html:submit onclick="javascript:submitMethod('endLearning');" styleClass="button">
									<fmt:message key="button.endLearning"/>
								</html:submit>	 				 		  					
								</td> 
							</tr>
					</table>
				</c:if> 			

				<c:if test="${ requestLearningReportViewOnly != 'true'}"> 			
			       	<table class="forms align-right" > 	  
							<tr> <td class="align-right"> 
								<html:submit onclick="javascript:submitMethod('endLearning');" styleClass="button">
									<fmt:message key="button.endLearning"/>
								</html:submit>	 				 		  					
								</td> 
							</tr>
					</table>
				</c:if> 			

	</html:form>
</c:if> 				    

<c:if test="${ requestLearningReportProgress == 'true'}"> 			
       	<table class="forms"> 	  
			<tr> <th scope="col">
				<fmt:message key="label.learner.progress"/>
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
</lams:html>







