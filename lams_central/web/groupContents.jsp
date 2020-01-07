<%@ include file="/common/taglibs.jsp"%>

<c:forEach var="lesson" items="${org.lessons}">
	<div id="${lesson.id}" class="j-single-lesson">
		<c:choose>
			<c:when test="${empty lesson.url}">
				<a class="text-danger"> <c:out value="${lesson.name}" />
				</a>
			</c:when>
			<c:otherwise>
				<a href="<c:out value="${lesson.url}"/>" class="sequence-name-link"> <c:out value="${lesson.name}" />
				</a>
			</c:otherwise>
		</c:choose>
		<c:if test="${lesson.state eq 4}">
			<i class="fa fa-minus-circle text-danger loffset10" title="<fmt:message key="label.disabled"/>"></i>
		</c:if>
		<c:if test="${lesson.state eq 6}">
			<i class="fa fa-archived text-danger loffset10" title="<fmt:message key="label.archived"/>"></i>
		</c:if>
		<c:if test="${lesson.dependent or lesson.scheduledFinish}">
			 <i class="fa fa-code-fork text-warning loffset10" title="<fmt:message key="index.conditions.flag.tooltip"/>"></i>
		</c:if>
		<c:if test="${lesson.completed}">
			<i class="fa fa-check-circle text-success loffset10" title="<fmt:message key="label.completed"/>"></i>
		</c:if>

		<div class="lesson-actions" style="transform: rotate(180deg);">
			<c:forEach var="lessonlink" items="${lesson.links}">
				<c:choose><c:when test="${addTourClass}"><c:set var="tourClass">class="tour-${lessonlink.id}"</c:set></c:when>
				<c:otherwise><c:set var="tourClass"></c:set></c:otherwise></c:choose>
				
				<a href="<c:out value='${lessonlink.url}'/>"  ${tourClass} style="padding-left: 10px; padding-right:10px;"
					<c:if test="${not empty lessonlink.tooltip}">
						title="<fmt:message key='${lessonlink.tooltip}'/>"
					</c:if>>
					 	<i style="transform: rotate(-180deg);" class="${lessonlink.style} lesson-action-label" title="<fmt:message key='${lessonlink.name}'/>"></i></a>
			</c:forEach>
		</div>
		<c:set var="addTourClass" value="false" />
	</div>
</c:forEach>
