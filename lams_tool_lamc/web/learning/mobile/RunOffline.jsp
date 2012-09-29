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
	<title><fmt:message key="activity.title" /></title>

	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
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

<body>

	<html:form action="/learning?method=displayMc&validate=false"
		method="POST" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="toolContentID" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="httpSessionID" />
		<html:hidden property="userID" />

		<div data-role="page" data-cache="false">
		
			<div data-role="header" data-theme="b" data-nobackbtn="true">
				<h1>
					<fmt:message key="activity.title" />
				</h1>
			</div>
	
			<div data-role="content">
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
			</div>	
								
			<div data-role="footer" data-theme="b" class="ui-bar">
				<span class="ui-finishbtn-right">

					<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
						<html:hidden property="learnerFinished" value="Finished" />
						
						<a href="#nogo" id="finishButton" onclick="javascript:submitForm('finish');return false"  data-role="button" data-icon="arrow-r">
							<span class="nextActivity"><fmt:message key="label.finished" /></span>
						</a>
					</c:if>
	
					<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
						<button type="submit" name="forwardtoReflection" data-icon="arrow-r">
							<fmt:message key="label.continue" />
						</button>
					</c:if>
				</span>
			</div>
			
			
		</div>
	</html:form>
</body>
</lams:html>








