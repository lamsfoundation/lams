<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:set var="hasLessonToSort">false</c:set>

<div class="j-course-contents">
		
	<div class="sequence-name">
		<div id="<c:out value="${orgBean.id}"/>-lessons" class="j-lessons">
		<c:if test="${allowSorting}">
			<div class="mycourses-right-buttons" style="display:none;">
				<a class="sorting" onclick="makeOrgSortable(<c:out value="${orgBean.id}"/>)" title="<fmt:message key="label.enable.lesson.sorting"/>">
					<img src="<lams:LAMSURL/>/images/sorting_disabled.gif">
				</a>
			</div>
		</c:if>
		<c:forEach var="lesson" items="${orgBean.lessons}">
			<c:set var="hasLessonToSort">true</c:set>
			<p id="<c:out value="${lesson.id}"/>" class="j-single-lesson">
				<c:if test="${empty lesson.url}">
					<a title="<c:out value="${lesson.description}"/>" class="disabled-sequence-name-link"> <c:out value="${lesson.name}" /></a> 
				</c:if>
				<c:if test="${not empty lesson.url}">
					<a title="<c:out value="${lesson.description}"/>" href="<c:out value="${lesson.url}"/>" class="sequence-name-link"> <c:out value="${lesson.name}" /></a> 
				</c:if>
				<c:if test="${lesson.completed}"> <span class="mycourses-completed-img" title="<fmt:message key="label.completed"/>" >&nbsp;</span> </c:if>
				<c:if test="${lesson.state eq 4}"> <span class="mycourses-stop-img" title="<fmt:message key="label.disabled"/>" >&nbsp;</span> </c:if>
				<c:if test="${lesson.state eq 6}"> <span class="mycourses-stop-img" title="<fmt:message key="label.archived"/>" >&nbsp;</span> </c:if>
				<c:forEach var="lessonlink" items="${lesson.links}">
					<a href="<c:out value='${lessonlink.url}'/>" class="sequence-action-link"> 
						<span class="${lessonlink.style}"> 
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
				<a href="<c:out value='${childlink.url}'/>" class="sequence-action-link thickbox<c:out value="${orgBean.id}"/>"> 
					<span class="mycourses-addlesson-img" >
						<fmt:message key="${childlink.name}" />
					</span>
				</a>
			</c:forEach>
		</p>
		
		<div class="sequence-name">
			<div id="<c:out value="${childOrg.id}"/>" class="j-subgroup-lessons"><c:forEach var="childLesson" items="${childOrg.lessons}">
				<c:set var="hasLessonToSort">true</c:set>
				<p id="<c:out value="${childLesson.id}"/>" class="j-single-subgroup-lesson">
					<c:if test="${empty childLesson.url}">
						<a title="<c:out value="${childLesson.description}"/>" class="disabled-sequence-name-link"> <c:out value="${childLesson.name}" /></a>
					</c:if>
					<c:if test="${not empty childLesson.url}">
						<a title="<c:out value="${childLesson.description}"/>" href="<c:out value='${childLesson.url}'/>" class="sequence-name-link"> <c:out value="${childLesson.name}" /></a> 
					</c:if>
					<c:if test="${childLesson.completed}"> <span class="mycourses-completed-img" title="<fmt:message key="label.completed"/>" >&nbsp;</span> </c:if>
					<c:if test="${childLesson.state eq 4}"> <span class="mycourses-stop-img" title="<fmt:message key="label.disabled"/>" >&nbsp;</span> </c:if>
					<c:if test="${childLesson.state eq 6}"> <span class="mycourses-stop-img" title="<fmt:message key="label.archived"/>" >&nbsp;</span> </c:if>
					<c:forEach var="childlessonlink" items="${childLesson.links}">
					<a href="<c:out value='${childlessonlink.url}'/>" class="sequence-action-link"> 
						<span class="${childlessonlink.style}"> 
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

<c:if test="${allowSorting}"><c:if test="${hasLessonToSort eq 'true'}">
<script>
	jQuery("div.mycourses-right-buttons").show();
</script>
</c:if></c:if>
