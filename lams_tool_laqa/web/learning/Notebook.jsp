<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title" /></title>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>

	<script language="JavaScript" type="text/JavaScript">
		function submitLearningMethod(actionMethod) {
			if (actionMethod == 'submitReflection') {
				document.getElementById("finishButton").disabled = true;
			}		
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) {
			submitLearningMethod(actionMethod);
		}
		
	</script>
</lams:head>

<body class="stripes">

<!-- form needs to be outside page so that the form bean can be picked up by Page tag. -->
<html:form action="/learning?validate=false" styleId="reflectionForm">

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

			
				<html:link href="#nogo" property="submitReflection" styleId="finishButton"
					onclick="javascript:submitMethod('submitReflection');return false" styleClass="btn btn-primary voffset5 pull-right">
					<div class="na">					
						<c:choose>
		 					<c:when test="${sessionMap.activityPosition.last}">
		 						<fmt:message key="button.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="button.endLearning" />
		 					</c:otherwise>
		 				</c:choose>
		 			</div>
				</html:link>
		
		</div>

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
