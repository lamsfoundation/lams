<%@ include file="/common/taglibs.jsp"%>

<table>
	<tr>
		<td>
			<h2>
				<c:out value="${scribeUserDTO.firstName} ${scribeUserDTO.lastName}" escapeXml="true"/>
			</h2>
		</td>
	</tr>
	<tr>
		<td>
			<p><lams:out value="${scribeUserDTO.notebookEntry}" escapeHtml="true"/></p>
		</td>
	</tr>
</table>

