<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="type" required="true" rtexprvalue="true"%>
<%@ attribute name="style" required="false" rtexprvalue="true"%>
<%@ attribute name="title" required="false" rtexprvalue="true"%>
<%@ attribute name="titleHelpURL" required="false" rtexprvalue="true"%>
<%@ attribute name="headingContent" required="false" rtexprvalue="true"%>

<div class="row no-gutter">
	<div class="col-xs-12">
		<div class="container" id="${idName}">
			<div class="panel panel-default panel-${type}-page">
				<div class="panel-heading">
					<c:if test="${not empty title}">
						<div class="panel-title panel-${type}-title">
							<c:out value="${title}" escapeXml="true" />
							<c:if test="${not empty titleHelpURL}">
								<a style="float: right" href="${titleHelpURL}" target="_new"><span class="help" style="margin: 0px"></span></a>
							</c:if>
						</div>
					</c:if>
					<c:if test="${not empty headingContent}">
						<c:out value="${headingContent}" escapeXml="true" />
					</c:if>
				</div>
				<div class="panel-body panel-${type}-body">
					<jsp:doBody />
				</div>
			</div>
		</div>
		<! -- End content container -->
	</div>
</div>
