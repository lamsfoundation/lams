<c:set var="formAction" value="/gate?method=knockGate&activityID=${GateForm.map.activityId}"/>

<p><fmt:message key="label.gate.refresh.message"/></p>

<c:if test="${GateForm.map.previewLesson == true}">
	<p>&nbsp;</p>
	<c:set var="formAction"><c:out value="${formAction}"/>&force=true</c:set>
	<p><em><fmt:message key="label.gate.preview.message"/></em></p>
	<p>&nbsp;<p>
</c:if>

<html:form action="${formAction}" target="_self">
<div class="right-buttons">
	<html:submit styleClass="button"><fmt:message key="label.next.button"/></html:submit>
</div>
</html:form>
	