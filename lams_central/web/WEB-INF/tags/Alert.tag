<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>

<%@ attribute name="close" required="false" rtexprvalue="true"%>
<%@ attribute name="id" required="false" rtexprvalue="true"%>
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

<div class="row no-gutter">
	<div class="col-12 col-sm-offset-2 col-sm-8">
		<div id="${idName}" class="alert alert-${alertType}" style="margin-bottom: 10px; padding: 8px">
			<div class="media">
				<div class="media-left">
					<i class="fa fa-lg fa-${iconType} text-muted"></i>
				</div>
				<div class="media-body">
					<c:if test="${close}">
							<a href="#" class="close pull-right" data-dismiss="alert" aria-label="close">&times;</a>
					</c:if>
					<jsp:doBody />
				</div>
			</div>
		</div>
	</div>
</div>
