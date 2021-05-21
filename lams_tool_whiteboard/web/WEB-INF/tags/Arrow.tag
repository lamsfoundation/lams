<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<%-- state = up: up arrow;  state = down: down arrow --%>
<%@ attribute name="state" required="true" rtexprvalue="true"%>
<%@ attribute name="titleKey" required="false" rtexprvalue="true"%>
<%@ attribute name="onclick" required="false" rtexprvalue="true"%>
<%@ attribute name="id" required="false" rtexprvalue="true"%>
<%@ attribute name="display" required="false"  rtexprvalue="true"%>

<c:choose>
	<c:when test="${not empty titleKey}">
		<c:set var="fullTitle">title="<fmt:message key='${titleKey}'/>"</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="fullTitle" value=""/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${not empty onclick}">
		<c:set var="fullOnclick">onclick="${onclick}"</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="fullOnclick" value=""/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${not empty id}">
		<c:set var="fullId">id="${id}"</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="fullId" value=""/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${not empty display}">
	<c:set var="fullDisplay">style='display:${display}'</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="fullDisplay" value=""/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${fn:containsIgnoreCase(state, 'up')}">
		<i class="fa fa-long-arrow-up fa-pull-left" ${fullId} ${fullTitle} ${fullOnclick} ${fullDisplay}></i>
	</c:when>
	<c:otherwise>
		<i class="fa fa-long-arrow-down fa-pull-right"  ${fullId} ${fullTitle} ${fullOnclick} ${fullDisplay}></i>
	</c:otherwise>
</c:choose>
