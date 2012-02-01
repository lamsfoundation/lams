<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
	<lams:css/>
	
	<script type="text/javascript">
		var lessonId="${lsId}";
		
		function removePrecedingLesson(precedingLessonId){
			document.location.href="<lams:LAMSURL/>lessonConditions.do?method=removeLessonDependency&lsId=" + lessonId
						  + "&precedingLessonId=" + precedingLessonId;
		}
	</script>
</lams:head>
    
<body class="stripes">
<div id="page">
<div id="content">
	<h4>
		<fmt:message key="label.conditions.box.title">
			<fmt:param>${title}</fmt:param>
		</fmt:message>
	</h4>
	
	<c:choose>
		<c:when test="${empty precedingLessons}">
			<i>There are no dependencies for this lesson.</i>
		</c:when>
		<c:otherwise>
			<ul>
				<c:forEach var="precedingLesson" items="${precedingLessons}">
					<li>
						<c:out value="${precedingLesson.name}" />
						<img src="<lams:LAMSURL/>images/icons/cross.png"
							 style="cursor: pointer; margin-left: 10px; postion: relative; top: 2px"
							 title="Delete lesson dependency"
							 onclick="javascript:removePrecedingLesson(${precedingLesson.id})" />
					</li>
				</c:forEach>
			</ul>
		</c:otherwise>
	</c:choose>
</div>
</div>
</body>
</lams:html>