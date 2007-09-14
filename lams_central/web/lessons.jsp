<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<div style="clear:both;"></div>

<h2 class="small-space-top"><fmt:message key="title.all.my.lessons" />
</h2>

<div class="shading-bg">

<c:if test="${not empty beans}">
	<c:forEach var="group" items="${beans}">
		<br/>
		<h4><c:out value="${group.name}"/></h4>
		<ul><c:forEach var="lesson" items="${group.lessons}">
			<li><a href="<c:out value="${lesson.url}"/>" class="sequence-name-link"> 
				<c:out value="${lesson.name}" />
			</a></li>
		</c:forEach></ul>
		<ul><c:forEach var="subgroup" items="${group.childIndexOrgBeans}">
			<c:out value="${subgroup.name}"/>
			<ul><c:forEach var="s_lesson" items="${subgroup.lessons}">
				<li><a href="<c:out value="${s_lesson.url}"/>" class="sequence-name-link"> 
					<c:out value="${s_lesson.name}" />
				</a></li>
			</c:forEach></ul>
		</c:forEach></ul>
	</c:forEach>
</c:if>
<c:if test="${empty beans}">
	<p class="align-left"><fmt:message key="msg.no.lessons"/></p>
</c:if>

<div align="center">
<input class="button" 
	type="button" 
	value="<fmt:message key="label.return.to.myprofile" />"
	onclick="javascript:document.location='index.do?state=active&tab=profile'" />
</div>

</div>
