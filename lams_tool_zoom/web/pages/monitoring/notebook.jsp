<%@ include file="/common/taglibs.jsp"%>

<c:set var="title">
	<fmt:message key="heading.notebookEntry" />
</c:set>
<lams:Page title="${title}" type="monitor">
	<h4>
		<c:out value="${userDTO.firstName} ${userDTO.lastName}" escapeXml="true"/>
	</h4>

	<table class="table table-striped table-condensed">
		<tr>
			<td>
				<fmt:message key="label.created" />
			</td>
			<td>
				<lams:Date value="${userDTO.notebookEntryDTO.createDate }"></lams:Date>

			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="label.lastModified" />
			</td>
			<td>
				<lams:Date value="${userDTO.notebookEntryDTO.lastModified }"></lams:Date>
			</td>
		</tr>

		<tr>
			<td>
				<fmt:message key="label.notebookEntry" />
			</td>
			<td>
				<lams:out value="${userDTO.notebookEntryDTO.entry}" escapeHtml="true"/>
			</td>
		</tr>
	</table>

	 <a href="javascript:history.back();" class="btn btn-sm btn-primary">
	 	<fmt:message key="button.close"/>
	 </a>

</lams:Page>
