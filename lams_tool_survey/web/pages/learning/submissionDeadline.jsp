<!DOCTYPE html>


<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>

	<title><fmt:message key="label.learning.title" /></title>
	<script type="text/javascript">
		function finishSession() {
			document.getElementById("finishButton").disabled = true;
			document.location.href = '<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		function continueReflect() {
			document.location.href = '<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
	</script>
</lams:head>

<body class="stripes">
	<lams:Page type="learner" title="${sessionMap.title}">

		<lams:Alert id="submissionDeadline" type="danger" close="false">
			<fmt:message key="authoring.info.teacher.set.restriction">
				<fmt:param>
					<lams:Date value="${sessionMap.submissionDeadline}" />
				</fmt:param>
			</fmt:message>
		</lams:Alert>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="panel panel-default voffset10">
				<div class="panel-heading panel-title">

					<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
				</div>
				<div class="panel-body">

					<c:choose>
						<c:when test="${empty sessionMap.reflectEntry}">
							<p>
								<fmt:message key="message.no.reflection.available" />

							</p>
						</c:when>
						<c:otherwise>
							<div class="panel-body bg-warning">
								<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
							</div>
						</c:otherwise>
					</c:choose>

					<button property="ContinueButton" onclick="return continueReflect()" class="voffset5 btn btn-sm btn-default">
						<fmt:message key="label.edit" />
					</button>
				</div>
			</div>
		</c:if>


		<div class="voffset10">
			<c:choose>
				<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
					<a href="#nogo" property="FinishButton" onclick="return continueReflect()"
						class="btn btn-primary pull-right">
						<fmt:message key="label.continue" />
					</a>
				</c:when>
				<c:otherwise>
					<a href="#nogo" property="FinishButton" styleId="finishButton" onclick="return finishSession()"
						class="btn btn-primary pull-right na">
						<span class="nextActivity"> <c:choose>
								<c:when test="${sessionMap.isLastActivity}">
									<fmt:message key="label.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.finished" />
								</c:otherwise>
							</c:choose>
						</span>
					</a>
				</c:otherwise>
			</c:choose>
		</div>

		<!--closes footer-->
	</lams:Page>
</body>
</lams:html>
