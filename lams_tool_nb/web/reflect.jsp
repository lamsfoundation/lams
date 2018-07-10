<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>
<%@ include file="/includes/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	function submitForm(methodName) {
		var f = document.getElementById('learnerForm');
		f.submit();
	}
</script>

<lams:Page type="learner" title="${title}">

	<form:form action="/learner" method="post" onsubmit="disableFinishButton();" modelAttribute="learnerForm" id="learnerForm">
		<div class="form-group">
			<div class="panel">
				<lams:out value="${reflectInstructions}" escapeHtml="true" />
			</div>

			<textarea rows="4" name="reflectionText" value="${reflectEntry}" class="form-control"
				id="focusedInput"></textarea>

			<form:hidden path="toolSessionID" />
			<form:hidden path="mode" />
			<form:hidden path="method" value="finish" />

			<a href="#nogo" class="btn btn-primary pull-right voffset10" onclick="submitForm('finish')">
				<c:choose>
					<c:when test="${activityPosition.last}">
						<fmt:message key="button.submit" />
					</c:when>
					<c:otherwise>
						<fmt:message key="button.finish" />
					</c:otherwise>
				</c:choose>
			</a>
		</div>

	</form:form>

	<!-- Comments: the extra div counteracts the float -->
	<c:if test="${allowComments}">
		<div class="row no-gutter"><div class="col-xs-12"></div></div>
		<lams:Comments toolSessionId="${NbLearnerForm.toolSessionID}"
			toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>" likeAndDislike="${likeAndDislike}" readOnly="true"
			pageSize="10" sortBy="1" />
	</c:if>
	<!-- End comments -->

</lams:Page>

<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focusedInput").focus();
	}
</script>

