<%@ include file="/common/taglibs.jsp"%>

<c:set var="title"><fmt:message key="heading.reflection"/></c:set>
<lams:Page title="${title}" type="learner">

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

</lams:Page>