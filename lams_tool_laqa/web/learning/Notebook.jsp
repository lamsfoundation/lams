<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title" /></title>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>

	<script language="JavaScript" type="text/JavaScript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
		
	</script>
</lams:head>

<body class="stripes">

<!-- form needs to be outside page so that the form bean can be picked up by Page tag. -->
<html:form action="/learning?validate=false" styleId="reflectionForm" onsubmit="disableFinishButton()">

<lams:Page type="learner" title="${generalLearnerFlowDTO.activityTitle}">

		<html:hidden property="method" value="submitReflection"/>
		<html:hidden property="toolSessionID" />
		<html:hidden property="userID" />
		<html:hidden property="httpSessionID" />
		<html:hidden property="totalQuestionCount" />

			<p>
				<lams:out value="${generalLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
			</p>

			<html:textarea styleId="focused" rows="5" property="entryText" styleClass="form-control"></html:textarea>

			
				<button id="finishButton" class="btn btn-primary voffset5 pull-right na">
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
		
	<div id="footer"></div>
</lams:Page>

</html:form>


<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>

</body>
</lams:html>
