<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<!-- Group header -->
<c:if test="${orgBean.allowSorting}">
	<div class="mycourses-left-buttons">
		<a class="sorting" href="#" onClick="javascript:makeOrgSortable(${orgBean.id})" title="<fmt:message key="label.enable.lesson.sorting"/>">
			<img src="<lams:LAMSURL/>images/sorting_disabled.gif"> </a>
	</div>
</c:if>

<c:set var="org" value="${orgBean}" />
<%@ include file="groupHeader.jsp"%>


<!-- Group contents -->
<div class="j-course-contents">
	<div class="sequence-name">
		<div id="${orgBean.id}-lessons" class="lesson-table">
			<%@ include file="groupContents.jsp"%>
		</div>
	</div>
	
	<!-- Child organisations -->
	<c:forEach var="childOrg" items="${orgBean.childIndexOrgBeans}">
		<div class="group-name">
			<div class="child-org-name">
				<c:out value="${childOrg.name}" />
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