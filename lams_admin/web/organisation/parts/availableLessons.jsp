<%@ include file="/taglibs.jsp"%>

<c:if test="${empty lessons}">
		There are no lessons to clone.
</c:if>
	
<c:if test="${not empty lessons}">
	<p>Check the box for each lesson to clone it.</p>
	<ul>
		<c:forEach items="${lessons}" var="lesson">
			<li><input id="lessons" name="lessons" type="checkbox" value="<c:out value="${lesson.lessonId}" />" checked="checked" />
				<a class="lessonNameLink" id="<c:out value="${lesson.lessonId}" />"><c:out value="${lesson.lessonName}" /></a> 
		</c:forEach>
	</ul>
	
	<c:forEach items="${lessons}" var="lesson">
		<div id="lessonDialog-<c:out value="${lesson.lessonId}" />" title="<c:out value="${lesson.lessonName}" />" style="display:none;">
			<p>
				<c:out value="${lesson.lessonDescription}" />
				<c:if test="${empty lesson.lessonDescription}">
					(No lesson description)
				</c:if>
			</p>
		</div>
	</c:forEach>
</c:if>