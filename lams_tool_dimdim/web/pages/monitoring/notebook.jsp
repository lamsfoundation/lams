<%@ include file="/common/taglibs.jsp"%>

<div id="header"></div>

<div id="content">

	<h1>
		<fmt:message key="heading.notebookEntry"/> 
	</h1>

	<h2>
		${userDTO.firstName} ${userDTO.lastName }
	</h2>

	<table>
		<tr>
			<td class="field-name" style="width:20%;">
				<fmt:message key="label.created" />
			</td>
			<td>
				<lams:Date value="${userDTO.notebookEntryDTO.createDate }"></lams:Date>
				
			</td>
		</tr>
		<tr>
			<td class="field-name" style="width:20%;">
				<fmt:message key="label.lastModified" />
			</td>
			<td>
				<lams:Date value="${userDTO.notebookEntryDTO.lastModified }"></lams:Date>
			</td>
		</tr>
	
		<tr>
			<td class="field-name" style="width:20%;">
				<fmt:message key="label.notebookEntry" />
			</td>
			<td>
				<c:out value="${userDTO.notebookEntryDTO.entry}" escapeXml="false"></c:out>
			</td>
		</tr>
	</table>
	
	<html:link href="monitoring.do?"></html:link>
	
</div>

<div id="footer">
</div>