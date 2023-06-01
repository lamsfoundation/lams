<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<fmt:message key="title.reflection" var="reflectionLabel"/>

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}" >
	<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
	</script>

	<lams:errors5/>

	<div id="instructions" class="instructions">
		<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
	</div>
	<div class="row">
		<div class="col-12 text-primary">
			<hr class="mx-5">
		</div>
	</div>
	<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" modelAttribute="refForm" id="refForm">

		<div class="container-xxl">
			<div class="row">
				<div class="col-12">

					<div class="card lcard lcard-no-borders shadow mb-3">
						<div class="card-header lcard-header-button-border">
							<fmt:message key="title.reflection" />
						</div>
						<div class="card-body mb-2">
							<div class="form-group">
								<form:hidden path="userID" />
								<form:hidden path="sessionMapID" />
								<form:textarea aria-label="${reflectionLabel}" aria-multiline="true" aria-required="true" required="true" path="entryText" cssClass="form-control" id="focused" rows="5"></form:textarea>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="activity-bottom-buttons">
			<button class="btn btn-primary" id="finishButton">

				<c:choose>
					<c:when test="${isLastActivity}">
						<fmt:message key="button.submit" />
					</c:when>
					<c:otherwise>
						<fmt:message key="button.finish" />
					</c:otherwise>
				</c:choose>

			</button>
		</div>
	</form:form>

	<div id="footer"></div>


</lams:PageLearner>