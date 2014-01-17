
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.httpSessionID]}" />

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" /></title>

	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>
	<script language="JavaScript" type="text/JavaScript">
		function submitLearningMethod(actionMethod){
			if (actionMethod == 'endLearning') {
				document.getElementById("finishButton").disabled = true;
			}
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod){
			submitLearningMethod(actionMethod);
		}
	</script>
</lams:head>

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<fmt:message key="activity.title" />
		</h1>
	</div><!-- /header -->

	<div data-role="content">	
		<html:form action="/learning?validate=false" method="POST" styleId="form">
			<html:hidden property="method" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />

			<div class="warning">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
					
			<div class="space-bottom-top align-right">

				<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
					<html:link href="#nogo" property="endLearning" styleId="finishButton"
						onclick="javascript:submitMethod('endLearning');return false"
						styleClass="button">
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
				</c:if>

				<c:if test="${generalLearnerFlowDTO.reflection == 'true'}">
					<html:button property="forwardtoReflection"
						onclick="javascript:submitMethod('forwardtoReflection');"
						styleClass="button">
						<fmt:message key="label.continue" />
					</html:button>
				</c:if>

			</div>
		</html:form>
	</div>
	
	<div data-role="footer" data-theme="b">
		<h2>&nbsp;</h2>
	</div><!-- /footer -->

</div>
</body>
</lams:html>
