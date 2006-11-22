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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
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
<head class="stripes">
	<html:base />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css/>
	<title><fmt:message key="activity.title" /></title>
	
		<script language="JavaScript" type="text/JavaScript">
			function submitNextQuestionSelected() 
			{
				++document.McLearningForm.questionIndex.value;
				document.McLearningForm.nextQuestionSelected.value = 1;
				document.McLearningForm.submit();
			}
		</script>
</head>

<body class="stripes">

<div id="content">
	<html:form  action="/learning?method=displayMc&validate=false" enctype="multipart/form-data" method="POST" target="_self">
		<html:hidden property="toolContentID"/>						
		<html:hidden property="toolSessionID"/>						
		<html:hidden property="httpSessionID"/>			
		<html:hidden property="userID"/>								
		<html:hidden property="userOverPassMark"/>						
		<html:hidden property="passMarkApplicable"/>										
		<html:hidden property="learnerProgress"/>										
		<html:hidden property="learnerProgressUserId"/>										
		<html:hidden property="questionListingMode"/>					
		
		<h1>
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>
		
		<%@ include file="/common/messages.jsp"%>
		
		<c:choose> 
		  <c:when test="${mcGeneralLearnerFlowDTO.questionListingMode == 'questionListingModeSequential'}" > 
				<jsp:include page="/learning/SingleQuestionAnswersContent.jsp" /> 
		  </c:when> 
		  <c:otherwise>
			  	<jsp:include page="/learning/CombinedAnswersContent.jsp" /> 
		  </c:otherwise>
		</c:choose> 
	</html:form>
</div>

<div id="footer"></div>

</body>
</html:html>









	
	