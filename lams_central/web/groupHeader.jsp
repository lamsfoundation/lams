<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<div class="row">
	<div class="left-buttons">
		<h2><a class="j-group-header"><c:out value="${orgBean.name}" /></a>
			<c:if test="${not empty orgBean.archivedDate}"><small>(<fmt:message key="label.archived"/> <lams:Date value="${orgBean.archivedDate}"/>)</small></c:if>
		</h2>
	</div>
	<c:forEach var="link" items="${orgBean.links}">
		<div class="mycourses-right-buttons">
			<a class="<c:out value='${link.style}'/>" href="<c:out value='${link.url}'/>"> 
				<fmt:message key="${link.name}" /> 
			</a>
		</div>	
	</c:forEach>
</div>

