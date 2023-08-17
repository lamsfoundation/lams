<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}" >
	<script type="text/javascript">
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
    </script>

	<div class="container-lg">
		<lams:Alert5 id="deadline" type="danger" close="false">
			<fmt:message key="label.sorry.the.deadline.has.passed" />
		</lams:Alert5>

		<!-- Display reflections -->
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn and empty sessionMap.submissionDeadline}">
			<div class="card shadow-sm mt-5">
				<div class="card-header">
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</div>
				
				<div class="card-body">
					<div class="m-2" aria-label="<fmt:message key='monitor.summary.td.notebookInstructions'/>">
						<lams:out escapeHtml="true" value="${sessionMap.reflectInstructions}" />
					</div>
					<hr/>

					<div class="m-2">
						<p>
							<c:choose>
								<c:when test="${empty sessionMap.reflectEntry}">
									<em> <fmt:message key="message.no.reflection.available" /></em>
								</c:when>
								<c:otherwise>
									<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
								</c:otherwise>
							</c:choose>
						</p>
						
						<div>
							<button name="FinishButton" onclick="return continueReflect()" class="btn btn-sm btn-secondary">
								<fmt:message key="label.edit" />
							</button>
						</div>
					</div>
				</div>
			</div>
		</c:if>

		<div class="activity-bottom-buttons">
			<c:choose>
				<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished) && empty sessionMap.submissionDeadline}">
					<button name="FinishButton" onclick="return continueReflect()" class="btn btn-primary">
						<fmt:message key="label.continue" />
					</button>
				</c:when>
				<c:otherwise>
					<a href="#nogo" name="FinishButton" id="finishButton" onclick="return finishSession()" class="btn btn-primary na">
						<c:choose>
							<c:when test="${sessionMap.isLastActivity}">
								<fmt:message key="label.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finished" />
							</c:otherwise>
						</c:choose>
					</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</lams:PageLearner>
