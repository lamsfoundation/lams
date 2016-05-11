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

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" />
	</title>
	
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) {
			document.VoteLearningForm.dispatch.value=actionMethod; 
			document.VoteLearningForm.submit();
		}
	</script>

</lams:head>

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${voteGeneralLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>
	</div>

	<div data-role="content">

		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
	
			<html:hidden property="dispatch" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="userLeader" />
			<html:hidden property="groupLeaderName" />
			<html:hidden property="useSelectLeaderToolOuput" />
	
				<p>
					<lams:out value="${voteGeneralLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
				</p>
	
				<html:textarea cols="60" rows="8" property="entryText" styleClass="text-area"></html:textarea>
	
		</html:form>
	</div>

	<div data-role="footer" data-theme="b">
		<div class="finishButtonDiv">
			<button name="submitReflection" id="finishButton" onclick="javascript:submitMethod('submitReflection');return false"
					data-theme="b" data-icon="arrow-r">
				<span class="nextActivity">								
					<c:choose>
	 					<c:when test="${sessionMap.activityPosition.last}">
	 						<fmt:message key="button.submit" />
	 					</c:when>
	 					<c:otherwise>
	 		 				<fmt:message key="button.endLearning" />
	 					</c:otherwise>
	 				</c:choose>
	 			</span>
			</button>
		</div>
	</div>

</div>	
</body>
</lams:html>








