<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>

<%@ attribute name="close" required="false" rtexprvalue="true"%>
<%@ attribute name="id" required="false" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="type" required="false" rtexprvalue="true"%>

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
		<c:set var="iconType" value="exclamation-circle" />
	</c:when>
</c:choose>

<div class="row justify-content-md-center">
	<div class="col-md-auto">
		<div id="${idName}" class="alert alert-${alertType} alert-dismissible shadow-sm" role="alert">
			<c:if test="${not empty title}">
				<span class="fa fa-${iconType} "></span> <span class="alert-heading font-weight-bold">${title}</span>
				<hr>
			</c:if>
			<c:if test="${close}">
				<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
			</c:if>

			<p class="mb-0">
				<jsp:doBody />
			</p>
		</div>
	</div>
</div>	