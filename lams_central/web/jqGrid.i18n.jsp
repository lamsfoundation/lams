<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

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
 	<c:when test="${language eq 'no'}">
 	 	<%@ include file="/includes/javascript/jqgrid-i18n/grid.locale-no.js"%>
 	 	$.extend(true, $.jgrid,$.jgrid.regional['${language}']);
 	 </c:when>
</c:choose>