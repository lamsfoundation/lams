<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<div class="course-header">
	<span class="lead">
		<strong><c:out value="${orgBean.name}" /></strong>
	</span>
	<a href="#" onclick="javascript:toggleFavoriteOrganisation(${orgBean.id});" class="tour-favorite-organisation">
		<c:choose>
			<c:when test="${orgBean.favorite}">
				<i id="favorite-star" class="fa fa-star" title="<fmt:message key='label.remove.org.favorite'/>"></i>
			</c:when>
			<c:otherwise>
				<i id="favorite-star" class="fa fa-star-o" title="<fmt:message key='label.mark.org.favorite'/>"></i>
			</c:otherwise>
		</c:choose>
	</a>
	
	<!-- Group header -->
	<c:set var="org" value="${orgBean}" />
	<c:set var="addTourClass" value="true" /> <%-- turn on for the buttons in the course header and for the first lesson then turn off --%>
	<%@ include file="groupHeader.jsp"%>
</div>

<!-- Group contents -->
<div class="j-course-contents">
	<div class="sequence-list">
		<div id="${orgBean.id}-lessons" class="lesson-table">
			<%@ include file="groupContents.jsp"%>
		</div>
	</div>
	
	<!-- Child organisations -->
	<c:forEach var="childOrg" items="${orgBean.childIndexOrgBeans}">
		<div class="group-name">
			<div class="child-org-name">
				<strong><c:out value="${childOrg.name}" /></strong>
				<c:if test="${not empty childOrg.archivedDate}">
					<small>(<fmt:message key="label.archived"/> <lams:Date value="${childOrg.archivedDate}"/>)</small>
				</c:if>
			</div>
			
			<c:set var="org" value="${childOrg}" />
			<%@ include file="groupHeader.jsp"%>

			<div id="${childOrg.id}-lessons" class="lesson-table subgroup-lesson-table">
				<%@ include file="groupContents.jsp"%>
			</div>
		</div>
	</c:forEach>
</div>
