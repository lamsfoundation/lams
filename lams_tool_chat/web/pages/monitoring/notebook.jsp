<%@ include file="/common/taglibs.jsp"%>

<table>
	<tr>
		<td>
			<h2>
				<c:out value="${chatUserDTO.firstName} ${chatUserDTO.lastName}" escapeXml="true"/>
			</h2>
		</td>
	</tr>
	<tr>
		<td>
			<p>
				<lams:out value="${chatUserDTO.notebookEntry}" escapeHtml="true"/>
			</p>
		</td>
	</tr>
</table>

