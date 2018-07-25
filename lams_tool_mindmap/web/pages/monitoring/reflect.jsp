<%@ include file="/common/taglibs.jsp"%>

<h3>
	<fmt:message>label.notebookEntry</fmt:message>
</h3>

<table class="table">
	<tr>
		<th>
			<c:out value="${mindmapUser}" escapeXml="true"/>
		</th>
	</tr>
	<tr>
		<td>
			<lams:out value="${reflectEntry}" escapeHtml="true" />
		</td>
	</tr>
</table>
		
<html:button styleClass="btn btn-primary voffset10 pull-right" property="backButton" onclick="history.go(-1)">
	<fmt:message>button.back</fmt:message>
</html:button>
