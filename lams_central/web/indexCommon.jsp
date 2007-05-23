<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:forEach var="orgBean" items="${orgBeans}">
	<div class="course-bg">
		<div class="row">
			<div class="left-buttons">
				<h2><c:out value="${orgBean.name}" />
					<c:if test="${not empty orgBean.archivedDate}"><small>(<fmt:message key="label.archived"/> <lams:Date value="${orgBean.archivedDate}"/>)</small></c:if>
				</h2>
			</div>
			<c:forEach var="link" items="${orgBean.links}">
				<div class="mycourses-right-buttons">
					<a class="<c:out value='${link.style}'/>" href="<c:out value='${link.url}'/>"> 
						<fmt:message key="${link.name}" /> 
					</a>
				</div>	
			</c:forEach>
		</div>
		<div class="sequence-name">
			<c:forEach var="lesson" items="${orgBean.lessons}">
				<p>
					<c:if test="${empty lesson.url}">
						<a class="disabled-sequence-name-link"> <c:out value="${lesson.name}" /></a> 
						<c:if test="${lesson.state eq 4}">&nbsp;(<fmt:message key="label.disabled"/>)</c:if>
						<c:if test="${lesson.state eq 6}">&nbsp;(<fmt:message key="label.archived"/>)</c:if>
					</c:if>
					<c:if test="${not empty lesson.url}">
						<a href="<c:out value="${lesson.url}"/>" class="sequence-name-link"> <c:out value="${lesson.name}" /></a> 
					</c:if>
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
				<c:forEach var="childLesson" items="${childOrg.lessons}">
					<p>
						<c:if test="${empty childLesson.url}">
							<a class="disabled-sequence-name-link"> <c:out value="${childLesson.name}" /></a>
							<c:if test="${childLesson.state eq 4}">&nbsp;(<fmt:message key="label.disabled"/>)</c:if>
							<c:if test="${childLesson.state eq 6}">&nbsp;(<fmt:message key="label.archived"/>)</c:if>
						</c:if>
						<c:if test="${not empty childLesson.url}">
							<a href="<c:out value='${childLesson.url}'/>" class="sequence-name-link"> <c:out value="${childLesson.name}" /></a> 
						</c:if>
						<c:forEach var="childlessonlink" items="${childLesson.links}">
						<a href="<c:out value='${childlessonlink.url}'/>" class="sequence-action-link"> 
							<span class="mycourses-monitor-img"> 
								<fmt:message key="${childlessonlink.name}" /> 
							</span>
						</a>
						</c:forEach>
					</p>
				</c:forEach>
			</div>
		</div>
	</c:forEach>
</div>
</c:forEach>
<c:if test="${empty orgBeans}">
	<p class="align-left"><fmt:message key="msg.groups.empty" /></p>
</c:if>
