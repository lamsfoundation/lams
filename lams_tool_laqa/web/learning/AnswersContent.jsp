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


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<html:html>
<head>
	<html:base />
	<lams:headItems />
	<title><bean:message key="activity.title" /></title>

	<script language="JavaScript" type="text/JavaScript">
		function submitLearningMethod(actionMethod) 
		{
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) 
		{
			submitLearningMethod(actionMethod);
		}
		
	</script>	
</head>

<body>
	<div id="page-learner">

<h1 class="no-tabs-below">
	<c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="false" />
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">
	  <html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
  		<html:hidden property="method"/>	 
		<html:hidden property="toolSessionID"/>						
		<html:hidden property="httpSessionID"/>		
		<html:hidden property="questionIndex"/>		
		<html:hidden property="totalQuestionCount"/>		
								
			<table>

			<tr> <td>
				<html:errors/>
			</td></tr>

			<c:if test="${generalLearnerFlowDTO.activityOffline == 'true'}"> 			
				<tr> <td NOWRAP>
					<bean:message key="label.learning.forceOfflineMessage"/>
				</td></tr>
			</c:if> 		
		
			<c:if test="${generalLearnerFlowDTO.activityOffline == 'false'}"> 			
				<tr><td NOWRAP>
					<c:out value="${generalLearnerFlowDTO.activityInstructions}" escapeXml="false"/> 
				</td></tr>
	
				<tr> <td NOWRAP>
					 <c:out value="${generalLearnerFlowDTO.userFeedback}" escapeXml="true"/> 
				</td></tr>
			
				<c:choose> 
				  <c:when test="${generalLearnerFlowDTO.questionListingMode == 'questionListingModeSequential'}" > 
						<jsp:include page="/learning/SequentialAnswersContent.jsp" /> 
					  </c:when> 

					  <c:otherwise>
						  	<jsp:include page="/learning/CombinedAnswersContent.jsp" /> 
				  </c:otherwise>
				</c:choose> 
			</c:if> 		
	 	</table>
	</html:form>
</div>

<div id="footer-learner"></div>

</div>
</body>
</html:html>









	
	