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

	<html:form action="/learner" method="post" onsubmit="disableFinishButton();" styleId="learnerForm">
		<div class="form-group">
			<div class="panel">
				<lams:out value="${reflectInstructions}" escapeHtml="true" />
			</div>

			<html:textarea rows="4" property="reflectionText" value="${reflectEntry}" styleClass="form-control"
				styleId="focusedInput"></html:textarea>

			<html:hidden property="toolSessionID" />
			<html:hidden property="mode" />
			<html:hidden property="method" value="finish" />

			<html:link href="#nogo" styleClass="btn btn-primary pull-right voffset10" onclick="submitForm('finish')">
				<c:choose>
					<c:when test="${activityPosition.last}">
						<fmt:message key="button.submit" />
					</c:when>
					<c:otherwise>
						<fmt:message key="button.finish" />
					</c:otherwise>
				</c:choose>
			</html:link>
		</div>

	</html:form>

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

