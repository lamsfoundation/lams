<%@ include file="/includes/taglibs.jsp"%>


<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
	function submitForm(methodName){
		var f = document.getElementById('learnerForm');
		var m = document.getElementById('methodVar');
		m.value=methodName
		f.submit();
	}
</script>

<div id="content">

	<h1>
		<c:out value="${NbLearnerForm.title}" escapeXml="true" />
	</h1>

	<p>
		<c:out value="${NbLearnerForm.basicContent}" escapeXml="false" />
	</p>

	<html:form action="/learner" target="_self"
		onsubmit="disableFinishButton();" styleId="learnerForm">
		<html:hidden property="toolSessionID" />
		<html:hidden property="mode" />
		<c:choose>
			<c:when test="${reflectOnActivity}">
				<input type="hidden" id="methodVar" name="method" value="reflect" />
			</c:when>
			<c:otherwise>
				<input type="hidden" id="methodVar" name="method" value="finish" />
			</c:otherwise>
		</c:choose>

		<c:if test="${userFinished and reflectOnActivity}">
			<div class="small-space-top">
				<h2>
					<lams:out value="${reflectInstructions}" escapeHtml="true"/>
				</h2>

				<c:choose>
					<c:when test="${empty reflectEntry}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" />
							</em>
						</p>

					</c:when>

					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${reflectEntry}" />
						</p>
					</c:otherwise>

				</c:choose>

			</div>
		</c:if>

		<div class="space-bottom-top align-right">
			<c:if test="${not NbLearnerForm.readOnly}">
				<c:choose>
					<c:when test="${reflectOnActivity}">
						<html:button property="continueButton" styleClass="button"
							onclick="submitForm('reflect')">
							<fmt:message key="button.continue" />
						</html:button>
					</c:when>
					<c:otherwise>


						<html:link href="#nogo" property="finishButton" styleClass="button"
							styleId="finishButton" onclick="submitForm('finish')">
							<span class="nextActivity">
								<c:choose>
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
			</c:if>
		</div>
	</html:form>
</div>




