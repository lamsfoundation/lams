<%@ include file="/common/taglibs.jsp"%>

<div id="header"></div>

<div id="content">

	<h1>
		<fmt:message key="heading.notebookEntry" />
	</h1>

	<h2>
		<c:out value="${userDTO.firstName} ${userDTO.lastName}" escapeXml="true"/>
	</h2>

	<table>
		<tr>
			<td class="field-name" style="width: 20%;">
				<fmt:message key="label.created" />
			</td>
			<td>
				<lams:Date value="${userDTO.notebookEntryDTO.createDate }"></lams:Date>

			</td>
		</tr>
		<tr>
			<td class="field-name" style="width: 20%;">
				<fmt:message key="label.lastModified" />
			</td>
			<td>
				<lams:Date value="${userDTO.notebookEntryDTO.lastModified }"></lams:Date>
			</td>
		</tr>

		<tr>
			<td class="field-name" style="width: 20%;">
				<fmt:message key="label.notebookEntry" />
			</td>
			<td>
				<lams:out value="${userDTO.notebookEntryDTO.entry}" escapeHtml="true"/>
			</td>
		</tr>
	</table>

	<html:link href="monitoring.do?"></html:link>

</div>

<div id="footer">
</div>
