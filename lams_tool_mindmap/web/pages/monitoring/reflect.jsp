<%@ include file="/common/taglibs.jsp"%>

	<h1>
		<c:out value="${reflectTitle}" escapeXml="true" />
	</h1>
	
	<h2>
		<c:out value="${mindmapUser}" escapeXml="true"/>
	</h2>
	
	<lams:out value="${reflectEntry}" escapeHtml="true" />
		
	<div align="right" class="space-bottom-top">
		<html:button styleClass="button" property="backButton" onclick="history.go(-1)">
			<fmt:message>button.back</fmt:message>
		</html:button>
	</div>
