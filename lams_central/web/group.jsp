<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<div class="course-header" style="padding-bottom: 5px; border-bottom: 1px solid #eee;">
<span class="lead"><strong><c:out value="${orgBean.name}" /></strong></span>
<!-- Group header -->
<c:set var="org" value="${orgBean}" />
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



<c:if test="${orgBean.allowSorting}">
	<div class="pull-right">
		<a class="sorting text-muted" href="#" onClick="javascript:makeOrgSortable(${orgBean.id})" title="<fmt:message key="label.enable.lesson.sorting"/>">
			<i class="fa fa-sort"></i></a>
	</div>
</c:if>

