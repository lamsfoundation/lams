<%@ include file="/common/taglibs.jsp"%>

<table>
	<tr>
		<td>
			<h2>
				${scribeUserDTO.firstName} ${scribeUserDTO.lastName}
			</h2>
		</td>
	</tr>
	<tr>
		<td>
			<p>${scribeUserDTO.notebookEntry}</p>
		</td>
	</tr>
</table>

