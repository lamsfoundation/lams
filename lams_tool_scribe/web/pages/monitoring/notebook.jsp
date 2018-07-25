<%@ include file="/common/taglibs.jsp"%>

<h4>
	<c:out value="${scribeUserDTO.firstName} ${scribeUserDTO.lastName}" escapeXml="true"/>
</h4>
<p><lams:out value="${scribeUserDTO.notebookEntry}" escapeHtml="true"/></p>

