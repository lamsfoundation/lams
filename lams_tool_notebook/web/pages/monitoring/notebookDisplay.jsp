<%@ include file="/common/taglibs.jsp"%>

<table>
	<tr>
		<td colspan="2">
			<h2>
				${userDTO.firstName} ${userDTO.lastName }
			</h2>
		</td>
	</tr>
	<tr>
		<td class="field-name" width="20%">
			<fmt:message key="label.created" />
		</td>
		<td>
			<lams:Date value="${userDTO.entryDTO.createDate }"></lams:Date>
			
		</td>
	</tr>
	<tr>
		<td class="field-name" width="20%">
			<fmt:message key="label.lastModified" />
		</td>
		<td>
			<lams:Date value="${userDTO.entryDTO.lastModified }"></lams:Date>
		</td>
	</tr>

	<tr>
		<td class="field-name">
			<fmt:message key="label.notebookEntry" />
		</td>
		<td>
			<c:out value="${userDTO.entryDTO.entry}" escapeXml="false"></c:out>
		</td>
	</tr>
</table>
