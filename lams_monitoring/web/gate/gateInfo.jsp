<%@ taglib uri="tags-lams" prefix="lams"%>

<c:if test="${not empty GateForm.map.gate.description}">
	<!-- general information section-->
	<p>
		<lams:out value="${GateForm.map.gate.description}" escapeHtml="true" />
	</p>
</c:if>

<!--waiting learner information table-->
<p>
	<strong> <fmt:message key="label.gate.waiting.learners">
			<fmt:param value="${GateForm.map.waitingLearners}" />
			<fmt:param value="${GateForm.map.totalLearners}" />
		</fmt:message>
	</strong>
</p>

<c:if test="${not GateForm.map.readOnly}">
	<c:if test="${not GateForm.map.gate.gateOpen}" >
		<html:form action="/gate?method=openGate" target="_self">
			<input type="hidden" name="activityId" value="${GateForm.map.activityId}" />
			<p><html:submit styleClass="btn btn-default"><fmt:message key="label.gate.open"/></html:submit></p>
		</html:form>
	</c:if>
</c:if>
