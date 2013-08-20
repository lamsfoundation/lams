<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<c:forEach var="lesson" items="${org.lessons}">
	<div id="${lesson.id}" class="j-single-lesson">
		<c:choose>
			<c:when test="${empty lesson.url}">
				<a class="disabled-sequence-name-link"> <c:out value="${lesson.name}" />
				</a>
			</c:when>
			<c:otherwise>
				<a href="<c:out value="${lesson.url}"/>" class="sequence-name-link"> <c:out value="${lesson.name}" />
				</a>
			</c:otherwise>
		</c:choose>
		<c:if test="${lesson.state eq 4}">
			<span class="mycourses-stop-img mycourses-span" title="<fmt:message key="label.disabled"/>">&nbsp;</span>
		</c:if>
		<c:if test="${lesson.state eq 6}">
			<span class="mycourses-stop-img mycourses-span" title="<fmt:message key="label.archived"/>">&nbsp;</span>
		</c:if>
		<c:if test="${lesson.dependent or lesson.scheduledFinish}">
			<span class="mycourses-conditions-img mycourses-span" title="<fmt:message key="index.conditions.flag.tooltip"/>">&nbsp;</span>
		</c:if>
		<c:if test="${lesson.completed}">
			<span class="mycourses-completed-img mycourses-span" title="<fmt:message key="label.completed"/>">&nbsp;</span>
		</c:if>

		<div class="lesson-actions">
			<c:forEach var="lessonlink" items="${lesson.links}">
				<a href="<c:out value='${lessonlink.url}'/>"
					<c:if test="${not empty lessonlink.tooltip}">
						title="<fmt:message key='${lessonlink.tooltip}'/>"
					</c:if>
				>
					<div class="${lessonlink.style} mycourses-div">
						<span class="lesson-action-label">
							<fmt:message key="${lessonlink.name}" />
						</span>
					</div>
				</a>
			</c:forEach>
		</div>
	</div>
</c:forEach>