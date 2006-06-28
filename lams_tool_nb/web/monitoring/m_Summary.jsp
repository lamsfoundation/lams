<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<table cellpadding="0">
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="basic.title" />
		</td>
		<td>
			<c:out value="${sessionScope.title}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="basic.content" />
		</td>
		<td>
			<c:out value="${sessionScope.content}" escapeXml="false" />
		</td>
	</tr>
</table>
