<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>
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

<div data-role="header" data-theme="b" data-nobackbtn="true">
	<h1>
		<c:out value="${NbLearnerForm.title}" escapeXml="false" />
	</h1>
</div>

<html:form action="/learner" target="_self" onsubmit="disableFinishButton();" styleId="learnerForm">
	<div data-role="content">
		<p>
			<c:out value="${NbLearnerForm.basicContent}" escapeXml="false" />
		</p>
	
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
					${reflectInstructions}
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
		
		<c:if test="${allowComments}">
			<lams:Comments toolSessionId="${NbLearnerForm.toolSessionID}" toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>" likeAndDislike="${likeAndDislike}"/>
		</c:if>
		
	</div>

	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
			<c:if test="${not NbLearnerForm.readOnly}">
				<c:choose>
					<c:when test="${reflectOnActivity}">
						<button name="continueButton" onclick="submitForm('reflect')" data-icon="arrow-r" data-theme="b">
							<fmt:message key="button.continue" />
						</button>
					</c:when>
						
					<c:otherwise>
						<a href="#nogo" name="finishButton"	id="finishButton" onclick="submitForm('finish')" data-role="button" data-icon="arrow-r" data-theme="b">
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
						</a>
					</c:otherwise>
				</c:choose>
			</c:if>
		</span>
	</div>
</html:form>