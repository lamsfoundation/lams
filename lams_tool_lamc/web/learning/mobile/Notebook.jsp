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
	
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<script src="${lams}includes/javascript/jquery-1.7.1.min.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
</lams:head>

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>
	</div><!-- /header -->

	<html:form action="/learning?method=displayMc&validate=false"
		styleId="reflectionForm" method="POST">
		<div data-role="content">
			<html:hidden property="toolContentID" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="submitReflection" />
		
			<p>
				<c:out value="${mcGeneralLearnerFlowDTO.reflectionSubject}" escapeXml="false" />
			</p>

			<html:textarea cols="60" rows="8" property="entryText"
				styleClass="text-area">
				<c:if test="${not empty mcGeneralLearnerFlowDTO.notebookEntry}">
					<c:out value="${mcGeneralLearnerFlowDTO.notebookEntry}" escapeXml="false"/>										
				</c:if>
			</html:textarea>
		</div>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
			<a href="#" name="submitReflection"
				onclick="javascript:document.McLearningForm.submit();return false" data-role="button" data-icon="arrow-r" data-theme="b">
				<span class="nextActivity"><fmt:message key="button.endLearning" /></span>
			</a>
		</span>
	</div><!-- /footer -->
	</html:form>

</div>
</body>
</lams:html>








