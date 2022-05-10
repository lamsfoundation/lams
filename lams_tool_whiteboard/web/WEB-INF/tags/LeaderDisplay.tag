<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<%@ attribute name="idName" required="false" rtexprvalue="true"%>
<%@ attribute name="messageKey" required="false" rtexprvalue="true"%>
<%@ attribute name="username" required="false" rtexprvalue="true"%>
<%@ attribute name="userId" required="true" rtexprvalue="true"%>

<c:if test="${empty idName}">
	<c:set var="idName" value="alert-leader${userId}" />
</c:if>

<c:if test="${empty messageKey}">
	<c:set var="messageKey" value="label.group.leader" />
</c:if>

<div class="row no-gutter">
	<div class="col-xs-12 col-sm-offset-2 col-sm-8">
		<div id="${idName}" class="alert alert-info leader-display">
			<h4><fmt:message key="${messageKey}">
				<fmt:param><c:out value="${username}" escapeXml="true"/></fmt:param>
			</fmt:message>&nbsp;
			<lams:Portrait userId="${userId}"/></h4>
		</div>
	</div>
</div>
