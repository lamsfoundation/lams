<%@ include file="/common/taglibs.jsp"%>

<table>
	<tr>
		<td>
			<h2>
				${chatUserDTO.firstName} ${chatUserDTO.lastName}
			</h2>
		</td>
	</tr>
	<tr>
		<td>
			<p>
				<lams:out value="${chatUserDTO.notebookEntry}" />
			</p>
		</td>
	</tr>
</table>

