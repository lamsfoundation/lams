<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="titleKey" required="false" rtexprvalue="true"%>
<%@ attribute name="titleHelpURL" required="false" rtexprvalue="true"%>
<%@ attribute name="panelBodyClass" required="false"  rtexprvalue="true"%>

<c:if test="${not empty titleKey}">
	<c:set var="title"><fmt:message key="${titleKey}" /></c:set>
</c:if>

<div class="card">
	<c:if test="${not empty title or not empty titleHelpURL}">
	<div class="card-header">
		<div class="card-title">
		<c:out value="${title}" escapeXml="true" />
		<c:if test="${not empty titleHelpURL}">
			<a style="float: right" href="${titleHelpURL}" target="_new"><span class="help" style="margin: 0px"></span></a>
		</c:if>
		</div>
	</div>
	</c:if>
	<div class="card-body ${panelBodyClass}">
		<jsp:doBody />
	</div>
</div>


		
			
