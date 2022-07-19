<!DOCTYPE html>

<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>

	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		function finish() {
			document.getElementById("finishButton").disabled = true;
			var finishUrl = "<lams:WebAppURL />learning/finish.do?sessionMapID=${sessionMapID}";
			location.href = finishUrl;
		}
		function notebook() {
			var finishUrl = "<lams:WebAppURL />learning/newReflection.do?sessionMapID=${sessionMapID}";
			location.href = finishUrl;
		}
	</script>
</lams:head>

<body class="stripes">

	<c:set var="title" scope="request">
		<fmt:message key="activity.title"></fmt:message>
	</c:set>

	<lams:Page type="learner" title="${title}">

		<lams:Alert id="submissionDeadline" close="false" type="danger">
			<fmt:message key="authoring.info.teacher.set.restriction">
				<fmt:param>
					<lams:Date value="${sessionMap.submissionDeadline}" />
				</fmt:param>
			</fmt:message>
		</lams:Alert>

		<c:choose>
			<c:when test="${sessionMap.reflectOn and (not sessionMap.userFinished)}">
				<button name="continueButton" onclick="javascript:notebook();" class="btn btn-primary pull-right">
					<fmt:message key="label.continue" />
				</button>
			</c:when>
			<c:otherwise>
				<a href="#nogo" name="finishButton" onclick="javascript:finish();"
					class="btn btn-primary pull-right na" id="finishButton">
					<span class="nextActivity"> <c:choose>
							<c:when test="${isLastActivity}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</span>
				</a>
			</c:otherwise>
		</c:choose>

		<div id="footer"></div>

	</lams:Page>

</body>
</lams:html>
