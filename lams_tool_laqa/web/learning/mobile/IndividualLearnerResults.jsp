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
	<style type="text/css">
		
	.field-name{
	color: #0087e5;
	font-weight: bold;
	text-align: left;
	padding-bottom:10px;
	}
	</style>
	<title><fmt:message key="activity.title" /></title>

	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
	<script language="JavaScript" type="text/JavaScript">
		function submitLearningMethod(actionMethod) 
		{
			if (actionMethod == 'endLearning') {
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

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>
	</div><!-- /header -->

	<div data-role="content">	

		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>	

		<c:if test="${(generalLearnerFlowDTO.lockWhenFinished == 'true')  && !generalLearnerFlowDTO.noReeditAllowed && (generalLearnerFlowDTO.showOtherAnswers) }">
			<div class="info space-bottom">
				<fmt:message key="label.responses.locked" />								
			</div>
		</c:if>

		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
			<html:hidden property="method" value="storeAllResults"/>
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />

			<ul data-role="listview" data-theme="c" id="qaQuestions" style="padding-top: 0;">
			<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">

				<li>
					<p class="space-top">
						<strong><fmt:message key="label.question" /> <c:out	value="${questionEntry.key}" />:</strong>
						<br>
						<c:out value="${questionEntry.value.question}" escapeXml="false" />
					</p>

					<c:forEach var="answerEntry" items="${generalLearnerFlowDTO.mapAnswersPresentable}">
						<c:if test="${answerEntry.key == questionEntry.key}">
							<p class="small-space-top user-answer">
								<strong> <fmt:message key="label.learning.yourAnswer" />
								</strong>
								<br>
								<c:out value="${answerEntry.value}" escapeXml="false" />
							</p>
						</c:if>
					</c:forEach>

					<c:if test="${(questionEntry.value.feedback != '') && (questionEntry.value.feedback != null) }">
						<p class="small-space-top small-space-bottom">
							<span class="field-name"> 
								<fmt:message key="label.feedback" />: 
							</span>
							<c:out value="${questionEntry.value.feedback}" escapeXml="false" />
						</p>
					</c:if>

				</li>
			</c:forEach>
			</ul>

			<div class="last-item">
			</div>

			<div class="small-space-top button-inside" >
				<c:if test="${!generalLearnerFlowDTO.noReeditAllowed}">
					<button name="redoQuestions" class="button" onclick="submitMethod('redoQuestions');" data-icon="back">
						<fmt:message key="label.redo" />
					</button>
				</c:if>
			</div>
		</html:form>
	</div>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
			<c:if test="${generalLearnerFlowDTO.showOtherAnswers}">
				<button name="viewAllResults" onclick="submitMethod('storeAllResults');" data-icon="arrow-r" data-theme="b">
					<fmt:message key="label.allResponses" />
				</button>
			</c:if>				

			<c:if test="${!generalLearnerFlowDTO.showOtherAnswers}">
				<c:if test="${generalLearnerFlowDTO.reflection != 'true'}">
				    <div class="space-bottom-top align-right">
						<a href="#nogo" name="endLearning" id="finishButton"
							onclick="javascript:submitMethod('storeAllResults');return false" data-role="button" data-icon="arrow-r" data-theme="b">
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
						</a>
				    </div>
				</c:if>
	
				<c:if test="${generalLearnerFlowDTO.reflection == 'true'}">
					<button name="forwardtoReflection"	onclick="javascript:submitMethod('storeAllResults');" data-icon="arrow-r" data-theme="b">
						<fmt:message key="label.continue" />
					</button>
				</c:if>
			</c:if>
		</span>
	</div><!-- /footer -->

</div>
</body>
</lams:html>

