<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>

<%@ attribute name="close" required="false" rtexprvalue="true"%>
<%@ attribute name="id" required="false" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="type" required="false" rtexprvalue="true"%>
<%@ attribute name="isIconDisplayed" required="false" rtexprvalue="true"%>
<c:if test="${empty isIconDisplayed}">
	<c:set var="isIconDisplayed" value="true" />
</c:if>

<c:set var="idName" value="alert-${type}" />
<c:if test="${not empty id}">
	<c:set var="idName" value="${id}" />
</c:if>

<c:set var="alertType" value="info" />
<c:set var="iconType" value="info-circle" />
<c:choose>
	<c:when test="${type == 'danger'}">
		<c:set var="alertType" value="danger" />
		<c:set var="iconType" value="times-circle" />
	</c:when>
	<c:when test="${type == 'success'}">
		<c:set var="alertType" value="success" />
		<c:set var="iconType" value="check-circle" />
	</c:when>
	<c:when test="${type == 'warning'}">
		<c:set var="alertType" value="warning" />
		<c:set var="iconType" value="circle-exclamation" />
	</c:when>
</c:choose>

<div class="row justify-content-md-center text-center">
	<div class="col-md-auto" style="min-width: 33%;">
		<div id="${idName}" class="alert alert-${alertType} shadow" role="alert">
			<c:if test="${close}">
				<button type="button" class="btn-close btn-sm float-end ms-4" data-bs-dismiss="alert" aria-label="Close"></button>
			</c:if>
				
			<c:if test="${not empty pageScope.title}">
				<span class="alert-heading fw-bold">${pageScope.title}</span>
				<hr>
			</c:if>
			
			<c:if test="${isIconDisplayed}">
				<i class="fa fa-lg fa-${iconType} text-muted me-1"></i>
			</c:if>
			<jsp:doBody />	
		</div>
	</div>
</div>
