<c:set var="formAction" value="/gate?method=knockGate"/>


<tr>
	<td><fmt:message key="label.gate.refresh.message"/></td>
</tr>

<c:if test="${GateForm.map.previewLesson == true}">
<tr><td>&nbsp;</td></tr>
<c:set var="formAction"><c:out value="${formAction}"/>&force=true</c:set>
<tr>
	<td><em><fmt:message key="label.gate.preview.message"/></em></td>
</tr>
<tr><td>&nbsp;</td></tr>
</c:if>

<tr>
	<td align="right">
			<html:form action="${formAction}" target="_self">
				<html:submit styleClass="button"><fmt:message key="label.next.button"/></html:submit>
			</html:form>
	</td>
<tr>
