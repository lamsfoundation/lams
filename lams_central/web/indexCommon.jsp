<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<c:forEach var="orgBean" items="${orgBeans}">
	<!--Start course section -->
	<div class="course-bg">
		<!--start course heading - coloured bar section-->
		<div class="row">
			<div class="left-buttons">
				<h2><c:out value="${orgBean.name}" /></h2>
			</div>
			<div class="mycourses-right-buttons">
				<c:forEach var="link" items="${orgBean.links}">
				<a class="<c:out value='${link.style}'/>" href="<c:out value='${link.url}'/>"> 
					<fmt:message key="${link.name}" /> 
				</a>
				</c:forEach>
			</div>
		</div>
		<!--end course heading - coloured bar section--> <!--start sequence name-->
		<c:forEach var="lesson" items="${orgBean.lessons}">
			<div class="sequence-name">
				<p>
					<a href="<c:out value="${lesson.url}"/>" class="sequence-name-link"> <c:out value="${lesson.name}" /></a> 
					<c:forEach var="lessonlink" items="${lesson.links}">
						<a href="<c:out value='${lessonlink.url}'/>" class="sequence-action-link"> 
							<img src="images/css/mycourses_monitor.jpg" alt="monitor" width="19" height="16" class="align-middle" /> 
							<fmt:message key="${lessonlink.name}" /> 
						</a>
					</c:forEach>
				</p>
			</div>
		</c:forEach> <!--close sequence name--> <!--start group name--> 
		<c:forEach var="childOrg" items="${orgBean.childIndexOrgBeans}">
		<div class="group-name">
			<p>
				<c:out value="${childOrg.name}" /> 
				<c:forEach var="childlink" items="${childOrg.links}">
					<a href="<c:out value='${childlink.url}'/>" class="sequence-action-link"> 
						<img src="images/css/mycourses_addlesson.jpg" alt="add lesson" width="13" height="15" class="align-middle"> 
						<fmt:message key="${childlink.name}" /> 
					</a>
				</c:forEach>
			</p>
			<c:forEach var="childLesson" items="${childOrg.lessons}">
				<p>
					<a href="<c:out value='${childLesson.url}'/>" class="sequence-name-link"> 
						<c:out value="${childLesson.name}" /> 
					</a> 
					<c:forEach var="childlessonlink" items="${childLesson.links}">
					<a href="<c:out value='${childlessonlink.url}'/>" class="sequence-action-link"> 
						<img src="images/css/mycourses_monitor.jpg" alt="monitor" width="19" height="16" border="0" class="align-middle"> 
						<fmt:message key="${childlessonlink.name}" /> 
					</a>
					</c:forEach>
				</p>
			</c:forEach>
		</div>
	</c:forEach> <!--end group name --></div>
	<!-- close course section  -->
</c:forEach>
<c:if test="${empty orgBeans}">
	<p align="left"><fmt:message key="msg.groups.empty" /></p>
</c:if>
