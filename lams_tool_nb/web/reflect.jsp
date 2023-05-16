<!DOCTYPE html>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>

<lams:PageLearner toolSessionID="${nbLearnerForm.toolSessionID}" title="${nbLearnerForm.title}">

	<script>
		checkNextGateActivity('finishButton', '${nbLearnerForm.toolSessionID}', '', function () {
			submitForm('finish');
		});

		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}

		function submitForm(methodName) {
			var f = document.getElementById('nbLearnerForm');
			f.action = methodName + ".do";
			f.submit();
		}

		$(document).ready(function (){
			$('#focusedInput').focus();
		});
	</script>

	<lams:out value="${reflectInstructions}" escapeHtml="true" />

	<form:form method="post" onsubmit="disableFinishButton();" modelAttribute="nbLearnerForm" id="nbLearnerForm">
		<textarea rows="4" name="reflectionText" value="${reflectEntry}" class="form-control mt-2"
				  id="focusedInput"></textarea>

		<form:hidden path="toolSessionID" />
		<form:hidden path="mode" />

		<div class="activity-bottom-buttons">
			<button type="button" class="btn btn-primary" id="finishButton">
				<c:choose>
					<c:when test="${isLastActivity}">
						<fmt:message key="button.submit"/>
					</c:when>
					<c:otherwise>
						<fmt:message key="button.finish"/>
					</c:otherwise>
				</c:choose>
			</button>
		</div>
	</form:form>

	<!-- Comments: the extra div counteracts the float -->
	<c:if test="${allowComments}">
		<div class="row no-gutter"><div class="col-xs-12"></div></div>
		<lams:Comments toolSessionId="${nbLearnerForm.toolSessionID}"
					   toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>" likeAndDislike="${likeAndDislike}" readOnly="true"
					   pageSize="10" sortBy="1" />
	</c:if>
	<!-- End comments -->
</lams:PageLearner>