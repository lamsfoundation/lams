<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!-- Add More menu drop down first (if it contains links) -->
<c:if test="${not empty org.moreLinks}">
	<div class="mycourses-right-buttons">
		<div class="split-ui-button">
			<div>
				<span class="more-actions-button"><fmt:message key="index.moreActions" /></span>
			</div>
			<div>&nbsp;</div>
		</div>
		<ul>
			<c:forEach var="link" items="${org.moreLinks}">
				<li class="${link.style}" onClick="${link.url}"
					<c:if test="${not empty link.tooltip}">
               			title="<fmt:message key='${link.tooltip}'/>"
               		</c:if>
                >
					<fmt:message key="${link.name}" />
				</li>
			</c:forEach>
		</ul>
	</div>
</c:if> 

<c:forEach var="link" items="${org.links}">
	<c:choose>
		<c:when test="${link.name eq 'index.addlesson.single'}">
			<div class="mycourses-right-buttons">
				<div class="split-ui-button">
					<div onClick="<c:out value='${link.url}'/>"
						<c:if test="${not empty link.tooltip}">
		                	title="<fmt:message key='${link.tooltip}'/>"
		                </c:if>
		            >
						<span class="${link.style}"><fmt:message key="index.addlesson" /></span>
					</div>
					<div>&nbsp;</div>
				</div>
				<ul>
					<span><fmt:message key="index.single.activity.lesson.desc" /></span>
					<c:forEach var="tool" items="${tools}">
						<li onClick="javascript:showAddSingleActivityLessonDialog(${org.id}, ${tool.toolId}, ${tool.learningLibraryId})">
							<c:out value="${tool.toolDisplayName}" />
						</li>
					</c:forEach>
				</ul>
			</div>
		</c:when>
		<c:otherwise>
			<div class="mycourses-right-buttons">
				<div class="ui-button" onClick="<c:out value='${link.url}'/>"
					<c:if test="${not empty link.tooltip}">
				        title="<fmt:message key='${link.tooltip}'/>"
				    </c:if>
				>
					<span class="${link.style}"><fmt:message key="${link.name}" /></span>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</c:forEach>