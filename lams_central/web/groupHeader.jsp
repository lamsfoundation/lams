<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<div class="row">
	<div class="mycourses-left-buttons">
		<h2><span class="j-group-icon" style="vertical-align:middle;"></span>&nbsp;<a class="j-group-header"><c:out value="${orgBean.name}" /></a>
			<c:if test="${not empty orgBean.archivedDate}"><small>(<fmt:message key="label.archived"/> <lams:Date value="${orgBean.archivedDate}"/>)</small></c:if>
		</h2>
	</div>

<!-- Add More menu drop down first (if it contains links) -->
<c:if test="${not empty orgBean.moreLinks}">
	<div class="mycourses-right-buttons">
		<a id="more-actions-button-${orgBean.id}" class="more-actions-button" href="javascript:return false;">
			<fmt:message key="index.moreActions" />
		</a>
	</div>
</c:if> 

	<c:forEach var="link" items="${orgBean.links}">
		<c:set var="tooltip" value="" />
		<c:if test="${link.tooltip ne null}">
			<c:set var="tooltip">
				<fmt:message key="${link.tooltip}" />
			</c:set>
		</c:if>
		
		<div class="mycourses-right-buttons">
			<a class="<c:out value='${link.style}'/>" href="<c:out value='${link.url}'/>" title="${tooltip}"> 
				<fmt:message key="${link.name}" /> 
			</a>
		</div>	
	</c:forEach>
</div>

<div>
	<ul id="more-actions-list-${orgBean.id}" class="more-actions-list">
		<c:forEach var="link" items="${orgBean.moreLinks}">
			<c:set var="tooltip" value="" />
			<c:if test="${link.tooltip ne null}">
				<c:set var="tooltip">
					<fmt:message key="${link.tooltip}" />
				</c:set>
			</c:if>
			<li>
				<a class="${link.style}" href="${link.url}" title="${tooltip}">
					<fmt:message key="${link.name}" />
				</a>
			</li>
		</c:forEach>
	</ul>
</div>
