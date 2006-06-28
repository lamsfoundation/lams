<%@ include file="/includes/taglibs.jsp"%>

<table cellpadding="0">
	<tr>
		<td>
			<h1>
				<c:out value="${NbLearnerForm.title}" escapeXml="false" />
			</h1>

			<p>
				<c:out value="${NbLearnerForm.content}" escapeXml="false" />
			</p>
		</td>
	</tr>

	<c:if test="${sessionScope.readOnlyMode != 'true'}">
		<tr>
			<td align="right">
				<html:form action="/learner" target="_self">
					<html:submit property="method">
						<fmt:message key="button.finish" />
					</html:submit>
				</html:form>
			</td>
		</tr>
	</c:if>
</table>



