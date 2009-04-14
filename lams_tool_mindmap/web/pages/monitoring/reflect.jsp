<%@ include file="/common/taglibs.jsp"%>

	<h1>
		<c:out value="${reflectTitle}" escapeXml="false" />
	</h1>
	
	<h2>
		${mindmapUser}
	</h2>
	
	<c:out value="${reflectEntry}" escapeXml="false" />
		
	<div align="right" class="space-bottom-top">
		<html:button styleClass="button" property="backButton" onclick="history.go(-1)">
			<fmt:message>button.back</fmt:message>
		</html:button>
	</div>
