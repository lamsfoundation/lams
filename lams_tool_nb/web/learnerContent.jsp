<%@ include file="/includes/taglibs.jsp"%>

<h1 class="no-tabs-below">
	<c:out value="${NbLearnerForm.title}" escapeXml="false" />
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">

	<table cellpadding="0">
		<tr>
			<td>
				<p>
					<c:out value="${NbLearnerForm.content}" escapeXml="false" />
				</p>
			</td>
		</tr>

		<c:if test="${!NbLearnerForm.readOnly}">
			<tr>
				<td>
					<div class="right-buttons">
						<html:form action="/learner" target="_self">
								<html:hidden property="toolSessionID" />
								<html:hidden property="mode" />
								<c:choose>
									<c:when test="${reflectOnActivity}">
										<html:submit property="method" styleClass="button">
											<fmt:message key="button.continue" />
										</html:submit>
									</c:when>
									<c:otherwise>
										<html:submit property="method" styleClass="button">
											<fmt:message key="button.finish" />
										</html:submit>
									</c:otherwise>
								</c:choose>
						</html:form>
					</div>
				</td>
			</tr>
		</c:if>
	</table>
</div>

<div id="footer-learner"></div>


