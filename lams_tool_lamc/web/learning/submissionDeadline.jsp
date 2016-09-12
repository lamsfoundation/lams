<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

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

	<html:form action="/learning?method=displayMc&validate=false" method="POST" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="toolContentID" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="httpSessionID" />
		<html:hidden property="userID" />

		<div id="content">

			<h1>
				<fmt:message key="activity.title" />
			</h1>
			
			<div class="warning">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
				</fmt:message>							
			</div>
					
			<div class="space-bottom-top align-right">

				<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
					<html:hidden property="learnerFinished" value="Finished" />
					
					<html:link href="#nogo" styleClass="button" styleId="finishButton" onclick="javascript:submitForm('finish');return false">
						<span class="nextActivity">
							<c:choose>
			 					<c:when test="${activityPosition.last}">
			 						<fmt:message key="label.submit" />
			 					</c:when>
			 					<c:otherwise>
			 		 				<fmt:message key="label.finished" />
			 					</c:otherwise>
				 			</c:choose>
				 		</span>
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
