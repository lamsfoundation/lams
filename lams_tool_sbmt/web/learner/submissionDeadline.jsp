<!DOCTYPE html>

<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>

	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		function finish() {
			document.getElementById("finishButton").disabled = true;
			var finishUrl = "<html:rewrite page='/learner.do?method=finish&sessionMapID=${sessionMapID}'/>";
			location.href = finishUrl;
		}
		function notebook() {
			var finishUrl = "<html:rewrite page='/learning/newReflection.do?sessionMapID=${sessionMapID}'/>";
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
				<html:button property="continueButton" onclick="javascript:notebook();" styleClass="btn btn-primary pull-right">
					<fmt:message key="label.continue" />
				</html:button>
			</c:when>
			<c:otherwise>
				<html:link href="#nogo" property="finishButton" onclick="javascript:finish();"
					styleClass="btn btn-primary pull-right na" styleId="finishButton">
					<span class="nextActivity"> <c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</span>
				</html:link>
			</c:otherwise>
		</c:choose>

		<div id="footer"></div>

	</lams:Page>

</body>
</lams:html>
