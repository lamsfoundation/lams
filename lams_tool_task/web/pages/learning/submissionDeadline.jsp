<!DOCTYPE html>


<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
	<!--
		function finishSession() {
			document.getElementById("finishButton").disabled = true;
			document.location.href = '<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		function continueReflect() {
			document.location.href = '<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}

		-->
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

			<div class="row">
				<div class="col-xs-12">
					<div class="panel panel-default">
						<div class="panel-heading-sm">
							<div class="panel-title">${sessionMap.reflectInstructions}</div>
						</div>
						<div class="panel-body">
							<c:choose>
								<c:when test="${empty sessionMap.reflectEntry}">
									<fmt:message key="message.no.reflection.available" />
								</c:when>
								<c:otherwise>
									<div class="panel">
										<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
									</div>
								</c:otherwise>
							</c:choose>

							<html:button property="FinishButton" onclick="return continueReflect()" styleClass="btn btn-default voffset5">
								<fmt:message key="label.edit" />
							</html:button>


						</div>
					</div>
				</div>
			</div>

		</c:if>

		<div class="voffset10">
			<c:choose>
				<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
					<html:link href="#nogo" property="FinishButton" onclick="return continueReflect()"
						styleClass="btn btn-primary pull-right ">
						<fmt:message key="label.continue" />
					</html:link>
				</c:when>
				<c:otherwise>
					<html:link href="#nogo" property="FinishButton" styleId="finishButton" onclick="return finishSession()"
						styleClass="btn btn-primary pull-right na">
						<span class="nextActivity"> <c:choose>
								<c:when test="${sessionMap.activityPosition.last}">
									<fmt:message key="label.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.finished" />
								</c:otherwise>
							</c:choose>
						</span>
					</html:link>
				</c:otherwise>
			</c:choose>
		</div>

		<div id="footer"></div>
		<!--closes footer-->
	</lams:Page>
</body>
</lams:html>
