<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%-- In order to use this file, a page needs to have
	<c:import url="/jqGrid.i18n.jsp" context="/lams" />
	Also disable-cross-context has to be set to false in WEB-INF/jboss-web.xml file in the calling webapp
	See Gradebook for an example.
 --%>

// JQGRID i18n
<c:set var="language"><lams:user property="localeLanguage"/></c:set>
<c:choose>
	<c:when test="${language eq 'es'}">
		<%@ include file="/includes/javascript/jqgrid-i18n/grid.locale-es.js"%>
		$.extend(true, $.jgrid,$.jgrid.regional['${language}']);
 	</c:when>
 	 <c:when test="${language eq 'fr'}">
 	 	<%@ include file="/includes/javascript/jqgrid-i18n/grid.locale-fr.js"%>
 	 	$.extend(true, $.jgrid,$.jgrid.regional['${language}']);
 	 </c:when>
 	 <c:when test="${language eq 'el'}">
 	 	<%@ include file="/includes/javascript/jqgrid-i18n/grid.locale-el.js"%>
 	 	$.extend(true, $.jgrid,$.jgrid.regional['${language}']);
 	 </c:when>
</c:choose>