<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>
<%@ include file="/includes/taglibs.jsp"%>


<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
	function submitForm(methodName) {
		var f = document.getElementById('learnerForm');
		f.action += "?method=" + methodName;
		f.submit();
	}
</script>

<lams:Page type="learner" title="${NbLearnerForm.title}">
	<div class="panel">
		<c:out value="${NbLearnerForm.basicContent}" escapeXml="false" />
	</div>

	<html:form action="/learner" target="_self" onsubmit="disableFinishButton();" styleId="learnerForm">
		<html:hidden property="toolSessionID" />
		<html:hidden property="mode" />

		<c:if test="${userFinished and reflectOnActivity}">
			<div class="panel">
				<lams:out value="${reflectInstructions}" escapeHtml="true" />
			</div>

			<div class="bg-warning" id="reflectionEntry">
				<c:choose>
					<c:when test="${empty reflectEntry}">
						<fmt:message key="message.no.reflection.available" />
					</c:when>
					<c:otherwise>
						<lams:out escapeHtml="true" value="${reflectEntry}" />
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

		<c:if test="${allowComments}">
			<hr/>
			<lams:Comments toolSessionId="${NbLearnerForm.toolSessionID}" 
				toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>" likeAndDislike="${likeAndDislike}" anonymous="${anonymous}" />
		</c:if>


		<c:if test="${not NbLearnerForm.readOnly}">
			<c:choose>
				<c:when test="${reflectOnActivity}">

					<html:button  property="continueButton" styleClass="btn btn-sm btn-primary pull-right"
						onclick="submitForm('reflect')">
						<fmt:message key="button.continue" />
					</html:button>
				</c:when>
				<c:otherwise>


					<html:link href="#nogo" property="finishButton" styleClass="btn btn-primary pull-right voffset10 na"
						onclick="submitForm('finish')">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>

					</html:link>
				</c:otherwise>
			</c:choose>
		</c:if>

	</html:form>

</lams:Page>