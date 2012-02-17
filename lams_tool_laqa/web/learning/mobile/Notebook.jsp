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
	<script src="${lams}includes/javascript/jquery-1.7.1.min.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
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

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>
	</div><!-- /header -->

	<div data-role="content">	
		<html:form action="/learning?validate=false" styleId="reflectionForm">
			<html:hidden property="method" value="submitReflection"/>
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />
	
			<p>
				<c:out value="${generalLearnerFlowDTO.reflectionSubject}" escapeXml="false" />
			</p>
	
			<html:textarea cols="60" rows="8" property="entryText" styleClass="text-area"></html:textarea>
		</html:form>
	</div>
	
	<div data-role="footer" data-theme="b">
		<div class="finishButtonDiv">
			<button name="submitReflection" id="finishButton" onclick="javascript:submitMethod('submitReflection');return false"
					data-theme="b" data-icon="arrow-r">
				<span class="nextActivity"><fmt:message key="button.endLearning" /></span>
			</button>
		</div>
	</div><!-- /footer -->

</div>
</body>
</lams:html>
