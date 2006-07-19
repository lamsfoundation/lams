<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

	<H1><c:out value="${formBean.title}" escapeXml="false" /></H2>
	<p><c:out value="${formBean.content}" escapeXml="false" /></p>

	<H1><fmt:message key="titleHeading.statistics"/></H2>
	<%@ include file="m_Statistics.jsp"%>
