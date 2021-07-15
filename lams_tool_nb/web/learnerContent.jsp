<!DOCTYPE html>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title"/></title>
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/bootstrap.min.js" type="text/javascript"></script>
	<script src="${lams}learning/includes/javascript/gate-check.js"></script>
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${nbLearnerForm.toolSessionID}', '', function(){
			submitForm('finish');
		});
	</script>
</lams:head>

<body class="stripes">

<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
	function submitForm(methodName) {
		var f = document.getElementById('nbLearnerForm');
		f.action = methodName + ".do";
		f.submit();
	}
</script>

<lams:Page type="learner" title="${nbLearnerForm.title}">
	<div class="panel">
		<c:out value="${nbLearnerForm.basicContent}" escapeXml="false" />
	</div>

	<form:form modelAttribute="nbLearnerForm" target="_self" onsubmit="disableFinishButton();" id="nbLearnerForm">
		<form:hidden path="mode" />
		<form:hidden path="toolSessionID" />

		<c:if test="${userFinished and reflectOnActivity}">
			<div class="panel">
				<lams:out value="${reflectInstructions}" escapeHtml="true" />
			</div>

			<div class="bg-warning" id="reflectionEntry">
				<c:choose>
					<c:when test="${empty reflectEntry}">
						<fmt:message key="message.no.reflection.available" />
					</c:when>
					<c:otherwise>
						<lams:out escapeHtml="true" value="${reflectEntry}" />
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

		<c:if test="${allowComments}">
			<hr/>
			<lams:Comments toolSessionId="${nbLearnerForm.toolSessionID}" 
				toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>" likeAndDislike="${likeAndDislike}" anonymous="${anonymous}" />
		</c:if>


		<c:if test="${not nbLearnerForm.readOnly}">
			<c:choose>
				<c:when test="${reflectOnActivity}">

					<button  name="continueButton" class="btn btn-sm btn-primary pull-right"
						onclick="submitForm('reflect')">
						<fmt:message key="button.continue" />
					</button>
				</c:when>
				<c:otherwise>


					<a href="#nogo" id="finishButton" name="finishButton" class="btn btn-primary pull-right voffset10 na">
						<c:choose>
							<c:when test="${isLastActivity}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>

					</a>
				</c:otherwise>
			</c:choose>
		</c:if>

	</form:form>

</lams:Page>
	
</lams:html>