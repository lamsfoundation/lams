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
	<title><fmt:message key="activity.title" /></title>

	<script language="JavaScript" type="text/JavaScript">
		function submitLearningMethod(actionMethod) 
		{
			if (actionMethod == 'submitReflection') {
				document.getElementById("finishButton").disabled = true;
			}		
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) 
		{
			submitLearningMethod(actionMethod);
		}
		
	</script>
</lams:head>

<body class="stripes">
	<html:form action="/learning?validate=false" styleId="reflectionForm">
		<html:hidden property="method" value="submitReflection"/>
		<html:hidden property="toolSessionID" />
		<html:hidden property="userID" />
		<html:hidden property="httpSessionID" />
		<html:hidden property="totalQuestionCount" />

		<div id="content">

			<h1>
				<c:out value="${generalLearnerFlowDTO.activityTitle}"
					escapeXml="false" />
			</h1>

			<p>
				<c:out value="${generalLearnerFlowDTO.reflectionSubject}"
					escapeXml="false" />
			</p>

			<html:textarea cols="60" rows="8" property="entryText"
				styleClass="text-area"></html:textarea>

			<div class="space-bottom-top align-right">
				<html:link href="#" property="submitReflection" styleId="finishButton"
					onclick="javascript:submitMethod('submitReflection');return false" styleClass="button">
					<span class="nextActivity"><fmt:message key="button.endLearning" /></span>
				</html:link>
			</div>
		</div>
	</html:form>

	<div id="footer"></div>

</body>
</lams:html>








