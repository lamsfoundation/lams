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
					escapeXml="true" />
			</h1>

			<p>
				<c:out value="${generalLearnerFlowDTO.reflectionSubject}"
					escapeXml="true" />
			</p>

			<html:textarea cols="60" rows="8" property="entryText"
				styleClass="text-area"></html:textarea>

			<div class="space-bottom-top align-right">
				<html:link href="#nogo" property="submitReflection" styleId="finishButton"
					onclick="javascript:submitMethod('submitReflection');return false" styleClass="button">
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
				</html:link>
			</div>
		</div>
	</html:form>

	<div id="footer"></div>

</body>
</lams:html>
