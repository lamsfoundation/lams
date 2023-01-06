<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:set var="isCollapsingSubcoursesEnabled"><%=Configuration.getAsBoolean(ConfigurationKeys.ENABLE_COLLAPSING_SUBCOURSES)%></c:set>
<c:set var="isDirectOrgLaunchEnabled"><%=Configuration.getAsBoolean(ConfigurationKeys.ALLOW_DIRECT_LESSON_LAUNCH)%></c:set>

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
			
				<c:choose>
					<c:when test="${isCollapsingSubcoursesEnabled && not empty childOrg.lessons}">
						<a data-toggle="collapse" class="subcourse-title collapsed" data-groupid="${childOrg.id}">
							<c:choose>
								<c:when test="${childOrg.collapsed}">
									<i class="fa fa-plus-square-o"></i>
								</c:when>
								<c:otherwise>
									<i class="fa fa-minus-square-o"></i>
								</c:otherwise>
							</c:choose>
						
							<strong><c:out value="${childOrg.name}" /></strong>
						</a>
					</c:when>
					
					<c:when test="${isCollapsingSubcoursesEnabled}">
						<i class="fa" style="min-width:12px;"></i>
						<strong><c:out value="${childOrg.name}" /></strong>
					</c:when>
					
					<c:otherwise>
						<strong><c:out value="${childOrg.name}" /></strong>
					</c:otherwise>
				</c:choose>
				
				<c:if test="${not empty childOrg.archivedDate}">
					<small>(<fmt:message key="label.archived"/> <lams:Date value="${childOrg.archivedDate}"/>)</small>
				</c:if>
			</div>
			
			<c:set var="org" value="${childOrg}" />
			<%@ include file="groupHeader.jsp"%>
			
			<div id="${childOrg.id}-lessons" class="lesson-table subgroup-lesson-table panel-collapse collapse 
				<c:if test="${!childOrg.collapsed}">in</c:if>">
				<%@ include file="groupContents.jsp"%>
			</div>

		</div>
	</c:forEach>
</div>
