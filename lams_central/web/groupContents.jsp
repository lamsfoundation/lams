<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<div class="j-course-contents">
		
	<div class="sequence-name">
		<div id="<c:out value="${orgBean.id}"/>-lessons" class="j-lessons">
		<c:if test="${not empty orgBean.lessons}">
			<c:if test="${allowSorting}">
			<div class="mycourses-right-buttons">
				<a onclick="makeOrgSortable(<c:out value="${orgBean.id}"/>)">
					<fmt:message key="label.enable.lesson.sorting"/>
				</a>
			</div>
			</c:if>
		</c:if>
		<c:forEach var="lesson" items="${orgBean.lessons}">
			<p id="<c:out value="${lesson.id}"/>" class="j-single-lesson">
				<c:if test="${empty lesson.url}">
					<a title="<c:out value="${lesson.description}"/>" class="disabled-sequence-name-link"> <c:out value="${lesson.name}" /></a> 
				</c:if>
				<c:if test="${not empty lesson.url}">
					<a title="<c:out value="${lesson.description}"/>" href="<c:out value="${lesson.url}"/>" class="sequence-name-link"> <c:out value="${lesson.name}" /></a> 
				</c:if>
				<c:if test="${lesson.completed}"> <span class="mycourses-completed-img" title="<fmt:message key="label.completed"/>" ></span> </c:if>
				<c:if test="${lesson.state eq 4}"> <span class="mycourses-stop-img" title="<fmt:message key="label.disabled"/>" ></span> </c:if>
				<c:if test="${lesson.state eq 6}"> <span class="mycourses-stop-img" title="<fmt:message key="label.archived"/>" ></span> </c:if>
				<c:forEach var="lessonlink" items="${lesson.links}">
					<a href="<c:out value='${lessonlink.url}'/>" class="sequence-action-link"> 
						<span class="mycourses-monitor-img"> 
							<fmt:message key="${lessonlink.name}" /> 
						</span>
					</a>
				</c:forEach>
			</p>
		</c:forEach>
		</div>
	</div>
		
	<c:forEach var="childOrg" items="${orgBean.childIndexOrgBeans}">
	<div class="group-name">
		<p>
			<c:out value="${childOrg.name}" />
			<c:if test="${not empty childOrg.archivedDate}">
				<small>(<fmt:message key="label.archived"/> <lams:Date value="${childOrg.archivedDate}"/>)</small>
			</c:if>
			<c:forEach var="childlink" items="${childOrg.links}">
				<a href="<c:out value='${childlink.url}'/>" class="sequence-action-link"> 
					<span class="mycourses-addlesson-img">
						<fmt:message key="${childlink.name}" />
					</span>
				</a>
			</c:forEach>
		</p>
		
		<div class="sequence-name">
			<div id="<c:out value="${childOrg.id}"/>" class="j-subgroup-lessons"><c:forEach var="childLesson" items="${childOrg.lessons}">
				<p id="<c:out value="${childLesson.id}"/>" class="j-single-subgroup-lesson">
					<c:if test="${empty childLesson.url}">
						<a title="<c:out value="${childLesson.description}"/>" class="disabled-sequence-name-link"> <c:out value="${childLesson.name}" /></a>
					</c:if>
					<c:if test="${not empty childLesson.url}">
						<a title="<c:out value="${childLesson.description}"/>" href="<c:out value='${childLesson.url}'/>" class="sequence-name-link"> <c:out value="${childLesson.name}" /></a> 
					</c:if>
					<c:if test="${childLesson.completed}"> <span class="mycourses-completed-img" title="<fmt:message key="label.completed"/>" ></span> </c:if>
					<c:if test="${childLesson.state eq 4}"> <span class="mycourses-stop-img" title="<fmt:message key="label.disabled"/>" ></span> </c:if>
					<c:if test="${childLesson.state eq 6}"> <span class="mycourses-stop-img" title="<fmt:message key="label.archived"/>" ></span> </c:if>
					<c:forEach var="childlessonlink" items="${childLesson.links}">
					<a href="<c:out value='${childlessonlink.url}'/>" class="sequence-action-link"> 
						<span class="mycourses-monitor-img"> 
							<fmt:message key="${childlessonlink.name}" /> 
						</span>
					</a>
					</c:forEach>
				</p>
			</c:forEach></div>
		</div>
	</div>
	</c:forEach>
	
</div>
