<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}" >
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

	<div class="container-lg">
		<lams:Alert5 id="submissionDeadline" close="false" type="danger">
			<fmt:message key="authoring.info.teacher.set.restriction">
				<fmt:param>
					<lams:Date value="${sessionMap.submissionDeadline}" />
				</fmt:param>
			</fmt:message>
		</lams:Alert5>
	
		<div class="activity-bottom-buttons">
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
		</div>
	
		<div id="footer"></div>		

	</div>



</lams:PageLearner>
