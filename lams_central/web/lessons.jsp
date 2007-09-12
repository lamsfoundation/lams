<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<div style="clear:both;"></div>

<h2 class="small-space-top"><fmt:message key="title.all.my.lessons" />
</h2>

<div class="shading-bg">

<c:if test="${not empty lessons}">
	<p>
	<c:forEach var="lesson" items="${lessons}">
		<a href="javascript:openLearner(<c:out value="${lesson.lessonID}"/>)" class="sequence-name-link"> 
			<c:out value="${lesson.lessonName}" />
		</a><br/>
	</c:forEach>
	</p>
</c:if>
<c:if test="${empty lessons}">
	<p class="align-left"><fmt:message key="msg.no.lessons"/></p>
</c:if>

<div align="center">
<input class="button" 
	type="button" 
	value="<fmt:message key="label.return.to.myprofile" />"
	onclick="javascript:document.location='index.do?state=active&tab=profile'" />
</div>

</div>
