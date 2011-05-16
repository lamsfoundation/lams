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
	<lams:css />
	<title><fmt:message key="activity.title" />
	</title>
	
	<script type="text/javascript">
		function disableFinishButton() {
			var elem = document.getElementById("finishButton");
			if (elem != null) {
				elem.disabled = true;
			}
		}
	         function submitForm(methodName){
	                var f = document.getElementById('messageForm');
	                f.submit();
	        }
	</script>
</lams:head>

<body class="stripes">

	<html:form action="/learning?method=displayMc&validate=false"
		method="POST" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="toolContentID" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="httpSessionID" />
		<html:hidden property="userID" />


		<div id="content">

			<h1>
				<fmt:message key="activity.title" />
			</h1>
			<c:choose>
				<c:when test="${empty mcLearnerStarterDTO.submissionDeadline}">
					<p>
						<fmt:message key="label.learning.forceOfflineMessage" />
					</p>
				</c:when>
				<c:otherwise>
					<div class="warning">
						<fmt:message key="authoring.info.teacher.set.restriction" >
							<fmt:param><lams:Date value="${mcLearnerStarterDTO.submissionDeadline}" /></fmt:param>
						</fmt:message>							
					</div>
				</c:otherwise>
			</c:choose>								
			<div class="space-bottom-top align-right">

				<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
					<html:hidden property="learnerFinished" value="Finished" />
					
					<html:link href="#" styleClass="button" styleId="finishButton" onclick="javascript:submitForm('finish');return false">
						<span class="nextActivity"><fmt:message key="label.finished" /></span>
					</html:link>
				</c:if>

				<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
					<html:submit property="forwardtoReflection" styleClass="button">
						<fmt:message key="label.continue" />
					</html:submit>
				</c:if>
			</div>
		</div>
	</html:form>
	<div id="footer"></div>
</body>
</lams:html>








