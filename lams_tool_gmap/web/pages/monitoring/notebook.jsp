<%@ include file="/common/taglibs.jsp"%>

<table>
	<tr>
		<td>
			<h2>
				${gmapUserDTO.firstName} ${gmapUserDTO.lastName}
			</h2>
		</td>
	</tr>
	<tr>
		<td>
			<p>
				<lams:out value="${gmapUserDTO.notebookEntry}" />
			</p>
		</td>
	</tr>
</table>

